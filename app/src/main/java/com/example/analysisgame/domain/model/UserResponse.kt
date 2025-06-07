package com.example.analysisgame.domain.model

data class UserResponse(
    val id: Int,
    val username: String,
    val registeredAt: String,
    val result: String?,
    val answers: List<Answer>?,
)
/*
"id": 4,
"username": "chotomate",
"registeredAt": "2025-06-07T20:08:39.0311121",
"result": null,
"answers": null*/
