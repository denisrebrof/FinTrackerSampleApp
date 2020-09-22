package com.dr.fintracker.ui.targetslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.FinTarget
import com.dr.fintracker.viewmodel.FinTargetViewModel
import kotlinx.android.synthetic.main.fin_target_list_view.view.*
import java.util.*

class TargetsListAdapter(
    var targets : List<FinTarget>,
    private val viewModel: FinTargetViewModel
) : RecyclerView.Adapter<TargetsListAdapter.FinTargetViewHolder>() {

    var forecastedBalance = 0

    class FinTargetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinTargetViewHolder {
        return FinTargetViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fin_target_list_view, parent, false)
        )
    }

    private fun calculateItemProgress(itemPos : Int) : Float{
        var balance = forecastedBalance
        for(pos in 0 until itemPos)
            balance-=targets[pos].cost
        val progress = balance.toFloat()/targets[itemPos].cost
        return progress.coerceIn(0f,1f)
    }

    fun updateListPositions() {
        for (target in targets) {
            target.position =  targets.indexOf(target)
            viewModel.update(target)
        }
    }

    override fun getItemCount(): Int = targets.count()

    override fun onBindViewHolder(holder: FinTargetViewHolder, position: Int) {
        val view = holder.itemView
        val target = targets[position]
        view.fin_target_list_view_cost_value.text = target.cost.toString()
        view.fin_target_list_view_title.text = target.title

        val itemProgress = calculateItemProgress(position)
        var forecastView = getForecastView(itemProgress,holder.itemView.context)

        forecastView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)

        val frame = view.fin_target_list_view_status_frame
        frame.removeAllViews()
        frame.addView(forecastView)
    }

    private fun getForecastView(progress : Float, context : Context) : View{
        when(progress){
            1f -> {
                val completedImage = ImageView(context)
                completedImage.setImageResource(R.drawable.ic_fin_target_completed_24)
                return completedImage
            }
            0f -> {
                val notStartedImage = ImageView(context)
                notStartedImage.setImageResource(R.drawable.ic_fin_target_pending_24)
                return notStartedImage
            }
            else -> {
                val color = ContextCompat.getColor(context, R.color.colorAccent)

                val progressText = TextView(context)
                progressText.text = "${(progress*100).toInt()}%"
                progressText.setTextColor(color)

                return progressText
            }
        }
    }

    fun onItemMove(fromPos: Int, toPos : Int){
        if (fromPos < toPos) {
            for (i in fromPos until toPos) {
                Collections.swap(targets, i, i + 1)
            }
        } else {
            for (i in fromPos downTo toPos + 1) {
                Collections.swap(targets, i, i - 1)
            }
        }
        notifyItemMoved(fromPos, toPos)
    }

    fun onItemSwiped(position: Int){
        viewModel.delete(targets[position])
    }
}