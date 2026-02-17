package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.appHeader
import com.example.therickandmorty.presentation.components.characterListItem
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

object FavoriteScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: FavoritesViewModel = koinViewModel()

        val uiState by viewModel.state.collectAsStateWithLifecycle()

        favoriteContent(
            isLoading = uiState.state.isLoading,
            error = uiState.state.isError,
            characters = uiState.state.successApiList,
            onCharacterClick = {
                navigator.push(CharacterDetailsScreen(it))
            },
        )
    }
}

@Composable
fun favoriteContent(
    isLoading: Boolean,
    error: String?,
    characters: List<CharacterDto>,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    Scaffold(
        topBar = {
            appHeader(title = "Favorites")
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
                isLoading -> {
                    shimmerItem()
                }

                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                characters.isEmpty() -> {
                    Text(
                        text = "Nenhum favorito encontrado",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    favoriteList(
                        characters = characters,
                        onCharacterClick = onCharacterClick,
                    )
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

@Preview(showBackground = true)
@Composable
fun favoriteEmptyPreview() {
    MaterialTheme {
        favoriteContent(
            isLoading = false,
            error = null,
            characters = emptyList(),
            onCharacterClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun favoriteLoadingPreview() {
    MaterialTheme {
        favoriteContent(
            isLoading = true,
            error = null,
            characters = emptyList(),
            onCharacterClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun favoriteErrorPreview() {
    MaterialTheme {
        favoriteContent(
            isLoading = false,
            error = "Erro ao carregar favoritos",
            characters = emptyList(),
            onCharacterClick = {},
        )
    }
}
