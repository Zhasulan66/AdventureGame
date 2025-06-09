package com.example.analysisgame.common

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.analysisgame.R

class Constants {

    companion object {
        val INTER_FONT_FAMILY = FontFamily(
            Font(R.font.inter_regular),
            Font(R.font.inter_bold, FontWeight.Bold),
            Font(R.font.inter_semibold, FontWeight.SemiBold),
            Font(R.font.inter_medium, FontWeight.Medium),
            Font(R.font.inter_light, FontWeight.Light),
            Font(R.font.inter_extralight, FontWeight.ExtraLight),
        )
    }

    object FaceDir {
        const val DEATH_R: Int = 2
        const val DEATH_L: Int = 3
        const val LEFT: Int = 1
        const val RIGHT: Int = 0
        const val IDLE_RIGHT: Int = 4
        const val IDLE_LEFT: Int = 5
    }

    object Sprite {
        const val SMALLER_SIZE = 16
        const val DEFAULT_SIZE = 32
        const val SCALE_MULTIPLIER = 6
        const val SIZE = SMALLER_SIZE * SCALE_MULTIPLIER
    }

    object Animation {
        const val SPEED: Int = 10
        const val AMOUNT: Int = 4
    }

    object BossState {
        const val IDLE = 0
        const val ATTACK = 1
        const val MOVE = 2
        const val DEATH = 3
    }
}