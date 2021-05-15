package com.example.cryptocurrencytradingsimulator.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.cryptocurrencytradingsimulator.viewmodels.BaseViewModel

open class BaseFragment : Fragment() {

    internal fun observeModelNavigation(model : BaseViewModel) {
        model.navigateToFragment.observe(this.viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(it.navigateAction, it.getParametersAsBundle())
            }
        })
    }
}