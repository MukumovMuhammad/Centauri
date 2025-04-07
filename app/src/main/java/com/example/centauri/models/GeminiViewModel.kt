package com.example.centauri.models


import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse


class GeminiViewModel: ViewModel() {
    companion object {
        const val API_KEY = ""
        const val TAG = "GEMENI_VIEW_MODEL_TAG"
    }
    val generativeModel = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = API_KEY)



    suspend fun isGemeniWorking(): Boolean {
        return try {
            val response: GenerateContentResponse = generativeModel.generateContent("Are you working? If yes just return true if no return false nothing more!")
            Log.i(TAG, "isGemeniWorking response : ${response.text}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "isGemeniWorking exception : ${e}")
            false
        }
    }



}