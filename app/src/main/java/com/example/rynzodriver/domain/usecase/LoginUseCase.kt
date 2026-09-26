package com.example.rynzodriver.domain.usecase

import com.example.rynzodriver.domain.model.User
import com.example.rynzodriver.domain.repository.auth.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(mobile: String, password: String): Result<User> {
        return repository.login(mobile, password)
    }
}
