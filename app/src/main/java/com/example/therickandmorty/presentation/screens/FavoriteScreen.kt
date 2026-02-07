package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.AppHeader
import com.example.therickandmorty.presentation.components.characterListItem
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

object FavoriteScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: FavoritesViewModel = koinViewModel()

        val uiState by viewModel.state.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                AppHeader(
                    title = "Favorites"
                )
            },
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
            ) {
                when {
                    uiState.state.isLoading -> {
                        shimmerItem()
                    }
                    uiState.state.isError != null -> {
                        Text(
                            text = uiState.state.isError ?: "Erro desconhecido",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    uiState.state.successApiList.isEmpty() -> {
                        Text(
                            text = "Nenhum favorito encontrado",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    else -> {
                        favoriteList(
                            characters = uiState.state.successApiList,
                            onCharacterClick = { character ->
                                navigator.push(CharacterDetailsScreen(character))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Suppress("FunctionNaming")
@Composable
fun favoriteList(
    characters: List<CharacterDto>,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(characters) { character ->
            characterListItem(
                character = character,
                onItemClick = { onCharacterClick(character) },
            )
        }
    }
}
