package com.example.analysisgame.data.remote

import com.example.analysisgame.domain.model.AnswerRequest
import com.example.analysisgame.domain.model.AnswerResponse
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AnalysisGameApiService {

    @POST("users")
    suspend fun registerUser(@Body userRequest: UserRequest): UserResponse

    @POST("answer")
    suspend fun createAnswer(
        @Body answerRequest: AnswerRequest
    ) : AnswerResponse

    @GET("users/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): UserResponse


}