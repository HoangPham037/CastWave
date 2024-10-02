package com.castwave.castwave.ui.fragment.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.request.LogInGoogleRequest
import com.castwave.castwave.data.model.request.LogInRequest
import com.castwave.castwave.databinding.FragmentSignInBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.deviceId
import com.castwave.castwave.utils.extension.onTextChanged
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    @Inject
    lateinit var preferences: Preferences

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val viewModel: AccountViewModel by viewModels()

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    override fun onStart() {
        super.onStart()
        binding.checkBox.isChecked = true
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun initView() {
        val str = SpannableString(requireContext().getString(R.string.txt_no_account))

        val typeface = ResourcesCompat.getFont(requireContext(), R.font.plus_jakarta_sans_bold)
        val style = typeface?.style ?: Typeface.BOLD
        val color = ContextCompat.getColor(requireContext(), R.color.green_02)

        val textToBold = requireContext().getString(R.string.txt_sign_up)
        setStyleTextView(str, style, textToBold)
        setStyleColorTextView(str, color, textToBold)

        val strRule = SpannableString(requireContext().getString(R.string.txt_rule))

        val textToBoldRuleOne = requireContext().getString(R.string.txt_setting_rule)
        setStyleTextView(strRule, style, textToBoldRuleOne)

        val textToBoldTwo = requireContext().getString(R.string.txt_security)
        setStyleTextView(strRule, style, textToBoldTwo)

        binding.tvSignUp.text = str
        binding.tvRule.text = strRule
    }

    private fun setStyleColorTextView(str: SpannableString, color: Int, text: String) {
        val start = str.indexOf(text)
        str.setSpan(
            ForegroundColorSpan(color),
            start,
            start + text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun setStyleTextView(str: SpannableString, style: Int, tvToBold: String) {
        val start = str.indexOf(tvToBold)
        str.setSpan(
            StyleSpan(style), start, start + tvToBold.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }


    private fun initAction() {
        binding.ivGoogle.setOnClickListener {
            signGoogle()
        }
        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
        binding.tvLogIn.setOnClickListener {
            loginAccount()
        }
        binding.tvSignUp.setOnClickListener {
            openScreenWithContainer(requireContext(), SignUpFragment::class.java, null)
        }
        binding.tvForgotPassword.setOnClickListener {
            openScreenWithContainer(requireContext(), ForgotPasswordFragment::class.java, null)
        }
        binding.edtPassword.onTextChanged { password ->
            if (password?.isEmpty() == true) {
                binding.textInputLayout.endIconDrawable = null
            } else {
                binding.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                binding.textInputLayout.setEndIconDrawable(R.drawable.custom_password_eye)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java).idToken?.let { token ->
                        preferences.setString(Constants.KEY_GOOGLE_TOKEN, token)
                        viewModel.rxLoginGoogle.subscribe { jwtToken ->
                            preferences.setString(Constants.KEY_JWT_TOKEN, jwtToken)
                            rxBus.send(
                                Buser(
                                    Constants.KEY_NAVIGATE_FRAGMENT,
                                    Constants.VALUE_NAVIGATE_FRAGMENT
                                )
                            )
                        }.addToBag()
                        viewModel.rxMessage.subscribe {
                            toast(it)
                        }.addToBag()
                        viewModel.isLoading.subscribe {
                            if (it) showDialog()
                            else hideDialog()
                        }.addToBag()
                        viewModel.signInGoogle(
                            LogInGoogleRequest(
                                token,
                                requireContext().deviceId()
                            )
                        )
                    }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun signGoogle() {
        if (binding.checkBox.isChecked) {
            mGoogleSignInClient?.signInIntent?.let { signInIntent ->
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        } else toast(requireContext().getString(R.string.txt_clause_policy))
    }

    private fun loginAccount() {
        val tvGmail = binding.edtGmail.text.toString().trim()
        val tvPassword = binding.edtPassword.text.toString()
        if (tvGmail.isEmpty() || tvPassword.isEmpty()) {
            toast(requireContext().getString(R.string.txt_user_name_password_null))
            return
        }
        if (isValidEmail(tvGmail)) {
            if (binding.checkBox.isChecked) {
                val loginRequest = LogInRequest(
                    email = tvGmail,
                    password = tvPassword,
                    deviceId = requireContext().deviceId()
                )
                viewModel.rxLogin.subscribe { token ->
                    preferences.setString(Constants.KEY_JWT_TOKEN, token)
                    rxBus.send(
                        Buser(Constants.KEY_NAVIGATE_FRAGMENT, Constants.VALUE_NAVIGATE_FRAGMENT)
                    )
                }.addToBag()
                viewModel.rxMessage.subscribe { message ->
                    toast(message)
                }.addToBag()
                viewModel.isLoading.subscribe {
                    if (it) showDialog()
                    else hideDialog()
                }.addToBag()
                viewModel.login(loginRequest)
            } else toast(requireContext().getString(R.string.txt_clause_policy))
        } else {
            toast(requireContext().getString(R.string.txt_gmail_is_incorrect))
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun getLayoutBinding(): FragmentSignInBinding =
        FragmentSignInBinding.inflate(layoutInflater)
}