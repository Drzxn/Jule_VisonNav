package com.example.indoornavigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.indoornavigation.ui.screens.MainScreen
import com.example.indoornavigation.ui.screens.MapEditorScreen
import com.example.indoornavigation.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("map_editor") { MapEditorScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
