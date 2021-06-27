package com.example.cryptocurrencytradingsimulator.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cryptocurrencytradingsimulator.MainApplication
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoActionsFragmentBinding
import com.example.cryptocurrencytradingsimulator.databinding.CryptoInfoFragmentBinding
import com.example.cryptocurrencytradingsimulator.di.GlideApp
import com.example.cryptocurrencytradingsimulator.notifications.NotificationService
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoListAdapter
import com.example.cryptocurrencytradingsimulator.ui.adapters.TransactionListAdapter
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.utils.DecimalDigitsInputFilter
import com.example.cryptocurrencytradingsimulator.utils.MaxMinInputFilter
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModel
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chart_marker.view.*
import kotlinx.android.synthetic.main.crypto_info_fragment.view.*
import kotlinx.android.synthetic.main.crypto_list_fragment.*
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sign


@AndroidEntryPoint
class CryptoActionFragment : BaseFragment() {

    val cryptoViewModel: CryptoViewModel by viewModels({ requireParentFragment() })
    lateinit var transactionsAdapter: TransactionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<CryptoActionsFragmentBinding>(
            inflater,
            R.layout.crypto_actions_fragment,
            container,
            false
        )

        transactionsAdapter = TransactionListAdapter(requireContext())
        binding.transactionList.adapter = transactionsAdapter
        cryptoViewModel.transactions.observe(viewLifecycleOwner) {
            transactionsAdapter.submitList(it)
        }
        bindEditTextSeekBar(
            binding.editTextPrice,
            binding.seekBarPrice,
            binding.editTextAmount,
            binding.seekBarAmount
        )
        binding.currentlyOwned.text = getString(R.string.currentlyOwned, "0", "0")
        return binding.root
    }

    fun bindEditTextSeekBar(
        etPrice: EditText,
        sbPrice: SeekBar,
        etAmount: EditText,
        sbAmount: SeekBar
    ) {
        val price: Double = cryptoViewModel.crypto.value!!.current_price!!
        val money: Double = 2500.00
        val maxAmount:Double = money/price
        val minAmount:Double = 0.01/price
        Log.e("aaa", minAmount.toString())
        var changedBy: EditText = etPrice
        etPrice.filters = arrayOf(DecimalDigitsInputFilter(32, 2), MaxMinInputFilter(0.01, money))

        etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (etPrice.hasFocus()) changedBy = etPrice
                if (changedBy == etPrice) {

                    try {
                        val amount = s.toString().toDouble()/price
                        val progress =
                            ((s.toString().toDouble().roundToInt() / money) * 1000).roundToInt()
                        sbPrice.progress = progress

                        etAmount.setText(amount.toString())
                        sbAmount.progress = ((amount/maxAmount)*1000).roundToInt()
                    } catch (ex: Exception) {
                    }
                }
            }
        })

        sbPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    etPrice.requestFocus()
                    val prog = ((progress.toDouble() / 1000) * money).toInt().toString()
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
                        val progress =
                            ((s.toString().toDouble() / maxAmount) * 1000 ).roundToInt()
                        sbAmount.progress = progress
                        val prc = s.toString().toDouble() * price
                        etPrice.setText((prc).toString())
                        sbPrice.progress = ((prc/money) * 1000).roundToInt()
                    } catch (ex: Exception) {
                    }
                }
            }
        })

        sbAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    etAmount.requestFocus()
                    val prog = ((progress.toDouble() / 1000) * maxAmount).toString()
                    etAmount.setText(prog)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    fun onSendNotificationsButtonClick(view: View?) {
        NotificationService.setAlarm(requireContext())
    }

}