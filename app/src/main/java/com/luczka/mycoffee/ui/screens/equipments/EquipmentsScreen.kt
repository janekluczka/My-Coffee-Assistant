package com.luczka.mycoffee.ui.screens.equipments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.icons.AddIcon

sealed class EquipmentsAction {
    data object NavigateToEquipmentInput : EquipmentsAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentsScreen(
    onAction: (EquipmentsAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val filterListState = rememberLazyListState()
    val equipmentListState = rememberLazyListState()

    val buttonExpanded by remember {
        derivedStateOf { equipmentListState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name_short),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(id = R.string.fab_add_equipment))
                },
                icon = {
                    AddIcon()
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                onClick = {
                    val action = EquipmentsAction.NavigateToEquipmentInput
                    onAction(action)
                },
                expanded = buttonExpanded
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

        }
    }
}