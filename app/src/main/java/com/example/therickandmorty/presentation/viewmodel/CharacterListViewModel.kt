package com.example.therickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.therickandmorty.model.usecase.LoadListUseCaseInt
import com.example.therickandmorty.presentation.viewmodel.states.CharacterListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class CharacterListViewModel(
    private val loadListUseCaseInt: LoadListUseCaseInt,
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> =
        _state.onStart {
                loadCharacters()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
                initialValue = CharacterListState(),
            )

    private fun loadCharacters() {
        val charactersFlow =
            loadListUseCaseInt
                .execute(
                    name = null,
                    status = null,
                ).cachedIn(viewModelScope)

        _state.update {
            it.copy(characters = charactersFlow)
        }
    }
}
