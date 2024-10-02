package com.castwave.castwave.data.model.request

data class LogInRequest(
    val email: String,
    val password: String,
    val deviceId: String
)
