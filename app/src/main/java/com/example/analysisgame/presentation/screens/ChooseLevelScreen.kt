package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY
import com.example.analysisgame.presentation.navigation.Screen

@Composable
fun ChooseLevelScreen (
    navController: NavController
){
    var currentLevel by remember { mutableIntStateOf(1) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.sky_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // First 5 clickable levels
                items(5) { index ->
                    val level = index + 1
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (currentLevel == level) Color.Red else Color.LightGray)
                            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                            .clickable { currentLevel = level },
                            //.shadow(4.dp, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$level level",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    }
                }

                // Next 5 non-clickable levels
                items(5) { index ->
                    val level = index + 6
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$level level",
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 22.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = (-40).dp)
                    .width(200.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Yellow)
                    .border(4.dp, Color(0xFFADA700), RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate(Screen.GameScreen.route + "/${currentLevel}")
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