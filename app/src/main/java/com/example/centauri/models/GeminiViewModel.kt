package com.example.centauri.models


import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.centauri.R
import com.example.centauri.templates.TestQuestion
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import io.ktor.http.ContentType.Application.Json
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext


class GeminiViewModel: ViewModel() {
    companion object {
        const val API_KEY = ""
        const val TAG = "GEMENI_VIEW_MODEL_TAG"
    }
    val generativeModel = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = API_KEY)


    val chat = generativeModel.startChat()




    suspend fun startNewTest(context: Context): TestQuestion {
        clearChatHistory()

        val Lesson: String = """
            Lesson 1
            ${context.getString(R.string.lesson1_title_1)} 
            ${context.getString(R.string.lesson1_text_1)} 
            ${context.getString(R.string.lesson1_title_2)} 
            ${context.getString(R.string.lesson1_text_2)}
            ${context.getString(R.string.lesson1_title_3)} 
            ${context.getString(R.string.lesson1_text_3)} 
            ${context.getString(R.string.lesson1_title_4)} 
            ${context.getString(R.string.lesson1_text_4)}
            
            Lesson 2
            ${context.getString(R.string.lesson2_title_1)} 
            ${context.getString(R.string.lesson2_text_1)} 
            ${context.getString(R.string.lesson2_title_2)} 
            ${context.getString(R.string.lesson2_text_2)}
            ${context.getString(R.string.lesson2_title_3)} 
            ${context.getString(R.string.lesson2_text_3)}
            
            Lesson 3
            ${context.getString(R.string.lesson3_title_1)} 
            ${context.getString(R.string.lesson3_text_1)} 
            ${context.getString(R.string.lesson3_title_2)} 
            ${context.getString(R.string.lesson3_text_2)}
            ${context.getString(R.string.lesson3_title_3)} 
            ${context.getString(R.string.lesson3_text_3)}     
            
            Lesson 4
            ${context.getString(R.string.lesson4_title_1)} 
            ${context.getString(R.string.lesson4_text_1)} 
            ${context.getString(R.string.lesson4_title_2)} 
            ${context.getString(R.string.lesson4_text_2)}
            ${context.getString(R.string.lesson4_title_3)} 
            ${context.getString(R.string.lesson4_text_3)}
        """.trimIndent()


        val startPromptExplonation = """
You are an expert in Astronomy and Astrophysics, and your role is to test your student’s understanding of the following lessons:
${Lesson}
Create a multiple-choice question based strictly on the content of the above lessons. You will generate one question at a time.
Your response must be in valid JSON format only — do NOT include any explanations, comments, or code blocks.

Follow this exact structure for the response:
{
  "feedback": "Give your feedback for the last question here (this wil be asked later in the chat).",
  "question": "Which planet is the third from the Sun?",
  "A": "Mercury",
  "B": "Venus",
  "C": "Earth",
  "D": "Mars",
  "answer": 2
}
Guidelines:

Provide only one question per response.

Ensure the question is directly related to the given lesson content.

Keep the question clear and concise.

answer should be the index of the correct choice, where:

A = 0

B = 1

C = 2

D = 3

Return only the JSON object — no extra text, no formatting, no explanations.
        """.trimIndent()

        return try {
            val response : GenerateContentResponse = chat.sendMessage(prompt = startPromptExplonation)
            Log.i(TAG, "startNewTest response : ${response.text}")
            val json = Json{ignoreUnknownKeys = true}
            val jsonRegex = "\\{[\\s\\S]*\\}".toRegex()
            val match = jsonRegex.find(response.text.toString())
            val jsonString = match?.value ?: ""

            Log.i(TAG, "startNewTest jsonString : $jsonString")
            json.decodeFromString<TestQuestion>(jsonString)
        } catch (e: Exception){
            Log.e(TAG,"Chat error:  ${e.message}")

            TestQuestion("Something went wrong","Something went wrong","","","","",1)
        }
    }


    suspend fun nextTest(context: Context, userAnswer: Int): TestQuestion {
        val prompt = "the student chosed $userAnswer! Give a concise feedback on that and next question in the same Json format!"

        return try {
            val response : GenerateContentResponse = chat.sendMessage(prompt = prompt)
            Log.i(TAG, "next test response : ${response.text}")
            val json = Json{ignoreUnknownKeys = true}
            val jsonRegex = "\\{[\\s\\S]*\\}".toRegex()
            val match = jsonRegex.find(response.text.toString())
            val jsonString = match?.value ?: ""

            Log.i(TAG, "next test  jsonString : $jsonString")
            json.decodeFromString<TestQuestion>(jsonString)
        } catch (e: Exception){
            Log.e(TAG,"Chat error:  ${e.message}")

            TestQuestion("Something went wrong","Something went wrong","","","","",1)
        }
    }

    suspend fun testResult(context: Context, correctAnswers: Int): String{
        var past: String = "past"
        if (correctAnswers > 8){
            past = "past the test and now can continue studying other lessons"
        }
        else{
            past = "did not past the test and need to review the lessons"
        }
        val prompt = """Great now as you see the student answered corectly only $correctAnswers out of 10 questions.
            |This means that he $past
            |Now just give your small feedbacks on how he did?""".trimMargin()

        return try {
            val response : GenerateContentResponse = chat.sendMessage(prompt = prompt)
            Log.i(TAG, "next test response : ${response.text}")
            response.text.toString()

        } catch (e: Exception){
            Log.e(TAG,"Chat error:  ${e.message}")

            "Sorry the AI assistence is not working Try later"

        }
    }


    suspend fun sendChatMessage(context: Context, message: String){
        try {
            val response = chat.sendMessage(prompt = message)
            Log.i(TAG, "Chat message response : ${response.text}")
        } catch (e: Exception){
            Log.e(TAG,"Chat error:  ${e.message}")
        }
    }


    fun clearChatHistory(){
        chat.history.clear()
    }

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