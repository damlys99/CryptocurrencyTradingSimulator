package com.example.cryptocurrencytradingsimulator.ui

import android.graphics.Color.WHITE
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Owned
import com.example.cryptocurrencytradingsimulator.databinding.CryptoActionsFragmentBinding
import com.example.cryptocurrencytradingsimulator.ui.adapters.TransactionListAdapter
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.utils.DecimalDigitsInputFilter
import com.example.cryptocurrencytradingsimulator.utils.MaxMinInputFilter
import com.example.cryptocurrencytradingsimulator.utils.MyFormatter
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.crypto_actions_fragment.*
import java.util.*
import kotlin.math.roundToInt


@AndroidEntryPoint
class CryptoActionFragment : BaseFragment() {

    val cryptoViewModel: CryptoViewModel by viewModels({ requireParentFragment() })
    lateinit var transactionsAdapter: TransactionListAdapter
    lateinit var binding: CryptoActionsFragmentBinding
    var price: Double = 0.0
    lateinit var money: MutableLiveData<Float>
    lateinit var owned: MutableLiveData<Owned>
    var maxAmount: Double = 0.0
    var maxVal: Float = 0F
    var amount: Double = 0.0
    var buy: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.crypto_actions_fragment,
            container,
            false
        )
        binding.buySellGroup.setOnCheckedChangeListener { group, checkedId ->
            val checked = group.findViewById<RadioButton>(checkedId)
            when (checked.text) {
                getString(R.string.buy) -> buy.value = true
                else -> buy.value = false
            }
        }

        transactionsAdapter = TransactionListAdapter(requireContext())
        binding.transactionList.adapter = transactionsAdapter
        cryptoViewModel.transactions.observe(viewLifecycleOwner) {
            transactionsAdapter.submitList(it)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.transactionList.smoothScrollToPosition(0)},
                50
            )
        }
        setListHeaders()

        price = cryptoViewModel.crypto.value!!.current_price!!
        owned = cryptoViewModel.owned
        owned.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.currentlyOwned.text =
                    getString(
                        R.string.currentlyOwned,
                        MyFormatter.double(it.amount),
                        MyFormatter.currency(it.amount * price)
                    )
            }
            else{
                                binding.currentlyOwned.text =
                    getString(
                        R.string.currentlyOwned,
                        MyFormatter.double(0),
                        MyFormatter.currency(0)
                    )
            }
        }
        money = cryptoViewModel.money

        buy.observe(viewLifecycleOwner) {
            if (it) {
                maxVal = money.value!!
                binding.buttonBuy.text = getString(R.string.buy)
            } else {
                maxVal = (owned.value!!.amount * price).toFloat()
                binding.buttonBuy.text = getString(R.string.sell)
            }

            setRanges()
        }

        money.observe(viewLifecycleOwner) {
            if(buy.value!!) {
                maxVal = money.value!!
            }
            else{
                maxVal = (cryptoViewModel.owned.value!!.amount * price).toFloat()
            }
            setRanges()
        }


        bindEditTextSeekBar(
            binding.editTextPrice,
            binding.seekBarPrice,
            binding.editTextAmount
        )
        binding.buttonBuy.setOnClickListener {
            if (editTextPrice.text.isNotEmpty() && editTextPrice.text.toString()
                    .toDouble() != 0.0
            ) {
                if (buy.value!!) {
                    cryptoViewModel.buyCrypto(
                        binding.editTextPrice.text.toString().toDouble(),
                        amount
                    )
                } else {
                    cryptoViewModel.sellCrypto(
                        binding.editTextPrice.text.toString().toDouble(),
                        amount
                    )
                }
            }
        }

        notificationsObservers()
        return binding.root
    }

    private fun setRanges() {
        clearForm()
        maxAmount = maxVal / price
        binding.tvPriceMin.text = 0.toString()
        binding.tvPriceMax.text = maxVal.toString()
        binding.editTextPrice.filters =
            arrayOf(DecimalDigitsInputFilter(32, 2), MaxMinInputFilter(0.01, maxVal.toDouble()))
        binding.editTextAmount.filters = arrayOf(DecimalDigitsInputFilter(32, 10))
    }

    private fun clearForm(){
        binding.editTextAmount.setText("")
        binding.editTextPrice.setText("")
        binding.seekBarPrice.progress = 0
    }

    private fun bindEditTextSeekBar(
        etPrice: EditText,
        sbPrice: SeekBar,
        etAmount: EditText,
    ) {
        var changedBy: EditText = etPrice

        etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (etPrice.hasFocus()) changedBy = etPrice
                if (changedBy == etPrice) {

                    try {
                        val amountTemp = s.toString().toDouble() / price
                        val progress =
                            ((s.toString().toDouble().roundToInt() / maxVal) * 1000).roundToInt()
                        sbPrice.progress = progress
                        amount = amountTemp
                        etAmount.setText(MyFormatter.double(amount))
                    } catch (ex: Exception) {
                        etAmount.setText("")
                    }
                }
            }
        })

        sbPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    etPrice.requestFocus()
                    val prog = ((progress.toDouble() / 1000) * maxVal).toInt().toString()
                    etPrice.setText(prog)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (etAmount.hasFocus()) changedBy = etAmount
                if (changedBy == etAmount) {
                    try {
                        val prc = s.toString().toDouble() * price
                        amount = s.toString().toDouble()
                        etPrice.setText((prc).toString())
                        sbPrice.progress = ((prc / maxVal) * 1000).roundToInt()
                    } catch (ex: Exception) {
                    }
                }
            }
        })

    }

    private fun setListHeaders() {
        binding.headerIncome.text = requireContext().getString(R.string.headerIncome)
        binding.headerBalance.text = requireContext().getString(R.string.headerBalance)
        binding.headerPrice.text = requireContext().getString(R.string.headerPrice)
        binding.headerOwned.text = requireContext().getString(R.string.headerOwned)
        binding.headerDate.text = requireContext().getString(R.string.headerDate)
        binding.headerAmount.text = requireContext().getString(R.string.headerAmount)

        val n = binding.headerLinearLayout.childCount
        repeat(n) {
            val elem = binding.headerLinearLayout.getChildAt(it) as TextView
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                elem.setBackgroundColor(requireContext().getColor(R.color.purple_200))
                elem.setTextColor(WHITE)
            }
            elem.setTypeface(elem.typeface, Typeface.BOLD)
        }

    }

    private fun notificationsObservers(){
        cryptoViewModel.notification.observe(viewLifecycleOwner){
            binding.editTextNotificationPrice.clearFocus()
            if(it != null) {
                binding.buttonSetNotification.isChecked = true
                binding.editTextNotificationPrice.setText(it.price.toString())
            }
            else{
                binding.buttonSetNotification.isChecked = false
                if(binding.editTextNotificationPrice.text.isEmpty()) {
                    binding.buttonSetNotification.isEnabled = false
                }
            }
        }

        binding.buttonSetNotification.setOnCheckedChangeListener{
                _, isChecked ->
                when (isChecked) {
                    true -> {
                        cryptoViewModel.setNotification(binding.editTextNotificationPrice.text.toString())
                    }
                    else -> cryptoViewModel.deleteNotification()
                }
        }

        binding.editTextNotificationPrice.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(binding.editTextNotificationPrice.hasFocus()) {
                    cryptoViewModel.deleteNotification()
                    binding.buttonSetNotification.isEnabled = p0.toString().isNotEmpty()
                }
            }

        })
    }



}