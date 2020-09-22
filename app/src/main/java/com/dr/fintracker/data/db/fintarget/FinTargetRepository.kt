package com.dr.fintracker.data.db.fintarget

import com.dr.fintracker.data.db.FinTrackerDB
import com.dr.fintracker.data.entities.FinTarget
import javax.inject.Inject

class FinTargetRepository @Inject constructor(
    private val database : FinTrackerDB
) {
    suspend fun insert(target: FinTarget) = database.getFinTargetDAO().insert(target)
    suspend fun update(target: FinTarget) = database.getFinTargetDAO().update(target)
    suspend fun delete(target: FinTarget) = database.getFinTargetDAO().delete(target)
    fun getFinTargets() = database.getFinTargetDAO().getAllTargets()
}