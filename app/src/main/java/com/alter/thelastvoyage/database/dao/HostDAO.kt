package com.alter.thelastvoyage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.alter.thelastvoyage.database.model.Host

@Dao
interface HostDAO {

    @Insert(onConflict = REPLACE)
    fun insert(vararg hosts: Host)

    @Update
    fun update(vararg hosts: Host)

    @Delete
    fun delete(vararg hosts: Host)

    @Query("SELECT * FROM host")
    fun getAll(): LiveData<List<Host>>

    @Query("SELECT * FROM host WHERE id = :id")
    fun get(id: Long): LiveData<Host>
}