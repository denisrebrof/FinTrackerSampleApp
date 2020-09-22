package com.dr.fintracker.data.db.moneyemission

import com.dr.fintracker.data.db.FinTrackerDB
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val database : FinTrackerDB
) {
    suspend fun insert(wallet: Wallet) = database.getWalletDAO().insert(wallet)
    suspend fun update(wallet: Wallet) = database.getWalletDAO().update(wallet)
    suspend fun delete(wallet: Wallet){
        val walletDao = database.getWalletDAO()
        val emissions = walletDao.getEmissions(wallet.walletID)
        for(emission in emissions){
            walletDao.delete(emission)
        }
        database.getWalletDAO().delete(wallet)
    }
    suspend fun setSelectedWallet(wallet: Wallet?) {
        database.getWalletDAO().setSelectedWallet(wallet)
    }
    fun getWallets() = database.getWalletDAO().getAllWallets()
    fun getSelectedWallet() = database.getWalletDAO().getSelectedWallet()

    suspend fun insert(emission: MoneyEmission) = database.getWalletDAO().insert(emission)
    suspend fun update(emission: MoneyEmission) = database.getWalletDAO().update(emission)
    suspend fun delete(emission: MoneyEmission) = database.getWalletDAO().delete(emission)
    fun getEmissions(walletId : Int) = database.getWalletDAO().getEmissionsLiveData(walletId)

}