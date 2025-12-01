package com.example.starwarsgarage.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState

class StarshipPagingSource(
    private val starshipApi: StarshipApi
) : PagingSource<Int, Starship>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Starship> {
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
