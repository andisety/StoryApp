package com.andi.storyapp.network

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryResponeItem (
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("description")
    val description: String,
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("lat")
    val lat: Double?=null,
    @field:SerializedName("lon")
    val lon: Double?=null,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String
        ):Parcelable