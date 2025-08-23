package com.example.therickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.domain.repository.CharacterRepository
import com.example.therickandmorty.presentation.paging.CharacterPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    private val _nameFilter = MutableStateFlow<String?>(null)
    val nameFilter = _nameFilter.asStateFlow()
    private val _statusFilter = MutableStateFlow<String?>(null)
    val statusFilter = _statusFilter.asStateFlow()

    private var _charactersFlow = MutableStateFlow<PagingData<CharacterDto>>(PagingData.empty())
    val charactersFlow = _charactersFlow.asStateFlow()

    fun applyFilters(
        name: String?,
        status: String?,
    ) {
        _nameFilter.value = name
        _statusFilter.value = status
        loadCharacters()
    }

     fun loadCharacters() {
        viewModelScope.launch {
            val pager =
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = { CharacterPagingSource(repository, _nameFilter.value, _statusFilter.value) },
                )
            _charactersFlow.value = pager.flow.cachedIn(viewModelScope).first()
        }
    }
}
