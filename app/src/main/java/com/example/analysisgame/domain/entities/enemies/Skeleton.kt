package com.example.analysisgame.domain.entities.enemies

import android.graphics.PointF
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Character
import com.example.analysisgame.domain.entities.GameCharacters
import java.util.Random

class Skeleton(
    pos: PointF
) : Character(pos, GameCharacters.SKELETON) {
    private var lastDirChange = System.currentTimeMillis()
    private var rand = Random()

    fun update(delta: Double) {
        updateMove(delta)
        updateAnimation()
    }

    private fun updateMove(delta: Double) {
        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            setFaceDir(rand.nextInt(4))
            lastDirChange = System.currentTimeMillis()
        }

        when (getFaceDir()) {
            Constants.FaceDir.DEATH_R -> {
                hitbox.top += (delta * 300).toFloat()
                if (hitbox.top >= GAME_HEIGHT - 200) setFaceDir(Constants.FaceDir.DEATH_L)
            }

            Constants.FaceDir.DEATH_L -> {
                hitbox.top -= (delta * 300).toFloat()
                if (hitbox.top <= 0) setFaceDir(Constants.FaceDir.DEATH_R)
            }

            Constants.FaceDir.RIGHT -> {
                setDirSelector(0)
                hitbox.left += (delta * 300).toFloat()
                if (hitbox.left >= GAME_WIDTH - 100) setFaceDir(Constants.FaceDir.LEFT)
            }

            Constants.FaceDir.LEFT -> {
                setDirSelector(1)
                hitbox.left -= (delta * 300).toFloat()
                if (hitbox.left <= 0) setFaceDir(Constants.FaceDir.RIGHT)
            }
        }
    }

}