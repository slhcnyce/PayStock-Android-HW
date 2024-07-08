package com.scy.paystock.views.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.companents.ErrorDialog
import com.scy.paystock.views.login.ui.theme.PayStockTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesView(navController: NavHostController , viewModel: NotesViewModel = NotesViewModel()) {
    val noteList by viewModel.response.collectAsState()
val showDialog by viewModel.showDialog.collectAsState()
val showLoading by viewModel.showLoading.collectAsState()
val errorMessage by viewModel.message.collectAsState()

val newNoteText = remember { mutableStateOf("") }

Scaffold(
topBar = {
    CreateTopAppBar(
        navController = navController,
        title = "Stok Sorgu"
    )
}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppBarHeight)
    ) {
        // Top section for adding new note
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = newNoteText.value,
                onValueChange = { newNoteText.value = it },
                label = { Text("Yeni Not Ekle") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Save the new note
                    if (newNoteText.value.isNotBlank()) {
                        viewModel.createNoteDetail(newNoteText.value)
                        newNoteText.value = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Kaydet")
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (showLoading) {
                // Show loading indicator when loading is true
                CircularProgressIndicator()
            } else {
                // Show note list when loading is false
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    noteList?.let { noteList ->
                        itemsIndexed(
                            items = noteList,
                            key = { _, item ->
                                item.id // Assuming NoteDetailDto has an id field
                            }
                        ) { index, item ->
                            val state = rememberDismissState(
                                confirmValueChange = {
                                    if (it == DismissValue.DismissedToStart) {
                                        viewModel.deleteNoteDetail(item.id)
                                    }
                                    true
                                }
                            )

                            SwipeToDismiss(
                                state = state,
                                background = {
                                    val color = when (state.dismissDirection) {
                                        DismissDirection.StartToEnd -> Color.Transparent
                                        DismissDirection.EndToStart -> Color.Red
                                        null -> Color.Transparent
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = color)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White,
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                        )
                                    }
                                },
                                dismissContent = {
                                    NoteListItem(item)
                                },
                                directions = setOf(DismissDirection.EndToStart)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    } ?: run {

                    }
                }
            }
            ErrorDialog(
                showDialog = showDialog,
                onDismiss = { viewModel.dismissDialog() },
                message = errorMessage
            )
        }
    }
}
}
@Preview(showBackground = true)
@Composable
fun NotesViewPreview() {
    PayStockTheme {
        NotesView( navController = rememberNavController())
    }
}
 


@Composable
fun NoteListItem(note: NoteDetailDto) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(note.note)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopAppBar(navController: NavHostController,
                    title: String) {
    TopAppBar(
        title = { Text(
            text = title,
            modifier = Modifier.fillMaxWidth(), // Stretch content horizontally
            textAlign = TextAlign.Center // Center the text
        )
                },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary // Set background color
        )
    )
}

private val AppBarHeight = 56.dp // Adjust this value based on your app's top app bar height
