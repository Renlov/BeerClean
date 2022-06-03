package com.headgehog.data.api

import androidx.annotation.IntRange
import com.headgehog.data.entities.EntitiesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/beers/")
    suspend fun getBeerApi(
        @Query("query") query: String? = null,
        @Query("pages") @IntRange(from = 1) page : Int = 1,
        @Query("pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE,
    ) : Response<ArrayList<EntitiesItem>>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 20
    }
}

