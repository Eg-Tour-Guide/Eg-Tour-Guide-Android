package com.egtourguide.home.di

import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.data.repository.HomeRepositoryImpl
import com.egtourguide.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideHomeRepository(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi = homeApi)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofitClient: Retrofit): HomeApi {
        return retrofitClient.create(HomeApi::class.java)
    }
}