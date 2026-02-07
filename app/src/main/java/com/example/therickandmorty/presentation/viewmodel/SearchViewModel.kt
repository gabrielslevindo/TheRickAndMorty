package com.example.therickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.therickandmorty.domain.usecase.LoadListUseCaseInt
import com.example.therickandmorty.presentation.viewmodel.actions.SearchAction
import com.example.therickandmorty.presentation.viewmodel.states.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val loadListUseCaseInt: LoadListUseCaseInt
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState(characters = flowOf(PagingData.empty())))

    val state: StateFlow<SearchState> = _state.asStateFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.ApplyFilters -> applyFilters(action.name, action.status)
        }
    }

    private fun applyFilters(
        name: String?,
        status: String?
    ) {
        _state.update {
            it.copy(
                name = name,
                status = status
            )
        }
        loadCharacters()
    }

    private fun loadCharacters() {
        val current = _state.value

        val charactersFlow =
            loadListUseCaseInt
                .execute(
                    name = current.name,
                    status = current.status
                )
                .cachedIn(viewModelScope)

        _state.update {
            it.copy(characters = charactersFlow)
        }
    }

}
