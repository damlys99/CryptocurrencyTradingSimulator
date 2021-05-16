package com.example.cryptocurrencytradingsimulator.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoListFragmentBinding
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoItemClickListener
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoListAdapter
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class CryptoListFragment : BaseFragment(), CryptoItemClickListener {

    lateinit var adapter: CryptoListAdapter
    private val cryptoListViewModel: CryptoListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<CryptoListFragmentBinding>(
            inflater,
            R.layout.crypto_list_fragment,
            container,
            false
        )

        binding.viewModel = cryptoListViewModel
        cryptoListViewModel.cryptos.observe(viewLifecycleOwner) { res ->
            adapter = CryptoListAdapter(res, this)
            adapter.submitList(res)
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }


    override fun chooseCrypto(crypto: Crypto) {
        Toast.makeText(activity, "Selected crypto", Toast.LENGTH_SHORT).show()
    }
}