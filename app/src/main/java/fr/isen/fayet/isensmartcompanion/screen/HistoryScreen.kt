package fr.isen.fayet.isensmartcompanion.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.fayet.isensmartcompanion.R
import fr.isen.fayet.isensmartcompanion.models.AppDatabase
import fr.isen.fayet.isensmartcompanion.models.ChatMessage
import fr.isen.fayet.isensmartcompanion.screen.component.displayResponse
import fr.isen.fayet.isensmartcompanion.screen.component.displayUserPrompt
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(innerPadding: PaddingValues, db: AppDatabase) {
    val messageDao = db.historyChatDao()
    val scope = rememberCoroutineScope()
    var messageHistory by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }

    // Charger l'historique des messages au démarrage
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                messageHistory = messageDao.getAll() // Utilisez getAll() pour charger les messages
                Log.d("HistoryScreen", "Messages chargés : ${messageHistory.size}")
            } catch (e: Exception) {
                Log.e("RoomError", "Failed to load messages: ${e.message}")
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        messageDao.deleteAll() // Supprimer tous les messages
                        messageHistory = messageDao.getAll() // Recharger l'historique
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Delete, "deleteHistoryButton")
            }
        }
    ) { paddingValues ->
        HistoryDetail(
            messageHistory = messageHistory,
            innerPaddingValues = paddingValues,
            onDeleteMessage = { message ->
                scope.launch {
                    messageDao.deleteOne(message) // Supprimer un message spécifique
                    messageHistory = messageDao.getAll() // Recharger l'historique
                }
            }
        )
    }
}

@Composable
fun HistoryDetail(
    messageHistory: List<ChatMessage>,
    innerPaddingValues: PaddingValues,
    onDeleteMessage: (ChatMessage) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        itemsIndexed(messageHistory) { index, chatMessage ->
            if(chatMessage.isFromUser){
                displayUserPrompt(chatMessage.message)
            }
            else{
                displayResponse(chatMessage.message)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = if (chatMessage.isFromUser) colorResource(id = R.color.user_message_background)
                        else colorResource(id = R.color.bot_message_background),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                // Afficher le message
                Text(
                    text = chatMessage.message,
                    fontSize = 16.sp,
                    color = if (chatMessage.isFromUser) colorResource(id = R.color.user_message_text)
                    else colorResource(id = R.color.bot_message_text)
                )

                // Afficher la date et le bouton de suppression
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(chatMessage.date)),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.text_secondary)
                    )

                    Text(
                        text = "Delete",
                        modifier = Modifier.clickable { onDeleteMessage(chatMessage) },
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.error),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}