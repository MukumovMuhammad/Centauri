package com.example.centauri.rv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.centauri.R

class rvAdapterNasaNews(private val newsList: List<ApodNewsData>): RecyclerView.Adapter<rvAdapterNasaNews.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.rv_nasa_news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.i("rvAdapterNasaNews", "onBindViewHolder is on work")
        val item = newsList[position]

        Log.i("rvAdapterNasaNews", "the item is $item")

        Glide.with(holder.itemView.context)
            .load(item.url)
            .placeholder(R.drawable.ic_img_default)
            .error(R.drawable.ic_img_default)
            .into(holder.imageView)

        holder.dateTextView.text = item.date
        holder.titleTextView.text = item.title

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_apod)
        val dateTextView = itemView.findViewById<TextView>(R.id.text_date)
        val titleTextView = itemView.findViewById<TextView>(R.id.text_title)
    }


}