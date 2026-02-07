package com.example.therickandmorty.model.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.model.repository.CharacterRepository
import com.example.therickandmorty.presentation.paging.CharacterPagingSource
import kotlinx.coroutines.flow.Flow

interface LoadListUseCaseInt {
    fun execute(
        name: String?,
        status: String?,
    ): Flow<PagingData<CharacterDto>>
}

class LoadListUseCaseIntImpl(
    private val repository: CharacterRepository,
) : LoadListUseCaseInt {
    override fun execute(
        name: String?,
        status: String?,
    ): Flow<PagingData<CharacterDto>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory = {
                CharacterPagingSource(
                    repository = repository,
                    name = name,
                    status = status,
                )
            },
        ).flow
}
