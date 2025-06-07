package com.example.analysisgame.presentation.navigation

sealed class Screen(val route: String) {

    data object SplashScreen: Screen("splash_screen")

    data object LoginScreen : Screen("login_screen")

    data object HomeScreen : Screen("home_screen")

    data object ChooseLevelScreen : Screen("choose_level_screen")

    data object GameScreen : Screen("game_screen")


}