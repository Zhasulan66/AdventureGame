package com.example.analysisgame.domain.entities

import android.graphics.PointF
import android.graphics.RectF
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.common.Constants

class Player() : Character(
    PointF((GAME_WIDTH / 2).toFloat() - Constants.Sprite.SIZE,
        (GAME_HEIGHT / 2).toFloat() - Constants.Sprite.SIZE) ,
    GameCharacters.ROGUE)
{
    fun update(delta: Double, movePlayer: Boolean) {
        if (!movePlayer) {
           when(getFaceDir()){
               Constants.FaceDir.RIGHT -> setFaceDir(Constants.FaceDir.IDLE_RIGHT)
               Constants.FaceDir.LEFT -> setFaceDir(Constants.FaceDir.IDLE_LEFT)
           }
        }
        updateAnimation()
    }
}