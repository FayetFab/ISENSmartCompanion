package fr.isen.fayet.isensmartcompanion.service

import fr.isen.fayet.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.http.GET

public interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}