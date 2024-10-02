package com.castwave.castwave.repository

import com.castwave.castwave.data.ApiService
import com.castwave.castwave.data.model.request.DeleteAccountRequest
import com.castwave.castwave.data.model.request.LogInGoogleRequest
import com.castwave.castwave.data.model.request.LogInRequest
import com.castwave.castwave.data.model.request.LogoutRequest
import com.castwave.castwave.data.model.request.PasswordRequest
import com.castwave.castwave.data.model.response.ResponseCommon
import com.castwave.castwave.data.model.request.RegisterRequest
import com.castwave.castwave.data.model.request.UpdateProfileRequest
import com.castwave.castwave.data.model.response.AvatarResponse
import com.castwave.castwave.data.model.response.LoginResponse
import com.castwave.castwave.data.model.response.VerifyResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {
    fun logInWithAccount(body: LogInRequest) : Single<LoginResponse>{
        return apiService.logInWithAccount(body = body)
    }

    fun logoutAccount(body: LogoutRequest) : Single<ResponseCommon> {
        return apiService.logoutAccount(body = body)
    }

    fun verifyAccount() : Single<VerifyResponse> {
        return apiService.verifyAccount()
    }

    fun sendOtp(email: String) : Single<ResponseCommon> {
        return apiService.sendOtp(email = email)
    }

    fun verifyOtp(email: String, otp: String) : Single<ResponseCommon> {
        return apiService.verifyOtp(email = email, otp =  otp)
    }

    fun registerAccount(body: RegisterRequest) : Single<ResponseCommon> {
        return apiService.registerAccount(body = body)
    }

    fun signInGoogle(logInRequest: LogInGoogleRequest): Single<LoginResponse> {
        return apiService.signInGoogle(logInRequest)
    }

    fun deleteAccount( body: DeleteAccountRequest): Single<ResponseCommon> {
        return apiService.deleteAccount(body)
    }

    fun resetPassword(body: PasswordRequest): Single<ResponseCommon> {
        return apiService.resetPassword(body)
    }

    fun forgotPassword(email: String): Single<ResponseCommon> {
        return apiService.forgotPassword(email = email)
    }
    fun updateProfile(body: UpdateProfileRequest): Single<ResponseCommon> {
        return apiService.updateProfile(body)
    }

    fun updateAvatar(body: MultipartBody.Part): Single<AvatarResponse> {
        return apiService.updateAvatar(body )
    }
}