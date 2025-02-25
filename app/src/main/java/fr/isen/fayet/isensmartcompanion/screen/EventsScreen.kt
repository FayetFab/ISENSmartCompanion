package fr.isen.fayet.isensmartcompanion.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.fayet.isensmartcompanion.models.EventModel


@Composable
fun EventsScreen(innerPadding: PaddingValues, eventHandler: (EventModel) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(innerPadding)) {
        val events = EventModel.fakeEvents()
        LazyColumn {
            items(events) { event ->
                EventRow(event) { }
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
            Row(horizontalArrangement = Arrangement.End) {

            }
            Text(event.title)
            Text(event.description)
            Text(event.date)
            Text(event.location)
            Text(event.category)
        }
    }


}
