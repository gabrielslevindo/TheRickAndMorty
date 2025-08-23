package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.components.nameFilterField
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.components.statusFilter
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

object SearchScreen : Screen {
    @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
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
                nameFilterField(
                    value = nameFilter,
                    onValueChange = { newName ->
                        nameFilter = newName
                        viewModel.applyFilters(nameFilter, selectedStatusFilter)
                    },
                )
                statusFilter(
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
}
