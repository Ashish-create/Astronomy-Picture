package com.example.myapod.repository.newtork

import com.example.myapod.repository.room.ApodEntityClass

sealed class ResultData {

    data class Success(val value: ApodEntityClass?) : ResultData()

    data class Failure(val message: String?) : ResultData()

    data class Progress(val value: String? = null) : ResultData()

    companion object {

        fun progress(value: String?): ResultData = Progress(value)

        fun success(value: ApodEntityClass?): ResultData = Success(value)

        fun failure(error_msg: String?): ResultData = Failure(error_msg)

    }

}