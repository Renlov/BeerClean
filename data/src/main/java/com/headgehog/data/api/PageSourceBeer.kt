package com.headgehog.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.headgehog.data.entities.EntitiesItem.Companion.toBeer
import com.headgehog.domain.entities.Beer
import retrofit2.HttpException

//Int - страница
//Beer - что будет возвращать
/**
 *Пагинация
 */
class PageSourceBeer(
    private val apiService: ApiService,
    private val query : String
) : PagingSource<Int, Beer>() {
    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        val anchorPosition =  state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        if (query.isEmpty()) return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        val page : Int = params.key ?: 1
        val pageSize : Int = params.loadSize.coerceAtMost(ApiService.MAX_PAGE_SIZE)

        val response = apiService.getBeerApi(query, page, pageSize)
        return if (response.isSuccessful){
            val beers = checkNotNull(response.body()?.let {
                it.map { entities ->
                    entities.toBeer()
                }
            })
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (beers.size<pageSize) null else page + 1
            LoadResult.Page(beers, prevKey, nextKey)
        } else {
            LoadResult.Error(HttpException(response))
        }
    }
}