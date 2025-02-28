package fr.isen.fayet.isensmartcompanion.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.fayet.isensmartcompanion.models.EventModel
import fr.isen.fayet.isensmartcompanion.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun EventsScreen(innerPadding: PaddingValues, eventHandler: (EventModel) -> Unit) {
    val context = LocalContext.current
    //var events = remember { mutableStateOf<List<EventModel>>(listOf()) }
    val events = remember { mutableStateListOf<EventModel>() }

    LaunchedEffect(Unit) {

        val call = RetrofitInstance.api.getEvents()
        call.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(p0: Call<List<EventModel>>, p1: Response<List<EventModel>>) {
                //First option (the better)
                //events.value = p1.body() ?: listOf()

                //Second option
//                p1.body()?.let {
//                    events.value = it
//                }
                //3eme option
                if (p1.isSuccessful) {
                    p1.body()?.let { eventList ->
                        events.clear()
                        events.addAll(eventList)
                        Log.d("fetchEvents", "Nombre d'événements reçus: ${events.size}")
                    } ?: Log.e("fetchEvents", "Réponse vide")
                } else {
                    Log.e("fetchEvents", "Erreur API: ${p1.code()}")
                }
            }

            override fun onFailure(p0: Call<List<EventModel>>, p1: Throwable) {
                Log.e("request", p1.message ?: "request failed")
            }
        })

    }

    Column(modifier = Modifier.padding(innerPadding)) {
        //val events = EventModel.fakeEvents()
        LazyColumn {
            //First option
//            items(events.value) { event ->
//                EventRow(event, eventHandler)
//            }
            //3eme option
            items(events) { event ->
                EventRow(event, eventHandler)
            }
        }
    }

}
@Composable
fun EventRow(event: EventModel, eventHandler: (EventModel) -> Unit) {
    val context = LocalContext.current
    Card(modifier = Modifier.padding(16.dp).clickable{
        eventHandler(event)
        }
    ){
        Column(modifier = Modifier.padding(16.dp)) {
//            Text(event.id)
//            Text(event.title)
//            Text(event.description)
//            Text(event.date)
//            Text(event.location)
//            Text(event.category)
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "📍 ${event.location}", style = MaterialTheme.typography.bodySmall)
            Text(text = "📅 ${event.date}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
