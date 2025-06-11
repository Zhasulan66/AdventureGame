package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.presentation.navigation.Screen

@Composable
fun SettingsScreen(
    navController: NavController
){
    var isSoundOn by remember { mutableStateOf(true) }
    var isMusicOn by remember { mutableStateOf(true) }
    val options = listOf("EN", "RU", "KZ")
    var currentIndex by remember { mutableStateOf(0) }
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
                .width(200.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if(isSoundOn) Color.Green else Color.Red)
                    .border(4.dp, if(isSoundOn) Color(0xFF096900) else Color(0xFF850000), RoundedCornerShape(20.dp))
                    .clickable {
                        isSoundOn = !isSoundOn
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sound ${if(isSoundOn) "ON" else "OFF"}",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if(isMusicOn) Color.Green else Color.Red)
                    .border(4.dp, if(isMusicOn) Color(0xFF096900) else Color(0xFF850000), RoundedCornerShape(20.dp))
                    .clickable {
                        isMusicOn = !isMusicOn
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Music ${if(isMusicOn) "ON" else "OFF"}",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Green)
                    .border(4.dp, Color(0xFF096900), RoundedCornerShape(20.dp))
                    .clickable {
                        currentIndex = (currentIndex + 1) % options.size
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Language ${options[currentIndex]}",
                    fontFamily = INTER_FONT_FAMILY,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
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