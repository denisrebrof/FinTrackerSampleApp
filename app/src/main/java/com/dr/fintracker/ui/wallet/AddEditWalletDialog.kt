package com.dr.fintracker.ui.wallet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dr.fintracker.R
import kotlinx.android.synthetic.main.add_edit_wallet_dialog.view.*

class AddEditWalletDialog(
    private val onCreateWallet: AddNewWalletDialogListener,
    private val title : String = "",
    private val balance : Int = 0
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireActivity());
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_edit_wallet_dialog, null)
        dialogBuilder.setView(dialogView)

        if(title.isNotEmpty())
            dialogView.add_wallet_title_et.setText(title)
        dialogView.add_wallet_start_balance_et.setText(balance.toString())
        dialogView.confirm_new_wallet_button.setOnClickListener {
            onCreateWallet.onWalletCreated(
                dialogView.add_wallet_title_et.text.toString(),
                dialogView.add_wallet_start_balance_et.text.toString().toInt()
            )
            dismiss()
        }

        dialogView.revert_new_wallet_button.setOnClickListener {
            dismiss()
        }
        return dialogBuilder.create()
    }

    interface AddNewWalletDialogListener {
        fun onWalletCreated(walletTitle: String, walletBalance : Int)
    }
}