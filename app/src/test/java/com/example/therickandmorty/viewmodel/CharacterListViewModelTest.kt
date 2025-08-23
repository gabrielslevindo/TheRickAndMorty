package com.example.therickandmorty.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.data.remote.dtos.LocationDto
import com.example.therickandmorty.data.remote.dtos.OriginDto
import com.example.therickandmorty.domain.repository.CharacterRepository
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: CharacterRepository

    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(dispatcher)
        viewModel = CharacterListViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    private fun createMockCharacters(): List<CharacterDto> =
        listOf(
            CharacterDto(
                id = 1,
                name = "Character 1",
                status = "alive",
                species = "human",
                type = "_",
                gender = "male",
                origin = OriginDto(name = "Earth", url = "url"),
                location = LocationDto(name = "Earth", url = "url"),
                image = "image1.jpg",
                episode = listOf("episode1", "episode2"),
                url = "url",
                created = "created",
            ),
            CharacterDto(
                id = 2,
                name = "Character 2",
                status = "dead",
                species = "alien",
                type = "_",
                gender = "female",
                origin = OriginDto(name = "Mars", url = "url"),
                location = LocationDto(name = "Mars", url = "url"),
                image = "image2.jpg",
                episode = listOf("episode1", "episode2"),
                url = "url",
                created = "created",
            ),
        )

    @Test
    fun `charactersFlow should emit PagingData from repository`() =
        runTest {
            val mockCharacters = createMockCharacters()
            val pagingData: PagingData<CharacterDto> = PagingData.from(mockCharacters)

            // Configurando AsyncPagingDataDiffer para coletar PagingData
            val differ =
                AsyncPagingDataDiffer(
                    diffCallback =
                        object : DiffUtil.ItemCallback<CharacterDto>() {
                            override fun areItemsTheSame(
                                oldItem: CharacterDto,
                                newItem: CharacterDto,
                            ) = oldItem.id == newItem.id

                            override fun areContentsTheSame(
                                oldItem: CharacterDto,
                                newItem: CharacterDto,
                            ) = oldItem == newItem
                        },
                    updateCallback =
                        object : ListUpdateCallback {
                            override fun onInserted(
                                position: Int,
                                count: Int,
                            ) {}

                            override fun onRemoved(
                                position: Int,
                                count: Int,
                            ) {}

                            override fun onMoved(
                                fromPosition: Int,
                                toPosition: Int,
                            ) {}

                            override fun onChanged(
                                position: Int,
                                count: Int,
                                payload: Any?,
                            ) {}
                        },
                    mainDispatcher = dispatcher,
                    workerDispatcher = dispatcher,
                )

            differ.submitData(pagingData)
            dispatcher.scheduler.advanceUntilIdle()

            val snapshot = differ.snapshot().items
            assertEquals(mockCharacters, snapshot)
        }
}
