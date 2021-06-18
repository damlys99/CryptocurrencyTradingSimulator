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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoFragmentBinding
import com.example.cryptocurrencytradingsimulator.di.GlideApp
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
import kotlinx.android.synthetic.main.crypto_fragment.view.*
import kotlinx.android.synthetic.main.crypto_list_fragment.*
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.sign


@AndroidEntryPoint
class CryptoFragment : BaseFragment() {

    private val mArgs: CryptoFragmentArgs by navArgs()

    @Inject
    lateinit var cryptoViewModelFactory: CryptoViewModelFactory
    val lineEntries: ArrayList<Entry> = arrayListOf()
    val lineDataSet: LineDataSet = LineDataSet(lineEntries, "Price")
    val lineData: LineData = LineData(lineDataSet)
    var first: Long = 1000000000000L
    val cryptoViewModel: CryptoViewModel by viewModels {
        CryptoViewModel.provideFactory(cryptoViewModelFactory, mArgs.cryptoId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<CryptoFragmentBinding>(
            inflater,
            R.layout.crypto_fragment,
            container,
            false
        )
        cryptoViewModel.crypto.observe(viewLifecycleOwner) {
            if (it.id!!.isNotBlank()) {
                binding.cryptoLayout.visibility = View.VISIBLE
                binding.loader.visibility = View.GONE
            }
            bindUi(binding, it)
        }

        setChart(binding.lineChart)
        binding.chartRange.setOnCheckedChangeListener { group, checkedId ->
            val checked = group.findViewById<RadioButton>(checkedId)
            lateinit var formatterPattern: String
            when(checked.text) {
                getString(R.string.chart_radio_label_1d) -> formatterPattern = "HH:mm"
                else -> formatterPattern = "MM-dd"
            }
            binding.lineChart.xAxis.valueFormatter = object : IAxisValueFormatter {
                override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                    val formatter = DateTimeFormatter.ofPattern(formatterPattern)
                    // Log.d("aaa", (value.toLong() + (first)).toString())
                    return formatter.format(
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(value.toLong() + first), ZoneId.systemDefault())
                    )
                }
            }
            cryptoViewModel.getChartData(group.findViewById<RadioButton>(checkedId).text.toString())
        }

        cryptoViewModel.chartData.observe(viewLifecycleOwner) { it ->
            lineEntries.clear()
            first = it!!.prices!!.first()[0].toLong()
            //Log.d("aa", first.toString())
            cryptoViewModel.chartData.value!!.prices!!.forEach {
                lineEntries.add(Entry(((it[0].toLong() - first)).toFloat(), it[1].toFloat()))

            }
            lineDataSet.values = lineEntries
            lineData.removeDataSet(0)
            lineData.addDataSet(lineDataSet)
            binding.lineChart.data = lineData
            binding.lineChart.notifyDataSetChanged()
            binding.lineChart.animateY(1000)

        }
        binding.viewModel = cryptoViewModel

        observeModelNavigation(cryptoViewModel)
        return binding.root
    }

    fun bindUi(binding: CryptoFragmentBinding, crypto: Crypto) {
        binding.cryptoName.text = crypto.name
        binding.cryptoSymbol.text = crypto.symbol
        GlideApp.with(requireContext()).load(crypto.image).into(binding.cryptoIcon)
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("USD")
        currencyFormat.maximumFractionDigits = 10
        binding.cryptoPrice.text = currencyFormat.format(crypto.current_price)
        val percentFormat = NumberFormat.getPercentInstance()
        percentFormat.maximumFractionDigits = 2
        percentFormat.minimumFractionDigits = 2
        binding.cryptoPriceChange.text =
            percentFormat.format(crypto.price_change_percentage_24h_in_currency!! / 100)
        when (sign(crypto.price_change_percentage_24h_in_currency)) {
            1.0 -> binding.cryptoPriceChange.setTextColor(Color.GREEN)
            -1.0 -> binding.cryptoPriceChange.setTextColor(Color.RED)
        }
        binding.cryptoAdditionalInfo.caiMarketCap.text = crypto.market_cap
        binding.cryptoAdditionalInfo.cai24High.text = currencyFormat.format(crypto.high_24h)
        binding.cryptoAdditionalInfo.cai24Low.text = currencyFormat.format(crypto.low_24h)
        binding.cryptoAdditionalInfo.caiVolume.text = crypto.total_volume
        binding.cryptoAdditionalInfo.caiAth.text = currencyFormat.format(crypto.ath)
        binding.cryptoAdditionalInfo.caiAtl.text = currencyFormat.format(crypto.atl)
        binding.cryptoAdditionalInfo.caiPriceChange.text = currencyFormat.format(crypto.price_change_24h)
        binding.cryptoAdditionalInfo.caiPriceChangePercent.text = percentFormat.format(crypto.price_change_percentage_24h_in_currency!! / 100)


    }

    fun setChart(elem: LineChart) {
        Utils.init(context)
        lineDataSet.setColor(BLACK)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawHighlightIndicators(true)
        lineDataSet.setHighLightColor(RED);
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = BLUE
        lineDataSet.setDrawValues(false)


//        elem.description.setText("Price in last 12 days");
//        elem.description.setTextSize(12F);
        elem.animateY(1000);
//        elem.getXAxis().setGranularityEnabled(true);
//        elem.getXAxis().setGranularity(1.0f);
        //elem.xAxis.labelCount = lineDataSet.entryCount;
        elem.xAxis.position = XAxis.XAxisPosition.BOTTOM
        elem.xAxis.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
               // Log.d("aaa", (value.toLong() + (first)).toString())
                return formatter.format(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(value.toLong() + first), ZoneId.systemDefault())
                )
            }
        }
        elem.setDrawMarkers(true)
        elem.marker = YourMarkerView(context, R.layout.chart_marker)
        elem.setTouchEnabled(true)
        elem.setScaleEnabled(false);
    }
    //class MyXAxisValueFormatter(): IAxisValueFormatter {
//    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//        Log.d(fir)
//        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
//        return formatter.format(LocalDateTime.ofEpochSecond(value.toLong(), 0, ZoneOffset.UTC) + first)
//    }

//}

}


class YourMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
    private var tvContent: TextView? = findViewById(R.id.tvContent) as TextView

    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvContent!!.text = e.y.toString()
        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }
}