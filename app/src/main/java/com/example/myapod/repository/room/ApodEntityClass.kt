package com.example.myapod.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull


@Entity(tableName = "ApodEntityClass")
data class ApodEntityClass(

    @NotNull
    @PrimaryKey
    @SerializedName("date")
    @ColumnInfo(name = "date")
    val date: String,
    @SerializedName("explanation")
    @ColumnInfo(name = "explanation")
    val explanation: String?,
    @SerializedName("hdurl")
    @ColumnInfo(name = "hdurl")
    val hdurl: String?,
    @SerializedName("media_type")
    @ColumnInfo(name = "media_type")
    val media_type: String?,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,
    @SerializedName("url")
    @ColumnInfo(name = "url")
    val url: String?,
    @SerializedName("isFav")
    @ColumnInfo(name = "isFav")
    var isFav: Boolean


)