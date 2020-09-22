package com.dr.fintracker.other

import com.dr.fintracker.data.entities.Wallet

class DefaultWalletProvider : IDefaultWalletProvider{
    override fun getWallet(): Wallet {
        val defWallet = Wallet("Default Wallet", 0)
        defWallet.isSelected = true
        return defWallet
    }
}