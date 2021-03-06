package com.example.cryptocurrencytradingsimulator.utils

import android.text.InputFilter
import android.text.Spanned

class MaxMinInputFilter(private val min: Double, private val max: Double): InputFilter {
    override fun filter(
        source: CharSequence?,
        p1: Int,
        p2: Int,
        dest: Spanned?,
        p4: Int,
        p5: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toDouble()
            if (isInRange(min, max, input) || input == 0.0)
                return null
        } catch (nfe: NumberFormatException) { }
        return ""
    }

    private fun isInRange(a: Double, b: Double, c: Double): Boolean {
        return (c in a..b)
    }
}