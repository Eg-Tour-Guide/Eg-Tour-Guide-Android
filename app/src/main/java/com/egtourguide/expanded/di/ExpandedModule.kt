package com.egtourguide.expanded.di

import com.egtourguide.expanded.data.ExpandedApi
import com.egtourguide.expanded.data.repository.ExpandedRepositoryImpl
import com.egtourguide.expanded.domain.repository.ExpandedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpandedModule {

    @Provides
    @Singleton
    fun provideExpandedRepository(expandedApi: ExpandedApi): ExpandedRepository {
        return ExpandedRepositoryImpl(expandedApi = expandedApi)
    }

    @Provides
    @Singleton
    fun provideExpandedApi(retrofitClient: Retrofit): ExpandedApi {
        return retrofitClient.create(ExpandedApi::class.java)
    }
}