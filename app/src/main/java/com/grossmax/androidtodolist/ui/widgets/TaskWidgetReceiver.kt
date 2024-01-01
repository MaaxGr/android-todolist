package com.grossmax.androidtodolist.ui.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.grossmax.androidtodolist.utils.koinGet
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TaskWidgetReceiver : GlanceAppWidgetReceiver() {

    private val coroutineScope = MainScope()
    private val viewModel: TaskWidgetViewModel = koinGet()

    override val glanceAppWidget: GlanceAppWidget
        get() = TaskWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i("TaskWidgetReceiver", "onUpdate")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == "updateAction") {
            coroutineScope.launch {
                Log.i("TaskWidgetReceiver", "onReceive")
                viewModel.loadTodoList()
            }
        }
    }

}