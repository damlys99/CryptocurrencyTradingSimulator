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
import com.example.cryptocurrencytradingsimulator.data.models.Transaction
import com.example.cryptocurrencytradingsimulator.data.models.TransactionType
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionListAdapter internal constructor(private val context: Context): ListAdapter<Transaction, TransactionListAdapter.TransactionListViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item,parent,false)
        return TransactionListViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item)
    }

    class TransactionListViewHolder constructor(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView){

        val income = itemView.findViewById<TextView>(R.id.itemIncome)
        val balance = itemView.findViewById<TextView>(R.id.itemBalance)
        val price = itemView.findViewById<TextView>(R.id.itemPrice)
        val owned = itemView.findViewById<TextView>(R.id.itemOwned)
        val date = itemView.findViewById<TextView>(R.id.itemDate)
        val quantity = itemView.findViewById<TextView>(R.id.itemAmount)
        fun bind(currentTransactionListItem: Transaction){
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("USD")
            currencyFormat.maximumFractionDigits = 8

            val fractionFormat = NumberFormat.getInstance()
            fractionFormat.maximumFractionDigits = 8

            val formatterPattern = "yyyy-MM-dd HH:mm"
            val formatter = DateTimeFormatter.ofPattern(formatterPattern)

            income.text = currencyFormat.format(currentTransactionListItem.income)
            balance.text = currencyFormat.format(currentTransactionListItem.balance)
            price.text = currencyFormat.format(currentTransactionListItem.price)
            owned.text = fractionFormat.format(currentTransactionListItem.owned)
            date.text = formatter.format(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(currentTransactionListItem.date), ZoneId.systemDefault()))
            quantity.text = fractionFormat.format(currentTransactionListItem.quantity)
            if(currentTransactionListItem.type == TransactionType.BUY){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    itemView.setBackgroundColor(context.getColor(R.color.red))
                }
            }
            else{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    itemView.setBackgroundColor(context.getColor(R.color.green))
                }
            }
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
