package com.grossmax.androidtodolist.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grossmax.androidtodolist.ui.theme.AppDarkGray
import com.grossmax.androidtodolist.ui.theme.AppNearlyWhite
import com.grossmax.androidtodolist.ui.theme.Typography

@Composable
fun AddItemScreen(
    navController: NavController,
    viewModel: AddItemScreenViewModel = viewModel(),
    finishOnLeave: Boolean
) {
    val context = LocalContext.current as? Activity

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppDarkGray)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (finishOnLeave) {
                            context?.finish()
                        } else {
                            navController.navigateUp()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppNearlyWhite,
                        )
                    }
                    Text(
                        text = "Add Task",
                        color = AppNearlyWhite,
                        style = Typography.headlineSmall
                    )
                }

                val title by viewModel.title.collectAsState()

                Column(modifier = Modifier.padding(8.dp)) {

                    TextField(
                        value = title,
                        onValueChange = { viewModel.setTitle(it) },
                        placeholder = { Text("Task title") },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                    )

                    Button(onClick = {
                        viewModel.save {
                            if (finishOnLeave) {
                                context?.finish()
                            } else {
                                navController.navigateUp()
                            }
                        }
                    }) {
                        Text(text = "Submit Task")
                    }


                }
            }
        }
    )



}