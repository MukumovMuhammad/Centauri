package com.example.centauri

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat


class DialogWindows(var context: Context) {

    interface DialogCallback{
        fun onOkCLicked()
    }

    fun langChoosing(onItemClicked: (String) -> Unit){

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_languages, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.findViewById<TextView>(R.id.titleTextView).text = "Languages"
        dialogView.findViewById<TextView>(R.id.messageTextView).text = "Change the language of the app"

        dialogView.findViewById<RadioButton>(R.id.radio_en).setOnClickListener {
            onItemClicked("en")
            dialog.dismiss()
        }

        dialogView.findViewById<RadioButton>(R.id.radio_ru).setOnClickListener {
            onItemClicked("ru")
            dialog.dismiss()
        }

        dialog.show()

    }

    fun testResult(wasCorrect: Boolean, message: String, callback: DialogCallback, the_title: String = ""){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_space_alert, null)



        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Optional: transparent background
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        var title : String
        if (wasCorrect){
            dialogView.findViewById<TextView>(R.id.titleTextView).setTextColor(ContextCompat.getColor(context, R.color.bright_green))
            title = R.string.correct.toString()
        }
        else{
            dialogView.findViewById<TextView>(R.id.titleTextView).setTextColor(ContextCompat.getColor(context, R.color.bright_red))
            title = R.string.incorrect.toString()
        }

        if (the_title.isNotBlank()) title = the_title


        dialogView.findViewById<TextView>(R.id.titleTextView).text = title
        dialogView.findViewById<TextView>(R.id.messageTextView).text = message
        dialogView.findViewById<Button>(R.id.btnCancel).visibility = View.GONE

        dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            callback.onOkCLicked()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false) //also prevent the back button from closing the dialog

        dialog.show()

    }

    fun showSpaceDialog(title: String, message: String, callback: DialogCallback, showCancel: Boolean = true) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_space_alert, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Optional: transparent background
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.findViewById<TextView>(R.id.titleTextView).text = title
        dialogView.findViewById<TextView>(R.id.messageTextView).text = message
        if (!showCancel) dialogView.findViewById<Button>(R.id.btnCancel).visibility = View.GONE

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            callback.onOkCLicked()
            dialog.dismiss()
        }

        dialog.show()
    }

}