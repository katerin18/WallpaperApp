package com.example.wallpaperapp

import com.example.wallpaperapp.retrofit.DataResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesAPI {
    @GET("search")

    fun getResponse(
        @Query("apikey") apikey: String,
        @Query("q") query: String,
        @Query("purity") purity: String,
        @Query("sorting") sorting: String,
        @Query("page") page: Int = 1,
        @Query("ratios") ratios: String = "9x16,9x18,10x16",
    ): retrofit2.Call<DataResponseModel>
}