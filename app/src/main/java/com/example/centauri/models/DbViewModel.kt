package com.example.firebasetodoapp

import LocaleHelper
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centauri.models.ApodNewsData
import com.example.centauri.models.UserData
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

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

    fun getLessonContext(lessonNumber: Int, context: Context, onResult: (ArrayList<String>) -> Unit){
        var lang = LocaleHelper.getSavedLanguage(context)
        if (lang == "en") lang = ""
        db.collection("images").document("lessons_contexts$lang").get()
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


    fun getLessonTitles(lessonNumber: Int, context: Context, onResult: (ArrayList<String>) -> Unit){
        var lang = LocaleHelper.getSavedLanguage(context)
        if (lang == "en") lang = ""
        db.collection("images").document("lessons_titles$lang").get()
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

    suspend fun getNasaApod(): ApodNewsData {
       var client = HttpClient(Android){
            install(ContentNegotiation) {
                json()
            }
        }
        try {
            val response = client.get("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY")
            val json = Json { ignoreUnknownKeys = true }
            return json.decodeFromString<ApodNewsData>(response.bodyAsText())
        } catch (e: IOException) {
            return ApodNewsData("Network Error", "Please check your internet connection", "","")
        } catch (e: SerializationException) {

            return ApodNewsData("Data Error", "Failed to parse data", "","")
        } catch (e: Exception) {
            return ApodNewsData("Unknown Error", "Something went wrong", "","")
        }
    }
}