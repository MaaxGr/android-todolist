package com.grossmax.androidtodolist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grossmax.androidtodolist.R
import com.grossmax.androidtodolist.data.database.entity.ToDoEntity
import com.grossmax.androidtodolist.ui.theme.AndroidToDoListTheme
import com.grossmax.androidtodolist.ui.theme.AppDarkGray
import com.grossmax.androidtodolist.ui.theme.AppNearlyWhite
import com.grossmax.androidtodolist.ui.theme.Purple40
import com.grossmax.androidtodolist.ui.theme.Typography
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                viewModel.reload()
            }
            else -> {}
        }
    }

    val tasks by viewModel.testState.collectAsState()

    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Text(text = "Tasks", color = Purple40, style = Typography.headlineLarge)
        HomeScreenContent(
            todoEntities = tasks,
            onTodoCheckClicked = {
                viewModel.clickToDoCheck(it)
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    todoEntities: List<ToDoEntity>,
    onTodoCheckClicked: (ToDoEntity) -> Unit
) {
    LazyColumn {
        if (todoEntities.isNotEmpty()) {
            items(todoEntities.size) { index ->
                val task = todoEntities[index]

                TaskCard(
                    task = task,
                    checkClick = {
                        onTodoCheckClicked(task)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskCard(task: ToDoEntity, checkClick: () -> Unit) {
    Box(modifier = Modifier.padding(bottom = 2.dp)) {
        Row(
            modifier = Modifier
                .background(AppDarkGray)
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {

            val checked = task.checkedAt != null

            Icon(
                painter = painterResource(
                    if (checked) R.drawable.check_circle_24px else R.drawable.circle_24px
                ),
                contentDescription = "Complete",
                tint = if (checked) Purple40 else AppNearlyWhite,

                modifier = Modifier
                    .clickable {
                        checkClick()
                    }
                    .padding(end = 8.dp)
            )

            Text(
                text = task.title,
                style = Typography.bodyMedium.copy(color = AppNearlyWhite),
                textDecoration = if (checked) TextDecoration.LineThrough else null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreviewUnchecked() {
    TaskCard(
        task = ToDoEntity(
            0, "Test", null,
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        ),
        checkClick = {

        }
    )

}

@Preview(showBackground = true)
@Composable
fun TaskCardPreviewChecked() {

    TaskCard(
        task = ToDoEntity(
            uid = 0,
            title = "Test",
            checkedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        ),
        checkClick = {
        }
    )

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AndroidToDoListTheme {
        HomeScreen()
    }
}