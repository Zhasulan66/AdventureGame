package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (
    navController: NavController
){
    //val viewModel = hiltViewModel<MainViewModel>()
    //val savedUserId: String? by viewModel.readUserId().collectAsState(initial = null)
    val savedUserId = 1

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_skateboarding_24),
            contentDescription = "img",
            modifier = Modifier.fillMaxWidth(0.44f),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Book seats at your favorite establishment",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = INTER_FONT_FAMILY,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth(0.6f)

        )
    }

    // Navigate to the home screen after a delay
    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate(if(savedUserId == null) Screen.LoginScreen.route else Screen.HomeScreen.route) {
            popUpTo(Screen.SplashScreen.route) {
                inclusive = true
            }
        }
    }

}