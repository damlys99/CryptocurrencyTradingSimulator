package com.example.cryptocurrencytradingsimulator.ui.adapters

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
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoListItemBinding
import com.squareup.picasso.Picasso

class CryptoListAdapter internal  constructor(val crypto: List<Crypto>, private val listener: CryptoItemClickListener):
    ListAdapter<Crypto, CryptoListAdapter.CryptoListViewHolder>(CryptoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoListViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item,parent,false)
        return CryptoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoListViewHolder, position: Int) {
        val item = crypto[position]
        holder.itemView.setOnClickListener { listener }
        holder.bind(crypto[position])
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    class CryptoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var imageView = itemView.findViewById<ImageView>(R.id.itCryptoIcon)
        var name = itemView.findViewById<TextView>(R.id.itCryptoName)
        var id = itemView.findViewById<TextView>(R.id.itCryptoId)

        fun bind(currentCrypto: Crypto){
            name.text = currentCrypto.name
            id.text = currentCrypto.id
            Picasso.get().load(currentCrypto.image).into(imageView)
        }
    }

}

interface CryptoItemClickListener{
    fun chooseCrypto(crypto: Crypto)
}

private class CryptoDiffCallback : DiffUtil.ItemCallback<Crypto>(){
    override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem == newItem
    }
}