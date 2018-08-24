package com.alter.thelastvoyage.repository

import androidx.lifecycle.LiveData
import com.alter.thelastvoyage.database.UniverseDatabase
import com.alter.thelastvoyage.database.model.Host
import com.alter.thelastvoyage.support.util.LogHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


object HostRepository {

    /** Log Tag.  */
    private val TAG = HostRepository::class.java.simpleName

    fun update(host: Host) : Long {
        FirebaseFirestore.getInstance()
                .collection("hosts")
                .document(host.id.toString())
                .set(host, SetOptions.merge())

        return UniverseDatabase.getInstance()
                .hostDAO()
                .insert(host)
    }

    fun updateAll(vararg hosts: Host) {
        val db = FirebaseFirestore.getInstance()
        for (host in hosts) {
            db.collection("hosts")
                    .document(host.id.toString())
                    .set(host, SetOptions.merge())
        }

        return UniverseDatabase.getInstance()
                .hostDAO()
                .insertAll(*hosts)
    }

    fun get(id: Long): LiveData<Host> {
        FirebaseFirestore.getInstance()
                .collection("hosts")
                .document(id.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val host = documentSnapshot.toObject<Host>(Host::class.java)
                    host?.let { UniverseDatabase.getInstance().hostDAO().update(it) }
                }

        return UniverseDatabase.getInstance().hostDAO().get(id)
    }

    fun getAll(): LiveData<List<Host>> {
        FirebaseFirestore.getInstance()
                .collection("hosts")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val hosts = mutableListOf<Host>()
                        for (documentSnapshot in task.result) {
                            val host = documentSnapshot.toObject(Host::class.java)
                            hosts.add(host)
                        }
                        UniverseDatabase.getInstance().hostDAO().updateAll(*hosts.toTypedArray())
                    } else {
                        LogHelper.e(TAG, "Error getting hosts.", task.exception)
                    }
                }

        return UniverseDatabase.getInstance().hostDAO().getAll()
    }

}