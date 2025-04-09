package com.example.analysisgame.domain.entities

import android.graphics.PointF
import android.graphics.RectF

abstract class Entity(
    pos: PointF,
    width: Float,
    height: Float
) {
    protected var hitbox: RectF = RectF(pos.x, pos.y, width, height)



}