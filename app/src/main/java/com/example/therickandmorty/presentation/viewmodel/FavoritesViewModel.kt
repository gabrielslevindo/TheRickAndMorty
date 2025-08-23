package com.example.therickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therickandmorty.data.local.extensions.toDto
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.domain.dataclass.CharacterData
import com.example.therickandmorty.domain.repository.CharacterRepository
import com.example.therickandmorty.presentation.states.StateView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StateView())
    val state = _state.asStateFlow()

    fun loadFavorites() =
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, isError = null)
            try {
                repository.getAllFavorites().collect { favoritesList ->
                    val dtoList: List<CharacterDto> = favoritesList.map { it.toDto() }

                    _state.value =
                        _state.value.copy(
                            SuccessApiList = dtoList,
                            isLoading = false,
                        )
                }
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(
                        isLoading = false,
                        isError = e.message ?: "Erro ao carregar favoritos",
                    )
            }
        }

    fun toggleFavorite(character: CharacterData) =
        viewModelScope.launch {
            val isFav = repository.isFavorite(character.id)
            if (isFav) {
                repository.deleteFavorite(character.id)
            } else {
                repository.insertFavorite(character)
            }
            loadFavorites()
        }

    suspend fun isFavorite(characterId: Int): Boolean =
        try {
            repository.isFavorite(characterId)
        } catch (e: Exception) {
            false
        }
}
