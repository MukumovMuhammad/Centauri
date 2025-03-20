package com.example.centauri

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = lessonList[position]
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: holder = $holder, position = $position")
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: item = $item")

        val itemType = getItemViewType(position)

        when (itemType){
            ItemType.LESSON.ordinal -> {
                holder.number!!.text = "Lesson ${item.number}"
                holder.title!!.text = item.title
            }
            ItemType.TEST.ordinal -> {
                holder.number!!.text =  "Test ${item.number}"
            }
        }


        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView.context, LessonTemplateActivity::class.java)
            intent.putExtra("lesson_number", item.number)
            intent.putExtra("partsNumber", item.numberOfParts)
            startActivity(holder.itemView.context, intent, null)
        }




    }


    inner class ViewHolder(itemView: View, itemType: Int) : RecyclerView.ViewHolder(itemView) {

        var number: TextView? = null;
        var icon: ImageView? = null;
        var title: TextView? = null;

        init {
            when (itemType){
                ItemType.LESSON.ordinal -> {
                    number = itemView.findViewById(R.id.lesson_number)
                    icon = itemView.findViewById(R.id.lesson_icon)
                    title = itemView.findViewById(R.id.lesson_title)
                }
                ItemType.TEST.ordinal -> {
                    number = itemView.findViewById(R.id.test_number)
                    icon = itemView.findViewById(R.id.test_icon)
                }

                else -> {

                }
            }
        }

    }
}
