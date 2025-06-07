package com.example.analysisgame.domain.model

import java.time.LocalDateTime

data class Answer(
    val id: Long,
    val user: UserResponse,
    val level: Int,
    val questionNumber: Int,
    val answer: Int,
    val answeredAt: LocalDateTime,

    )
