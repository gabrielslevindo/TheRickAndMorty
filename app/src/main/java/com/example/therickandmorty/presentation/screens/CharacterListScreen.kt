package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.presentation.components.AppHeader
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import org.koin.androidx.compose.koinViewModel

object CharacterListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: CharacterListViewModel = koinViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()

        val characters =
            state.value.characters.collectAsLazyPagingItems()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEAF5EF))
        ) {
            AppHeader(title = "Characters")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                characters.let {
                    if (
                        characters.itemCount == 0 &&
                        characters.loadState.refresh is LoadState.Loading
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        characterList(
                            isPaged = true,
                            pagedCharacters = characters,
                        ) { character ->
                            navigator.push(
                                CharacterDetailsScreen(character)
                            )
                        }
                    }
                }
            }
        }
    }
}

