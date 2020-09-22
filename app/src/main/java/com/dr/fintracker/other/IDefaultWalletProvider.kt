package com.dr.fintracker.other

import com.dr.fintracker.data.entities.Wallet

interface IDefaultWalletProvider {
    fun getWallet() : Wallet
}