package com.example.analysisgame.domain.gamestates

import com.example.analysisgame.presentation.game.Game

abstract class BaseState(private val game: Game) {
    fun getGame(): Game{
        return game
    }
}