package com.example.centauri

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide

class FullImageDialogFragment(private val imageUrl: String): DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_full_image)

        val imageView = dialog.findViewById<ImageView>(R.id.full_image)
        Glide.with(requireContext()).load(imageUrl).into(imageView)

        // Tap to dismiss
        imageView.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}