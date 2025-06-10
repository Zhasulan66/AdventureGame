package com.example.analysisgame.data.repository

import com.example.analysisgame.data.remote.AnalysisGameApiService
import com.example.analysisgame.domain.model.AnswerRequest
import com.example.analysisgame.domain.model.AnswerResponse
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse
import com.example.analysisgame.domain.repository.AnalysisGameRepository

class AnalysisGameRepositoryImpl(
    private val api: AnalysisGameApiService
) : AnalysisGameRepository {

    override suspend fun registerUser(userRequest: UserRequest): UserResponse {
        return api.registerUser(userRequest)
    }

    override suspend fun createAnswer(answerRequest: AnswerRequest): AnswerResponse {
        return api.createAnswer(answerRequest)
    }

    override suspend fun getUserByUsername(username: String): UserResponse {
        return api.getUserByUsername(username)
    }

}