package com.example.therickandmorty.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.example.therickandmorty.presentation.screens.CharacterListScreen
import com.example.therickandmorty.presentation.screens.FavoriteScreen
import com.example.therickandmorty.presentation.screens.SearchScreen

@Composable
fun bottomNavigationBar(navigator: Navigator) {
    val currentScreen = navigator.lastItem

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp,
    ) {
        NavigationBarItem(
            selected = currentScreen is CharacterListScreen,
            onClick = { if (currentScreen !is CharacterListScreen) navigator.replaceAll(CharacterListScreen) },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Characters",
                    tint =
                        if (currentScreen is CharacterListScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            label = {
                Text(
                    "Characters",
                    color =
                        if (currentScreen is CharacterListScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            colors =
                androidx.compose.material3.NavigationBarItemDefaults.colors(
                    indicatorColor =
                        androidx.compose.material3.MaterialTheme.colorScheme.primary
                            .copy(alpha = 0.2f),
                ),
        )

        NavigationBarItem(
            selected = currentScreen is FavoriteScreen,
            onClick = { if (currentScreen !is FavoriteScreen) navigator.replaceAll(FavoriteScreen) },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint =
                        if (currentScreen is FavoriteScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            label = {
                Text(
                    "Favorite",
                    color =
                        if (currentScreen is FavoriteScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            colors =
                androidx.compose.material3.NavigationBarItemDefaults.colors(
                    indicatorColor =
                        androidx.compose.material3.MaterialTheme.colorScheme.primary
                            .copy(alpha = 0.2f),
                ),
        )

        NavigationBarItem(
            selected = currentScreen is SearchScreen,
            onClick = { if (currentScreen !is SearchScreen) navigator.replaceAll(SearchScreen) },
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint =
                        if (currentScreen is SearchScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            label = {
                Text(
                    "Search",
                    color =
                        if (currentScreen is SearchScreen) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            },
            colors =
                androidx.compose.material3.NavigationBarItemDefaults.colors(
                    indicatorColor =
                        androidx.compose.material3.MaterialTheme.colorScheme.primary
                            .copy(alpha = 0.2f),
                ),
        )
    }
}
