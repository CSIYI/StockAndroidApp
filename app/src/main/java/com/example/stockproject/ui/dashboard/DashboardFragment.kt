package com.example.stockproject.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockproject.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.item_dashboard_listitem.view.*


class DashboardFragment : Fragment() {

    private lateinit var root: View
    private lateinit var lineChart: LineChart
    private lateinit var stockList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        lineChart = root.findViewById(R.id.chart1)
        stockList = root.findViewById(R.id.dashboard_stocklist)
        initLineChart()
        initStockList()

        return root
    }

    private fun initLineChart() {
        val count = 50
        val range = 10
        val avg = 50

        val values1: ArrayList<Entry> = ArrayList()
        for (i in 0 until 50) {
            val value = avg + (Math.random() - 0.5) * range
            values1.add(Entry(i.toFloat(), value.toFloat()))
        }

        val set1 = LineDataSet(values1, "DataSet 1")

        set1.setColor(ColorTemplate.getHoloBlue())
        set1.setLineWidth(2f)
        set1.fillAlpha = 0
//        set1.setHighLightColor(Color.rgb(244, 117, 117))
        set1.circleColors = listOf(Color.TRANSPARENT)
        set1.valueTextColor = Color.TRANSPARENT

        val lineData = LineData(set1)
        lineChart.data = lineData
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false

        lineChart.xAxis.isEnabled = false

        lineChart.setDescription("")
        lineChart.disableScroll()
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.setTouchEnabled(false)
//        lineChart.ax
    }

    private fun initStockList() {

        val data = arrayListOf("Apple", "Banana")
        stockList.layoutManager = LinearLayoutManager(context)
        stockList.adapter = StockAdapter(context, data)
    }
}

class StockAdapter(val context: Context?, val items: ArrayList<String>): RecyclerView.Adapter<StockViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_dashboard_listitem, parent,false)
                    .let { StockViewHolder(it) }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.tva.text = items[position]
    }
}

class StockViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tva = view.textView
}