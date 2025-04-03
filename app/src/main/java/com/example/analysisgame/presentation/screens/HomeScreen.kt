package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.domain.model.LoremText
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.presentation.navigation.Screen
import com.example.analysisgame.presentation.viewmodel.MainViewModel

@Composable
fun HomeScreen (
    navController: NavController
){
    /*val viewModel = hiltViewModel<MainViewModel>()
    val loremState by viewModel.loremState.collectAsState()

    when (loremState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Error -> {
            ErrorScreen(
                modifier = Modifier,
                retryAction = {
                    navController.navigate(route = navController.currentDestination?.route ?: ""){
                        popUpTo(navController.previousBackStackEntry?.destination?.id ?: return@navigate)
                    }
                }
            )
        }

        is Resource.Success -> {
            val lorem = (loremState as Resource.Success<LoremText>).data
            LoremSuccessScreen(
                navController = navController,
                lorem = lorem.text
            )
        }

        is Resource.Initial -> {
            //viewModel.fetchLoremWithNum("100")
        }
    }*/
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.BottomCenter
    ){
        Box(
            modifier = Modifier
                .offset(y = (-40).dp)
                .width(200.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Yellow)
                .clickable {
                    navController.navigate(Screen.GameScreen.route)
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Play",
                fontFamily = INTER_FONT_FAMILY,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun LoremSuccessScreen(
    navController: NavController,
    lorem: String
){
    Text(lorem)
}