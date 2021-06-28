package com.example.cryptocurrencytradingsimulator.utils

import android.content.Context
import android.widget.TextView
import com.example.cryptocurrencytradingsimulator.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class YourMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
    private var tvContent: TextView? = findViewById<TextView>(R.id.tvContent)

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