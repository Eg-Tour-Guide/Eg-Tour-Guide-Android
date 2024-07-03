package com.egtourguide.core.di

import com.egtourguide.core.data.CommonApi
import com.egtourguide.core.data.repository.CommonRepositoryImpl
import com.egtourguide.core.domain.repository.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideCommonRepository(commonApi: CommonApi): CommonRepository {
        return CommonRepositoryImpl(commonApi = commonApi)
    }

    @Provides
    @Singleton
    fun provideCommonApi(retrofit: Retrofit): CommonApi {
        return retrofit.create(CommonApi::class.java)
    }
}