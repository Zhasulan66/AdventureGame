package com.example.analysisgame.domain.model

data class AnswerRequest (
    val userName: String,
    val level: Int,
    val questionNumber: Int,
    val answer: Int // 0 - 4
)
