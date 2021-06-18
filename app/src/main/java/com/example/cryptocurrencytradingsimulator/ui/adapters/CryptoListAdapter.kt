package com.example.cryptocurrencytradingsimulator.ui.adapters

import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.api.ApiRepository
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.Favorite
import com.example.cryptocurrencytradingsimulator.di.GlideApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.Character.toLowerCase
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.sign

class CryptoListAdapter internal constructor(private val listener: CryptoItemClickListener, private val favoriteListener: FavoriteItemClickListener):
    ListAdapter<Crypto, CryptoListAdapter.CryptoListViewHolder>(CryptoDiffCallback()) {

    private var unfilteredList : List<Crypto> = arrayListOf()

    fun modifyList(list: List<Crypto>){
        unfilteredList = list
        submitList(list)
    }


    fun filter(query: CharSequence?) {
        val list = mutableListOf<Crypto>()
        // perform the data filtering
        if(!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.symbol!!.toLowerCase(Locale.getDefault()).contains(query.toString().toLowerCase(Locale.getDefault())) ||
                        it.name!!.toLowerCase(Locale.getDefault()).contains(query.toString().toLowerCase(Locale.getDefault())) })
        } else {
            list.addAll(unfilteredList)
        }

        submitList(list)
    }

    fun favorites(){
        val list = mutableListOf<Crypto>()
        list.addAll(unfilteredList.filter{it -> it.favorite == true})
        modifyList(list)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoListViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item,parent,false)
        return CryptoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoListViewHolder, position: Int) {
            val item = getItem(position)
            holder.itemView.setOnClickListener { listener.chooseCrypto(item) }
            holder.checkBox.setOnClickListener{ favoriteListener.addOrDeleteFavorite(item, holder.checkBox.isChecked) }
            holder.bind(item)

    }

    class CryptoListViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){

        var imageView = itemView.findViewById<ImageView>(R.id.itCryptoIcon)
        var name = itemView.findViewById<TextView>(R.id.itCryptoName)
        var symbol = itemView.findViewById<TextView>(R.id.itCryptoSymbol)
        var price = itemView.findViewById<TextView>(R.id.itCryptoPrice)
        var priceChange = itemView.findViewById<TextView>(R.id.itCryptoPriceChange)
        var checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

        fun bind(currentCryptoListItem: Crypto){
            name.text = currentCryptoListItem.name

            symbol.text = currentCryptoListItem.symbol

            checkBox.isChecked = currentCryptoListItem.favorite!!


            GlideApp.with(itemView.context).load(currentCryptoListItem.image).into(imageView)

            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("USD")
            currencyFormat.maximumFractionDigits = 10
            price.text = currencyFormat.format(currentCryptoListItem.current_price)

            val percentFormat = NumberFormat.getPercentInstance()
            percentFormat.maximumFractionDigits = 2
            percentFormat.minimumFractionDigits = 2
            if(currentCryptoListItem.price_change_percentage_24h_in_currency != null) {
                priceChange.text =
                    percentFormat.format(currentCryptoListItem.price_change_percentage_24h_in_currency / 100)
                when (sign(currentCryptoListItem.price_change_percentage_24h_in_currency)) {
                    1.0 -> priceChange.setTextColor(GREEN)
                    -1.0 -> priceChange.setTextColor(RED)
                }
            }
        }
    }


}

interface CryptoItemClickListener {
    fun chooseCrypto(cryptoListItem: Crypto)
}

private class CryptoDiffCallback : DiffUtil.ItemCallback<Crypto>(){
    override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem.id == newItem.id && oldItem.current_price == newItem.current_price
    }

    override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem == newItem
    }
}

interface FavoriteItemClickListener {
    fun addOrDeleteFavorite(cryptoListItem: Crypto, isChecked: Boolean)
}

private class FavoriteDiffCallback : DiffUtil.ItemCallback<Crypto>(){
    override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem.id == newItem.id && oldItem.favorite == newItem.favorite
    }

    override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem == newItem
    }
}
