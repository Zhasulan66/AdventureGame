package com.example.analysisgame.presentation.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.data.local.UserPreferences
import com.example.analysisgame.data.local.generateUniqueUsername
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse
import com.example.analysisgame.presentation.navigation.Screen
import com.example.analysisgame.presentation.viewmodel.MainViewModel

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
                        popUpTo(
                            navController.previousBackStackEntry?.destination?.id ?: return@navigate
                        )
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
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.village_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
            // contentAlignment = Alignment.BottomCenter
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                StrokedText(
                    text = "Shards of Sanity",
                    textColor = Color.White,
                    strokeColor = Color.Red,
                    myStrokeWidth = 6f,
                    fontSize = 40.sp,
                    modifier = Modifier.offset(x = (-150).dp, y = (-40).dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Green)
                    .border(4.dp, Color(0xFF096900), RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate(Screen.ProfileScreen.route + "/${userResponse.username}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Cyan)
                    .border(4.dp, Color(0xFF003454), RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate(Screen.SettingsScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Settings",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Yellow)
                    .border(4.dp, Color(0xFFADA700), RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate(Screen.ChooseLevelScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Play",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


        }
    }

}

@Composable
fun StrokedText(
    text: String,
    fontSize: TextUnit = 32.sp,
    textColor: Color = Color.White,
    strokeColor: Color = Color.Yellow,
    myStrokeWidth: Float = 8f, // in pixels
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val paint = Paint().apply {
            isAntiAlias = true
            textSize = fontSize.toPx()
            style = android.graphics.Paint.Style.STROKE
            color = strokeColor.toArgb()
            strokeWidth = myStrokeWidth
            //strokeWidth = this@StrokedText.strokeWidth
        }

        val fillPaint = Paint().apply {
            isAntiAlias = true
            textSize = fontSize.toPx()
            style = android.graphics.Paint.Style.FILL
            color = textColor.toArgb()
        }

        // Center the text
        val textX = 0f
        val textY = fontSize.toPx()

        drawContext.canvas.nativeCanvas.apply {
            drawText(text, textX, textY, paint)     // Stroke
            drawText(text, textX, textY, fillPaint) // Fill
        }
    }
}