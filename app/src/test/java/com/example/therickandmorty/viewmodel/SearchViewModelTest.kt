package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.dispatcher.MainDispatcherRule
import com.example.therickandmorty.fakeusecase.FakeLoadListUseCase
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.SearchAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotSame
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeUseCase: FakeLoadListUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        fakeUseCase = FakeLoadListUseCase()
        viewModel = SearchViewModel(fakeUseCase)
    }

    @Test
    fun `state starts with empty paging and null filters`() {
        runTest {
            val state = viewModel.state.value

            assertNull(state.name)
            assertNull(state.status)
        }
    }

    @Test
    fun `ApplyFilters updates name and status`() {
        runTest {
            viewModel.onAction(
                SearchAction.ApplyFilters(
                    name = "Rick",
                    status = "Alive",
                ),
            )

            val state = viewModel.state.value

            assertEquals("Rick", state.name)
            assertEquals("Alive", state.status)
        }
    }

    @Test
    fun `ApplyFilters calls use case with correct params`() {
        runTest {
            viewModel.onAction(
                SearchAction.ApplyFilters(
                    name = "Morty",
                    status = "Dead",
                ),
            )

            assertEquals("Morty", fakeUseCase.receivedName)
            assertEquals("Dead", fakeUseCase.receivedStatus)
        }
    }

    @Test
    fun `ApplyFilters updates characters flow`() {
        runTest {
            val initialFlow = viewModel.state.value.characters

            viewModel.onAction(
                SearchAction.ApplyFilters(
                    name = "Summer",
                    status = "Alive",
                ),
            )

            val updatedFlow = viewModel.state.value.characters

            assertNotSame(initialFlow, updatedFlow)
        }
    }
}
