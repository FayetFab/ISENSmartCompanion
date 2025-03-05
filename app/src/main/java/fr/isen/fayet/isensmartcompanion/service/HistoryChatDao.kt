package fr.isen.fayet.isensmartcompanion.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryChatDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMessage(message: ChatMessage)
//
//    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC")
//    suspend fun getAllMessages(): List<ChatMessage>
//
//    @Query("DELETE FROM chat_history WHERE id = :messageId")
//    suspend fun deleteMessage(messageId: Int)
//
//    @Query("DELETE FROM chat_history")
//    suspend fun clearHistory()
}