package com.duohome.utilities.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "utilities",
    foreignKeys = [
        ForeignKey(
            entity = Home::class,
            parentColumns = ["id"],
            childColumns = ["homeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TemplateUtility::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("homeId"), Index("templateId")]
)
data class Utility(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val homeId: Long,
    val templateId: Long
)

data class UtilityWithTemplate(
    @Embedded val utility: Utility,
    @Relation(parentColumn = "templateId", entityColumn = "id")
    val template: TemplateUtility
)