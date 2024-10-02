package com.castwave.castwave.ui.fragment.account

import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.data.model.request.DeleteAccountRequest
import com.castwave.castwave.databinding.FragmentEditAccountBinding
import com.castwave.castwave.databinding.PopupChoosePhotoBinding
import com.castwave.castwave.ui.fragment.login.LogInFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.ImageUtils
import com.castwave.castwave.utils.extension.setDrawableEnd
import com.castwave.castwave.utils.extension.showDialogConfirm
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class EditAccountFragment : BaseFragment<FragmentEditAccountBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private var userInfo: User? = null
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initViews()
        initAction()
    }

    private fun initViews() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
            userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            userInfo?.let { user -> setupViewUser(user) }
        }
    }

    private fun setupViewUser(user: User) {
        binding.tvGoogleAccount.text
        binding.tvName.text = user.Name
        binding.tvUserName.text = user.Name
        updateAvatar(user.Avatar, binding.ivAvatar)
        binding.tvId.text = String.format("ID: ${user.ID}")
        binding.tvPhone.text =
            user.PhoneNumber ?: requireContext().getString(R.string.txt_update_phone)
        if (user.isGoogle == Constants.INDEX_1) {
            binding.tvNameGoogle.text = user.ggName
            binding.tvNameGoogle.setDrawableEnd(0)
            updateAvatar(user.ggImg, binding.ivAvatarGoogle)
        }
    }

    private fun updateAvatar(avatar: String?, ivAvatar: CircleImageView) {
        Glide.with(requireContext()).load(avatar)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(false)
            .placeholder(R.drawable.iv_avatar)
            .error(R.drawable.iv_avatar)
            .into(ivAvatar)
    }

    private fun initAction() {
        binding.tvBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvChangeName.setOnClickListener {
            openScreenWithContainer(requireContext(), ChangeNameFragment::class.java, null)
        }
        binding.tvChangePhone.setOnClickListener {
            openScreenWithContainer(requireContext(), ChangePhoneFragment::class.java, null)
        }
        binding.tvChangePassword.setOnClickListener {
            openScreenWithContainer(requireContext(), ChangePasswordFragment::class.java, null)
        }
        binding.tvDeleteAccount.setOnClickListener {
            showDialogDeleteAccount()
        }
        binding.ivAvatar.setOnClickListener {
            openPopupChoosePhoto()
        }
        binding.ivCamera.setOnClickListener {
            openPopupChoosePhoto()
        }
    }

    private fun showDialogDeleteAccount() {
        requireContext().showDialogConfirm(
            R.style.DialogFullScreen,
            R.drawable.ic_header_delete_account,
            requireContext().getString(R.string.txt_delete_account),
            requireContext().getString(R.string.txt_content_delete_account),
            requireContext().getString(R.string.txt_delete_account),
            requireContext().getString(R.string.txt_cancel)
        ) {
            deleteAccount()
        }
    }

    private fun deleteAccount() {
        userInfo?.Email?.let { email ->
            viewModel.rxDeleteAccount.subscribe {
                preferences.removeWithKey(Constants.KEY_JWT_TOKEN)
                preferences.removeWithKey(Constants.KEY_USER_INFO)
                preferences.removeWithKey(Constants.KEY_GOOGLE_TOKEN)
                toast(it)
                openScreenWithContainer(
                    requireContext(),
                    LogInFragment::class.java,
                    null
                )
            }.addToBag()
            viewModel.rxMessage.subscribe {
                toast(it)
            }.addToBag()
            viewModel.isLoading.subscribe {
                if (it) showDialog()
                else hideDialog()
            }.addToBag()
            viewModel.deleteAccount(DeleteAccountRequest(email))
        } ?: run {
            toast(requireContext().getString(R.string.txt_error_delete_account))
        }
    }

    private fun openPopupChoosePhoto() {
        val popupDialog = BaseShowPopup(requireContext(), PopupChoosePhotoBinding::inflate)
        popupDialog.binding.tvCamera.setOnClickListener {
            openCamera()
            popupDialog.dismiss()
        }
        popupDialog.binding.tvLibrary.setOnClickListener {
            openLibrary()
            popupDialog.dismiss()
        }
        popupDialog.show(binding.ivCamera)
    }

    private fun openLibrary() {
        ImageUtils.loadImageView(this, resultLauncherGallery)
    }


    private fun openCamera() {
        ImageUtils.takePhotoFromCamera(this, resultLauncherCamera)
    }

    private var resultLauncherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.extras?.get("data")?.let { uri->
                    getPath(getImageUri(uri as Bitmap))?.let { uploadImageToServer(File(it)) }
                }
            }
        }

    private var resultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    getPath(uri)?.let { uploadImageToServer(File(it)) }
                }
            }
        }
    private fun getImageUri(mageBitmap: Bitmap): Uri {
        val tempFile = File.createTempFile("avatar", ".png")
        val bytes = ByteArrayOutputStream()
        mageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()
        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }
    private fun getPath(uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(
                        type,
                        ignoreCase = true
                    )
                ) return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    id.toLong()
                )
                return getDataColumn(contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )

        try {
            uri?.let {
                requireContext().contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )?.let {
                    cursor = it
                    if (it.moveToFirst()) {
                        return it.getString(it.getColumnIndexOrThrow(column))
                    }
                }
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun uploadImageToServer(file: File) {
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "fullAvatarUrl",
            file.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), file)
        )

        uploadImage(filePart)
    }

    private fun uploadImage(body: MultipartBody.Part) {
        viewModel.rxUpdateAvatar.subscribe {
            Glide.with(requireContext()).load(it.avatar).into(binding.ivAvatar)
            toast(it.avatar)
        }.addToBag()
        viewModel.rxMessage.subscribe { message ->
            toast(message)
        }.addToBag()
        viewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
        viewModel.updateAvatar(body)
    }

    override fun getLayoutBinding(): FragmentEditAccountBinding =
        FragmentEditAccountBinding.inflate(layoutInflater)
}
