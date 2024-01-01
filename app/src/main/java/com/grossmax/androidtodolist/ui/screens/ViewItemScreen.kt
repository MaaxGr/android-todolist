package com.grossmax.androidtodolist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grossmax.androidtodolist.R
import com.grossmax.androidtodolist.ui.theme.AppDarkGray
import com.grossmax.androidtodolist.ui.theme.AppNearlyWhite
import com.grossmax.androidtodolist.ui.theme.Purple40
import com.grossmax.androidtodolist.ui.theme.Typography


@Composable
fun ViewItemScreen(
    navController: NavController,
    itemId: Int,
    viewModel: ViewItemScreenViewModel = viewModel()
) {

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppDarkGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppNearlyWhite,
                        )
                    }
                    Text(
                        text = "Tasks",
                        color = AppNearlyWhite,
                        style = Typography.headlineSmall
                    )
                }

                val title by viewModel.taskTitle.collectAsState()

                Column(
                    modifier = Modifier
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    val checked by viewModel.checkedState.collectAsState()

                    TitleRow(
                        checked = checked,
                        title = title,
                        onTitleChange = { viewModel.setTitle(it) },
                        onCheckClick = { viewModel.updateToDoChecked() }
                    )

                    BottomArea(
                        onDeleteClick = {
                            viewModel.deleteToDo()
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    )

}

@Composable
fun TitleRow(
    checked: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    onCheckClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                if (checked) R.drawable.check_circle_24px else R.drawable.circle_24px
            ),
            contentDescription = "Complete",
            tint = if (checked) Purple40 else AppNearlyWhite,
            modifier = Modifier
                .clickable {
                    onCheckClick()
                }
                .padding(end = 8.dp)
                .size(32.dp)
        )


        BasicTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = Typography.headlineMedium.copy(color = AppNearlyWhite),
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview
@Composable
fun TitleRowPreview() {
    TitleRow(
        checked = false,
        title = "Test",
        onTitleChange = {},
        onCheckClick = {}
    )
}


@Composable
fun BottomArea(
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {

        Divider(
            color = AppNearlyWhite,
            thickness = 1.dp,
            modifier = Modifier
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Created X hours ago", color = AppNearlyWhite)

            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete",
                tint = AppNearlyWhite,
                modifier = Modifier
                    .clickable {
                        onDeleteClick()
                    }
            )
        }
    }
}

@Preview
@Composable
fun BottomAreaPreview() {
    Column(
        modifier = Modifier
            .background(AppDarkGray),
    ) {
        BottomArea(
            onDeleteClick = {}
        )
    }

}
