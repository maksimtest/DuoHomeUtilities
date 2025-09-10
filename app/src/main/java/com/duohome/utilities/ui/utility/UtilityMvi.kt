package com.duohome.utilities.ui.utility

import com.duohome.utilities.entity.TemplateUtility
import com.duohome.utilities.entity.UtilityWithTemplate

sealed interface UtilityIntent {
    data class Load(val homeId: Long) : UtilityIntent
    data class EditOpen(val utilityId: Long?) : UtilityIntent
    data class NameChanged(val value: String) : UtilityIntent
    data class TemplateChanged(val templateId: Long) : UtilityIntent
    data object Save : UtilityIntent
    data object CloseEditor : UtilityIntent
}

data class UtilityState(
    val homeId: Long = 0,
    val utilities: List<UtilityWithTemplate> = emptyList(),
    val templates: List<TemplateUtility> = emptyList(),
    val editorVisible: Boolean = false,
    val editingId: Long? = null,
    val nameInput: String = "",
    val selectedTemplateId: Long? = null,
    val isSaving: Boolean = false,
    val error: String? = null
)
