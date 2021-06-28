package com.example.cryptocurrencytradingsimulator.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Owned
import com.example.cryptocurrencytradingsimulator.databinding.CryptoActionsFragmentBinding
import com.example.cryptocurrencytradingsimulator.databinding.ProfileFragmentBinding
import com.example.cryptocurrencytradingsimulator.ui.adapters.OwnedItemClickListener
import com.example.cryptocurrencytradingsimulator.ui.adapters.OwnedListAdapter
import com.example.cryptocurrencytradingsimulator.ui.adapters.TransactionListAdapter
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.utils.MyFormatter
import com.example.cryptocurrencytradingsimulator.utils.YourMarkerView
import com.example.cryptocurrencytradingsimulator.viewmodels.ProfileViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), OwnedItemClickListener {

    val viewModel: ProfileViewModel by viewModels()
    lateinit var ownedListAdapter: OwnedListAdapter
    lateinit var binding: ProfileFragmentBinding
    val lineEntries: ArrayList<Entry> = arrayListOf()
    val lineDataSet: LineDataSet = LineDataSet(lineEntries, "Balance")
    val lineData: LineData = LineData(lineDataSet)
    var first: Long = 1000000000000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_fragment,
            container,
            false
        )

        viewModel.prices.observe(viewLifecycleOwner){
            it?.let {
                ownedListAdapter = OwnedListAdapter(this, it)
                binding.ownedList.adapter = ownedListAdapter
                ownedListAdapter.submitList(viewModel.owned.value!!)
            }
        }
        viewModel.money.observe(viewLifecycleOwner){
            binding.accountBalance.text = MyFormatter.currency(it)
        }

        val chart = binding.balanceChart
        setChart(chart)
        viewModel.chartData.observe(viewLifecycleOwner) { it ->
            lineEntries.clear()
            if(it.size > 0 ) {
                first = it.first().date
                it.forEach {
                    lineEntries.add(Entry(((it.date - first)).toFloat(), it.balance.toFloat()))
                }

                lineDataSet.values = lineEntries
                lineData.removeDataSet(0)
                lineData.addDataSet(lineDataSet)
                chart.data = lineData
                chart.notifyDataSetChanged()
                chart.animateY(1000)
            }

        }


        observeModelNavigation(viewModel)
        return binding.root
    }

    override fun chooseCrypto(ownedListItem: Owned) {
        viewModel.chooseCrypto(ownedListItem)
    }

    fun setChart(elem: LineChart) {
        Utils.init(context)
        lineDataSet.color = Color.BLACK
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawHighlightIndicators(true)
        lineDataSet.highLightColor = Color.RED
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.BLUE
        lineDataSet.setDrawValues(false)
        elem.animateY(1000)
        elem.xAxis.position = XAxis.XAxisPosition.BOTTOM
        elem.xAxis.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                val formatter = DateTimeFormatter.ofPattern("MM-dd")
                // Log.d("aaa", (value.toLong() + (first)).toString())
                return formatter.format(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(value.toLong() + first), ZoneId.systemDefault()
                    )
                )
            }
        }
        elem.setDrawMarkers(true)
        elem.marker = YourMarkerView(context, R.layout.chart_marker)
        elem.setTouchEnabled(true)
        elem.description.isEnabled = false
        elem.legend.isEnabled = false
        elem.setScaleEnabled(false)
    }

}