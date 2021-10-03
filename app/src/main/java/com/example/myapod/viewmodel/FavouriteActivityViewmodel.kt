package com.example.myapod.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapod.repository.ApodMainActivityRepo
import com.example.myapod.repository.room.ApodEntityClass
import kotlinx.coroutines.launch

class FavouriteActivityViewmodel(val apodMainActivityRepo: ApodMainActivityRepo) : ViewModel() {

    val allFavourites: LiveData<List<ApodEntityClass>?> =
        apodMainActivityRepo.allFavourites.asLiveData()

    fun updateFavInfo(date: String, isFav: Boolean) {
        viewModelScope.launch {

            apodMainActivityRepo.updateFavInfo(date, isFav)
        }

    }
}