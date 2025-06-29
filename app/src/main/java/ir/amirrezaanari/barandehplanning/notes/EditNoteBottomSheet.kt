package ir.amirrezaanari.barandehplanning.notes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.planning.components.CardColorPicker
import ir.amirrezaanari.barandehplanning.planning.components.CustomTextField
import ir.amirrezaanari.barandehplanning.planning.components.IconAndText
import ir.amirrezaanari.barandehplanning.planning.components.cardColors
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.red
import ir.amirrezaanari.barandehplanning.ui.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteBottomSheet(
    note: NoteEntity,
    viewModel: NoteViewModel,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var color by remember { mutableStateOf(Color(note.color)) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        containerColor = secondary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ویرایش یادداشت",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
            )
            Spacer(Modifier.height(10.dp))
            IconAndText(
                icon = Icons.Rounded.Title,
                text = "عنوان"
            )
            Spacer(Modifier.height(5.dp))
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                label = "عنوان جدید را وارد کنید",
                onValueChange = { title = it },
                singleline = true
            )

            IconAndText(
                icon = Icons.Rounded.TextFields,
                text = "جزئیات"
            )
            Spacer(Modifier.height(5.dp))
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = content,
                label = "متن جدید را وارد کنید",
                onValueChange = { content = it },
                singleline = false,
                maxlines = 5
            )

            IconAndText(
                icon = Icons.Rounded.ColorLens,
                text = "رنگ"
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                items(cardColors) { cardcolor ->
                    CardColorPicker(
                        color = cardcolor.color,
                        onClick = { color = cardcolor.color },
                        isSelected = cardcolor.color == color
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            Toast.makeText(context, "لطفاً عنوان و متن یادداشت را وارد کنید.", Toast.LENGTH_SHORT).show()
                        } else {
                            val updatedNote = note.copy(title = title, content = content, color = color.toArgb())
                            viewModel.updateNote(updatedNote)
                            onDismiss()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                        .padding(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = primary,
                        containerColor = mainwhite
                    )
                ) {
                    Text(
                        text = "ذخیره",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


                Button(
                    onClick = {
                        viewModel.deleteNote(note)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                        .padding(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = mainwhite,
                        containerColor = red
                    )
                ) {
                    Text("حذف")
                }
            }
        }
    }
}
