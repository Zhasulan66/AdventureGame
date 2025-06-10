package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.model.UserResponse
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
            viewModel.getUserResult(userName)
        }
    }
}

@Composable
fun ProfileSuccessScreen(
    navController: NavController,
    userResponse: UserResponse
){
    val result = userResponse.result ?: "empty"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        // contentAlignment = Alignment.BottomCenter
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("Welcome to Profile Screen")
        Spacer(modifier = Modifier.height(20.dp))
        Text("Your Result is $result")
    }
}