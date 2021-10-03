package com.example.myapod.viewmodel

import androidx.lifecycle.*
import com.example.myapod.repository.ApodMainActivityRepo
import com.example.myapod.repository.newtork.ResultData
import kotlinx.coroutines.launch

class ApodActivityViewModel(val apodMainActivityRepo: ApodMainActivityRepo) : ViewModel() {


    private var resultData: LiveData<ResultData> = apodMainActivityRepo.getResultData()


    fun fetchApodData(date: String, apiKey: String) {

        viewModelScope.launch {

            apodMainActivityRepo.fetchApodData(date, apiKey)
        }
    }

    fun getApodData(): LiveData<ResultData> {
        return resultData;
    }

    fun fetchApodDataFromDb(date: String) {

        viewModelScope.launch {

            apodMainActivityRepo.fetchApodDataFromDb(date, "Check Internet Connection")
        }
    }


    fun updateFavInfo(date: String, isFav: Boolean) {
        viewModelScope.launch {

            apodMainActivityRepo.updateFavInfo(date, isFav)
        }

    }
}