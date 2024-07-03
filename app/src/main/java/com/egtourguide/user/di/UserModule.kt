package com.egtourguide.user.di

import com.egtourguide.user.data.UserApi
import com.egtourguide.user.data.repository.UserRepositoryImpl
import com.egtourguide.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi): UserRepository = UserRepositoryImpl(userApi)
}