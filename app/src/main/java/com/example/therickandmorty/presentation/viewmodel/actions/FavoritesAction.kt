package com.example.therickandmorty.presentation.viewmodel.actions

import com.example.therickandmorty.model.dataclass.CharacterData

sealed interface FavoritesAction {
    data class ToggleFavorite(
        val character: CharacterData,
    ) : FavoritesAction

    data class IsFavorite(
        val characterId: Int,
    ) : FavoritesAction
}
