package com.example.therickandmorty.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.domain.repository.CharacterRepository

class CharacterPagingSource(
    private val repository: CharacterRepository,
    private val name: String? = null,
    private val status: String? = null,
) : PagingSource<Int, CharacterDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> =
        try {
            val page = params.key ?: 1
            val response =
                repository.getFilteredCharacters(
                    page = page,
                    name = name,
                    status = status,
                )

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}
