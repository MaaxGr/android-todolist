package com.grossmax.androidtodolist.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun ViewItemScreen(navController: NavController, id: Int) {
    Text(text = "View Item $id")
}