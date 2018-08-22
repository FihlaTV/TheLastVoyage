package com.alter.thelastvoyage.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Host::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("hostId"),
        onDelete = ForeignKey.SET_NULL)],
        indices = [Index(value = ["ra", "dec"])])
data class Planet(@PrimaryKey(autoGenerate = true) var id: Long,
                  var hostId: Long,
                  var name: String, 
                  val probability: Float?,
                  val ra: Float, 
                  val dec: Float, 
                  val distance: Float?,
                  val mass: Float?,
                  val radius: Float?,
                  val density: Float?,
                  val orbitPeriod: Float?,
                  val orbitAU: Float?,
                  val eccentricity: Float?,
                  val inclination: Float?,
                  val effTemperature: Float?,
                  val equTemperature: Float?,
                  val insolationFlux: Float?,
                  val numStars: Int = 0
)