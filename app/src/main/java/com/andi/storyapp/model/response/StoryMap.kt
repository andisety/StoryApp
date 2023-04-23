package com.andi.storyapp.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryMap(
    val name: String,
    val latitude: Double,
    val longitude: Double
):Parcelable