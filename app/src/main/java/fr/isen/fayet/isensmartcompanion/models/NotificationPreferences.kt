package fr.isen.fayet.isensmartcompanion.models

import android.content.Context
import android.content.SharedPreferences

class NotificationPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("event_notifications", Context.MODE_PRIVATE)

    // Vérifie si les notifications sont activées pour un événement
    fun isNotificationEnabled(eventId: String): Boolean {
        return sharedPreferences.getBoolean(eventId, false)
    }

    // Active ou désactive les notifications pour un événement
    fun setNotificationEnabled(eventId: String, enabled: Boolean) {
        sharedPreferences.edit().putBoolean(eventId, enabled).apply()
    }
}