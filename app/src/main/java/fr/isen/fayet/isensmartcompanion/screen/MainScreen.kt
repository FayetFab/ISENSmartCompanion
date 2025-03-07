package fr.isen.fayet.isensmartcompanion.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.fayet.isensmartcompanion.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import fr.isen.fayet.isensmartcompanion.models.AppDatabase
import fr.isen.fayet.isensmartcompanion.models.ChatMessage
import kotlinx.coroutines.withContext


@Composable
fun MainScreen(innerPadding: PaddingValues, generativeModel: GenerativeModel, db: AppDatabase) {
    val context = LocalContext.current
    var userInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val dao = db.historyChatDao()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding((innerPadding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painterResource(R.drawable.isen),
            context.getString(R.string.isen_logo),
            modifier = Modifier.size(150.dp)
        )
        Text(
            context.getString(R.string.app_name)
        )

        val lazyColumnState = rememberLazyListState()
        LaunchedEffect(messages.size) {
            lazyColumnState.scrollToItem(messages.size)
        }

        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.weight(1F)
        ) {
            items(messages) { chatMessage ->
                MessageBubble(message = chatMessage)
                //Text(response, modifier = Modifier.padding(8.dp))
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(value = userInput, onValueChange = { userInput = it}, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent),
                label = { Text("Pose une question") },
                modifier = Modifier
                    .weight(1F)
                    .padding(end = 8.dp)
            )
            OutlinedButton( onClick = {
                if (userInput.isNotBlank()) {
                    scope.launch(Dispatchers.IO) {
                        try {
                            // Enregistrer le message de l'utilisateur
                            val userMessage = ChatMessage(
                                message = userInput,
                                isFromUser = true,
                                date = System.currentTimeMillis()
                            )
                            //dao.insert(userMessage) Non fonctionnels
                            messages.add(userMessage)

                            // Envoyer la question à l'IA
                            val response = generativeModel.generateContent(
                                Content(parts = listOf(TextPart(userInput)))
                            )

                            // Enregistrer la réponse de l'IA
                            response.text?.let { aiResponse ->
                                val aiMessage = ChatMessage(
                                    message = aiResponse,
                                    isFromUser = false,
                                    date = System.currentTimeMillis()
                                )
                                //dao.insert(aiMessage)
                                messages.add(aiMessage)
                            } ?: run {
                                val errorMessage = ChatMessage(
                                    message = "Erreur : Réponse vide",
                                    isFromUser = false,
                                    date = System.currentTimeMillis()
                                )
                                messages.add(errorMessage)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Erreur : ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e("IAError", "Erreur : ${e.localizedMessage}")
                            }

                            // Ajouter un message d'erreur à l'UI
                            val errorMessage = ChatMessage(
                                message = "Erreur : ${e.localizedMessage}",
                                isFromUser = false,
                                date = System.currentTimeMillis()
                            )
                            messages.add(errorMessage)
                        } finally {
                            // Réinitialiser le champ de saisie
                            userInput = ""
                        }
                    }
                }

            }, modifier = Modifier.background(Color.Red, shape = RoundedCornerShape(70)),
                content = {
                    Image(
                        painterResource(R.drawable.send), "",
                        modifier = Modifier.size(50.dp)
                    )
                })
        }


    }

}

@Composable
fun MessageBubble(message: ChatMessage) {
    val bubbleColor = if (message.isFromUser) Color.Red else Color.Blue
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = message.message,
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.White
        )
    }
}