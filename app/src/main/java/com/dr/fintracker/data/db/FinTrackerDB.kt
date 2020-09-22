package com.dr.fintracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dr.fintracker.data.db.moneyemission.WalletDAO
import com.dr.fintracker.data.db.fintarget.FinTargetDAO
import com.dr.fintracker.data.entities.FinTarget
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet

@Database(entities = [FinTarget::class, MoneyEmission::class, Wallet::class], version = 1)
abstract class FinTrackerDB : RoomDatabase(){
    abstract fun getFinTargetDAO() : FinTargetDAO
    abstract fun getWalletDAO() : WalletDAO
}