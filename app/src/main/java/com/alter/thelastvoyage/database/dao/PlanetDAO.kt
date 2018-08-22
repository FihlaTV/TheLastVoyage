package com.alter.thelastvoyage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.alter.thelastvoyage.database.model.Planet

@Dao
interface PlanetDAO {

    @Insert(onConflict = REPLACE)
    fun insert(vararg planets: Planet)

    @Update
    fun update(vararg planets: Planet)

    @Delete
    fun delete(vararg planets: Planet)

    @Query("SELECT * FROM planet")
    fun getAll(): LiveData<List<Planet>>

    @Query("SELECT * FROM planet WHERE id = :id")
    fun get(id: Long): LiveData<Planet>

    @Query("SELECT * FROM host, planet WHERE id = :id")
    fun getHost(id: Long): LiveData<Planet>
}