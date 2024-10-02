package com.castwave.castwave.base

import androidx.lifecycle.ViewModel
import com.castwave.castwave.data.model.response.ResponseCommon
import com.castwave.castwave.data.remote.config.NetworkInterceptor
import com.castwave.castwave.utils.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel(
    val schedulerProvider: SchedulerProvider
) : ViewModel() {
    val isLoading: PublishSubject<Boolean> = PublishSubject.create()
    val rxMessage: PublishSubject<String> = PublishSubject.create()

    private val disposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun Disposable.addToBag() {
        disposable.add(this)
    }

    fun <T> Single<T>.onLoading(): Single<T> {
        return this.doOnSubscribe { isLoading.onNext(true) }
            .doFinally { isLoading.onNext(false) }
    }

    val Throwable.msgError: String
        get() {
            return when (this) {
                is NetworkInterceptor.NoConnectivityException -> "Không có kết nối mạng.\nKiểm tra lại kết nối 4G/WIFI của bạn"
                is ConnectException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is SocketTimeoutException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is UnknownHostException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is HttpException -> {
                    when (this.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            "Phiên đăng nhập đã hết hạn\nVui lòng đăng nhập lại"
                        }

                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                            "Server đang bảo trì.Vui lòng thử lại sau ít phút"
                        }

                        else -> {
                            val errorBody = this.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                try {
                                    val errorResponse =
                                        Gson().fromJson(errorBody, ResponseCommon::class.java)
                                    errorResponse.message // Return message from error response
                                } catch (e: Exception) {
                                    "Lỗi không xác định từ server"
                                }
                            } else {
                                this.response()?.message() ?: "unknown error"
                            }
                        }
                    }
                }

                else -> this.message ?: "unknown error"
            }
        }
}