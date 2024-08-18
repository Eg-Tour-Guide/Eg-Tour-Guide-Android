package com.egtourguide.core.di

import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.utils.Constants.BASE_URL
import com.egtourguide.core.utils.Constants.NETWORK_TAG
import com.egtourguide.core.utils.DataStoreKeys.TOKEN_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHTTPClient(
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val httpClientLoggingInterceptor = HttpLoggingInterceptor { msg ->
            Timber.tag(NETWORK_TAG).e("Interceptor : $msg")
        }
        httpClientLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder().apply {
            addInterceptor(tokenInterceptor)
            authenticator(tokenAuthenticator)
            addInterceptor(httpClientLoggingInterceptor)
            readTimeout(2, TimeUnit.MINUTES)
            writeTimeout(2, TimeUnit.MINUTES)
            callTimeout(2, TimeUnit.MINUTES)
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
    fun provideTokenInterceptor(getFromDataStoreUseCase: GetFromDataStoreUseCase): TokenInterceptor {
        return TokenInterceptor { getFromDataStoreUseCase(TOKEN_KEY) }
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        getFromDataStoreUseCase: GetFromDataStoreUseCase
    ): TokenAuthenticator {
        return TokenAuthenticator(
            getToken = { getFromDataStoreUseCase(TOKEN_KEY) }
        )
    }
}

class TokenInterceptor(private val getToken: suspend () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { getToken() }
        val newRequest = chain.request().newBuilder().apply {
            addHeader("Accept", "application/json")
            token?.let { addHeader("Authorization", "Bearer $it") }
        }.build()
        return chain.proceed(newRequest)
    }
}

class TokenAuthenticator(
    private val getToken: suspend () -> String?
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val newToken = runBlocking { getToken() }

        return if (newToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else {
            null
        }
    }
}
