package com.example.music.data.remote.service

import com.example.music.data.dto.response.ResponsePrankRecordFolder
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by TruyenDev
 */

interface PrankFolderGifService {
    @GET("prankgifcate/search")
    suspend fun fetchCategoryGif(@Query("filter") filter: String, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Response<ResponsePrankRecordFolder>
}
