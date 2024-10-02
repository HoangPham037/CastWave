package com.castwave.castwave.data.model

import java.io.Serializable

data class Song(
    val id: Int,
    val name: String? =null,
) : Serializable
