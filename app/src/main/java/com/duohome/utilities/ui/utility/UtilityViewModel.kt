package com.duohome.utilities.ui.utility


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duohome.utilities.data.TemplateRepository
import com.duohome.utilities.data.UtilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val repo: UtilityRepository,
    private val templatesRepo: TemplateRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UtilityState())
    val state: StateFlow<UtilityState> = _state.asStateFlow()

    private var utilitiesJob: kotlinx.coroutines.Job? = null
    private var templatesJob: kotlinx.coroutines.Job? = null

    fun dispatch(intent: UtilityIntent) {
        when (intent) {
            is UtilityIntent.Load -> subscribe(intent.homeId)
            is UtilityIntent.EditOpen -> openEditor(intent.utilityId)
            is UtilityIntent.NameChanged -> _state.update { it.copy(nameInput = intent.value) }
            is UtilityIntent.TemplateChanged -> _state.update { it.copy(selectedTemplateId = intent.templateId) }
            UtilityIntent.Save -> save()
            UtilityIntent.CloseEditor -> _state.update { it.copy(editorVisible = false, editingId = null, nameInput = "") }
        }
    }

    private fun subscribe(homeId: Long) {
        _state.update { it.copy(homeId = homeId) }
        utilitiesJob?.cancel()
        templatesJob?.cancel()
        utilitiesJob = viewModelScope.launch {
            repo.observeForHome(homeId).collect { list -> _state.update { it.copy(utilities = list) } }
        }
        templatesJob = viewModelScope.launch {
            templatesRepo.observeTemplates().collect { t -> _state.update { it.copy(templates = t) } }
        }
    }

    private fun openEditor(utilityId: Long?) = viewModelScope.launch {
        if (utilityId == null) {
            _state.update { it.copy(editorVisible = true, editingId = null, nameInput = "", selectedTemplateId = it.templates.firstOrNull()?.id) }
        } else {
            val u = repo.get(utilityId) ?: return@launch
            _state.update { it.copy(editorVisible = true, editingId = utilityId, nameInput = u.name, selectedTemplateId = u.templateId) }
        }
    }

    private fun save() = viewModelScope.launch {
        val s = _state.value
        val name = s.nameInput.trim()
        val templateId = s.selectedTemplateId
        if (name.isBlank() || templateId == null) {
            _state.update { it.copy(error = "Name and template are required") }
            return@launch
        }
        _state.update { it.copy(isSaving = true, error = null) }
        val id = repo.upsert(s.editingId, name, s.homeId, templateId)
        _state.update { it.copy(isSaving = false, editorVisible = false, editingId = id, nameInput = "") }
    }
}
