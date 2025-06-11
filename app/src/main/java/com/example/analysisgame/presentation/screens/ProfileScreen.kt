package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.model.UserResponse
import com.example.analysisgame.presentation.navigation.Screen
import com.example.analysisgame.presentation.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userName: String
){
    val context = LocalContext.current
    val viewModel = hiltViewModel<MainViewModel>()
    val profileState by viewModel.profileState.collectAsState()

    when (profileState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Error -> {
            ErrorScreen(
                modifier = Modifier,
                retryAction = {
                    navController.navigate(route = navController.currentDestination?.route ?: "") {
                        popUpTo(navController.previousBackStackEntry?.destination?.id ?: return@navigate)
                    }
                }
            )
        }

        is Resource.Success -> {
            val response = (profileState as Resource.Success<UserResponse>).data
            ProfileSuccessScreen(
                navController = navController,
                userResponse = response
            )
        }

        is Resource.Initial -> {
            // Just show a placeholder UI, logic handled in LaunchedEffect above
            //viewModel.getUserResult(userName)
            ProfileSuccessScreen(
                navController = navController,
                userResponse = UserResponse(0, "", "", null)
            )
        }
    }
}

@Composable
fun ProfileSuccessScreen(
    navController: NavController,
    userResponse: UserResponse
){
    val result = userResponse.result ?: "empty"
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.valley_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(4.dp, Color(0xFF009688), RoundedCornerShape(10.dp))
                .background(Color(0xFF4CAF50)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Welcome to Profile Screen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Your Result is $result",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (-320).dp, y = (-140).dp)
                .width(60.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF4CAF50))
                .clickable {
                    navController.navigate(Screen.HomeScreen.route){
                        popUpTo(0) { inclusive = true } // clears entire backstack
                        launchSingleTop = true
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Back",
                color = Color.White
            )
        }
    }
}