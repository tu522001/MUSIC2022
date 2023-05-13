package com.example.music.data.remote.service

import com.example.music.data.dto.frames.DataFrames
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by TruyenDev
 */

interface FramesService {
    @GET("frames.json")
    suspend fun fetchFrames(): Response<DataFrames>
}
