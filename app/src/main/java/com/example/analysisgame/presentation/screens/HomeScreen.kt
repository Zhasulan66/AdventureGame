package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.data.local.UserPreferences
import com.example.analysisgame.data.local.generateUniqueUsername
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse
import com.example.analysisgame.presentation.navigation.Screen
import com.example.analysisgame.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val username by UserPreferences.getUsername(context).collectAsState(initial = null)
    val viewModel = hiltViewModel<MainViewModel>()
    val userState by viewModel.userState.collectAsState()

    // Run only once if username is null
    LaunchedEffect(username) {
        if (username == null) {
            val uniqueUsername = generateUniqueUsername()
            UserPreferences.saveUsername(context, uniqueUsername)
            viewModel.registerUser(UserRequest(uniqueUsername))
        } else {
            viewModel.successUserState(username!!)
        }
    }

    when (userState) {
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
            val response = (userState as Resource.Success<UserResponse>).data
            UserSuccessScreen(
                navController = navController,
                userResponse = response
            )
        }

        is Resource.Initial -> {
            // Just show a placeholder UI, logic handled in LaunchedEffect above
            Text("Initializing...")
        }
    }
}

@Composable
fun UserSuccessScreen(
    navController: NavController,
    userResponse: UserResponse
){
    val viewModel = hiltViewModel<MainViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        // contentAlignment = Alignment.BottomCenter
    ){
        Spacer(modifier = Modifier.height(60.dp))
        Text("Welcome to Home Screen")
        Spacer(modifier = Modifier.height(20.dp))
        if (userResponse.username != null) {
            Text(text = "Welcome back, ${userResponse.username}!")
        } else {
            Text(text = "Saving default username...")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .width(200.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Yellow)
                    .clickable {
                        navController.navigate(Screen.ChooseLevelScreen.route)
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
}