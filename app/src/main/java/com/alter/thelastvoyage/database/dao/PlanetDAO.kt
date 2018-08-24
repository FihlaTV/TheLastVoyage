package com.alter.thelastvoyage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.alter.thelastvoyage.database.model.Planet

@Dao
interface PlanetDAO {

    @Insert(onConflict = REPLACE)
    fun insert(planet: Planet) : Long

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg planets: Planet)

    @Update
    fun update(planet: Planet)

    @Update
    fun updateAll(vararg planets: Planet)

    @Delete
    fun delete(planet: Planet)

    @Delete
    fun deleteAll(vararg planets: Planet)

    @Query("SELECT * FROM planet WHERE id = :id")
    fun get(id: Long): LiveData<Planet>

    @Query("SELECT * FROM planet")
    fun getAll(): LiveData<List<Planet>>
}