package com.grossmax.androidtodolist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.updateAll
import androidx.navigation.compose.rememberNavController
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.ui.composables.AppNavHost
import com.grossmax.androidtodolist.ui.theme.AndroidToDoListTheme
import com.grossmax.androidtodolist.ui.widgets.TaskWidget
import com.grossmax.androidtodolist.ui.widgets.TaskWidgetReceiver
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val todoListRepository: TodoListRepository by koinInject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this
        todoListRepository.addChangeListener {
            val intent = Intent(context, TaskWidgetReceiver::class.java).apply {
                action = "updateAction"
            }
            context.sendBroadcast(intent)
        }

        setContent {
            AndroidToDoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val explicitNavigateTo = intent.extras?.getString("navigateTo")

                    Log.i("MainActivity", "navigateTo: $explicitNavigateTo")

                    AppNavHost(navController, explicitNavigateTo)
                    LaunchedEffect(key1 = Unit) {
                        if (explicitNavigateTo != null) {
                            navController.popBackStack()
                            navController.navigate(explicitNavigateTo)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        GlobalScope.launch {
            Log.i("MainActivity", "onPause")
            TaskWidget().updateAll(applicationContext)
        }
    }

    override fun onResume() {
        super.onResume()

        val explicitNavigateTo = intent?.extras?.getString("navigateTo")

        Log.i("MainActivity", "resume: $explicitNavigateTo")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val explicitNavigateTo = intent?.extras?.getString("navigateTo")

        Log.i("MainActivity", "new intent: $explicitNavigateTo")
    }
}