package com.dr.fintracker.data.db.fintarget

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dr.fintracker.data.entities.FinTarget

@Dao
interface FinTargetDAO  {
    @Insert
    suspend fun insert(target: FinTarget)
    @Update
    suspend fun update(target: FinTarget)
    @Delete
    suspend fun delete(target: FinTarget)

    @Query("SELECT * FROM fin_targets ORDER BY position")
    fun getAllTargets() : LiveData<List<FinTarget>>

    @Query("SELECT COUNT(*) FROM fin_targets")
    fun getTargetsCount() : LiveData<Int>

}