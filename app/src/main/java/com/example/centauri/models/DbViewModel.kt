package com.example.firebasetodoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centauri.models.UserData

import com.google.firebase.firestore.FirebaseFirestore

class DbViewModel: ViewModel() {

    val db = FirebaseFirestore.getInstance()

    companion object{
        const val TAG = "dbTAG"
    }

    private val _user = MutableLiveData<UserData>()
    var curentUser: LiveData<UserData> = _user



    fun addUserData(user: UserData, callback: (Boolean) -> Unit){
        Log.i(TAG, "addUserData fun is on work")
        val userHash = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "rating" to user.rating,
            "password" to user.password,
            "testCompleted" to user.testCompleted
        )

        Log.i(TAG, "the user data ${user.email} , ${user.username}, will be added in collection named users")
        db.collection("users").document(user.email).set(userHash)
            .addOnSuccessListener {
                Log.i(TAG, "User added successfully to Firestore!")

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding user to Firestore", e)
                callback(false)
            }

    }

    fun getUserData(email: String, onResult: (UserData) -> Unit){
        Log.i(TAG, "getUserData fun is on work")
        var user: UserData = UserData("null", "null", 0, "null", 0)
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                Log.i(TAG, "getUserData fun is Succeed")
                if(document.exists()){
                    Log.i(TAG, "document ${document.data} is exist")
                    val testCompleted = document.get("testCompleted").toString().toInt()
                    val username = document.getString("username").toString()
                    val email = document.getString("email").toString()
                    val rating = document.get("rating").toString().toInt()
                    val password = document.getString("password").toString()

                    user = UserData(username, email, rating, password, testCompleted)
                    onResult(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting user data from Firestore", exception)
                user = UserData("null", "null", 0, "null", 0)
                onResult(user)
                }
    }

    fun testPastUser(userEmail: String,testNumber: Int, callback: (Boolean) -> Unit){
        Log.i(TAG, "testPastUser fun is on work testNumber $testNumber")

        getUserData(userEmail) { userData ->
            if (userData.testCompleted >= testNumber) {
                callback(true)
                return@getUserData
            }
            Log.i(TAG, "testPastUser fun is Succeed")
            db.collection("users").document(userData.email).update("testCompleted", testNumber)
                .addOnSuccessListener {
                    Log.i(TAG, "testPastUser fun is Succeed")
                    callback(true)
                    return@addOnSuccessListener
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding user to Firestore", e)
                    callback(false)
                }
        }


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
                Log.e(TAG, "Error getting lesson images: $exception")
                onResult(arrayListOf()) // return empty list on failure
            }
    }

    fun getLessonContext(lessonNumber: Int, onResult: (ArrayList<String>) -> Unit){
        db.collection("images").document("lessons_contexts").get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val contexts = document.get("lesson$lessonNumber") as? ArrayList<String> ?: arrayListOf()
                    onResult(contexts)
                }
                else{
                    onResult(arrayListOf())
                }

            }
            .addOnFailureListener{e ->
                Log.e(TAG, "Error getting lesson contexts: $e")
                onResult(arrayListOf("Sorry something went wrong", "Sorry something went wrong Check Internet Connection"))
            }
    }


    fun getLessonTitles(lessonNumber: Int, onResult: (ArrayList<String>) -> Unit){
        db.collection("images").document("lessons_titles").get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val titles = document.get("lesson$lessonNumber") as? ArrayList<String> ?: arrayListOf()
                    onResult(titles)
                }
                else{
                    onResult(arrayListOf())
                }

            }
            .addOnFailureListener{e ->
                Log.e(TAG, "Error getting lesson titles: $e")
                onResult(arrayListOf("Sorry something went wrong", "Sorry something went wrong Check Internet Connection"))
            }
    }

    fun getLessonTitlesAndContext(LessonsToGet: ArrayList<Int>, onResult: (ArrayList<String>) -> Unit) {
        var titles: ArrayList<String> = arrayListOf()
        var isTitlesAdded: Boolean = false

        var text: ArrayList<String> = arrayListOf()
        var isTextAdded: Boolean = false

                        ///////////// TITLES /////////////
        db.collection("images").document("lessons_titles").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    for (i in LessonsToGet) {
                        val the_titles =
                            document.get("lesson$i") as? ArrayList<String> ?: arrayListOf()
                        for (t in the_titles) {
                            titles.add(t)
                        }
                    }
                    isTitlesAdded = true
                }
                if (isTextAdded){
                    onResult(putLessonsTogether(titles, text))
                }

            }
                        ///////////// TEXT /////////////
        db.collection("images").document("lessons_contexts").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    for (i in LessonsToGet) {
                        val the_titles =
                            document.get("lesson$i") as? ArrayList<String> ?: arrayListOf()
                        for (t in the_titles) {
                            text.add(t)
                        }
                    }
                    isTextAdded = true
                }

                if (isTitlesAdded){
                    onResult(putLessonsTogether(titles, text))
                }

            }

    }

    private fun putLessonsTogether(the_titles: ArrayList<String>, the_contexts: ArrayList<String>): ArrayList<String>{
        val newArrayList: ArrayList<String> = arrayListOf()
        for (i in 0..the_titles.size-1){
            newArrayList.add(the_titles[i])
            newArrayList.add(the_contexts[i])
        }


        return newArrayList
    }




//    fun changeCurentUser(email: String, onComplete: (Boolean) -> Unit) {
//        Log.i(TAG, "changeCurrentUser is on work!")
//        db.collection("users").document(email).get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    val testCompleted = document.get("testCompleted").toString().toInt()
//                    val username = document.getString("username").toString()
//                    val email = document.getString("email").toString()
//                    val rating = document.get("rating").toString().toInt()
//                    val password = document.getString("password").toString()
//
//                    _user.value = UserData(username, email, rating, password, testCompleted)
//                    Log.i(TAG, "the user new value is ${curentUser.value}")
//                    onComplete(true)
//                } else {
//                    onComplete(false)
//                }
//            }
//            .addOnFailureListener {
//                Log.e(TAG, "Error getting user data from Firestore", it)
//                _user.value = UserData("null", "null", 0, "null", 0)
//                onComplete(false)
//            }
//    }



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