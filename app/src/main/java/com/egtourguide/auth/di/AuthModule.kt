package com.egtourguide.auth.di

import com.egtourguide.auth.data.AuthApi
import com.egtourguide.auth.data.repository.AuthRepositoryImpl
import com.egtourguide.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi = authApi)
    }
}