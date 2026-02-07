package com.example.therickandmorty.fakeusecase

import androidx.paging.PagingData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.domain.usecase.LoadListUseCaseInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLoadListUseCaseInt(
    private val characters: List<CharacterDto>,
) : LoadListUseCaseInt {
    override fun execute(
        name: String?,
        status: String?,
    ): Flow<PagingData<CharacterDto>> = flowOf(PagingData.from(characters))
}
