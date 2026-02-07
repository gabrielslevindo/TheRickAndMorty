package com.example.therickandmorty.presentation.viewmodel.states

import androidx.paging.PagingData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val name: String? = null,
    val status: String? = null,
    val characters: Flow<PagingData<CharacterDto>>
)

