package com.alter.thelastvoyage.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Ship(@PrimaryKey(autoGenerate = true) var id: Long,
                var name: String
)