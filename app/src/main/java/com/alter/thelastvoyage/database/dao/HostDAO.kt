package com.alter.thelastvoyage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.alter.thelastvoyage.database.model.Host

@Dao
interface HostDAO {

    @Insert(onConflict = REPLACE)
    fun insert(host: Host) : Long

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg hosts: Host)

    @Update
    fun update(host: Host)

    @Update
    fun updateAll(vararg hosts: Host)

    @Delete
    fun delete(host: Host)

    @Delete
    fun deleteAll(vararg hosts: Host)

    @Query("SELECT * FROM host WHERE id = :id")
    fun get(id: Long): LiveData<Host>

    @Query("SELECT * FROM host")
    fun getAll(): LiveData<List<Host>>
}