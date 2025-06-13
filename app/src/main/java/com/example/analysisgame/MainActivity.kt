package com.example.analysisgame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.analysisgame.domain.gamestates.MusicManager
import com.example.analysisgame.domain.gamestates.SettingsManager
import com.example.analysisgame.presentation.navigation.Navigation
import com.example.analysisgame.presentation.ui.theme.AnalysisGameTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var gameContext: Context

        var GAME_WIDTH = 0
        var GAME_HEIGHT = 0

        fun getGameContext(): Context {
            return gameContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameContext = this

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)

        GAME_WIDTH = dm.widthPixels
        GAME_HEIGHT = dm.heightPixels

        // Load settings ONCE when app starts
        lifecycleScope.launch {
            SettingsManager.loadSettings(applicationContext)
        }

        //enableEdgeToEdge()
        setContent {
            AnalysisGameTheme {
                Navigation(this)
            }
        }
    }

    override fun onBackPressed() {
        MusicManager.stopMusic() // stop music safely
        super.onBackPressed()
    }


}