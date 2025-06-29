package ir.amirrezaanari.barandehplanning.ai.gemini


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseProgressIndicator
import dev.jeziellago.compose.markdowntext.MarkdownText
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.red
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import kotlinx.coroutines.launch

@Composable
fun ChatRoute(
    navController: NavHostController,
    prompt: String,
    stats: String,
    chatViewModel: ChatViewModel = viewModel()
) {
    val chatUiState by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = prompt) {
        chatViewModel.setSystemPrompt(prompt)
    }

    LaunchedEffect(key1 = stats) {
        if (stats.isNotBlank()) {
            val initialMessage = "این برنامه من هست و این رو به خاطر داشته باش که دربارش ازت سوال میپرسم \n $stats"
            chatViewModel.sendMessage(initialMessage)
        }
    }
    Scaffold(
        bottomBar = {
            MessageInput(
                onSendMessage = { inputText ->
                    chatViewModel.sendMessage(inputText)
                },
                resetScroll = {
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.padding(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = secondary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Home Icon",
                            tint = mainwhite,
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { navController.popBackStack() }
                        )
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("هوشیار", fontSize = 23.sp)
                        }
                    }
                }
            }
            // Messages List
            ChatList(chatUiState.messages, listState)
        }
    }
}

@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 4.dp),
        reverseLayout = true,
        state = listState
    ) {
        items(chatMessages.reversed()) { message ->
            ChatBubbleItem(message)
        }
    }
}

@Composable
fun ChatBubbleItem(
    chatMessage: ChatMessage
) {
    val isModelMessage = chatMessage.participant == Participant.MODEL ||
            chatMessage.participant == Participant.ERROR

    val backgroundColor = when (chatMessage.participant) {
        Participant.MODEL -> secondary
        Participant.USER -> red
        Participant.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isModelMessage) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val senderName = when (chatMessage.participant) {
        Participant.MODEL -> "هوشیار"
        Participant.USER -> "شما"
        Participant.ERROR -> "خطا"
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = senderName,
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row {
            BoxWithConstraints {
                Card(
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = bubbleShape,
                    modifier = Modifier.widthIn(0.dp, maxWidth * 0.85f)
                ) {
                    MarkdownText(
                        markdown = chatMessage.text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            if (chatMessage.isPending) {
                BallPulseProgressIndicator(
                    color = red,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun MessageInput(
    onSendMessage: (String) -> Unit,
    resetScroll: () -> Unit = {}
) {
    var userMessage by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                if (userMessage.isNotBlank()) {
                    onSendMessage(userMessage)
                    userMessage = ""
                    resetScroll()
                }
                focusManager.clearFocus()
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(0.15f)
        ) {
            Image(
                alignment = Alignment.Center,
                contentDescription = "send message",
                painter = painterResource(R.drawable.send),
            )
//            Icon(
//                Icons.Default.Send,
//                contentDescription = "send",
//                modifier = Modifier
//            )
        }
        OutlinedTextField(
            value = userMessage,
            label = { Text("پیامتو اینجا بنویس!") },
            placeholder = { Text("چجوری برنامه\u200Cم رو بهتر کنم؟") },
            onValueChange = { userMessage = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,

                ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
                .heightIn(max = 100.dp),

            maxLines = 3,
            singleLine = false,

            shape = RoundedCornerShape(25),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedPlaceholderColor = mainwhite,
                unfocusedTextColor = mainwhite,
                unfocusedPrefixColor = mainwhite,
                unfocusedSuffixColor = mainwhite,
                unfocusedLeadingIconColor = mainwhite,
                unfocusedTrailingIconColor = mainwhite,
                unfocusedSupportingTextColor = mainwhite,
                focusedIndicatorColor = mainwhite,
                focusedTextColor = mainwhite,
                focusedLabelColor = mainwhite,

                )
        )
    }
//    ElevatedCard(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//
//    }
}

//@Composable
//fun SendPlan(
//    OnClick: () -> Unit
//){
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.076f),
//            shape = RoundedCornerShape(25),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = red,
//                contentColor = mainwhite
//            ),
//            onClick = OnClick
//        ) {
//            Text(
//                text = "بزن تا شروع کنیم!",
//                fontSize = 18.sp,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}