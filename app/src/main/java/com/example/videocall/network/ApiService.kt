package com.example.videocall.network

import com.example.videocall.models.CreateRoomRequest
import com.example.videocall.models.CreateRoomResponse
import com.example.videocall.models.LoginRequest
import com.example.videocall.models.RegisterRequest
import com.example.videocall.models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<TokenResponse>

    @POST("room/createRoom")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<CreateRoomResponse>
}