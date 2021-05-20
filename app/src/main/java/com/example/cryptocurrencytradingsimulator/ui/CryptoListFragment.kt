package com.example.cryptocurrencytradingsimulator.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoListFragmentBinding
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoItemClickListener
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoListAdapter
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.crypto_list_fragment.*
import kotlin.concurrent.thread


@AndroidEntryPoint
class CryptoListFragment : BaseFragment(), CryptoItemClickListener {

    lateinit var adapter: CryptoListAdapter
    private val cryptoListViewModel: CryptoListViewModel by activityViewModels()


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

        binding.swipeRecyclerView.setOnRefreshListener{

            cryptoListViewModel.loadData()
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRecyclerView.isRefreshing = false
            }, 1500)
        }

        binding.viewModel = cryptoListViewModel
        adapter = CryptoListAdapter(this)
        binding.recyclerView.adapter = adapter
        cryptoListViewModel.cryptos.observe(viewLifecycleOwner) { res ->
            adapter.submitList(res)
            if (res.size > 0) binding.progressBarCryptoList.visibility = View.GONE
        }

        return binding.root
    }


    override fun chooseCrypto(crypto: Crypto) {
        Toast.makeText(activity, "Selected crypto", Toast.LENGTH_SHORT).show()
    }

}