package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.core.domain.DataError
import com.example.therickandmorty.core.domain.DataException
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.appHeader
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.components.nameFilterField
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.components.statusFilter
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.SearchAction
import com.example.therickandmorty.presentation.viewmodel.states.SearchState
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

object SearchScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: SearchViewModel = koinViewModel()

        val navigator = LocalNavigator.currentOrThrow

        val state by viewModel.state.collectAsStateWithLifecycle()
        searchScreenContent(
            state = state,
            action = viewModel::onAction,
            onCharacterClick = {
                navigator.push(CharacterDetailsScreen(it))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchScreenContent(
    state: SearchState,
    action: (SearchAction) -> Unit,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    val pullState = rememberPullToRefreshState()

    var nameFilter by remember {
        mutableStateOf(state.name ?: "")
    }

    var selectedStatusFilter by remember {
        mutableStateOf(state.status ?: "")
    }

    val characterStatusList =
        listOf("Alive", "Dead", "Unknown")

    val characters =
        state.characters.collectAsLazyPagingItems()

    PullToRefreshBox(
        state = pullState,
        isRefreshing = characters.loadState.refresh is LoadState.Loading,
        onRefresh = {
            characters.refresh()
        },
    ) {
        Scaffold(
            topBar = {
                appHeader(title = "Characters")
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
                    onValueChange = {
                        nameFilter = it

                        action(
                            SearchAction.ApplyFilters(
                                name = nameFilter,
                                status = selectedStatusFilter,
                            ),
                        )
                    },
                )

                statusFilter(
                    statuses = characterStatusList,
                    selectedStatus = selectedStatusFilter,
                    onStatusSelected = {
                        selectedStatusFilter = it

                        action(
                            SearchAction.ApplyFilters(
                                name = nameFilter,
                                status = selectedStatusFilter,
                            ),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier =
                        Modifier
                            .padding(top = 8.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 24.dp,
                                    topEnd = 24.dp,
                                ),
                            ).background(MaterialTheme.colorScheme.background),
                ) {
                    when (val loadState = characters.loadState.refresh) {
                        is LoadState.Loading -> {
                            shimmerItem()
                        }

                        is LoadState.Error -> {
                            val error = loadState.error

                            val message =
                                if (error is DataException) {
                                    when (error.error) {
                                        DataError.Remote.NO_INTERNET ->
                                            "Sem conexão com a internet"

                                        else ->
                                            "Erro desconhecido"
                                    }
                                } else {
                                    "Erro inesperado"
                                }

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = message,
                                        color = MaterialTheme.colorScheme.error,
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { characters.retry() },
                                        colors =
                                            ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = MaterialTheme.colorScheme.primary,
                                            ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                                    ) {
                                        Text(
                                            text = "Tentar novamente",
                                            color = Color.Black,
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            characterList(
                                isPaged = true,
                                pagedCharacters = characters,
                                onItemClick = onCharacterClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun searchScreenPreview() {
    MaterialTheme {
        val previewCharacters =
            flowOf(
                PagingData.from(
                    List(10) { index ->

                        CharacterDto(
                            id = index,
                            name = "Character $index",
                            image = "",
                            status = "Alive",
                            species = "Human",
                            gender = "Male",
                            type = "",
                        )
                    },
                ),
            )

        val previewState =
            SearchState(
                name = "",
                status = "",
                characters = previewCharacters,
            )

        searchScreenContent(
            state = previewState,
            action = {},
            onCharacterClick = {},
        )
    }
}
