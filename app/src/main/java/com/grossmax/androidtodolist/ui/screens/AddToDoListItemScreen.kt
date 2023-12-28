package com.grossmax.androidtodolist.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.grossmax.androidtodolist.ui.widgets.WidgetViewModel
import com.grossmax.androidtodolist.utils.koinGet

@Composable
fun AddToDoListItemScreen(navController: NavController) {

    val viewModel: WidgetViewModel = koinGet()

    Column {
        var text by remember { mutableStateOf("Hello") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )
        
        val context = LocalContext.current as? Activity

        Button(onClick = {
            viewModel.addTodoItem(text)
            context?.finish()
        }) {

            Text(text = "finish")
        }

        Text(text = "Add")
    }


}