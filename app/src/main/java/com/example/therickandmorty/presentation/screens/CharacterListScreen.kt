package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import org.koin.androidx.compose.koinViewModel

object CharacterListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: CharacterListViewModel = koinViewModel()
        val characters = viewModel.charactersFlow.collectAsLazyPagingItems()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Characters") },
                )
            },
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            ) {
                if (characters.itemCount == 0 &&
                    characters.loadState.refresh is androidx.paging.LoadState.Loading
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    characterList(
                        isPaged = true,
                        pagedCharacters = characters,
                    ) { character ->
                        navigator.push(CharacterDetailsScreen(character))
                    }
                }
            }
        }
    }
}
