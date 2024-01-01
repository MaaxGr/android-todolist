package com.grossmax.androidtodolist.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grossmax.androidtodolist.data.TodoListRepository
import com.grossmax.androidtodolist.ui.screens.AddToDoListItemScreen
import com.grossmax.androidtodolist.ui.screens.HomeScreen
import com.grossmax.androidtodolist.ui.screens.ViewItemScreen
import com.grossmax.androidtodolist.ui.theme.AndroidToDoListTheme
import com.grossmax.androidtodolist.ui.widgets.MyAppWidget
import com.grossmax.androidtodolist.ui.widgets.MyAppWidgetReceiver
import com.grossmax.androidtodolist.utils.koinInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainActivity : ComponentActivity() {

    private val todoListRepository: TodoListRepository by koinInject()

    private val mutex = Mutex()
    private val activityScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this
        todoListRepository.addChangeListener {
            val intent = Intent(context, MyAppWidgetReceiver::class.java).apply {
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

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController) }
                        composable("add") { AddToDoListItemScreen(navController) }
                        composable(
                            route = "view/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) {
                            ViewItemScreen(navController = navController, it.arguments!!.getInt("taskId"))
                        }
                    }

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
            MyAppWidget().updateAll(applicationContext)
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