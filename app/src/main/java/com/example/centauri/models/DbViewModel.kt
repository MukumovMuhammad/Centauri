package com.example.firebasetodoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centauri.models.UserData

import com.google.firebase.firestore.FirebaseFirestore

class DbViewModel: ViewModel() {

    val db = FirebaseFirestore.getInstance()


    private val _user = MutableLiveData<UserData>()
    val curentUser: LiveData<UserData> = _user



    fun addUserData(user: UserData, callback: (Boolean) -> Unit){
        Log.i("dbTAG", "addUserData fun is on work")
        val userHash = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "rating" to user.rating,
            "password" to user.password
        )

        Log.i("dbTAG", "the user data ${user.email} , ${user.username}, will be added in collection named users")
        db.collection("users").document(user.email).set(userHash)
            .addOnSuccessListener {
                Log.i("dbTAG", "User added successfully to Firestore!")
                changeCurentUser(user)
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("dbTAG", "Error adding user to Firestore", e)
                callback(false)
            }

    }

    fun getUserData(email: String) : UserData{
        Log.i("dbTAG", "getUserData fun is on work")
        var user: UserData = UserData("null", "null", 0, "null")
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                Log.i("dbTAG", "getUserData fun is Succeed")
                if(document.exists()){
                    Log.i("dbTAG", "document ${document.data} is exist")
                    val username = document.getString("username")
                    val email = document.getString("email")
                    val rating = document.getString("rating")
                    val password = document.getString("password")

                    user = UserData(username!!, email!!, rating!!.toInt(),password!!)
                }
            }


        return user;
    }

    fun getLessonImgs(lessonNumber: Int, onResult: (ArrayList<String>) -> Unit) {
        db.collection("images").document("lessons_imgs").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val images = document.get("lesson$lessonNumber") as? ArrayList<String> ?: arrayListOf()
                    onResult(images)
                } else {
                    onResult(arrayListOf()) // empty list if doc doesn't exist
                }
            }
            .addOnFailureListener { exception ->
                Log.e("dbTAG", "Error getting lesson images: $exception")
                onResult(arrayListOf()) // return empty list on failure
            }
    }




    fun changeCurentUser(newUser: UserData){
        _user.value = newUser
    }


    fun isUserExist(email: String): Boolean{
        var isExist = false
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    isExist = true
                } else {
                    isExist = false
                }
            }

        return isExist;
    }
}