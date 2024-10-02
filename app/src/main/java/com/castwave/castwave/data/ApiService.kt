package com.castwave.castwave.data

import com.castwave.castwave.data.model.request.DeleteAccountRequest
import com.castwave.castwave.data.model.request.LogInGoogleRequest
import com.castwave.castwave.data.model.request.LogInRequest
import com.castwave.castwave.data.model.request.LogoutRequest
import com.castwave.castwave.data.model.request.PasswordRequest
import com.castwave.castwave.data.model.request.RegisterRequest
import com.castwave.castwave.data.model.request.UpdateAvatarRequest
import com.castwave.castwave.data.model.request.UpdateProfileRequest
import com.castwave.castwave.data.model.response.AvatarResponse
import com.castwave.castwave.data.model.response.LoginResponse
import com.castwave.castwave.data.model.response.ResponseCommon
import com.castwave.castwave.data.model.response.VerifyResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST(ApiEndPoint.URL_LOGIN)
    fun logInWithAccount(@Body body: LogInRequest): Single<LoginResponse>

    @POST(ApiEndPoint.URL_LOGOUT)
    fun logoutAccount(@Body body: LogoutRequest): Single<ResponseCommon>

    @GET(ApiEndPoint.URL_VERIFY)
    fun verifyAccount(): Single<VerifyResponse>

    @GET(ApiEndPoint.URL_SEND_OTP)
    fun sendOtp(@Query("email") email: String): Single<ResponseCommon>

    @GET(ApiEndPoint.URL_VERIFY_OTP)
    fun verifyOtp(@Query("email") email: String, @Query("otp") otp: String): Single<ResponseCommon>

    @POST(ApiEndPoint.URL_REGISTER)
    fun registerAccount(@Body body: RegisterRequest): Single<ResponseCommon>

    @POST(ApiEndPoint.URL_LOGIN_GOOGLE)
    fun signInGoogle(@Body logInRequest: LogInGoogleRequest): Single<LoginResponse>

    @POST(ApiEndPoint.URL_DELETE_ACCOUNT)
    fun deleteAccount(@Body body: DeleteAccountRequest): Single<ResponseCommon>

    @POST(ApiEndPoint.URL_RESET_PASSWORD)
    fun resetPassword(@Body body: PasswordRequest): Single<ResponseCommon>

    @GET(ApiEndPoint.URL_FORGOT_PASSWORD)
    fun forgotPassword(@Query("email") email: String): Single<ResponseCommon>

    @POST(ApiEndPoint.URL_UPDATE_PROFILE)
    fun updateProfile(@Body body: UpdateProfileRequest): Single<ResponseCommon>

    @Multipart
    @POST(ApiEndPoint.URL_UPDATE_AVATAR)
    fun updateAvatar(@Part body: MultipartBody.Part): Single<AvatarResponse>
}