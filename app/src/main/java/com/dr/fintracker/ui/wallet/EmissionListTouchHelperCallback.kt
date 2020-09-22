package com.dr.fintracker.ui.wallet

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dr.fintracker.ui.moneyemissions.DeleteEmissionDialog

class EmissionListTouchHelperCallback(
    private val fragmentManager: FragmentManager?,
    val onDeleteConfirmed: (position: Int) -> Unit,
    val onDeleteAborted: () -> Unit
    ) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        fragmentManager?.let {
            val transaction = it.beginTransaction()
            val newFragment = DeleteEmissionDialog(
                DialogInterface.OnClickListener { _, _ ->
                    onDeleteConfirmed(viewHolder.adapterPosition)
                },
                DialogInterface.OnClickListener { _, _ ->
                    onDeleteAborted()
                }
            )
            newFragment.show(transaction, "dialog")
        }
    }
}