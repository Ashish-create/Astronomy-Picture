package com.example.myapod.views

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapod.R
import com.example.myapod.repository.room.ApodEntityClass
import com.ms.square.android.expandabletextview.ExpandableTextView

class FavouriteActivityAdapter(private val clickhandler: (Int) -> Unit) :
    ListAdapter<ApodEntityClass, FavouriteActivityAdapter.ViewHolder>(FavouritesComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_fav_item, parent, false)

        return ViewHolder(view, parent.context,clickhandler)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val value = getItem(position)
        var description = value?.explanation
        var imageUrl = value?.url
        val title = value?.title
        val date = value?.date

        if (!TextUtils.isEmpty(description)) {
            holder.expandableTextView.text = description
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(holder.context)
                .load(imageUrl)
                .into(holder.apodMainImage)
        }

        if (!TextUtils.isEmpty(title)) {
            holder.apodTitle.text = title
        }
        if (!TextUtils.isEmpty(date)) {
            holder.apodDate.text = date
        }


    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, var context: Context, clickhandler: (Int) -> Unit) : RecyclerView.ViewHolder(ItemView) {
        var expandableTextView =
            ItemView.findViewById<View>(R.id.expand_text_view)
                .findViewById<View>(R.id.expand_text_view) as ExpandableTextView

        var apodMainImage = ItemView.findViewById<ImageView>(R.id.apod_main_image)
        var apodTitle = ItemView.findViewById<TextView>(R.id.apod_title)
        var apodDate = ItemView.findViewById<TextView>(R.id.apod_date)
        var deleteFav =ItemView.findViewById<ImageView>(R.id.deletefav)

        init {
            deleteFav.setOnClickListener{
                clickhandler(layoutPosition)
            }
        }

    }


    class FavouritesComparator : DiffUtil.ItemCallback<ApodEntityClass>() {
        override fun areItemsTheSame(oldItem: ApodEntityClass, newItem: ApodEntityClass): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ApodEntityClass,
            newItem: ApodEntityClass
        ): Boolean {
            return oldItem.date == newItem.date
        }
    }


}
