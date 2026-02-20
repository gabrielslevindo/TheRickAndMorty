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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.therickandmorty.core.domain.DataError
import com.example.therickandmorty.core.domain.DataException
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.appHeader
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.components.shimmerItem
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

object CharacterListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: CharacterListViewModel = koinViewModel()

        val state = viewModel.state.collectAsStateWithLifecycle()

        val characters = state.value.characters.collectAsLazyPagingItems()

        characterListContent(
            characters = characters,
            isLoading = characters.loadState.refresh is LoadState.Loading,
            onCharacterClick = {
                navigator.push(CharacterDetailsScreen(it))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun characterListContent(
    characters: LazyPagingItems<CharacterDto>,
    isLoading: Boolean,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    val pullState = rememberPullToRefreshState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFEAF5EF)),
    ) {
        appHeader(title = "Characters")

        PullToRefreshBox(
            state = pullState,
            isRefreshing = characters.loadState.refresh is LoadState.Loading,
            onRefresh = {
                characters.refresh()
            },
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
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
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    is LoadState.Error -> {
                        val error = loadState.error

                        val message =
                            if (error is DataException) {
                                when (error.error) {
                                    DataError.Remote.NO_INTERNET -> "Sem conexão com a internet"

                                    DataError.Remote.SERVER -> "Erro no servidor"

                                    DataError.Remote.REQUEST_TIMEOUT -> "Tempo de conexão esgotado"

                                    DataError.Remote.TOO_MANY_REQUESTS -> "Muitas requisições"

                                    else -> "Erro desconhecido"
                                }
                            } else {
                                "Erro inesperado"
                            }

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

                    else -> {
                        if (characters.itemCount == 0 && isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                            )
                        } else {
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
fun characterListContentPreview() {
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
            ).collectAsLazyPagingItems()

        characterListContent(
            characters = previewCharacters,
            isLoading = false,
            onCharacterClick = {},
        )
    }
}
