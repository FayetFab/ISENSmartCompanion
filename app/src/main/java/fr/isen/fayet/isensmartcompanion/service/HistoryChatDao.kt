package fr.isen.fayet.isensmartcompanion.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.isen.fayet.isensmartcompanion.models.ChatMessage

@Dao
interface HistoryChatDao {
    @Query("SELECT * FROM chatmessage")
    suspend fun getAll(): List<ChatMessage>

    @Insert
    suspend fun insert(vararg chatMessage: ChatMessage)

    @Delete
    suspend fun deleteOne(vararg chatMessage: ChatMessage)

    @Query("DELETE FROM chatmessage")
    suspend fun deleteAll()
}