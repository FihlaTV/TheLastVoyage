package com.alter.thelastvoyage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alter.thelastvoyage.TheLastVoyage
import com.alter.thelastvoyage.database.dao.HostDAO
import com.alter.thelastvoyage.database.dao.PlanetDAO
import com.alter.thelastvoyage.database.model.Host
import com.alter.thelastvoyage.database.model.Modifier
import com.alter.thelastvoyage.database.model.Planet
import com.alter.thelastvoyage.database.model.Ship


@Database(entities = [Host::class, Planet::class, Modifier::class, Ship::class], version = 1)
abstract class UniverseDatabase : RoomDatabase() {

    abstract fun hostDAO(): HostDAO
    abstract fun planetDAO(): PlanetDAO

    companion object {

        /** Class instance. */
        private lateinit var instance: UniverseDatabase

        @JvmOverloads
        fun init(context: Context = TheLastVoyage.instance, name: String = "universe.db") {
            synchronized(UniverseDatabase::class) {
                instance = Room.databaseBuilder(context.applicationContext,
                        UniverseDatabase::class.java, name)
                        .build()
            }
        }

        @JvmOverloads
        fun getInstance(context: Context = TheLastVoyage.instance): UniverseDatabase {
            if (!::instance.isInitialized) {
                init(context)
            }
            return instance
        }
    }
}