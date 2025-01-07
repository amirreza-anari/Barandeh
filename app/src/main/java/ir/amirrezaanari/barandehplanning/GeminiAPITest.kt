package ir.amirrezaanari.barandehplanning


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ChatScreen(generativeModel: GenerativeModel) {
    var userInput by remember { mutableStateOf(TextFieldValue()) }
    var chatHistory by remember { mutableStateOf(listOf<String>()) }
    var responseText by remember { mutableStateOf("Hello! How can I assist you today?") }
    var isLoading by remember { mutableStateOf(false) }

    // Function to handle the message sending logic
    fun sendMessage() {
        if (userInput.text.isNotEmpty()) {
            val newUserMessage = "User: ${userInput.text}"
            chatHistory = chatHistory + newUserMessage
            userInput = TextFieldValue("") // Clear input field

            isLoading = true

            // Run chat message generation on a background thread
            // We use coroutine to ensure the UI doesn't freeze
            kotlinx.coroutines.GlobalScope.launch {
                val chatInput = content {
                    text(chatHistory.joinToString("\n"))
                }

                try {
                    val response = generativeModel.generateContent(chatInput)
                    withContext(Dispatchers.Main) {
                        chatHistory = chatHistory + "Bot: ${response.text}"
                        responseText = response.text.toString()
                        isLoading = false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        responseText = "Error: ${e.message}"
                        isLoading = false
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display chat history
        Column(modifier = Modifier.weight(1f)) {
            chatHistory.forEach { message ->
                Text(text = message, modifier = Modifier.padding(vertical = 4.dp))
            }

            if (isLoading) {
                Text(text = "Bot is typing...", color = Color.Gray)
            } else {
                Text(text = responseText)
            }
        }

        // User Input Section
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )

            Button(onClick = { sendMessage() }) {
                Text("Send")
            }
        }
    }
}
