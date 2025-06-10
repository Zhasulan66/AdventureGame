package com.example.analysisgame.domain.repository

import com.example.analysisgame.domain.model.AnswerRequest
import com.example.analysisgame.domain.model.AnswerResponse
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse

interface AnalysisGameRepository {

    suspend fun registerUser(userRequest: UserRequest): UserResponse

    suspend fun createAnswer(answerRequest: AnswerRequest): AnswerResponse

    suspend fun getUserByUsername(username: String): UserResponse



}