package com.dr.fintracker.ui.wallet

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteWalletDialog (
    private val onConfirm : DialogInterface.OnClickListener,
    private val onCancel : DialogInterface.OnClickListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(com.dr.fintracker.R.string.delete_wallet_dialog_title))
            .setPositiveButton(R.string.ok,onConfirm)
            .setNegativeButton(R.string.cancel,onCancel)
            .create()
    }

}