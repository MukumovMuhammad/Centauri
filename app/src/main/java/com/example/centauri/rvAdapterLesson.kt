package com.example.centauri

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView

class rvAdapterLesson(private val lessonList: Array<rvItemsData>) : RecyclerView.Adapter<rvAdapterLesson.ViewHolder>() {

    enum class ItemType{
        PART,
        LESSON,
        TEST

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutId = when (viewType) {
            ItemType.LESSON.ordinal -> R.layout.rv_lessons_items
            ItemType.TEST.ordinal -> R.layout.rv_test_items
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val view = layoutInflater.inflate(layoutId, parent, false)
        return ViewHolder(view, viewType)

    }

    override fun getItemViewType(position: Int): Int {
        return when(lessonList[position].isTest){
            true -> ItemType.TEST.ordinal
            false -> ItemType.LESSON.ordinal
        }
    }

    override fun getItemCount(): Int = lessonList.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = lessonList[position]
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: holder = $holder, position = $position")
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: item = $item")

        val itemType = getItemViewType(position)

        when (itemType){
            ItemType.LESSON.ordinal -> {
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text = "Lesson ${item.number}"
                holder.title!!.text = item.title
            }
            ItemType.TEST.ordinal -> {
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text =  "Test ${item.number}"
            }
        }


        if (item.isClosed){
            holder.dark_overlay!!.visibility = View.VISIBLE
            holder.lockIcon!!.visibility = View.VISIBLE
        }else{
            holder.dark_overlay!!.visibility = View.GONE
            holder.lockIcon!!.visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            if(item.isClosed){
                Toast.makeText(holder.itemView.context, "This lesson is closed", Toast.LENGTH_SHORT).show()
            }
            else{
                var intent = Intent(holder.itemView.context, LessonTemplateActivity::class.java)
                intent.putExtra("lesson_number", item.number)
                intent.putExtra("partsNumber", item.numberOfParts)
                startActivity(holder.itemView.context, intent, null)
            }
        }




    }


    inner class ViewHolder(itemView: View, itemType: Int) : RecyclerView.ViewHolder(itemView) {

        var dark_overlay: ImageView? = null;
        var lockIcon: ImageView? = null;
        var number: TextView? = null;
        var icon: ImageView? = null;
        var title: TextView? = null;

        init {
            when (itemType){
                ItemType.LESSON.ordinal -> {
                    number = itemView.findViewById(R.id.lesson_number)
                    icon = itemView.findViewById(R.id.lesson_icon)
                    title = itemView.findViewById(R.id.lesson_title)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }
                ItemType.TEST.ordinal -> {
                    number = itemView.findViewById(R.id.test_number)
                    icon = itemView.findViewById(R.id.test_icon)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }

                else -> {

                }
            }
        }

    }
}
