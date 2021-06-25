package com.example.cryptocurrencytradingsimulator.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptocurrencytradingsimulator.ui.CryptoActionFragment
import com.example.cryptocurrencytradingsimulator.ui.CryptoInfoFragment

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0){
            return CryptoInfoFragment()
        }
        else{
            return CryptoActionFragment()
        }
    }
}