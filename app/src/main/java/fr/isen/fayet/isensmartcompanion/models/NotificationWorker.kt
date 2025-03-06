package fr.isen.fayet.isensmartcompanion.models

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Récupérer les données de l'événement
        val eventTitle = inputData.getString("event_title") ?: return Result.failure()
        val eventDescription = inputData.getString("event_description") ?: return Result.failure()


        // Créer la notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        // Créer le canal de notification (nécessaire pour Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Event Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construire la notification
        val notification = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Remplacez par votre icône de notification
            .setContentTitle("Rappel : $eventTitle")
            .setContentText(eventDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Afficher la notification
        notificationManager.notify(eventTitle.hashCode(), notification)
        Log.i("TAGNOTIF", "Erreurnotif ")

        return Result.success()
    }
}