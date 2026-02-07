package com.example.therickandmorty.testusecase

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.therickandmorty.model.usecase.LoadListUseCaseIntImpl
import com.example.therickandmorty.fakerepository.FakeCharacterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

suspend fun <T : Any> Flow<PagingData<T>>.collectData(): List<T> = this.asSnapshot()

@OptIn(ExperimentalCoroutinesApi::class)
class LoadListUseCaseTest {
    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var useCase: LoadListUseCaseIntImpl

    @Before
    fun setup() {
        fakeRepository = FakeCharacterRepository()
        useCase = LoadListUseCaseIntImpl(fakeRepository)
    }

    @Test
    fun `execute with filters should emit filtered characters`() {
        runTest {
            val pagingData =
                useCase.execute(
                    name = "Filtered",
                    status = "alive",
                )

            val items = pagingData.collectData()

            assertEquals(1, items.size)
            assertEquals("Filtered Character", items.first().name)
            assertEquals("alive", items.first().status)
        }
    }
}
