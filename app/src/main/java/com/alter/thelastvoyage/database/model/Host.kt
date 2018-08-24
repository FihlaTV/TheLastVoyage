package com.alter.thelastvoyage.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ra", "dec"])])
data class Host(@PrimaryKey(autoGenerate = true) var id: Long,
                var name: String,
                var ra: Float,
                var dec: Float,
                var distance: Float? = null,
                var mass: Float? = null,
                var radius: Float? = null,
                var density: Float? = null,
                var spectralType: String? = null,
                var surfaceGravity: Float? = null,
                var luminosity: Float? = null,
                var effTemperature: Float? = null,
                var metallicity: Float? = null,
                var age: Float? = null
)