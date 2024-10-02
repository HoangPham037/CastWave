package com.castwave.castwave.data.model.response

import com.castwave.castwave.data.model.User

data class VerifyResponse(
    val success: Boolean,
    val message: String,
    val user: User
)
