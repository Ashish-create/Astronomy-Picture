package com.example.myapod.views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapod.R
import com.example.myapod.Utility.UtilityClass
import com.example.myapod.repository.ApodMainActivityRepo
import com.example.myapod.repository.newtork.ResultData
import com.example.myapod.repository.room.ApodDataBase
import com.example.myapod.repository.room.ApodEntityClass
import com.example.myapod.viewmodel.ApodActivityViewModel
import com.example.myapod.viewmodel.ApodViewModelFactory
import com.ms.square.android.expandabletextview.ExpandableTextView
import tutorials.droid.datepicker.DatePickerFragment


class ApodMainActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var apodMainContainer: ConstraintLayout

    private lateinit var showFavPictures: TextView
    private lateinit var datePicker: TextView


    private lateinit var favToggleButton: ToggleButton
    private lateinit var expandableTextView: ExpandableTextView
    private lateinit var apodMainImage: ImageView
    private lateinit var apodTitle: TextView
    private lateinit var apodDate: TextView

    private lateinit var progressBar: ProgressBar

    private var apodActivityViewModel: ApodActivityViewModel? = null

    lateinit var currentDate: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColorBelowM()
        setContentView(R.layout.activity_apod_main)
        initializeViewModel()
        initializeViews()
        observeDataChanges()
        setUpVisibilityOnActivityRecreated()
    }

    private fun initializeViewModel() {

        val apodDao = ApodDataBase.getDataseClient(application).apodDao()
        val repository = ApodMainActivityRepo(apodDao)
        val factory = ApodViewModelFactory(repository)
        apodActivityViewModel =
            ViewModelProvider(this, factory).get(ApodActivityViewModel::class.java)

    }


    private fun setStatusBarColorBelowM() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                window.statusBarColor = resources.getColor(R.color.activity_apod_main_bg_color)
                window.navigationBarColor = resources.getColor(R.color.activity_apod_main_bg_color)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun initializeViews() {

        apodMainContainer = findViewById(R.id.apod_main_container)

        showFavPictures = findViewById(R.id.show_fav)
        showFavPictures.setOnClickListener(this)

        datePicker = findViewById(R.id.datepicker)
        datePicker.setOnClickListener(this)

        favToggleButton = findViewById(R.id.favbutton)
        favToggleButton.setOnClickListener(this)

        progressBar = findViewById(R.id.progressBar)



        expandableTextView =
            findViewById<View>(R.id.expand_text_view).findViewById<View>(R.id.expand_text_view) as ExpandableTextView

        apodMainImage = findViewById(R.id.apod_main_image)
        apodTitle = findViewById(R.id.apod_title)
        apodDate = findViewById(R.id.apod_date)

    }

    private fun observeDataChanges() {

        apodActivityViewModel?.getApodData()?.observe(this, Observer { resultData ->
            resultData?.let {
                when (resultData) {

                    is ResultData.Progress -> {
                        resultData.value
                        toggleMainContainerVisibility(true)

                    }
                    is ResultData.Failure -> {
                        Toast.makeText(this, resultData.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = GONE
                        apodMainContainer.visibility = GONE

                    }
                    is ResultData.Success -> {

                        toggleMainContainerVisibility(false)
                        updateViewWithData(resultData.value)


                    }


                }

            }


        })

    }

    private fun updateViewWithData(value: ApodEntityClass?) {

        var description = value?.explanation
        var imageUrl = value?.url
        val title = value?.title
        val date = value?.date

        if (!TextUtils.isEmpty(description)) {
            expandableTextView.text = description
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(this)
                .load(imageUrl)
                .into(apodMainImage)
        }

        if (!TextUtils.isEmpty(title)) {
            apodTitle.text = title
        }
        if (!TextUtils.isEmpty(date)) {
            apodDate.text = date
        }

    }

    private fun toggleMainContainerVisibility(toggleFlag: Boolean) {
        if (toggleFlag) {
            apodMainContainer.visibility = GONE
            progressBar.visibility = VISIBLE
        } else {

            apodMainContainer.visibility = VISIBLE
            progressBar.visibility = GONE
        }
    }


    private fun setUpVisibilityOnActivityRecreated() {
        var resultData = apodActivityViewModel?.getApodData()

        if (resultData == null) {
            apodMainContainer.visibility = GONE
        } else {

            if (resultData.value is ResultData.Success) {

                val apodEntityClassItem = (resultData.value as ResultData.Success).value
                if (apodEntityClassItem != null && !TextUtils.isEmpty(apodEntityClassItem.date)) {
                    apodMainContainer.visibility = VISIBLE
                    favToggleButton.visibility = VISIBLE
                    if (apodEntityClassItem.isFav) {
                        favToggleButton.background = getDrawable(R.drawable.ic_fav)
                        favToggleButton.isChecked = true
                    } else {
                        favToggleButton.background = getDrawable(R.drawable.ic_fav_bg)
                        favToggleButton.isChecked = false
                    }
                    currentDate = apodEntityClassItem.date;
                } else {

                    favToggleButton.visibility = GONE
                }

            } else {

                apodMainContainer.visibility = GONE
            }

        }


    }


    override fun onClick(viewId: View?) {
        when (viewId?.id) {

            R.id.datepicker -> {
                DatePickerFragment().show(supportFragmentManager, "DATE PICKER");
            }
            R.id.favbutton -> {
                if (favToggleButton.isChecked) {
                    Toast.makeText(this, "Saved to favourites", Toast.LENGTH_SHORT).show()
                    favToggleButton.background = getDrawable(R.drawable.ic_fav)
                    setApodEntityFavStatus(true);
                    apodActivityViewModel?.updateFavInfo(currentDate, true)

                } else {
                    setApodEntityFavStatus(false)
                    Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
                    favToggleButton.background = getDrawable(R.drawable.ic_fav_bg)
                    apodActivityViewModel?.updateFavInfo(currentDate, false)
                }
            }
            R.id.show_fav -> {

                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }

            else -> {

            }

        }
    }

    private fun setApodEntityFavStatus(flag: Boolean) {

        var resultData = apodActivityViewModel?.getApodData()
        if (resultData != null && (resultData.value is ResultData.Success)) {
            val apodEntityClassItem = (resultData.value as ResultData.Success).value
            if (apodEntityClassItem != null) {
                apodEntityClassItem.isFav = flag;
            }

        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfWeek: Int) {
        updateDateInView(year, monthOfYear + 1, dayOfWeek)
        if (UtilityClass.isNetworkAvailable(this)) {
            apodMainContainer.visibility = GONE
            progressBar.visibility = VISIBLE
            favToggleButton.isChecked = false
            favToggleButton.background = getDrawable(R.drawable.ic_fav_bg)
            apodActivityViewModel?.fetchApodData(currentDate, "DEMO_KEY")
        } else {
            favToggleButton.isChecked = false
            favToggleButton.background = getDrawable(R.drawable.ic_fav_bg)
            apodMainContainer.visibility = GONE
            progressBar.visibility = VISIBLE
            apodActivityViewModel?.fetchApodDataFromDb(currentDate)
        }
    }

    private fun updateDateInView(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var monthOfYearString = ""
        var dayOfMonthString = ""
        if (monthOfYear < 10) {
            monthOfYearString = "0$monthOfYear"
        } else {
            monthOfYearString = "$monthOfYear"
        }
        if (dayOfMonth < 10) {
            dayOfMonthString = "0$dayOfMonth"
        } else {
            dayOfMonthString = "$dayOfMonth"
        }
        currentDate = "$year-$monthOfYearString-$dayOfMonthString"

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("asd", "ondestroy  called")
    }


    override fun onPause() {
        super.onPause()
        Log.i("asd", "ondestroy  called")
    }

}