package com.luczka.mycoffee.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

@Composable
fun BackIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
    }
}

@Composable
fun CloseIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
    }
}

@Composable
fun TopAppBarTitle(text: String) {
    Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
fun FavouriteToggleButton(checked: Boolean, onCheckedChange: () -> Unit) {
    IconButton(
        onClick = onCheckedChange,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        content = {
            if (checked) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
            } else {
                Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = null)
            }
        }
    )
}

@Composable
fun EditIconButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        content = { Icon(imageVector = Icons.Filled.Edit, contentDescription = null) }
    )
}

@Composable
fun DeleteIconButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        content = { Icon(imageVector = Icons.Filled.Delete, contentDescription = null) }
    )
}

@Preview
@Composable
fun TopBarComponentsLightPreview() {
    CenterAlignedTopAppBarPreview(darkTheme = false)
}

@Preview
@Composable
fun TopBarComponentsDarkPreview() {
    CenterAlignedTopAppBarPreview(darkTheme = true)
}

@Preview
@Composable
fun LightThemeTopAppBarWithBackIconPreview() {
    TopAppBarPreview(darkTheme = false, navigationIcon = { BackIconButton {} })
}

@Preview
@Composable
fun DarkThemeTopAppBarWithBackIconPreview() {
    TopAppBarPreview(darkTheme = true, navigationIcon = { BackIconButton {} })
}

@Preview
@Composable
fun LightThemeTopAppBarWithCloseIconPreview() {
    TopAppBarPreview(darkTheme = false, navigationIcon = { CloseIconButton {} })
}


@Preview
@Composable
fun DarkThemeTopAppBarWithCloseIconPreview() {
    TopAppBarPreview(darkTheme = true, navigationIcon = { CloseIconButton {} })
}

@Preview
@Composable
fun LightThemeTopAppBarWithCBackIconAndActionsDislikedPreview() {
    TopAppBarPreview(
        darkTheme = false,
        navigationIcon = { BackIconButton {} },
        actions = {
            FavouriteToggleButton(checked = false, onCheckedChange = {})
            EditIconButton {}
            DeleteIconButton {}
        }
    )
}

@Preview
@Composable
fun LightThemeTopAppBarWithCBackIconAndActionsLikedPreview() {
    TopAppBarPreview(
        darkTheme = false,
        navigationIcon = { BackIconButton {} },
        actions = {
            FavouriteToggleButton(checked = true, onCheckedChange = {})
            EditIconButton {}
            DeleteIconButton {}
        }
    )
}

@Preview
@Composable
fun DarkThemeTopAppBarWithCBackIconAndActionsDislikedPreview() {
    TopAppBarPreview(
        darkTheme = true,
        navigationIcon = { BackIconButton {} },
        actions = {
            FavouriteToggleButton(checked = false, onCheckedChange = {})
            EditIconButton {}
            DeleteIconButton {}
        }
    )
}

@Preview
@Composable
fun DarkThemeTopAppBarWithCBackIconAndActionsLikedPreview() {
    TopAppBarPreview(
        darkTheme = true,
        navigationIcon = { BackIconButton {} },
        actions = {
            FavouriteToggleButton(checked = true, onCheckedChange = {})
            EditIconButton {}
            DeleteIconButton {}
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CenterAlignedTopAppBarPreview(darkTheme: Boolean) {
    MyCoffeeTheme(darkTheme = darkTheme) {
        CenterAlignedTopAppBar(
            title = { TopAppBarTitle(text = stringResource(id = R.string.app_name_short)) }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBarPreview(
    darkTheme: Boolean,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    MyCoffeeTheme(darkTheme = darkTheme) {
        TopAppBar(
            navigationIcon = navigationIcon,
            title = { TopAppBarTitle(text = stringResource(id = R.string.app_name_full)) },
            actions = actions
        )
    }
}
