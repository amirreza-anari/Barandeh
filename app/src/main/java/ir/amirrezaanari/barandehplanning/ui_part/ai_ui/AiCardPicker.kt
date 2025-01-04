package ir.amirrezaanari.barandehplanning.ui_part.ai_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun AiPicker(
    items: List<AiModel>,
    state: PickerState = rememberPickerState(),
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item.name }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                val aiModel = getItem(index)
                AiCardPicker(aiName = aiModel.name, aiIcon = aiModel.icon, cardColor = aiModel.color)  // استفاده از AiCardPicker
            }
        }
    }
}

@Composable
fun AiCardPicker(aiName: String, aiIcon: Int, cardColor: Color) {
    Card(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = aiIcon),
                contentDescription = "Ai icon",
                modifier = Modifier.size(30.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = aiName,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class AiModel(val name: String, val icon: Int, val id: String, val color: Color)

val AiModels = listOf(
    AiModel(
        name = "ChatGPT 4o",
        icon = R.drawable.chatgpt,
        id = "gpt-4o",
        color = Color(0XFF74AA9C)
    ),
    AiModel(
        name = "Gemini 2.0",
        icon = R.drawable.gemini,
        id = "gemini-2.0-flash-exp",
        color = Color(0XFF1e1f20)
    ),
    AiModel(
        name = "Grok 2",
        icon = R.drawable.grok,
        id = "grok-2",
        color = Color(0xFF000000)
    ),
    AiModel(
        name = "Gemini 1.5",
        icon = R.drawable.gemini,
        id = "gemini-1.5-pro-latest",
        color = Color(0XFF1e1f20)
    ),
    AiModel(
        name = "Claude 3.5",
        icon = R.drawable.claude,
        id = "claude-3.5-haiku",
        color = Color(0XFFcc9b7a)
    ),
    AiModel(
        name = "Llama 3.1",
        icon = R.drawable.llama,
        id = "llama-3.1-sonar-large-128k-online",
        color = Color(0XFF1f1f1f)
    ),
    AiModel(
        name = "Mistral",
        icon = R.drawable.mistral,
        id = "mistral-large",
        color = Color(0xFF000000)
    ),
)


private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }