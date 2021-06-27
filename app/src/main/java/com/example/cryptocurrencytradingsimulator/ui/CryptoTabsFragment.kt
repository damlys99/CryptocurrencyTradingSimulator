package com.example.cryptocurrencytradingsimulator.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoTabsFragmentBinding
import com.example.cryptocurrencytradingsimulator.di.GlideApp
import com.example.cryptocurrencytradingsimulator.ui.adapters.ViewPagerAdapter
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModel
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.sign

@AndroidEntryPoint
class CryptoTabsFragment : Fragment() {


    private val mArgs: CryptoTabsFragmentArgs by navArgs()
    @Inject
    lateinit var cryptoViewModelFactory: CryptoViewModelFactory
    val cryptoViewModel: CryptoViewModel by viewModels {
        CryptoViewModel.provideFactory(cryptoViewModelFactory, mArgs.cryptoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<CryptoTabsFragmentBinding>(
            inflater,
            R.layout.crypto_tabs_fragment,
            container,
            false
        )

        val tabLayout = binding.cryptoTabLayout
        val viewPager = binding.pager
        viewPager.isUserInputEnabled = false
        viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position == 0 ){
                tab.text = getString(R.string.tab_info)
            }
            else{
                tab.text = getString(R.string.tab_actions)
            }
        }.attach()



        cryptoViewModel.crypto.observe(viewLifecycleOwner) {
            if (it.id!!.isNotBlank()) {
                binding.cryptoLayout.visibility = View.VISIBLE
                binding.loader.visibility = View.GONE
            }
            bindUi(binding, it)
        }



        return binding.root
    }

    fun bindUi(binding: CryptoTabsFragmentBinding, crypto: Crypto) {
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

    }


}