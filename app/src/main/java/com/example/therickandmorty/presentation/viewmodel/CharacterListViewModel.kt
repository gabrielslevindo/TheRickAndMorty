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
import kotlinx.coroutines.flow.Flow

class CharacterListViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    val charactersFlow: Flow<PagingData<CharacterDto>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { CharacterPagingSource(repository) },
        ).flow
            .cachedIn(viewModelScope)
}
