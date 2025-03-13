package com.luczka.mycoffee.ui.screens.equipmentinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.CloseIcon

sealed class EquipmentInputAction {
    data object NavigateUp : EquipmentInputAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentInputScreen(
    onAction: (EquipmentInputAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val action = EquipmentInputAction.NavigateUp
                            onAction(action)
                        }
                    ) {
                        CloseIcon()
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.equipment_input_top_bar_title_add),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            // TODO: Add save action
                        }
                    ) {
                        Text(text = stringResource(id = R.string.dialog_action_save))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            LazyColumn(modifier = Modifier.weight(1f)) {

            }
            Divider()
        }
    }
}