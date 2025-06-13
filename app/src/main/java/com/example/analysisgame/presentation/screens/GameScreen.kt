package com.example.analysisgame.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.data.local.UserPreferences
import com.example.analysisgame.presentation.game.GamePanel
import com.example.analysisgame.presentation.viewmodel.MainViewModel

@Composable
fun GameScreen(
    navController: NavController,
    context: Context,
    level: Int
){
    val username by UserPreferences.getUsername(context).collectAsState(initial = null)

    if (username != null) {
        val viewModel = hiltViewModel<MainViewModel>()
        val gamePanel = remember {
            GamePanel(context, level, navController, username!!, viewModel)
        }

        AndroidView(
            factory = { gamePanel },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Optional: show a loading screen while waiting
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}