package com.castwave.castwave.viewmodel

import androidx.lifecycle.viewModelScope
import com.castwave.castwave.base.BaseViewModel
import com.castwave.castwave.data.model.request.DeleteAccountRequest
import com.castwave.castwave.data.model.request.LogInGoogleRequest
import com.castwave.castwave.data.model.request.LogInRequest
import com.castwave.castwave.data.model.request.LogoutRequest
import com.castwave.castwave.data.model.request.PasswordRequest
import com.castwave.castwave.data.model.request.RegisterRequest
import com.castwave.castwave.data.model.request.UpdateProfileRequest
import com.castwave.castwave.data.model.response.AvatarResponse
import com.castwave.castwave.data.model.response.ResponseCommon
import com.castwave.castwave.data.model.response.VerifyResponse
import com.castwave.castwave.repository.Repository
import com.castwave.castwave.utils.SchedulerProvider
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: Repository,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(schedulerProvider) {
    val rxLogin: PublishSubject<String> = PublishSubject.create()
    val rxLogout: PublishSubject<String> = PublishSubject.create()
    val rxLoginGoogle: PublishSubject<String> = PublishSubject.create()
    val rxResetPassword: PublishSubject<String> = PublishSubject.create()
    val rxDeleteAccount: PublishSubject<String> = PublishSubject.create()
    val rxSendOtp: PublishSubject<ResponseCommon> = PublishSubject.create()
    val rxVerifyOtp: PublishSubject<ResponseCommon> = PublishSubject.create()
    val rxVerifyAcc: PublishSubject<VerifyResponse> = PublishSubject.create()
    val rxRegisterAccount: PublishSubject<ResponseCommon> = PublishSubject.create()
    val rxForgotPassword: PublishSubject<ResponseCommon> = PublishSubject.create()
    val rxUpdateProfile: PublishSubject<ResponseCommon> = PublishSubject.create()
    val rxUpdateAvatar: PublishSubject<AvatarResponse> = PublishSubject.create()
    val rxProgress: PublishSubject<Long> = PublishSubject.create()

    fun setProgress(timeUpdateProgress: Long, player: ExoPlayer) {
        viewModelScope.launch {
            while (isActive) {
                delay(timeUpdateProgress)
                rxProgress.onNext(player.currentPosition)
            }
        }
    }
    fun login(body: LogInRequest) {
        viewModelScope.launch {
            repository.logInWithAccount(body)
                .compose(schedulerProvider.ioToMainSingleScheduler())
                .onLoading()
                .subscribe({ response ->
                    rxLogin.onNext(response.token)
                }, {
                    rxMessage.onNext(it.msgError)
                }).addToBag()
        }
    }

    fun logoutAccount(body: LogoutRequest) {
        viewModelScope.launch {
            repository.logoutAccount(body = body)
                .compose(schedulerProvider.ioToMainSingleScheduler())
                .onLoading()
                .subscribe({ response ->
                    rxLogout.onNext(response.message)
                }, {
                    rxMessage.onNext(it.msgError)
                }).addToBag()
        }
    }

    fun verifyAccount() {
        repository.verifyAccount()
            .delay(2000L, TimeUnit.MILLISECONDS)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxVerifyAcc.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun sendOtp(email: String) {
        repository.sendOtp(email)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxSendOtp.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun verifyOtp(email: String, otp: String) {
        repository.verifyOtp(email, otp)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxVerifyOtp.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun registerAccount(body: RegisterRequest) {
        repository.registerAccount(body)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxRegisterAccount.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun signInGoogle(logInRequest: LogInGoogleRequest) {
        viewModelScope.launch {
            repository.signInGoogle(logInRequest)
                .compose(schedulerProvider.ioToMainSingleScheduler())
                .onLoading()
                .subscribe({ response ->
                    rxLoginGoogle.onNext(response.token)
                }, {
                    rxMessage.onNext(it.msgError)
                }).addToBag()
        }
    }

    fun deleteAccount(body: DeleteAccountRequest) {
        viewModelScope.launch {
            repository.deleteAccount(body)
                .compose(schedulerProvider.ioToMainSingleScheduler())
                .onLoading()
                .subscribe({ response ->
                    rxDeleteAccount.onNext(response.message)
                }, {
                    rxMessage.onNext(it.msgError)
                }).addToBag()
        }
    }

    fun resetPassword(passwordRequest: PasswordRequest) {
        viewModelScope.launch {
            repository.resetPassword(passwordRequest)
                .compose(schedulerProvider.ioToMainSingleScheduler())
                .onLoading()
                .subscribe({ response ->
                    rxResetPassword.onNext(response.message)
                }, {
                    rxMessage.onNext(it.msgError)
                }).addToBag()
        }
    }

    fun forgotPassword(email: String) {
        repository.forgotPassword(email)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxForgotPassword.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        repository.updateProfile(updateProfileRequest)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxUpdateProfile.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }

    fun updateAvatar(updateAvatarRequest: MultipartBody.Part) {
        repository.updateAvatar(updateAvatarRequest)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .onLoading()
            .subscribe({ response ->
                rxUpdateAvatar.onNext(response)
            }, { error ->
                rxMessage.onNext(error.msgError)
            }).addToBag()
    }
}