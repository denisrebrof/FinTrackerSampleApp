package com.dr.fintracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fin_targets")
data class FinTarget (
    var title : String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    @ColumnInfo(name = "cost")
    var cost = 0
    @ColumnInfo(name = "position")
    var position = 0
}

class FinTargetGroup(
    var name : String
) {

}

