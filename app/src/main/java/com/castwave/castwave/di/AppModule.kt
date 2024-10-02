package com.castwave.castwave.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.castwave.castwave.BuildConfig
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.data.ApiService
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.remote.config.NetworkInterceptor
import com.castwave.castwave.data.remote.config.TokenInterceptor
import com.castwave.castwave.repository.Repository
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRxBus() : RxBus {
        return RxBus()
    }

    @Provides
    @Singleton
    fun provideSharedPreference(context: Application): SharedPreferences =
        context.getSharedPreferences(Constants.KEY_PREFS_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return Preferences(sharedPreferences)
    }



    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProvider()

    @Provides
    @Singleton
    fun provideTokenInterceptor() = TokenInterceptor()

    @Provides
    @Singleton
    fun provideNetworkInterceptor(@ApplicationContext context: Context, preferences: Preferences) =
        NetworkInterceptor(context, preferences)

    @Singleton
    @Provides
    fun provideHTTPClient(
        tokenInterceptor: TokenInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRxJava2CallAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ApiService =
        retrofit.client(okHttpClient).build().create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService) : Repository {
        return Repository(apiService)
    }
}