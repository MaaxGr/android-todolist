package com.grossmax.androidtodolist.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.grossmax.androidtodolist.ui.screens.AddItemScreen
import com.grossmax.androidtodolist.ui.screens.HomeScreen
import com.grossmax.androidtodolist.ui.screens.ViewItemScreen
import com.grossmax.androidtodolist.ui.theme.AppDarkGray

@Composable
fun AppNavHost(navController: NavHostController, explicitNavigateTo: String? = null) {
    NavHost(navController = navController, startDestination = if (explicitNavigateTo != null) "blank" else "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("blank") {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(AppDarkGray)
            )
        }
        composable(
            route = "add?finishOnLeave={finishOnLeave}",
            arguments = listOf(
                navArgument("finishOnLeave") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val finishOnLeave = it.arguments?.getBoolean("finishOnLeave") ?: false
            AddItemScreen(
                navController = navController,
                finishOnLeave = finishOnLeave
            )
        }
        composable(
            route = "view/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) {
            ViewItemScreen(
                navController = navController,
                it.arguments!!.getInt("taskId")
            )
        }
    }

}