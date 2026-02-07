package com.example.therickandmorty.fakeusecase

import androidx.paging.PagingData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.model.usecase.LoadListUseCaseInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLoadListUseCase : LoadListUseCaseInt {
    var receivedName: String? = null
    var receivedStatus: String? = null

    override fun execute(
        name: String?,
        status: String?,
    ): Flow<PagingData<CharacterDto>> {
        receivedName = name
        receivedStatus = status
        return flowOf(PagingData.empty())
    }
}

class FakeLoadListUseCaseCharacter : LoadListUseCaseInt {
    var callCount = 0
    var receivedName: String? = null
    var receivedStatus: String? = null

    override fun execute(
        name: String?,
        status: String?,
    ): Flow<PagingData<CharacterDto>> {
        callCount++
        receivedName = name
        receivedStatus = status
        return flowOf(PagingData.empty())
    }
}
