package com.example.centauri

import android.content.Context
import androidx.appcompat.app.AlertDialog


class DialogWindows(var context: Context) {

    interface DialogCallback{
        fun onOkCLicked()
    }

    fun SimpleAlertDialog(title: String, message: String , callback: DialogCallback){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            callback.onOkCLicked()
            dialog.dismiss()
        }
        builder.show()

    }
}