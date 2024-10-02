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
import com.castwave.castwave.databinding.FragmentSignUpBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.deviceId
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private val viewModel: AccountViewModel by viewModels()
    private var mGoogleSignInClient: GoogleSignInClient? = null

    companion object {
        private const val RC_SIGN_IN = 1234
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
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

    private fun initData() {
        val str = SpannableString(requireContext().getString(R.string.txt_account))

        val typeface = ResourcesCompat.getFont(requireContext(), R.font.plus_jakarta_sans_bold)
        val style = typeface?.style ?: Typeface.BOLD
        val color = ContextCompat.getColor(requireContext(), R.color.green_02)

        val textToBold = requireContext().getString(R.string.txt_sign_in)
        setStyleTextView(str, style, textToBold)
        setStyleColorTextView(str, color, textToBold)

        val strRule = SpannableString(requireContext().getString(R.string.txt_rule))

        val textToBoldRuleOne = requireContext().getString(R.string.txt_setting_rule)
        setStyleTextView(strRule, style, textToBoldRuleOne)
        val textToBoldTwo = requireContext().getString(R.string.txt_security)
        setStyleTextView(strRule, style, textToBoldTwo)

        binding.tvSignIn.text = str
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
            StyleSpan(style),
            start,
            start + tvToBold.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun initAction() {
        binding.tvContinue.setOnClickListener {
            showEnterOtpFragment()
        }
        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
        binding.tvSignIn.setOnClickListener {
            openScreenWithContainer(requireContext(), SignInFragment::class.java, null)
        }
        binding.ivGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java).idToken?.let { token ->
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
                    viewModel.signInGoogle(LogInGoogleRequest(token, requireContext().deviceId()))
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun loginGoogle() {
        if (binding.checkBox.isChecked) {
            mGoogleSignInClient?.signInIntent?.let { signInIntent ->
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        } else toast(requireContext().getString(R.string.txt_clause_policy))
    }

    private fun showEnterOtpFragment() {
        val gmail = binding.edtGmail.text.toString()
        if (isValidEmail(gmail)) {
            if (binding.checkBox.isChecked) {
                viewModel.rxSendOtp.subscribe {
                    Bundle().apply {
                        putString(Constants.KEY_TYPE_OTP, Constants.KEY_SIGN_UP)
                        putString(Constants.KEY_SEND_OTP, gmail)
                        openScreenWithContainer(
                            requireContext(),
                            EnterOtpFragment::class.java,
                            this
                        )
                    }
                }.addToBag()
                viewModel.rxMessage.subscribe { message ->
                    toast(message)
                }.addToBag()
                viewModel.isLoading.subscribe {
                    if (it) showDialog()
                    else hideDialog()
                }.addToBag()
                viewModel.sendOtp(gmail)
            } else toast(requireContext().getString(R.string.txt_clause_policy))
        } else {
            toast(requireContext().getString(R.string.txt_gmail_is_incorrect))
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun getLayoutBinding(): FragmentSignUpBinding =
        FragmentSignUpBinding.inflate(layoutInflater)
}