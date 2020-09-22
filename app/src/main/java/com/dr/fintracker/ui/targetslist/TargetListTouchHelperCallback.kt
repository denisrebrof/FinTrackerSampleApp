package com.dr.fintracker.ui.targetslist

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TargetListTouchHelperCallback(
    private val adapter: TargetsListAdapter?,
    val fragmentManager: FragmentManager?
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT
) {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter?.onItemMove(from, to)
//        adapter?.updateListPositions()
        return true
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        fragmentManager?.let {
            val transaction = it.beginTransaction()
            val newFragment = DeleteTargetDialog(
                DialogInterface.OnClickListener { _, _ ->
                    adapter?.onItemSwiped(viewHolder.adapterPosition)
                },
                DialogInterface.OnClickListener { _, _ ->
                    adapter?.notifyDataSetChanged()
                }
            )
            newFragment.show(transaction, "dialog")
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
//                viewHolder?.itemView?.elevation = R.dimen.fin_target_list_view_selected_elevation.toFloat()
        viewHolder?.itemView?.alpha = 0.8f
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
//                viewHolder.itemView.elevation = R.dimen.fin_target_list_view_elevation.toFloat()
        viewHolder.itemView.alpha = 1f
        adapter?.notifyDataSetChanged()
    }
}