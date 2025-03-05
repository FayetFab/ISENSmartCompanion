package fr.isen.fayet.isensmartcompanion.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.withContext


@Composable
fun MainScreen(innerPadding: PaddingValues, generativeModel: GenerativeModel) {
    val context = LocalContext.current
    var userInput = remember { mutableStateOf("") }
    var responses = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxSize().padding((innerPadding)),
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
        Text(
            "", modifier = Modifier
                .fillMaxSize()
                .weight(0.5F)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(8.dp)) {
            TextField(value = userInput.value, onValueChange = { userInput.value = it}, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent),
                label = { Text("Pose une question") },
                modifier = Modifier.weight(1F)
            )
            OutlinedButton( onClick = {
                scope.launch(Dispatchers.IO) { // Utilisation de IO pour éviter le blocage UI
                    try {
                        val response = generativeModel.generateContent(Content(parts = listOf(TextPart(userInput.value))))

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "user input : ${userInput.value}", Toast.LENGTH_LONG).show()
                        }

                        response.text?.let {
                            responses.add(it)
                        } ?: responses.add("Erreur : Réponse vide")
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erreur : ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                        responses.add("Erreur : ${e.localizedMessage}")
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

        LazyColumn {
            items(responses, key = { it }) { response ->
                Text(response, modifier = Modifier.padding(8.dp))
            }
        }
    }

}