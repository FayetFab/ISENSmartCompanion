package fr.isen.fayet.isensmartcompanion.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import fr.isen.fayet.isensmartcompanion.service.HistoryChatDao
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(innerPadding: PaddingValues, chatDao: HistoryChatDao) {
//
//    val messages = remember { mutableStateListOf<ChatMessage>() }
//
//    LaunchedEffect(Unit) {
//        messages.addAll(chatDao.getAllMessages())
//    }
//
//    LazyColumn {
//        items(messages) { message ->
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text(text = "Question: ${message.question}", fontWeight = FontWeight.Bold)
//                Text(text = "Réponse: ${message.answer}")
//                Text(text = "Date: ${Date(message.timestamp)}", fontSize = 12.sp)
//            }
//        }
//    }
}