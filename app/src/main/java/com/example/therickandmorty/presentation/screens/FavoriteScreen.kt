@file:Suppress("FunctionNaming", "NoWildcardImports")

package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.CharacterListItem
import com.example.therickandmorty.presentation.components.ShimmerItem
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

object FavoriteScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: FavoritesViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.loadFavorites()
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Favorites",
                            fontSize = 24.sp,
                        )
                    },
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
                    state.isLoading -> {
                        ShimmerItem()
                    }
                    state.isError != null -> {
                        Text(
                            text = state.isError ?: "Erro desconhecido",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    state.SuccessApiList.isEmpty() -> {
                        Text(
                            text = "Nenhum favorito encontrado",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    else -> {
                        FavoriteList(
                            characters = state.SuccessApiList,
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
fun FavoriteList(
    characters: List<CharacterDto>,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(characters) { character ->
            CharacterListItem(
                character = character,
                onItemClick = { onCharacterClick(character) },
            )
        }
    }
}
