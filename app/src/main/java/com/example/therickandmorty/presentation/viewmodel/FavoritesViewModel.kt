package com.example.therickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therickandmorty.core.domain.DataError
import com.example.therickandmorty.core.domain.DataException
import com.example.therickandmorty.data.local.extensions.toDto
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.model.dataclass.CharacterData
import com.example.therickandmorty.model.repository.CharacterRepository
import com.example.therickandmorty.presentation.states.StateView
import com.example.therickandmorty.presentation.viewmodel.actions.FavoritesAction
import com.example.therickandmorty.presentation.viewmodel.states.FavoritesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FavoritesViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesState())
    val state =
        _state
            .onStart {
                loadFavorites()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
                initialValue = FavoritesState(),
            )

    fun onAction(action: FavoritesAction) {
        when (action) {
            is FavoritesAction.ToggleFavorite -> toggleFavorite(action.character)
            is FavoritesAction.IsFavorite -> checkIsFavorite(action.characterId)
        }
    }

    private fun loadFavorites() =
        viewModelScope.launch {
            _state.update {
                it.copy(
                    state = StateView(isLoading = true),
                )
            }
            try {
                repository.getAllFavorites().collect { favoritesList ->
                    val dtoList: List<CharacterDto> = favoritesList.map { it.toDto() }

                    _state.update {
                        it.copy(
                            state =
                                StateView(
                                    successApiList = dtoList,
                                    isLoading = false,
                                ),
                        )
                    }
                }
            } catch (e: DataException) {
                val message =
                    when (e.error) {
                        DataError.Local.DISK_FULL ->
                            "Espaço insuficiente no dispositivo"

                        DataError.Local.UNKNOWN ->
                            "Erro ao acessar o banco de dados"

                        else ->
                            "Erro desconhecido"
                    }

                _state.update {
                    it.copy(
                        state =
                            StateView(
                                isError = message,
                                isLoading = false,
                            ),
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        state =
                            StateView(
                                isError = "Erro inesperado",
                                isLoading = false,
                            ),
                    )
                }
            }
        }

    private fun toggleFavorite(character: CharacterData) {
        viewModelScope.launch {
            val isFav = repository.isFavorite(character.id)

            if (isFav) {
                repository.deleteFavorite(character.id)
            } else {
                repository.insertFavorite(character)
            }
            _state.update {
                it.copy(isFavorite = !isFav)
            }
            loadFavorites()
        }
    }

    private fun checkIsFavorite(characterId: Int) {
        viewModelScope.launch {
            val favorite =
                try {
                    repository.isFavorite(characterId)
                } catch (e: Exception) {
                    false
                }
            _state.update {
                it.copy(isFavorite = favorite)
            }
        }
    }
}
