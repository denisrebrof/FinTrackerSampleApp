package com.dr.fintracker.ui.moneyemissions

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteEmissionDialog(
    val onConfirm : DialogInterface.OnClickListener,
    val onCancel : DialogInterface.OnClickListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(com.dr.fintracker.R.string.delete_emission_dialog_title))
            .setPositiveButton(R.string.ok,onConfirm)
            .setNegativeButton(R.string.cancel,onCancel)
            .create()
    }

}