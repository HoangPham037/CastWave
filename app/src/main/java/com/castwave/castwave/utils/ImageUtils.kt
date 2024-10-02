package com.castwave.castwave.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.castwave.castwave.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


object ImageUtils {
    fun loadImageView(fragment: Fragment, resultLauncherCamera: ActivityResultLauncher<Intent>) {
        val arrayPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) arrayOf(
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,  Manifest.permission.READ_MEDIA_IMAGES
            ) else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                ) else arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
        Dexter.withContext(fragment.requireContext())
            .withPermissions(*arrayPermission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport) {
                    if (p0.areAllPermissionsGranted()) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                        }
                        resultLauncherCamera.launch(intent)
                    } else {
                        showPermissionRationale(
                            fragment,
                            fragment.requireContext()
                                .getString(R.string.txt_permission_external_storage),
                            fragment.requireContext()
                                .getString(R.string.txt_content_external_storage)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }

            }).withErrorListener {
                Toast.makeText(fragment.requireContext(), "Error occurred!", Toast.LENGTH_SHORT)
                    .show()
            }.onSameThread().check()
    }

    fun takePhotoFromCamera(
        fragment: Fragment,
        resultLauncherCamera: ActivityResultLauncher<Intent>
    ) {
        Dexter.withContext(fragment.requireContext())
            .withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport) {
                    if (p0.areAllPermissionsGranted()) {
                        resultLauncherCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    } else {
                        showPermissionRationale(
                            fragment,
                            fragment.requireContext().getString(R.string.txt_permission_camera),
                            fragment.requireContext().getString(R.string.txt_content_camera)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }

            }).withErrorListener {
                Toast.makeText(fragment.requireContext(), "Error occurred!", Toast.LENGTH_SHORT)
                    .show()
            }.onSameThread().check()
    }

    private fun showPermissionRationale(fragment: Fragment, title: String, message: String) {
        val dialog = AlertDialog.Builder(fragment.requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(fragment.requireContext().getString(R.string.txt_setting)) { _, _ ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", fragment.requireContext().packageName, null)
                    fragment.startActivity(this)
                }
            }.setNegativeButton(
                fragment.requireContext().getString(R.string.txt_cancel)
            ) { dialog, _ ->
                dialog.dismiss()
            }.create()
        dialog.show()
    }
}