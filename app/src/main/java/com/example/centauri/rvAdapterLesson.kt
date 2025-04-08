package com.example.centauri

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.centauri.models.rvItemType
import com.example.centauri.templates.LessonTemplateActivity
import com.example.centauri.templates.TestActivity

class rvAdapterLesson(private val lessonList: Array<rvItemsData>) : RecyclerView.Adapter<rvAdapterLesson.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutId = when (viewType) {
            rvItemType.LESSON.ordinal -> R.layout.rv_lessons_items
            rvItemType.TEST.ordinal -> R.layout.rv_test_items
            rvItemType.PART.ordinal -> R.layout.rv_part_item
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val view = layoutInflater.inflate(layoutId, parent, false)
        return ViewHolder(view, viewType)

    }

    override fun getItemViewType(position: Int): Int {
        return lessonList[position].itemType.ordinal
    }



    override fun getItemCount(): Int = lessonList.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = lessonList[position]
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: holder = $holder, position = $position")
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: item = $item")

        val itemType: rvItemType = lessonList[position].itemType
        Log.i("rvAdapterLesson_TAG", "onBindViewHolder() called with: itemType = $itemType")

        when (itemType){
            rvItemType.LESSON -> {
                Log.i("rvAdapterLesson_TAG", "onBindViewHolder() this Item is LESSON")
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text = "Lesson ${item.number}"
                holder.title!!.text = item.title
            }
            rvItemType.TEST -> {
                Log.i("rvAdapterLesson_TAG", "onBindViewHolder() this Item is TEST")
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text =  "Test ${item.number}"
            }
            else -> {
                Log.i("rvAdapterLesson_TAG", "onBindViewHolder() this Item is PART")
                holder.number!!.text = "Part ${item.number}"
                holder.title!!.text = item.title
            }
        }

        if (item.itemType != rvItemType.PART){
            if (item.isClosed){
                holder.dark_overlay!!.visibility = View.VISIBLE
                holder.lockIcon!!.visibility = View.VISIBLE
            }else {
                holder.dark_overlay!!.visibility = View.GONE
                holder.lockIcon!!.visibility = View.GONE
            }
        }



        holder.itemView.setOnClickListener {
            if (item.isClosed){
                Toast.makeText(holder.itemView.context, "This lesson is closed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            when (itemType){
                rvItemType.LESSON ->{
                    var intent = Intent(holder.itemView.context, LessonTemplateActivity::class.java)
                    intent.putExtra("lesson_number", item.number)
                    intent.putExtra("partsNumber", item.numberOfParts)
                    startActivity(holder.itemView.context, intent, null)
                }

                rvItemType.TEST ->{
                    var intent = Intent(holder.itemView.context, TestActivity::class.java)
                    intent.putExtra("lesson_number", item.number)
                    intent.putExtra("partsNumber", item.numberOfParts)
                    startActivity(holder.itemView.context, intent, null)
                }
                else ->{

                }
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
                rvItemType.LESSON.ordinal -> {
                    Log.i("rvAdapterLesson_TAG", "ViewHolder called itemType LESSON")
                    number = itemView.findViewById(R.id.lesson_number)
                    icon = itemView.findViewById(R.id.lesson_icon)
                    title = itemView.findViewById(R.id.lesson_title)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }
                rvItemType.TEST.ordinal -> {
                    Log.i("rvAdapterLesson_TAG", "ViewHolder called itemType TEST")
                    number = itemView.findViewById(R.id.test_number)
                    icon = itemView.findViewById(R.id.test_icon)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }

                rvItemType.PART.ordinal -> {
                    Log.i("rvAdapterLesson_TAG", "ViewHolder called itemType PART")
                    number = itemView.findViewById(R.id.part_num)
                    title = itemView.findViewById(R.id.part_text)
                }

                else -> {

                }
            }
        }

    }
}
