package com.duohome.utilities.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.duohome.utilities.entity.Home

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onOpenUtilities: (Long) -> Unit
) {
    val state by vm.state.collectAsState()

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Homes", style = MaterialTheme.typography.titleLarge)
                Button(onClick = { vm.dispatch(HomeIntent.EditOpen(null)) }) { Text("Add Home") }
            }
            Spacer(Modifier.height(12.dp))
            HomeList(state.homes, onOpenUtilities, onEdit = { vm.dispatch(HomeIntent.EditOpen(it.id)) })
        }
    }

    if (state.editorVisible) {
        HomeEditorDialog(
            name = state.nameInput,
            isSaving = state.isSaving,
            onNameChange = { vm.dispatch(HomeIntent.NameChanged(it)) },
            onDismiss = { vm.dispatch(HomeIntent.CloseEditor) },
            onSave = { vm.dispatch(HomeIntent.Save) }
        )
    }
}

@Composable
private fun HomeList(
    items: List<Home>,
    onOpenUtilities: (Long) -> Unit,
    onEdit: (Home) -> Unit
) {
    LazyColumn {
        items(items) { h ->
            ElevatedCard(
                onClick = { onOpenUtilities(h.id) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(h.name, style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { onEdit(h) }) { Text("Edit") }
                }
            }
        }
    }
}

@Composable
private fun HomeEditorDialog(
    name: String,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Home") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = onSave, enabled = !isSaving) { Text(if (isSaving) "Saving..." else "Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
