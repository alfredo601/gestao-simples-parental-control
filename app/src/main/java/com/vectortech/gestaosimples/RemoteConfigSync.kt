package com.vectortech.gestaosimples

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteConfigSync(private val prefs: PrefsManager) {
    fun start() {
        val deviceId = prefs.getDeviceId()
        if (deviceId.isEmpty()) return
        val ref = FirebaseDatabase.getInstance().getReference("devices").child(deviceId)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val password = snapshot.child("childPassword").getValue(String::class.java)
                val apps = snapshot.child("blockedApps").children.mapNotNull { it.getValue(String::class.java) }
                if (password != null) prefs.setChildPassword(password)
                if (apps.isNotEmpty()) prefs.setBlockedApps(apps)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

