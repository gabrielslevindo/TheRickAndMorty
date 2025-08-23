@file:Suppress("FunctionNaming", "NoWildcardImports")

package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.CharacterList
import com.example.therickandmorty.presentation.components.NameFilterField
import com.example.therickandmorty.presentation.components.ShimmerItem
import com.example.therickandmorty.presentation.components.StatusFilter
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

object SearchScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SearchViewModel = koinViewModel()

        LaunchedEffect(Unit) {
            viewModel.loadCharacters()
        }

        var nameFilter by remember {
            mutableStateOf(viewModel.nameFilter.value ?: "")
        }
        var selectedStatusFilter by remember {
            mutableStateOf(viewModel.statusFilter.value ?: "")
        }

        val characterStatusList = listOf("Alive", "Dead", "Unknown")

        val characters: LazyPagingItems<CharacterDto> = viewModel.charactersFlow.collectAsLazyPagingItems()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Characters",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
            ) {
                NameFilterField(
                    value = nameFilter,
                    onValueChange = { newName ->
                        nameFilter  = newName
                        viewModel.applyFilters(nameFilter, selectedStatusFilter)
                    },
                )
                StatusFilter(
                    statuses = characterStatusList,
                    selectedStatus = selectedStatusFilter,
                    onStatusSelected = { status ->
                        selectedStatusFilter = status
                        viewModel.applyFilters(nameFilter, selectedStatusFilter)
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        characters.loadState.refresh is LoadState.Loading -> {
                            ShimmerItem()
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
                            CharacterList(
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
}
