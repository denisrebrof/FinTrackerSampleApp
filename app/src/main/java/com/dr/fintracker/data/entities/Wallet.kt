package com.dr.fintracker.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName = "wallets")
data class Wallet(
    var title: String,
    var balance: Int = 0
) {
    var isSelected = false
    @PrimaryKey(autoGenerate = true)
    var walletID : Int = 0
}