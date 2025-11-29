package com.example.starwarsgarage.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException

class StarshipPagingSource(
    private val starshipApi: StarshipApi
) : PagingSource<Int, Starship>() {
    // --- START: Temporary change for testing retry ---
    companion object {
        private var shouldFail = true
    }
    // --- END: Temporary change ---

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Starship> {
        // --- START: Temporary change for testing retry ---
        if (params.key == null && shouldFail) {
            shouldFail = false // Make the next attempt succeed
            return LoadResult.Error(IOException("Simulated initial load failure"))
        }
        // --- END: Temporary change ---

        return try {
            val page = params.key ?: 1
            val response = starshipApi.getStarships(page)
            val starships = response.results
            LoadResult.Page(
                data = starships,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.next == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Starship>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}