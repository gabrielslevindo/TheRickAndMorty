package com.example.therickandmorty.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import kotlinx.coroutines.flow.flowOf

@Composable
fun characterList(
    isPaged: Boolean,
    pagedCharacters: LazyPagingItems<CharacterDto>,
    listCharacters: List<CharacterDto>? = null,
    onItemClick: (CharacterDto) -> Unit,
) {
    if (isPaged) {
        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        ) {
            items(pagedCharacters.itemCount) { index ->
                val character = pagedCharacters[index]
                character?.let {
                    characterListItem(character = it, onItemClick = onItemClick)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                if (pagedCharacters.loadState.append is androidx.paging.LoadState.Loading) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        shimmerItem()
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listCharacters?.forEach { character ->
                characterListItem(character = character, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun emptyPagingItems(): LazyPagingItems<CharacterDto> =
    flowOf(PagingData.empty<CharacterDto>())
        .collectAsLazyPagingItems()

@Preview
@Composable
fun characterListPreview() {
    MaterialTheme {
        val previewCharacters =
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
            }

        characterList(
            isPaged = false,
            pagedCharacters = emptyPagingItems(),
            listCharacters = previewCharacters,
            onItemClick = {},
        )
    }
}
