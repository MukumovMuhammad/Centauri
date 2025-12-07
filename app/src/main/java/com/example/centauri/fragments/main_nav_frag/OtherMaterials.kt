package com.example.centauri.fragments.main_nav_frag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.centauri.DialogWindows
import com.example.centauri.R
import com.example.centauri.activities.AuthActivity
import com.example.centauri.activities.templates.TestActivity
import com.example.centauri.databinding.FragmentNasaNewsBinding
import com.example.centauri.databinding.FragmentOtherMaterialsBinding
import com.example.centauri.models.AuthViewModel
import com.example.centauri.rv.rvAdapterLesson.Companion.TAG

class OtherMaterials : Fragment() {
    private lateinit var dialogWindows: DialogWindows
    private lateinit var binding : FragmentOtherMaterialsBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtherMaterialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialogWindows = DialogWindows(requireContext())
        super.onViewCreated(view, savedInstanceState)


        binding.icAboutCentauri.setOnClickListener{
            dialogWindows.showSpaceDialog(getString(R.string.about_centauri), getString(R.string.about_centauri_text),object :
                DialogWindows.DialogCallback {
                override fun onOkCLicked() {

                }

                override fun onCancelClicked() {
//
                }
            }, showCancel = false)
        }

        binding.icSolarPlanets.setOnClickListener{
            dialogWindows.showSpaceDialog(getString(R.string.coming_soon), getString(R.string.coming_soon_text),object :
                DialogWindows.DialogCallback {
                override fun onOkCLicked() {

                }

                override fun onCancelClicked() {

                }
            },showCancel = false)
        }

        binding.icScientist.setOnClickListener{
            dialogWindows.showSpaceDialog(getString(R.string.coming_soon), getString(R.string.coming_soon_text),object :
                DialogWindows.DialogCallback {
                override fun onOkCLicked() {

                }

                override fun onCancelClicked() {

                }
            },showCancel = false)
        }

    }


}