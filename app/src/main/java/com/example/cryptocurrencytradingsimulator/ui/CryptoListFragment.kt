package com.example.cryptocurrencytradingsimulator.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.databinding.CryptoListFragmentBinding
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoItemClickListener
import com.example.cryptocurrencytradingsimulator.ui.adapters.CryptoListAdapter
import com.example.cryptocurrencytradingsimulator.ui.adapters.FavoriteItemClickListener
import com.example.cryptocurrencytradingsimulator.ui.base.BaseFragment
import com.example.cryptocurrencytradingsimulator.viewmodels.CryptoListViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CryptoListFragment : BaseFragment(), CryptoItemClickListener, FavoriteItemClickListener, SearchView.OnQueryTextListener {

    lateinit var adapter: CryptoListAdapter
    lateinit var searchView: SearchView
    lateinit var binding: CryptoListFragmentBinding
    private val cryptoListViewModel: CryptoListViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



       binding = DataBindingUtil.inflate<CryptoListFragmentBinding>(
            inflater,
            R.layout.crypto_list_fragment,
            container,
            false
        )


        binding.swipeRecyclerView.setOnRefreshListener{
            cryptoListViewModel.loadData()
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRecyclerView.isRefreshing = false
            }, 1500)
        }

        binding.viewModel = cryptoListViewModel
        adapter = CryptoListAdapter(this, this)
        binding.recyclerView.adapter = adapter
        cryptoListViewModel.cryptos.observe(viewLifecycleOwner) { res ->
            adapter.modifyList(res)
            if(this::searchView.isInitialized) {
                adapter.filter(searchView.query)
            }
            if (res.isNotEmpty()){
                binding.progressBarCryptoList.visibility = View.GONE
                binding.swipeRecyclerView.visibility = View.VISIBLE
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> adapter.modifyList(cryptoListViewModel.cryptos.value!!)
                    1 -> adapter.favorites()
                }
                adapter.filter(searchView.query)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
               return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.listToolbar)
        setHasOptionsMenu(true)

        observeModelNavigation(cryptoListViewModel)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cryptolist_menu, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun chooseCrypto(cryptoListItem: Crypto) {
        cryptoListViewModel.chooseCrypto(cryptoListItem)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.recyclerView.scrollToPosition(0)
        },300)
        return true
    }

    override fun addOrDeleteFavorite(cryptoListItem: Crypto, isChecked: Boolean) {
        cryptoListViewModel.addOrDeleteFavorite(cryptoListItem, isChecked)
        if(binding.tabLayout.selectedTabPosition == 1)  adapter.favorites()
    }
}
