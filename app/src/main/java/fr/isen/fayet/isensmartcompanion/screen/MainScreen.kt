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
import androidx.compose.material3.OutlinedTextField
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
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import fr.isen.fayet.isensmartcompanion.models.Message
import kotlinx.coroutines.withContext


@Composable
fun MainScreen(innerPadding: PaddingValues, generativeModel: GenerativeModel) {
    val context = LocalContext.current
    var userInput = remember { mutableStateOf("") }
    var responses = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<Message>() }

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
            items(messages) { message ->
                MessageBubble(message = message)
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

            TextField(value = userInput.value, onValueChange = { userInput.value = it}, colors = TextFieldDefaults.colors(
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
                scope.launch(Dispatchers.IO) { // Utilisation de IO pour éviter le blocage UI
                    try {
                        val response = generativeModel.generateContent(Content(parts = listOf(TextPart(userInput.value))))

                        withContext(Dispatchers.Main) {
                            //Toast.makeText(context, "user input : ${userInput.value}", Toast.LENGTH_LONG).show()
                        }

                        response.text?.let {
                            // Ajouter le message de l'utilisateur
                            messages.add(Message(text = userInput.value, isUser = true))
                            // Ajouter la réponse de l'IA
                            messages.add(Message(text = it, isUser = false))
                        } ?: messages.add(Message(text = "Erreur : Réponse vide", isUser = false))
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erreur : ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            Log.i("IAID", "Erreur : ${e.localizedMessage}")
                        }
                        messages.add(Message(text = "Erreur : ${e.localizedMessage}", isUser = false))
                    } finally {
                        // Réinitialiser la TextField après l'envoi
                        userInput.value = ""
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
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.isUser) Color.Red else Color.Blue
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.White
        )
    }
}