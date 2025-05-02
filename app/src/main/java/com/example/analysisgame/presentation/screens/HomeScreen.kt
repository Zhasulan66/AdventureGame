package com.example.analysisgame.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var currentLevel by remember { mutableIntStateOf(1) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
       // contentAlignment = Alignment.BottomCenter
    ){
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
                        .clickable { currentLevel = level }
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$level level",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
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
                        fontSize = 18.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
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
                        navController.navigate(Screen.GameScreen.route + "/${currentLevel}")
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

@Composable
fun LoremSuccessScreen(
    navController: NavController,
    lorem: String
){
    Text(lorem)
}