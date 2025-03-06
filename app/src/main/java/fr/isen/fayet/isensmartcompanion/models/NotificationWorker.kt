package fr.isen.fayet.isensmartcompanion.models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Récupérer les données de l'événement
        val eventTitle = inputData.getString("event_title") ?: return Result.failure()
        val eventDescription = inputData.getString("event_description") ?: return Result.failure()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNotificationPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasNotificationPermission) {
                // La permission n'est pas accordée, ne pas afficher la notification
                return Result.failure()
            }
        }


        // Créer la notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val channelId = "event_reminders"

        // Créer le canal de notification (nécessaire pour Android 8.0+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Event Reminders",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construire la notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Remplacez par votre icône de notification
            .setContentTitle("Rappel : $eventTitle")
            .setContentText(eventDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Afficher la notification
        notificationManager.notify(eventTitle.hashCode(), notification)

        return Result.success()
    }
}