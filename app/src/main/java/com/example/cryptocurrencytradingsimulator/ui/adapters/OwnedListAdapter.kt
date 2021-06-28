package com.example.cryptocurrencytradingsimulator.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.*
import com.example.cryptocurrencytradingsimulator.utils.MyFormatter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OwnedListAdapter internal constructor(private val listener: OwnedItemClickListener, private val prices: Map<String, Price>) :
    ListAdapter<Owned, OwnedListAdapter.OwnedListViewHolder>(OwnedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnedListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.owned_crypto_list_item, parent, false)
        return OwnedListViewHolder(view, prices)
    }

    override fun onBindViewHolder(holder: OwnedListViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener{listener.chooseCrypto(item)}
        holder.bind(item)
    }

    class OwnedListViewHolder constructor(itemView: View, private val prices: Map<String, Price>) :
        RecyclerView.ViewHolder(itemView) {

        val amount = itemView.findViewById<TextView>(R.id.ownedItemAmount)
        val name = itemView.findViewById<TextView>(R.id.ownedItemName)
        val worth = itemView.findViewById<TextView>(R.id.ownedItemPrice)

        fun bind(currentOwnedListItem: Owned) {
            amount.text = MyFormatter.double(currentOwnedListItem.amount)
            name.text = currentOwnedListItem.cryptoName
            worth.text = MyFormatter.currency(currentOwnedListItem.amount * prices[currentOwnedListItem.cryptoId]!!.usd)
        }
    }
}

interface OwnedItemClickListener {
    fun chooseCrypto(cryptoListItem: Owned)
}


private class OwnedDiffCallback : DiffUtil.ItemCallback<Owned>(){
    override fun areItemsTheSame(oldItem: Owned, newItem: Owned): Boolean {
        return oldItem.cryptoId == newItem.cryptoId && oldItem.amount == newItem.amount
    }

    override fun areContentsTheSame(oldItem: Owned, newItem: Owned): Boolean {
        return oldItem == newItem
    }
}


