package com.example.myapod.views

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.myapod.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapod.repository.ApodMainActivityRepo
import com.example.myapod.repository.room.ApodDataBase
import com.example.myapod.repository.room.ApodEntityClass
import com.example.myapod.viewmodel.ApodViewModelFactory
import com.example.myapod.viewmodel.FavouriteActivityViewModelFactory
import com.example.myapod.viewmodel.FavouriteActivityViewmodel

class FavouriteActivity : AppCompatActivity() {

    private var favouriteActivityViewmodel: FavouriteActivityViewmodel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        initializeViewModel()
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        val noContentText = findViewById<TextView>(R.id.no_content)
        var recyclerViewData: List<ApodEntityClass>? = null

        val clickhandler: (Int) -> (Unit) = {

            var date = recyclerViewData?.get(it)?.date
            if (date != null && !TextUtils.isEmpty(date)) {
                favouriteActivityViewmodel?.updateFavInfo(date, false)
            }

        }

        val adapter = FavouriteActivityAdapter(clickhandler)
        recyclerview.adapter = adapter

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            recyclerview.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        } else {

            recyclerview.layoutManager = LinearLayoutManager(this)

        }

        // recyclerview.layoutManager = LinearLayoutManager(this)
        favouriteActivityViewmodel?.allFavourites?.observe(this) { favourites ->
            // Update the cached copy of the words in the adapter.
            if (favourites.isNullOrEmpty()) {
                recyclerview.visibility = View.GONE
                noContentText.visibility = View.VISIBLE
            } else {
                noContentText.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
            }
            recyclerViewData = favourites
            favourites.let { adapter.submitList(it) }
        }


    }

    private fun initializeViewModel() {
        val apodDao = ApodDataBase.getDataseClient(application).apodDao()
        val repository = ApodMainActivityRepo(apodDao)
        val factory = FavouriteActivityViewModelFactory(repository)
        favouriteActivityViewmodel =
            ViewModelProvider(this, factory).get(FavouriteActivityViewmodel::class.java)
    }

}
