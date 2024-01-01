package com.grossmax.androidtodolist.ui.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.grossmax.androidtodolist.ui.MainActivity
import com.grossmax.androidtodolist.R
import com.grossmax.androidtodolist.dataaccess.TodoListRepository
import com.grossmax.androidtodolist.dataaccess.room.entity.ToDoEntity
import com.grossmax.androidtodolist.ui.theme.AppDarkGray
import com.grossmax.androidtodolist.ui.theme.AppDarkGrayDivider1
import com.grossmax.androidtodolist.ui.theme.AppDarkGrayDivider2
import com.grossmax.androidtodolist.utils.koinGet
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {

    private val coroutineScope = MainScope()
    private val viewModel: WidgetViewModel = koinGet()

    override val glanceAppWidget: GlanceAppWidget
        get() = MyAppWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i("MyAppWidgetReceiver", "onUpdate")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == "updateAction") {
            coroutineScope.launch {
                Log.i("MyAppWidgetReceiver", "onReceive")
                viewModel.loadTodoList()
            }
        }
    }

}

class MyAppWidget : GlanceAppWidget() {

    private val viewModel: WidgetViewModel = koinGet()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.i("MyAppWidget", "provideGlance")
        viewModel.init()
        viewModel.loadTodoList()

        provideContent {
            // create your AppWidget here
            MyContent()
        }
    }

    @Composable
    private fun MyContent() {
        val viewModel: WidgetViewModel = koinGet()
        val value by viewModel.todoItems.collectAsState()

        Column(
            modifier = GlanceModifier.fillMaxSize().cornerRadius(12.dp).background(AppDarkGray),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            HeaderComponent(addToDoItemCallback = {
                viewModel.addTodoItem(it)
            })

            WidgetDivider(color = AppDarkGrayDivider2)
            BodyComponent(value = value)


        }
    }

    @Composable
    private fun WidgetDivider(color: Color) {
        Box(
            modifier = GlanceModifier.background(color).height(1.dp)
                .fillMaxWidth()
        ) {
            Text(text = "hi")
        }
    }

    @Composable
    private fun HeaderComponent(addToDoItemCallback: (String) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth().padding(8.dp)
        ) {

            // Left Area
            Row(
                horizontalAlignment = Alignment.Start,
                modifier = GlanceModifier.defaultWeight()
                    .padding(start = 8.dp)
                    .clickable(actionStartComposeView("home"))
            ) {
                Text(
                    text = "ToDos",
                    style = TextStyle(color = ColorProvider(Color.White)),
                )
            }

            // Right Area
            Row(
                horizontalAlignment = Alignment.End,
                modifier = GlanceModifier.defaultWeight()
            ) {
                Image(
                    provider = ImageProvider(R.drawable.sync_24px),
                    contentDescription = "Refresh",
                    modifier = GlanceModifier.padding(end = 12.dp).clickable {
                        actionRunCallback<RefreshAction>()
                    },
                )

                Image(
                    provider = ImageProvider(R.drawable.settings_24px),
                    contentDescription = "Settings",
                    modifier = GlanceModifier.padding(end = 12.dp),
                )

                Image(
                    provider = ImageProvider(R.drawable.add_24px),
                    contentDescription = "Add",
                    modifier = GlanceModifier.clickable(actionStartComposeView("add"))
                )
            }
        }
    }

    @Composable
    private fun BodyComponent(value: List<TodoListRepository.ToDoItem>) {
        LazyColumn(modifier = GlanceModifier) {
            value.forEachIndexed { index, it ->
                item {
                    Column {
                        val context = LocalContext.current
                        Row(
                            modifier = GlanceModifier.fillMaxWidth().padding(8.dp)
                                .clickable {
                                    val intent = Intent(context,
                                        MainActivity::class.java
                                    ).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                        putExtra(
                                            "navigateTo",
                                            "view/${it.uid}"
                                        )
                                        Log.i("MyAppWidget", "BodyComponent: $index")
                                    }
                                    Log.i("MyAppWidget", "BodyComponent: $index")
                                    context.startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.circle_24px),
                                contentDescription = "Check",
                                modifier = GlanceModifier.clickable {
                                    viewModel.checkTask(it)
                                }
                            )
                            Text(
                                text = it.title,
                                modifier = GlanceModifier.padding(start = 8.dp),
                                style = TextStyle(color = ColorProvider(Color.White))
                            )
                        }

                        if (index != value.size - 1) {
                            WidgetDivider(color = AppDarkGrayDivider1)
                        }
                    }


                }
            }
        }

    }

    class RefreshAction : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
            Log.i("MyAppWidget", "RefreshAction")
            // do some work but offset long-term tasks (e.g a Worker)
            MyAppWidget().update(context, glanceId)
        }
    }
}

@Composable
private fun actionStartComposeView(view: String): Action {
    return androidx.glance.appwidget.action.actionStartActivity(
        Intent(
            LocalContext.current,
            MainActivity::class.java
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("navigateTo", view)
        }
    )
}