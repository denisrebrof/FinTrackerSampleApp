package com.dr.fintracker.data.db.moneyemission

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet

@Dao
interface WalletDAO {

    @Insert
    suspend fun insert(wallet: Wallet)

    @Insert
    suspend fun insert(emission: MoneyEmission)

    @Update
    suspend fun update(wallet: Wallet)
    @Update
    suspend fun update(emission: MoneyEmission)

    @Delete
    suspend fun delete(wallet: Wallet)
    @Delete
    suspend fun delete(emission: MoneyEmission)

    @Query("SELECT * FROM wallets")
    fun getAllWallets() : LiveData<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE isSelected = 1")
    fun getSelectedWallet() : LiveData<Wallet>

    @Query("SELECT * FROM emissions WHERE walletId = :walletId")
    fun getEmissionsLiveData(walletId : Int) : LiveData<List<MoneyEmission>>

    @Query("SELECT * FROM emissions WHERE walletId = :walletId")
    suspend fun getEmissions(walletId : Int) : List<MoneyEmission>

    @Transaction
    suspend fun setSelectedWallet(wallet: Wallet?) {
        if (wallet != null) {
            setSelectedWalletById(wallet.walletID)
        }
    }

    @Query("UPDATE wallets SET isSelected = CASE WHEN walletID = :walletId THEN 1 ELSE 0 END")
    suspend fun setSelectedWalletById(walletId: Int)
}