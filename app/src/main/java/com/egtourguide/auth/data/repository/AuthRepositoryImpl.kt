package com.egtourguide.auth.data.repository

import com.egtourguide.auth.data.AuthApi
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : AuthRepository {

}