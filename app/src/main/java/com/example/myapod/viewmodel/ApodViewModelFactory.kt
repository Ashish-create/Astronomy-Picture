package com.example.myapod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapod.repository.ApodMainActivityRepo


class ApodViewModelFactory(
    private val repository: ApodMainActivityRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodActivityViewModel::class.java)) {
            return ApodActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}