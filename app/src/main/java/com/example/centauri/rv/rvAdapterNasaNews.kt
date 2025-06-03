package com.example.centauri.rv

import android.annotation.SuppressLint
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.centauri.DialogWindows
import com.example.centauri.FullImageDialogFragment
import com.example.centauri.R
import com.example.centauri.activities.AuthActivity
import com.example.centauri.activities.templates.TestActivity
import com.example.centauri.models.AuthState
import com.example.centauri.models.AuthViewModel
import com.example.centauri.models.DbViewModel
import com.example.centauri.models.UserData
import com.example.centauri.rv.rvAdapterLesson.Companion
import com.google.android.gms.tasks.OnSuccessListener

class rvAdapterNasaNews(private val newsList: ArrayList<ApodNewsData>): RecyclerView.Adapter<rvAdapterNasaNews.ViewHolder>() {

    private lateinit var dialogWindows: DialogWindows
    private val authViewModel = AuthViewModel()
    private val dbViewModel = DbViewModel()
    private val isAuthenticated = authViewModel.authState.value == AuthState.Authenticated

    private var isUserDataRefreshed : Boolean = false
    private var savedNewsDatas: ArrayList<String> = arrayListOf()
    private var userData: UserData = UserData(null.toString(), null.toString(), 0, null.toString(), 0)
    companion object{
        const val TAG = "rvNasaNewsAdapter_TAG"
    }

    init {
        Log.i(TAG, "rvAdapterLesson() called")
        if (isAuthenticated){
            refreshUserData(onSuccess = { success ->
                isUserDataRefreshed = success
            if (success){
                Log.i(TAG, "rvAdapterNasaNews() currentUser of db is not nul -> currentUser value: ${userData}")
            }
            })
        }
    }
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
        holder.contentTextView.text = item.explanation

        if (isUserDataRefreshed && savedNewsDatas.contains(item.date)){
            holder.save_icon.setImageResource(R.drawable.ic_bookmark_filled)
        }


        holder.save_icon.setOnClickListener {
            if (isAuthenticated) {
                if (savedNewsDatas.contains(item.date)) {
                    savedNewsDatas.remove(item.date)


                    dbViewModel.removeNasaNews(authViewModel.getCurrentUser()?.email.toString(), item){success ->
                        if (success){
                            holder.save_icon.setImageResource(R.drawable.ic_bookmark)
                            Toast.makeText(holder.itemView.context, "News was removed", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText((holder.itemView.context), "Something went wrong", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                else{
                    savedNewsDatas.add(item.date)

                    dbViewModel.saveNasaNews(authViewModel.getCurrentUser()?.email.toString(), item){success ->
                        if (success){
                            holder.save_icon.setImageResource(R.drawable.ic_bookmark_filled)
                            Toast.makeText(holder.itemView.context, "News saved", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText((holder.itemView.context), "Something went wrong", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
            else{
                var intent: Intent
                intent = Intent(holder.itemView.context, AuthActivity::class.java)
                startActivity(holder.itemView.context, intent, null)
            }
        }

        holder.contentTextView.setOnClickListener {
//            Toast.makeText(holder.itemView.context, "NASA news ${item.title} clicked", Toast.LENGTH_SHORT).show()
//            dialogWindows.nasaItemNews(item)
            if (holder.contentTextView.maxLines == 1) {
                holder.contentTextView.maxLines = Int.MAX_VALUE
            }
            else{
                holder.contentTextView.maxLines = 1
            }

        }

        holder.imageView.setOnClickListener {
            dialogWindows.fullImageFragment(item.url)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_apod)
        val dateTextView = itemView.findViewById<TextView>(R.id.text_date)
        val titleTextView = itemView.findViewById<TextView>(R.id.text_title)
        val contentTextView = itemView.findViewById<TextView>(R.id.text_content)
        val save_icon = itemView.findViewById<ImageView>(R.id.save_btns)
    }

    fun showSavedOnce(context: Context, onSuccess: (Boolean) -> Unit){
        if (isAuthenticated){
            newsList.clear()
            refreshUserData(onSuccess = {success ->
                isUserDataRefreshed = success
                onSuccess(success)
                if (success){
                    newsList.addAll(userData.apodNasaNews)

                }

            })
        }
        else{
            var intent: Intent
            intent = Intent(context, AuthActivity::class.java)
            startActivity(context, intent, null)
        }
    }

    fun refreshUserData(onSuccess: (Boolean) -> Unit) {
            dbViewModel.getUserData(authViewModel.getCurrentUser()?.email.toString()) { _userData ->

                userData = _userData

                if (_userData.username != null) {
                    onSuccess(true)
                    Log.i(TAG, "rvAdapterNasaNews() currentUser of db is not nul -> currentUser value: ${_userData}")

                    for (i in 0.. _userData.apodNasaNews.size - 1) {
//                    Log.i(TAG, "rvAdapterNasaNews() trying to add  value: ${nasanews[i].date}")
                        savedNewsDatas.add(_userData.apodNasaNews[i].date)
                        notifyDataSetChanged()
                    }

//                Log.i(TAG, "Saved Data News is finished the datas: ${savedNewsDatas} ")
                } else {
                    Log.i(TAG, "rvAdapterNasaNews() currentUser of db is nul -> currentUser value: ${userData}")
                    onSuccess(false)
                }

            }
    }
}