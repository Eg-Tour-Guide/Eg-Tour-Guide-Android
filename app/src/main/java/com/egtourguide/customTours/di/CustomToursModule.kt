package com.egtourguide.customTours.di

import com.egtourguide.customTours.data.CustomToursApi
import com.egtourguide.customTours.data.repository.CustomToursRepositoryImpl
import com.egtourguide.customTours.domain.repository.CustomToursRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomToursModule {

    @Provides
    @Singleton
    fun provideCustomToursApi(retrofit: Retrofit): CustomToursApi =
        retrofit.create(CustomToursApi::class.java)

    @Provides
    @Singleton
    fun provideCustomToursRepository(customToursApi: CustomToursApi): CustomToursRepository =
        CustomToursRepositoryImpl(customToursApi)
}