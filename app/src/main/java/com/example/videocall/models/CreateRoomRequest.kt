package com.example.videocall.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val roomName: String,
)
