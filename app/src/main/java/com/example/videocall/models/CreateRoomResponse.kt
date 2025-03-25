package com.example.videocall.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomResponse(
    val roomId: Int,
    val success: Boolean,
)
