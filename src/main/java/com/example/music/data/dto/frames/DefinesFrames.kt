package com.example.music.data.dto.frames

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by TruyenDev on 29/10/2022.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class DefinesFrames(
    @Json(name = "start")
    val start: Int = 0,
    @Json(name = "end")
    val end: Int = 0,
    @Json(name = "totalCollageItemContainer")
    val totalCollageItemContainer: Int = 0,
    @Json(name = "indexDefineCollage")
    val indexDefineCollage: Int = 0,
    ) : Parcelable
