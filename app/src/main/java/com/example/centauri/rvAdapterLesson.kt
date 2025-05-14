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
import androidx.core.content.ContextCompat.getContextForLanguage
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.centauri.models.GeminiViewModel
import com.example.centauri.models.rvItemType
import com.example.centauri.templates.LessonTemplateActivity
import com.example.centauri.templates.TestActivity
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.DbViewModel

class rvAdapterLesson(private val lessonList: Array<rvItemsData>) : RecyclerView.Adapter<rvAdapterLesson.ViewHolder>() {

    private lateinit var dialogWindows: DialogWindows

    private val authViewModel = AuthViewModel()
    private val dbViewModel = DbViewModel()

    private val isAuthenticated = authViewModel.authState.value == AuthState.Authenticated
    private var testCompleted: Int = 0
    
    companion object{
        const val TAG = "rvAdapterLesson_TAG"
    }
    
    init {
        Log.i(TAG, "rvAdapterLesson() called")

        dbViewModel.getUserData(authViewModel.getCurrentUser()?.email.toString()) { userData ->

            if (userData.username != null) {
                Log.i(TAG, "rvAdapterLesson() currentUser of db is not nul -> currentUser value: ${userData}")
                testCompleted = userData.testCompleted
                notifyDataSetChanged()
            } else {
                Log.i(TAG, "rvAdapterLesson() currentUser of db is nul -> currentUser value: ${userData}")
                testCompleted = 0
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        dialogWindows = DialogWindows(parent.context)
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

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isClosed : Boolean = (
                when (testCompleted){
                    0 -> position > 5
                    1 -> position > 10
                    2 -> position > 16
                    else -> false
                }
                )
        val item = lessonList[position]
        Log.i(TAG, "onBindViewHolder() called with: holder = $holder, position = $position")
        Log.i(TAG, "onBindViewHolder() called with: position = $position")
        Log.i(TAG, "onBindViewHolder() called with: item = $item")
        Log.i(TAG, "onBindViewHolder() called with: item is close ?  = $isClosed")

        val itemType: rvItemType = lessonList[position].itemType
        Log.i(TAG, "onBindViewHolder() called with: itemType = $itemType")

        when (itemType){
            rvItemType.LESSON -> {
                Log.i(TAG, "onBindViewHolder() this Item is LESSON")
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text = holder.itemView.context.getString(R.string.lesson) + " " + item.number.toString()
                holder.title!!.text = item.title
            }
            rvItemType.TEST -> {
                Log.i(TAG, "onBindViewHolder() this Item is TEST")
                holder.icon!!.setImageResource(item.icon)
                holder.number!!.text =  holder.itemView.context.getString(R.string.test) + " " + item.number.toString()
            }
            else -> {
                Log.i(TAG, "onBindViewHolder() this Item is PART")
                holder.number!!.text = holder.itemView.context.getString(R.string.part) + " " + item.number.toString()
                holder.title!!.text = item.title
            }
        }

        if (item.itemType != rvItemType.PART){
            if (isClosed){
                holder.dark_overlay!!.visibility = View.VISIBLE
                holder.lockIcon!!.visibility = View.VISIBLE
            }else {
                holder.dark_overlay!!.visibility = View.GONE
                holder.lockIcon!!.visibility = View.GONE
            }
        }



        holder.itemView.setOnClickListener {
            if (isClosed){
                Toast.makeText(holder.itemView.context, getString(holder.itemView.context,R.string.lesson_closed), Toast.LENGTH_SHORT).show()
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
                    dialogWindows.showSpaceDialog(getString(holder.itemView.context,R.string.start_test) , getString(holder.itemView.context,R.string.take_test_description),object : DialogWindows.DialogCallback{
                        override fun onOkCLicked() {
                            var intent: Intent
                            if (isAuthenticated){

                                dbViewModel.getUserData(authViewModel.getCurrentUser()?.email.toString()) { userData ->
                                    if (userData.username != null) {
                                        intent = Intent(holder.itemView.context, TestActivity::class.java)
                                        intent.putExtra("lesson_number", item.number)
                                        intent.putExtra("partsNumber", item.numberOfParts)
                                        intent.putExtra("userEmail", AuthViewModel().getCurrentUser()!!.email.toString())
                                        startActivity(holder.itemView.context, intent, null)
                                    } else {
                                        Log.i(
                                            TAG,
                                            "onBindViewHolder() currentUser of db is nul -> currentUser value: ${dbViewModel.curentUser.value}"
                                        )
                                        dialogWindows.testResult(
                                            false,
                                            getString(holder.itemView.context,R.string.fail_to_get_data),
                                            object : DialogWindows.DialogCallback {
                                                override fun onOkCLicked() {
                                                    return
                                                }

                                            },
                                            getString(holder.itemView.context,R.string.error)
                                        )
                                    }
                                }

                            }
                            else{
                                intent = Intent(holder.itemView.context, AuthActivity::class.java)
                                startActivity(holder.itemView.context, intent, null)
                            }
                        }
                    })

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
                    Log.i(TAG, "ViewHolder called itemType LESSON")
                    number = itemView.findViewById(R.id.lesson_number)
                    icon = itemView.findViewById(R.id.lesson_icon)
                    title = itemView.findViewById(R.id.lesson_title)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }
                rvItemType.TEST.ordinal -> {
                    Log.i(TAG, "ViewHolder called itemType TEST")
                    number = itemView.findViewById(R.id.test_number)
                    icon = itemView.findViewById(R.id.test_icon)
                    lockIcon = itemView.findViewById(R.id.lock_icon)
                    dark_overlay = itemView.findViewById(R.id.dark_overlay)
                }

                rvItemType.PART.ordinal -> {
                    Log.i(TAG, "ViewHolder called itemType PART")
                    number = itemView.findViewById(R.id.part_num)
                    title = itemView.findViewById(R.id.part_text)
                }

                else -> {

                }
            }
        }

    }
}
