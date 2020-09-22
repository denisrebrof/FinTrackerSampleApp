package com.dr.fintracker.viewmodel

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dr.fintracker.data.entities.Wallet


class WalletAdapter(
    var _context: Context,
    var resId: Int,
    var wallets: List<Wallet>
) : ArrayAdapter<Wallet>(_context, resId, wallets) {

    override fun getCount(): Int {
        return wallets.count()
    }

    override fun getItem(position: Int): Wallet? {
        return wallets[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = TextView(context)
        label.setTextColor(Color.BLACK);
        label.text = wallets[position].title
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK);
        label.text = wallets[position].title
        return label
    }
}