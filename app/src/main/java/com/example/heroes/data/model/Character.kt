package com.example.heroes.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    val resourceURI: String?
) : Parcelable