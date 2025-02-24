package ir.amirrezaanari.barandehplanning.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteItem(
    note: NoteEntity,
    viewModel: NoteViewModel,
) {
    var showEditSheet by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val animatedHeight by animateDpAsState(
        targetValue = if (expanded) 250.dp else 85.dp,
        label = "noteItemHeight"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "noteItemAlpha"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .padding(vertical = 2.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color).copy(alpha = 0.55f),
        ),
        border = BorderStroke(3.dp, Color(note.color))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                IconButton(
                    onClick = { showEditSheet = true }
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "note edit"
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                ) {
                    item {
                        Text(
                            text = note.content,
                            modifier = Modifier.alpha(animatedAlpha),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = note.timestamp,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
    if (showEditSheet) {
        EditNoteBottomSheet(
            note = note,
            viewModel = viewModel,
            onDismiss = { showEditSheet = false }
        )
    }
}