package com.example.analysisgame.presentation.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.analysisgame.presentation.LanguageManager
import com.example.analysisgame.presentation.screens.ChooseLevelScreen
import com.example.analysisgame.presentation.screens.GameScreen
import com.example.analysisgame.presentation.screens.HomeScreen
import com.example.analysisgame.presentation.screens.ProfileScreen
import com.example.analysisgame.presentation.screens.SettingsScreen
import com.example.analysisgame.presentation.screens.SplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Navigation(
    context: Context
) {
    val systemUiController = rememberSystemUiController()
    val window = (context as Activity).window

    DisposableEffect(Unit) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        // Enable immersive sticky mode
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        onDispose {
            controller.show(WindowInsetsCompat.Type.systemBars()) // Restore when exiting
        }
    }

    val navController = rememberNavController()
    val languageManager = LanguageManager(LocalContext.current)


    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        //SplashScreen
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(
                navController = navController
            )
        }

        //HomeScreen
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                navController = navController,
            )
        }

        //ChooseLevelScreen
        composable(route = Screen.ChooseLevelScreen.route){
            ChooseLevelScreen(
                navController = navController,
            )
        }

        //GameScreen
        composable(route = Screen.GameScreen.route + "/{level}",
            arguments = listOf(
                navArgument("level"){
                    type = NavType.IntType
                    defaultValue = 1
                    nullable = false
                }
            )){ entry ->
            GameScreen(
                navController = navController,
                context = context,
                level = entry.arguments!!.getInt("level")
            )
        }

        //ProfileScreen
        composable(route = Screen.ProfileScreen.route + "/{username}",
            arguments = listOf(
                navArgument("username"){
                    type = NavType.StringType
                    defaultValue = " "
                    nullable = false
                }
            )) { entry ->
            ProfileScreen(
                navController = navController,
                userName = entry.arguments!!.getString("username")!!
            )
        }

        //SettingsScreen
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(
                navController = navController,
            )
        }


    }
}