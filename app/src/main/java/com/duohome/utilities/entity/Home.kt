package com.duohome.utilities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homes")
data class Home(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)