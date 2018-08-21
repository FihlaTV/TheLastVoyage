package com.alter.thelastvoyage.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ra", "dec"])])
data class Host(@PrimaryKey(autoGenerate = true) var id: Long,
                var name: String,
                var ra: Float,
                var dec: Float,
                var distance: Float?,
                var mass: Float?,
                var radius: Float?,
                var density: Float?,
                var spectralType: String?,
                var surfaceGravity: Float?,
                var luminosity: Float?,
                var effTemperature: Float?,
                var metallicity: Float?,
                var age: Float?
)