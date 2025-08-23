package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.example.therickandmorty.presentation.components.bottomNavigationBar

object MainScreen : Screen {
    @Composable
    override fun Content() {
        Navigator(CharacterListScreen) { navigator ->
            val currentScreen = navigator.lastItem

            Scaffold(
                bottomBar = {
                    if (currentScreen is CharacterListScreen ||
                        currentScreen is FavoriteScreen ||
                        currentScreen is SearchScreen
                    ) {
                        bottomNavigationBar(navigator)
                    }
                },
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    currentScreen.Content()
                }
            }
        }
    }
}
