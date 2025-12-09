package com.example.centauri.fragments.profile_frags


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.centauri.activities.MainActivity
import com.example.centauri.databinding.FragmentProfileBinding
import com.example.centauri.models.AuthViewModel
import com.example.centauri.models.DbViewModel
import com.example.centauri.models.UserData
import java.text.SimpleDateFormat
import java.util.Locale


class ProfileFragment : Fragment() {
    companion object{
        const val TAG = "ProfileFragment_TAG"
    }

    private lateinit var  db: DbViewModel
    private lateinit var  authViewModel: AuthViewModel
    private lateinit var binding: FragmentProfileBinding
    var userData: UserData = UserData(
        null.toString(),
        null.toString(),
        password = null.toString()
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        authViewModel = AuthViewModel()
        db = DbViewModel()

        db.getUserData(authViewModel.getCurrentUser()?.email.toString()){ user->
            Log.i(TAG, "user data is $user")
            userData = user

            binding.tvUsername.text = userData.username
            binding.tvEmail.text = userData.email

            userData.joined?.let { date ->
                val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                binding.tvJoinDate.text = "Joined: " + format.format(date).toString() // Format the Date object
            } ?: run {
                binding.tvJoinDate.text = "N/A"
            }

            binding.tvProgressPercent.text = (userData.PartCompleted * 25).toString() + "%"
            binding.pbLessonProgress.progress = userData.PartCompleted * 25
            binding.tvPassedCount.text = userData.PartCompleted.toString()
            binding.tvAvgMark.text = userData.AvarageMark.toString()
        }

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.ivBackArrow.setOnClickListener {
            val intent: Intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}