package fr.isen.fayet.isensmartcompanion.models

import com.google.firebase.BuildConfig
import com.google.firebase.vertexai.GenerativeModel
//
//
//object GoogleIA {
//
//    val key = BuildConfig.API_KEY
//    val generativeModel = GenerativeModel("gemini-1.5-flash", key)
//
//    suspend fun sendPrompt(prompt: String): String {
//        var response = String()
//        try {
//            val body = generativeModel.generateContent(prompt)
//            response = body.text.toString()
//        }
//        catch (err : Exception){
//            response = "Gemini AI couldn't answer to this"
//        }
//
//        return response
//    }
//
//}