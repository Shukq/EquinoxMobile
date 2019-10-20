package com.quinox.domain.useCases

interface UseCaseProvider {
    fun makeAuthenticationUseCase() : AuthenticationUseCase
}