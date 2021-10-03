package com.example.myapod.repository.newtork

import com.example.myapod.repository.room.ApodEntityClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiInterface {

    @GET("/planetary/apod")
    suspend fun getApodData(
        @Query("date") date: String,
        @Query("api_key") apikey: String
    ): Response<ApodEntityClass>

}