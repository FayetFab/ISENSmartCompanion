package fr.isen.fayet.isensmartcompanion.models

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import fr.isen.fayet.isensmartcompanion.service.HistoryChatDao

@Database(
    entities = [ChatMessage::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyChatDao() : HistoryChatDao
}