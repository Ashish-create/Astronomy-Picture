package com.example.myapod.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ApodDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(apodEntityClass: ApodEntityClass): Long

    @Query("SELECT * FROM ApodEntityClass WHERE date =:date")
    suspend fun getPictureByDate(date: String?): ApodEntityClass

    @Query("UPDATE ApodEntityClass SET isFav=:fav WHERE date = :date")
    suspend fun updateFavInfo(date: String, fav: Boolean)

    @Query("SELECT * FROM ApodEntityClass WHERE isFav = 1")
    fun getFavouriteList(): Flow<List<ApodEntityClass>?>

}