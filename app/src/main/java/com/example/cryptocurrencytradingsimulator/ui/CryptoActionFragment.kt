package com.example.cryptocurrencytradingsimulator.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
import kotlin.math.sign


@AndroidEntryPoint
class CryptoActionFragment : BaseFragment() {

    val cryptoViewModel: CryptoViewModel by viewModels({requireParentFragment()})
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
        binding.currentlyOwned.text = getString(R.string.currentlyOwned, "0", "0")
        return binding.root
    }

    fun onSendNotificationsButtonClick(view: View?) {
        NotificationService.setAlarm(requireContext())
    }

}