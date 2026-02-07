package com.example.therickandmorty.presentation.states

import com.example.therickandmorty.data.remote.dtos.CharacterDto

data class StateView(
    val isLoading: Boolean = false,
    val successApiList: List<CharacterDto> = emptyList(),
    val isError: String? = null,
)
