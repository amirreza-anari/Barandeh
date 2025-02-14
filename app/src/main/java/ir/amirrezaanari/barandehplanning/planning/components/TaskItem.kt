package ir.amirrezaanari.barandehplanning.planning.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: TaskEntity,
    onTaskClick: (TaskEntity) -> Unit,
    onCheckChange: (TaskEntity) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    val animatedHeight by animateDpAsState(
        targetValue = if (expanded) 300.dp else 90.dp,
        label = "noteItemHeight"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "noteItemAlpha"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (task.isChecked)
            Color(task.color).copy(alpha = 0.7f)
        else
            Color(task.color),
        animationSpec = tween(durationMillis = 300),
        label = "backgroundAnimation"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (task.isChecked) 1f else 0.75f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "checkScaleAnimation"
    )

    val textColor by animateColorAsState(
        targetValue = if (task.isChecked)
            mainwhite.copy(alpha = 0.7f)
        else
            mainwhite,
        animationSpec = tween(durationMillis = 300),
        label = "textColorAnimation"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {expanded = !expanded},
                onLongClick = {onTaskClick(task)},
            )
            .padding(vertical = 5.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (task.isPlanned) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onCheckChange(task) },
                        colors = CardDefaults.cardColors(
                            containerColor = primary
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = task.isChecked,
                            ) { isChecked ->
                                if (isChecked) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "Checked",
                                        tint = green,
                                        modifier = Modifier
                                            .fillMaxSize(0.9f)
                                            .scale(checkScale)
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = task.title,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = textColor
                )

                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = primary.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = task.startTime.toPersianDigits(),
                        )
                        Text(
                            text = task.endTime.toPersianDigits(),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
            ) {
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
                                text = task.details,
                                modifier = Modifier.alpha(animatedAlpha),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ){
                    IconButton(
                        onClick = { onTaskClick(task) },
                    ) {
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = "note edit"
                        )
                    }
                }
            }

        }
    }
}
