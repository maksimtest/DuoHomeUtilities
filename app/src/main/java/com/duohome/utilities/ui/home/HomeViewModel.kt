package com.duohome.utilities.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duohome.utilities.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeHomes().collect { list ->
                _state.update { it.copy(isLoading = false, homes = list) }
            }
        }
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> Unit
            is HomeIntent.EditOpen -> openEditor(intent.id)
            is HomeIntent.NameChanged -> _state.update { it.copy(nameInput = intent.value) }
            HomeIntent.Save -> save()
            HomeIntent.CloseEditor -> _state.update { it.copy(editorVisible = false, editingId = null, nameInput = "") }
        }
    }

    private fun openEditor(id: Long?) = viewModelScope.launch {
        if (id == null) {
            _state.update { it.copy(editorVisible = true, editingId = null, nameInput = "") }
        } else {
            val existing = repo.get(id)
            _state.update { it.copy(editorVisible = true, editingId = id, nameInput = existing?.name ?: "") }
        }
    }

    private fun save() = viewModelScope.launch {
        val s = _state.value
        if (s.nameInput.isBlank()) {
            _state.update { it.copy(error = "Name cannot be empty") }
            return@launch
        }
        _state.update { it.copy(isSaving = true, error = null) }
        val id = repo.upsert(s.editingId, s.nameInput.trim())
        _state.update { it.copy(isSaving = false, editorVisible = false, editingId = id, nameInput = "") }
    }
}
