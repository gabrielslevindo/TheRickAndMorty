package com.example.therickandmorty.presentation.viewmodel.actions

import com.example.therickandmorty.domain.dataclass.CharacterData

sealed interface FavoritesAction {
    data class ToggleFavorite(val character: CharacterData) : FavoritesAction
    data class isFavorite(val characterId: Int): FavoritesAction
}
