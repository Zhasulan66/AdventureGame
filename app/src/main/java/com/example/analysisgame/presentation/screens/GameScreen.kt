package com.example.analysisgame.presentation.screens

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.analysisgame.presentation.game.GamePanel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun GameScreen(
    navController: NavController,
    context: Context
){

    val gamePanel = GamePanel(context)
    /*Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.LightGray)
    ){
        //Text("Welcome game!")
        //gamePanel
    }*/

    AndroidView(
        factory = { gamePanel },
        modifier = Modifier.fillMaxSize()
    )

}