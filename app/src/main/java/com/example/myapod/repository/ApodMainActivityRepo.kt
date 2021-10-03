package com.example.myapod.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapod.repository.newtork.ApodApiClient
import com.example.myapod.repository.newtork.ResultData
import com.example.myapod.repository.room.ApodDAO
import com.example.myapod.repository.room.ApodEntityClass
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ApodMainActivityRepo(var apodDao: ApodDAO) {

    private val resultData: MutableLiveData<ResultData> by lazy {
        MutableLiveData<ResultData>()
    }


    lateinit var response: Response<ApodEntityClass>
    suspend fun fetchApodData(date: String, apiKey: String) {
        resultData.value = ResultData.progress(null)

        try {
            response = ApodApiClient.getClient.getApodData(date, apiKey)
            if (response.isSuccessful && response.body() != null) {
                resultData.value = ResultData.success(response.body()!!)
                apodDao.insertData(response.body()!!)

            } else if (response.body() == null) {
                resultData.value = ResultData.failure(response.message())
            } else {
                //:Data can be fetched from db,if available, in case of exception (as a fallback)
                fetchApodDataFromDb(date,response.message())
                Log.i("TAG", response.message());
                //resultData.value = ResultData.failure(response.message())
            }
        } catch (e: Exception) {
            //:Data can be fetched from db,if available, in case of exception (as a fallback)
            fetchApodDataFromDb(date, "Network error:Pls check the connection")
            //resultData.value = ResultData.failure("Network error:Pls check the connection")
            e.stackTrace
        }

    }

    fun getResultData(): LiveData<ResultData> {
        return resultData;
    }

    suspend fun fetchApodDataFromDb(date: String, message: String) {

        var data = apodDao.getPictureByDate(date)
        if (data == null) {

            resultData.value = ResultData.failure(message)

        } else {

            resultData.value = ResultData.success(data)
        }

    }


    suspend fun updateFavInfo(date: String, isFav: Boolean) {

        apodDao.updateFavInfo(date, isFav)

    }

    val allFavourites: Flow<List<ApodEntityClass>?> = apodDao.getFavouriteList()


}