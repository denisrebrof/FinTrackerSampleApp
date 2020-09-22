package com.dr.fintracker.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.EmissionDirection
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.viewmodel.WalletViewModel
import kotlinx.android.synthetic.main.money_emission_item.view.*
import java.text.DateFormat
import java.util.*

class MoneyEmissionsAdapter(
    var emissions: List<MoneyEmission>,
    private var onAddNewItemClicked : View.OnClickListener,
    private val viewModel: WalletViewModel
) : RecyclerView.Adapter<MoneyEmissionsAdapter.EmissionHolder>() {

    class EmissionHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmissionHolder {
        val viewID = when(viewType){
            ViewType.AddNewItem.value -> R.layout.add_new_money_emission_item
            else -> R.layout.money_emission_item
        }
        val itemView = LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return EmissionHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmissionHolder, position: Int) {
        val view = holder.itemView
        when(position){
            in 0 until emissions.count() -> bindEmissionListElementView(view, emissions[position])
            else -> bindAddNewEmissionView(view)
        }
    }

    private fun bindAddNewEmissionView(view : View){
        onAddNewItemClicked.let { view.setOnClickListener(onAddNewItemClicked) }
    }

    private fun bindEmissionListElementView(view : View, emission : MoneyEmission){
        val emissionData = emission.emissionData
        val startDateFormatted = DateFormat.getDateInstance(DateFormat.SHORT).format(emissionData.startDate)
        view.money_emission_item_title.text = emission.title
        view.money_emission_item_value.text = emissionData.value.toString()
        view.money_emission_item_start_value.text = startDateFormatted
        view.money_emission_item_period_value.text = emission.repeatRule.name

        val imageID = when(emissionData.direction){
            EmissionDirection.Add -> R.drawable.ic_arrow_upward_24
            EmissionDirection.Spend -> R.drawable.ic_arrow_downward_24
        }
        view.money_emission_item_direction_ic.setImageResource(imageID)
    }

    override fun getItemCount(): Int = emissions.count() + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < emissions.count()) ViewType.Item.value else ViewType.AddNewItem.value
    }

    fun onItemSwiped(adapterPosition: Int) {
        viewModel.deleteEmission(emissions[adapterPosition])
    }

    enum class ViewType(val value: Int){
        Item(0),
        AddNewItem(1)
    }
}