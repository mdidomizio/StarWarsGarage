package com.example.starwarsgarage.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import timber.log.Timber

class StarshipPagingSource(
    private val starshipApi: StarshipApi
) : PagingSource<Int, StarshipBasic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StarshipBasic> {
        return try {
            val page = params.key ?: 1
            Timber.tag("miriam").d("Loading page: $page")
            val response = starshipApi.getStarships(page)
            val starships = response.data
            Timber.tag("miriam").d("Loaded ${starships.size} starships for page $page")
            LoadResult.Page(
                data = starships,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1
            )
        } catch (e: Exception) {
            Timber.tag("miriam").e(e, "Error loading starships")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StarshipBasic>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
