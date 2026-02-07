package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.presentation.components.AppHeader
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.components.nameFilterField
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.components.statusFilter
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.SearchAction
import com.example.therickandmorty.presentation.viewmodel.states.SearchState
import org.koin.androidx.compose.koinViewModel

object SearchScreen : Screen {
    @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: SearchViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()
        SearchScreenContent(
            state = state,
            action = viewModel::onAction
        )
    }
}

@Composable
fun SearchScreenContent (
    state: SearchState,
    action: (SearchAction) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    var nameFilter by remember {
        mutableStateOf(state.name ?: "")
    }
    var selectedStatusFilter by remember {
        mutableStateOf(state.status ?: "")
    }

    val characterStatusList = listOf("Alive", "Dead", "Unknown")

    val characters = state.characters.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            AppHeader(title = "Characters")
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            nameFilterField(
                value = nameFilter,
                onValueChange = { newName ->
                    nameFilter = newName
                    action(SearchAction.ApplyFilters(name = nameFilter, status = selectedStatusFilter))
                },
            )
            statusFilter(
                statuses = characterStatusList,
                selectedStatus = selectedStatusFilter,
                onStatusSelected = { status ->
                    selectedStatusFilter = status
                    action(SearchAction.ApplyFilters(name = nameFilter, status = selectedStatusFilter))
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .background(MaterialTheme.colorScheme.background)) {
                when {
                    characters.loadState.refresh is LoadState.Loading -> {
                        shimmerItem()
                    }
                    characters.loadState.refresh is LoadState.Error -> {
                        val e = characters.loadState.refresh as LoadState.Error
                        Text(
                            text = e.error.message ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    else -> {
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
}