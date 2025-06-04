package com.example.centauri

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.ortiz.touchview.TouchImageView
import com.bumptech.glide.request.transition.Transition


class FullImageDialogFragment(private val imageUrl: String): DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_full_image)

        val imageView = dialog.findViewById<TouchImageView>(R.id.full_image)

        val closeIcon = dialog.findViewById<ImageView>(R.id.ic_close)



//        Loading url image into TouchView (through)
        Glide.with(requireContext())
            .load(imageUrl)
            .thumbnail(Glide.with(requireContext()).load(R.drawable.loading_gif))
            .error(R.drawable.ic_img_default)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    imageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    imageView.setImageDrawable(placeholder)
                }
            })


        // Tap to dismiss
        closeIcon.setOnClickListener {
            dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        return dialog
    }
}