package com.example.analysisgame.domain.entities

import android.graphics.PointF
import android.graphics.RectF
import com.example.analysisgame.common.Constants

abstract class Character(
    pos: PointF,
    private val gameCharType: GameCharacters
) : Entity(pos, 1f, 1f) {

    private var aniTick = 0
    private var aniIndex = 0
    private var faceDir = Constants.FaceDir.RIGHT
    private var dirSelector = 0

    protected fun updateAnimation() {
        aniTick++
        if (aniTick >= Constants.Animation.SPEED) {
            aniTick = 0
            aniIndex++
            if (aniIndex >= Constants.Animation.AMOUNT) aniIndex = 0
        }
    }

    fun resetAnimation() {
        aniTick = 0
        aniIndex = 0
    }

    fun getAniIndex(): Int {
        return aniIndex
    }

    open fun getFaceDir(): Int {
        return faceDir
    }

    fun setFaceDir(faceDir: Int) {
        this.faceDir = faceDir
    }

    fun getGameCharType(): GameCharacters {
        return gameCharType
    }

    fun getHitBox(): RectF {
        return hitbox
    }

    fun getDirSelector(): Int {
        return dirSelector
    }

    fun setDirSelector(dir: Int) {
        this.dirSelector = dir
    }

}