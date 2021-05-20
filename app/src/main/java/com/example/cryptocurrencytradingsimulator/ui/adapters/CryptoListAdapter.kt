package com.example.cryptocurrencytradingsimulator.ui.adapters

import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.module.AppGlideModule
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoListItemBinding
import com.example.cryptocurrencytradingsimulator.di.GlideApp
import java.text.NumberFormat
import java.util.*
import kotlin.math.sign

class CryptoListAdapter internal  constructor(private val listener: CryptoItemClickListener):
    ListAdapter<Crypto, CryptoListAdapter.CryptoListViewHolder>(CryptoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoListViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item,parent,false)
        return CryptoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoListViewHolder, position: Int) {
            val item = getItem(position)
            holder.itemView.setOnClickListener { listener.chooseCrypto(item) }
            holder.bind(item)

    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    class CryptoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var imageView = itemView.findViewById<ImageView>(R.id.itCryptoIcon)
        var name = itemView.findViewById<TextView>(R.id.itCryptoName)
        var symbol = itemView.findViewById<TextView>(R.id.itCryptoSymbol)
        var price = itemView.findViewById<TextView>(R.id.itCryptoPrice)
        var priceChange = itemView.findViewById<TextView>(R.id.itCryptoPriceChange)

        fun bind(currentCrypto: Crypto){
            name.text = currentCrypto.name

            symbol.text = currentCrypto.symbol

            GlideApp.with(itemView.context).load(currentCrypto.image).into(imageView)

            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("USD")
            currencyFormat.maximumFractionDigits = 10
            price.text = currencyFormat.format(currentCrypto.current_price)

            val percentFormat = NumberFormat.getPercentInstance()
            percentFormat.maximumFractionDigits = 2
            percentFormat.minimumFractionDigits = 2
            priceChange.text = percentFormat.format(currentCrypto.price_change_percentage_24h_in_currency/100)
            when(sign(currentCrypto.price_change_percentage_24h_in_currency)){
                1.0 -> priceChange.setTextColor(GREEN)
                -1.0 -> priceChange.setTextColor(RED)
            }
        }
    }

}

interface CryptoItemClickListener {
    fun chooseCrypto(crypto: Crypto)
}

private class CryptoDiffCallback : DiffUtil.ItemCallback<Crypto>(){
    override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem.id == newItem.id && oldItem.current_price == newItem.current_price
    }

    override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem == newItem
    }
}
