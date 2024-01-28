package com.purpleratchet.nttesttask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DealsAdapter(var deals: List<Server.Deal>) : RecyclerView.Adapter<DealsAdapter.DealViewHolder>() {
    class DealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Привязка элементов UI
        val timeStamp: TextView = view.findViewById(R.id.deal_time_stamp)
        val instrumentName: TextView = view.findViewById(R.id.deal_instrument_name)
        val price: TextView = view.findViewById(R.id.deal_price)
        val amount: TextView = view.findViewById(R.id.deal_volume)
        val side: TextView = view.findViewById(R.id.deal_operation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deal, parent, false)
        return DealViewHolder(view)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        holder.timeStamp.text = deal.timeStamp.toString()
        holder.instrumentName.text = deal.instrumentName
        holder.price.text = String.format("%.2f", deal.price)
        holder.amount.text = deal.amount.roundToInt().toString()
        holder.side.text = deal.side.toString()
        // Изменение цвета в зависимости от типа сделки
        val colorResId = if (deal.side == Server.Deal.Side.BUY) R.color.colorBuy else R.color.colorSell
        holder.price.setTextColor(ContextCompat.getColor(holder.itemView.context, colorResId))
    }

    override fun getItemCount() = deals.size

    // Метод для обновления данных
    fun updateDeals(newDeals: List<Server.Deal>) {
        deals = newDeals
        notifyDataSetChanged()
    }
}