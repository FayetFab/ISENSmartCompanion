package fr.isen.fayet.isensmartcompanion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.fayet.isensmartcompanion.models.EventModel
import fr.isen.fayet.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getSerializableExtra(EventDetailActivity.eventExtraKey) as? EventModel
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  if(event != null) {
                      EventDetail(event, innerPadding)
                  }
                }
            }
        }
    }
    companion object {
        val eventExtraKey = "eventExtraKey"
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "EventDetailActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "EventDetailActivity onResume")
    }

    override fun onStop() {
        Log.d("lifecycle", "EventDetailActivity onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("lifecycle", "EventDetailActivity onDestroy")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d("lifecycle", "EventDetailActivity onPause")
        super.onPause()
    }
}

@Composable
fun EventDetail(event : EventModel, innerPaddingValues: PaddingValues) {
    Column(Modifier
        .fillMaxSize()
        .padding(innerPaddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = event.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = event.date,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = event.description,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = "Lieu : ${event.location}",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Catégorie : ${event.category}",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ISENSmartCompanionTheme {
        EventDetail(EventModel.fakeEvents().first(), PaddingValues(8.dp))
    }
}