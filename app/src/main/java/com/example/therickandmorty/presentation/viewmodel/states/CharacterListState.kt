package com.example.therickandmorty.presentation.viewmodel.states

import androidx.paging.PagingData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CharacterListState(
    val characters: Flow<PagingData<CharacterDto>> = emptyFlow()
)
