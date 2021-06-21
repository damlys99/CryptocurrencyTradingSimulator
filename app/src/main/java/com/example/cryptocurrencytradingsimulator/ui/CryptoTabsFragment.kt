package com.example.cryptocurrencytradingsimulator.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModel
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoViewModelFactory
import javax.inject.Inject

class CryptoTabsFragment : Fragment() {


    private val mArgs: CryptoFragmentArgs by navArgs()
    @Inject
    lateinit var cryptoViewModelFactory: CryptoViewModelFactory
    val cryptoViewModel: CryptoViewModel by activityViewModels {
        CryptoViewModel.provideFactory(cryptoViewModelFactory, mArgs.cryptoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.crypto_tabs_fragment, container, false)
    }

}