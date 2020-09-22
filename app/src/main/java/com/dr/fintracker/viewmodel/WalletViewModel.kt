package com.dr.fintracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dr.fintracker.data.db.moneyemission.WalletRepository
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WalletViewModel @ViewModelInject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {

    fun getEmissions(walletId : Int) = walletRepository.getEmissions(walletId)

    fun getSelectedWallet() = walletRepository.getSelectedWallet()

    fun getSelectedWalletEmissions() : LiveData<List<MoneyEmission>>{
        val selectedWallet = walletRepository.getSelectedWallet()
        return Transformations.switchMap(selectedWallet){
            selectedWallet.value?.walletID?.let { it1 -> getEmissions(it1) }
        }
    }

    fun getWallets() = walletRepository.getWallets()

    fun insertWallet(wallet: Wallet) =
        GlobalScope.launch {
            walletRepository.insert(wallet)
        }

    fun updateWallet(wallet: Wallet) =
        GlobalScope.launch {
            walletRepository.update(wallet)
        }

    fun deleteWallet(wallet: Wallet) =
        GlobalScope.launch {
            walletRepository.delete(wallet)
        }

    fun insertEmission(emission: MoneyEmission) =
        GlobalScope.launch {
            walletRepository.insert(emission)
        }

    fun updateEmission(emission: MoneyEmission) =
        GlobalScope.launch {
            walletRepository.update(emission)
        }

    fun deleteEmission(emission: MoneyEmission) =
        GlobalScope.launch {
            walletRepository.delete(emission)
        }

    fun setSelectedWallet(wallet: Wallet?) {
        GlobalScope.launch {
            walletRepository.setSelectedWallet(wallet)
        }
    }
}