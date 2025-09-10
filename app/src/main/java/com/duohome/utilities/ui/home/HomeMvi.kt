package com.duohome.utilities.ui.home


import com.duohome.utilities.entity.Home

sealed interface HomeIntent {
    data object Load : HomeIntent
    data class EditOpen(val id: Long?) : HomeIntent
    data class NameChanged(val value: String) : HomeIntent
    data object Save : HomeIntent
    data object CloseEditor : HomeIntent
}

data class HomeState(
    val isLoading: Boolean = false,
    val homes: List<Home> = emptyList(),
    val editorVisible: Boolean = false,
    val editingId: Long? = null,
    val nameInput: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)
