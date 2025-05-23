package com.example.centauri.models

import LocaleHelper
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centauri.rv.ApodNewsData
import com.google.firebase.firestore.FieldValue
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
            "testCompleted" to user.testCompleted,
            "apodNasaNews" to user.apodNasaNews
        )

        Log.i(TAG, "the user data ${user.email} , ${user.username}, will be added in collection named users")
        db.collection("users").document(user.email).set(userHash)
            .addOnSuccessListener {
                Log.i(TAG, "User added successfully to Firestore!")
                callback(true)
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
                    Log.i(TAG, "The document exist: ${document.data} ")


                    user = document.toObject(UserData::class.java)!!
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
            Log.e(TAG, "Network error: ${e.message}")
            return ApodNewsData("Network Error", "Please check your internet connection", "","")
        } catch (e: SerializationException) {

            Log.e(TAG, "Error parsing JSON: ${e.message}")
            return ApodNewsData("Data Error", "Failed to parse data", "","")
        } catch (e: Exception) {

            Log.e(TAG, "Error: ${e.message}")
            return ApodNewsData("Unknown Error", "Something went wrong", "","")
        }
    }


    suspend fun getNasa10News(): List<ApodNewsData> {

        Log.i(TAG, "getNasa10News fun is on work")
        var client = HttpClient(Android){
            install(ContentNegotiation) {
                json()
            }
        }
        try {
            Log.i(TAG,"getNasa10News fun is Succeed")
            val response = client.get("https://api.nasa.gov/planetary/apod?count=10&api_key=DEMO_KEY")
            val json = Json { ignoreUnknownKeys = true }
            Log.i(TAG,"got the response ${response.bodyAsText()}")
            return json.decodeFromString(response.bodyAsText())
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}")
            return  listOf<ApodNewsData>(
                ApodNewsData("Network Error", "Please check your internet connection", "","")
            )
        } catch (e: SerializationException) {

            Log.e(TAG, "Error parsing JSON: ${e.message}")
            return  listOf<ApodNewsData> (ApodNewsData( "Data Error", "Failed to parse data", "",""))
        } catch (e: Exception) {

            Log.e(TAG, "Error: ${e.message}")
            return  listOf<ApodNewsData> (ApodNewsData("Unknown Error", "Something went wrong", "",""))
        }
    }

    fun saveNasaNews(userEmail: String, apodNewsData: ApodNewsData, callback: (Boolean) -> Unit){
        db.collection("users").document(userEmail).update("apodNasaNews", FieldValue.arrayUnion(apodNewsData))
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener{
                callback(false)
            }
    }

    fun removeNasaNews(userEmail: String, apodNewsData: ApodNewsData, callback: (Boolean) -> Unit){
        db.collection("users").document(userEmail).update("apodNasaNews", FieldValue.arrayRemove(apodNewsData))
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener{
                callback(false)

            }
    }


}


