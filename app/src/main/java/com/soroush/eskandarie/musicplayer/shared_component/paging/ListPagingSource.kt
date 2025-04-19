package com.soroush.eskandarie.musicplayer.shared_component.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ListPagingSource<T: Any>(
    private val fullList: List<T>
): PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let{ anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val fromIndex = page * pageSize
        val toIndex = (fromIndex + pageSize).coerceAtMost(fullList.size)

        return if (fromIndex < fullList.size) {
            LoadResult.Page(
                data = fullList.subList(fromIndex, toIndex),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (toIndex == fullList.size) null else page + 1
            )
        } else {
            LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
    }

}