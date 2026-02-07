package com.example.therickandmorty.presentation.viewmodel.states

import com.example.therickandmorty.presentation.states.StateView

data class FavoritesState(
    val state: StateView = StateView(isLoading = true),
    val isFavorite: Boolean = false,
)
