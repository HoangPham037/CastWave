package com.castwave.castwave.data.model

data class User(
    val UserID: String,
    val ID: String,
    val Email: String,
    var Name: String,
    var Avatar: String,
    var PhoneNumber: String?,
    val isGoogle: Int,
    var ggName: String?,
    var ggImg: String?,
    val RegistrationDate: String,
    val LastLoginDate: String
)
