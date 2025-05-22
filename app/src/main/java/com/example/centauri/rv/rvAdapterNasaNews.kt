package com.example.centauri.rv

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.centauri.DialogWindows
import com.example.centauri.R

class rvAdapterNasaNews(private val newsList: List<ApodNewsData>): RecyclerView.Adapter<rvAdapterNasaNews.ViewHolder>() {

    private lateinit var dialogWindows: DialogWindows
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        dialogWindows = DialogWindows(parent.context)
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.rv_nasa_news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.i("rvAdapterNasaNews", "onBindViewHolder is on work")
        val item = newsList[position]

        Log.i("rvAdapterNasaNews", "the item is $item")

        Glide.with(holder.itemView.context)
            .load(item.url)
            .thumbnail(Glide.with((holder.itemView.context)).load(R.drawable.loading_gif))
            .error(R.drawable.ic_img_default)
            .into(holder.imageView)

        holder.dateTextView.text = item.date
        holder.titleTextView.text = item.title

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "NASA news ${item.title} clicked", Toast.LENGTH_SHORT).show()
            dialogWindows.nasaItemNews(item)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_apod)
        val dateTextView = itemView.findViewById<TextView>(R.id.text_date)
        val titleTextView = itemView.findViewById<TextView>(R.id.text_title)
    }
}