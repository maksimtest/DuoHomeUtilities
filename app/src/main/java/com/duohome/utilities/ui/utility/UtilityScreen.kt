package com.duohome.utilities.ui.utility


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.duohome.utilities.entity.TemplateUtility
import com.duohome.utilities.entity.UtilityWithTemplate

@Composable
fun UtilityScreen(
    homeId: Long,
    vm: UtilityViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(homeId) { vm.dispatch(UtilityIntent.Load(homeId)) }
    val state by vm.state.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Utilities", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            OutlinedButton(onClick = onBack) { Text("Back") }
            Button(onClick = { vm.dispatch(UtilityIntent.EditOpen(null)) }) { Text("Add") }
        }
        Spacer(Modifier.height(8.dp))
        UtilityList(state.utilities, onEdit = { id -> vm.dispatch(UtilityIntent.EditOpen(id)) })
    }

    if (state.editorVisible) {
        UtilityEditorDialog(
            name = state.nameInput,
            selectedTemplateId = state.selectedTemplateId,
            templates = state.templates,
            isSaving = state.isSaving,
            onNameChange = { vm.dispatch(UtilityIntent.NameChanged(it)) },
            onTemplateChange = { vm.dispatch(UtilityIntent.TemplateChanged(it)) },
            onDismiss = { vm.dispatch(UtilityIntent.CloseEditor) },
            onSave = { vm.dispatch(UtilityIntent.Save) }
        )
    }
}

@Composable
private fun UtilityList(
    items: List<UtilityWithTemplate>,
    onEdit: (Long) -> Unit
) {
    LazyColumn {
        items(items) { row ->
            ElevatedCard(
                onClick = { onEdit(row.utility.id) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(row.utility.name, style = MaterialTheme.typography.titleMedium)
                    Text("Template: ${row.template.name}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UtilityEditorDialog(
    name: String,
    selectedTemplateId: Long?,
    templates: List<TemplateUtility>,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onTemplateChange: (Long) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentName = templates.firstOrNull { it.id == selectedTemplateId }?.name ?: "Select…"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Utility") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    singleLine = true
                )
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = currentName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Template") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        templates.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.name) },
                                onClick = {
                                    onTemplateChange(t.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onSave, enabled = !isSaving) { Text(if (isSaving) "Saving..." else "Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
