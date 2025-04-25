package com.example.analysisgame.common

import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class Utils {
    companion object {
        fun getDistanceBetweenPoints(p1x: Float, p1y: Float, p2x: Float, p2y: Float): Float {
            return sqrt((p1x - p2x).pow(2) + (p1y - p2y).pow(2))
        }

        fun circleIntersectsRect(
            circleX: Float,
            circleY: Float,
            circleRadius: Float,
            rect: RectF
        ): Boolean {
            // Calculate the distance between the center of the circle and the closest point on the rectangle
            val closestX: Float = clamp(circleX, rect.left, rect.right)
            val closestY: Float = clamp(circleY, rect.top, rect.bottom)
            val distance: Float =
                getDistanceBetweenPoints(circleX, circleY, closestX, closestY)

            // Check if the distance is less than the radius of the circle
            return distance < circleRadius
        }


        private fun clamp(value: Float, min: Float, max: Float): Float {
            return max(min, min(value, max))
        }
    }
}