package ir.amirrezaanari.barandehplanning.tools

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.notes.AddNoteBottomSheet
import ir.amirrezaanari.barandehplanning.notes.NoteItem
import ir.amirrezaanari.barandehplanning.notes.NoteViewModel
import ir.amirrezaanari.barandehplanning.notes.NoteViewModelFactory
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary

@Composable
fun NotesScreen() {

    var showAddBottomSheet by remember { mutableStateOf(false) }

    val viewModel: NoteViewModel =
        viewModel(factory = NoteViewModelFactory(LocalContext.current.applicationContext as Application))


    val notes by remember { viewModel.allNotes }.collectAsState()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddBottomSheet = true },
                containerColor = mainwhite,
                contentColor = primary
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add task by voice",
                    tint = primary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) { paddingValues ->
        if (notes.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.emptynote),
                    contentDescription = "no note",
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    """
                    هیچ یادداشتی نداری!
                    یک دونه بنویس :)
                """.trimIndent(),
                    textAlign = TextAlign.Center
                )

            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(notes) { note ->
                    NoteItem(note, viewModel)
                }
            }
        }
        if (showAddBottomSheet) {
            AddNoteBottomSheet(
                onDismiss = { showAddBottomSheet = false },
                viewModel = viewModel
            )
        }
    }
}