package com.castwave.castwave.data.remote.config

import android.content.Context
import com.castwave.castwave.R
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.utils.hasNetworkConnection
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


class NetworkInterceptor @Inject constructor(val context: Context, val preferences: Preferences) : Interceptor{

    /*
    * used to enter the process of sending HTTP requests and receiving responses
    * **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferences.getString("jwt_token")?:""
        /*
         * We check if there is internet
         * available in the device. If not, pass
         * the networkState as NO_INTERNET.
         * */
        when {
            !hasNetworkConnection(context) -> {
                throw NoConnectivityException(context)
            }
            token.isNotEmpty() -> {
                return chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                )
            }
            else -> {
                return chain.proceed(
                    chain.request()
                        .newBuilder()
                        .build()
                )
            }
        }
    }


    class NoConnectivityException(val context: Context) : IOException() {
        override fun getLocalizedMessage(): String? {
            return context.resources.getString(R.string.no_internet_connection)
        }
    }
}