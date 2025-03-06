package fr.isen.fayet.isensmartcompanion

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//ilfaut implementer les 3dessous
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import fr.isen.fayet.isensmartcompanion.models.EventModel
import fr.isen.fayet.isensmartcompanion.models.NotificationPreferences
import fr.isen.fayet.isensmartcompanion.models.NotificationWorker
import fr.isen.fayet.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import java.util.concurrent.TimeUnit

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
    val context = LocalContext.current
    val notificationPreferences = remember { NotificationPreferences(context) }
    var isNotificationEnabled by remember { mutableStateOf(notificationPreferences.isNotificationEnabled(event.id)) }

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

        // Icône d'abonnement aux notifications
        IconButton(
            onClick = {
                isNotificationEnabled = !isNotificationEnabled
                notificationPreferences.setNotificationEnabled(event.id, isNotificationEnabled)
                if (isNotificationEnabled) {
                    scheduleNotification(context, event)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                imageVector = if (isNotificationEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                contentDescription = if (isNotificationEnabled) "Désactiver les notifications" else "Activer les notifications"
            )
            }
    }
}

fun scheduleNotification(context: Context, event: EventModel) {
    val workManager = WorkManager.getInstance(context)

    // Préparer les données à passer au Worker
    val data = Data.Builder()
        .putString("event_title", event.title)
        .putString("event_description", event.description)
        .build()

    // Créer une tâche de travail unique avec un délai de 10 secondes
    val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
        .setInputData(data)
        .setInitialDelay(10, TimeUnit.SECONDS) // Notification après 10 secondes
        .build()

    // Planifier la tâche
    workManager.enqueue(notificationWork)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ISENSmartCompanionTheme {
        EventDetail(EventModel.fakeEvents().first(), PaddingValues(8.dp))
    }
}