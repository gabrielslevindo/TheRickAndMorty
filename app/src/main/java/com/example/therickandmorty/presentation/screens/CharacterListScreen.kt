package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.appHeader
import com.example.therickandmorty.presentation.components.characterList
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

object CharacterListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: CharacterListViewModel = koinViewModel()

        val state = viewModel.state.collectAsStateWithLifecycle()

        val characters =
            state.value.characters.collectAsLazyPagingItems()

        characterListContent(
            characters = characters,
            isLoading = characters.loadState.refresh is LoadState.Loading,
            onCharacterClick = {
                navigator.push(CharacterDetailsScreen(it))
            },
        )
    }
}

@Composable
fun characterListContent(
    characters: LazyPagingItems<CharacterDto>,
    isLoading: Boolean,
    onCharacterClick: (CharacterDto) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFEAF5EF)),
    ) {
        appHeader(title = "Characters")

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
