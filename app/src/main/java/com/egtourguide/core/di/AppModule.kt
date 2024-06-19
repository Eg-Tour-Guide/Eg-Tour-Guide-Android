package com.egtourguide.core.di

import android.util.Log
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.utils.Constants.BASE_URL
import com.egtourguide.core.utils.Constants.NETWORK_TAG
import com.egtourguide.core.utils.DataStoreKeys.TOKEN_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHTTPClient(token: String): OkHttpClient {
        val httpClientLoggingInterceptor = HttpLoggingInterceptor { msg ->
            Log.i(NETWORK_TAG, "Interceptor : $msg")
        }
        httpClientLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder().apply {
            addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Accept", "application/json")
                newRequest.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1ZWY5NWRlMGM0NTI4NGI3MmVjODBjYSIsImlhdCI6MTcxNzg4MTU0NCwiZXhwIjoxNzI1NjU3NTQ0fQ.p5vUCq3vvx6Mj3zwQNNstPfoT2b17ATaO_J7ecQYvkI")
                chain.proceed(newRequest.build())
            }
            addInterceptor(httpClientLoggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    fun provideUserToken(getFromDataStoreUseCase: GetFromDataStoreUseCase): String {
        return runBlocking { getFromDataStoreUseCase(key = TOKEN_KEY) ?: "" }
    }
}