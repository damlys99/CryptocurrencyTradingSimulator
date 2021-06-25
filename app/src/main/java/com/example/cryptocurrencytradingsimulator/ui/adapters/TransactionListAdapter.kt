package com.example.cryptocurrencytradingsimulator.ui.adapters

import android.content.Context
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Typeface
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
import com.example.cryptocurrencytradingsimulator.data.models.Transaction
import com.example.cryptocurrencytradingsimulator.di.GlideApp
import kotlinx.android.synthetic.main.transaction_list_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Character.toLowerCase
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.sign

class TransactionListAdapter internal constructor(private val context: Context): ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item,parent,false)
        return TransactionListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        if(position == 0){
            holder.itemView.itemIncome.text = context.getString(R.string.headerIncome)
            holder.itemView.itemBalance.text = context.getString(R.string.headerBalance)
            holder.itemView.itemPrice.text = context.getString(R.string.headerPrice)
            holder.itemView.itemOwned.text = context.getString(R.string.headerOwned)
            holder.itemView.itemDate.text = context.getString(R.string.headerDate)
            holder.itemView.itemQuantity.text = context.getString(R.string.headerQuantity)

            val n = holder.itemView.itemLinearLayout.childCount
            repeat(n){
                val elem =  holder.itemView.itemLinearLayout.getChildAt(it) as TextView
                elem.setBackgroundColor(context.getColor(R.color.purple_200))
                elem.setTypeface(elem.typeface, Typeface.BOLD)
            }

        }
        else {
            val item = getItem(position)
            holder.bind(item)
        }

    }

    class TransactionListViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){

        val income = itemView.findViewById<TextView>(R.id.itemIncome)
        val balance = itemView.findViewById<TextView>(R.id.itemBalance)
        val price = itemView.findViewById<TextView>(R.id.itemPrice)
        val owned = itemView.findViewById<TextView>(R.id.itemOwned)
        val date = itemView.findViewById<TextView>(R.id.itemDate)
        val quantity = itemView.findViewById<TextView>(R.id.itemQuantity)
        fun bind(currentTransactionListItem: Transaction){
            income.text = currentTransactionListItem.income.toString()
            balance.text = currentTransactionListItem.balance.toString()
            price.text = currentTransactionListItem.price.toString()
            owned.text = currentTransactionListItem.owned.toString()
            date.text = currentTransactionListItem.date.toString()
            quantity.text = currentTransactionListItem.quantity.toString()
        }
    }


}

private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>(){
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.pk == newItem.pk
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}
