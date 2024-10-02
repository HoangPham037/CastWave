package com.castwave.castwave.data.model.request

data class PasswordRequest(
    val email: String,
    val newPassword: String,
    val oldPassword: String?,
    val type: String
)
