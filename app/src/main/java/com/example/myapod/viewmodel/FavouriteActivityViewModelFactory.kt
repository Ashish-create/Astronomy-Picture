package com.example.myapod.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapod.repository.ApodMainActivityRepo


class FavouriteActivityViewModelFactory(
    private val repository: ApodMainActivityRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteActivityViewmodel::class.java)) {
            return FavouriteActivityViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}