package com.example.stockproject.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockproject.R
import com.example.stockproject.model.IndexInfo
import com.example.stockproject.model.StockInfo
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.item_dashboard_indexitem.view.*
import kotlinx.android.synthetic.main.item_dashboard_stockitem.view.*


class DashboardFragment : Fragment() {

    private lateinit var stockList: RecyclerView
    private lateinit var indexList: RecyclerView
    private lateinit var dashboardViewModel: DashboardViewModel


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        indexList = root.findViewById(R.id.dashboard_indexlist)
        stockList = root.findViewById(R.id.dashboard_stocklist)

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        // Observe Stock Info List
        stockList.layoutManager = LinearLayoutManager(context)
        dashboardViewModel.stockInfo.observe(this, Observer {
            stockList.adapter = StockAdapter(context, it)
        })

        indexList.layoutManager = LinearLayoutManager(context)
        dashboardViewModel.indexInfo.observe(this, Observer {
            indexList.adapter = IndexAdapter(context, it)
        })

        dashboardViewModel.initData()
        return root
    }
}
class IndexAdapter(val context: Context?, val items: List<IndexInfo>): RecyclerView.Adapter<IndexAdapter.IndexVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexVH =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_dashboard_indexitem, parent,false)
                    .let { IndexVH(it) }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: IndexVH, position: Int) {
        holder.tvDayHigh.text = items[position].dayHigh.toString()
        holder.tvDayLow.text = items[position].dayLow.toString()
        holder.tvSymbol.text = items[position].index
        holder.tvPrice.text = items[position].price.toString()
        holder.tvOpen.text = items[position].open.toString()

        val volume = items[position].volume
        holder.tvVolume.text = when {
            volume >= 1000000000 -> String.format("%1$.2fB", volume / 100000000.0)
            volume >= 1000000 -> String.format("%1$.2fM", volume / 1000000.0)
            volume >= 1000 -> String.format("%1$.2fK", volume / 1000.0)
            else -> volume.toString()
        }

        inflateLineChart(holder.chart, items[position].datapoint)
    }

    private fun inflateLineChart(lineChart: LineChart, data: List<Double>) {
        val set = LineDataSet(
                data.mapIndexed { i, d -> Entry(i.toFloat(), d.toFloat()) },
                ""
        )

        set.setColor(ColorTemplate.getHoloBlue())
        set.setLineWidth(2f)
        set.fillAlpha = 0
        set.circleColors = listOf(Color.TRANSPARENT)
        set.valueTextColor = Color.TRANSPARENT

        val lineData = LineData(set)
        lineChart.data = lineData
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.isEnabled = false
        lineChart.setDescription("")
        lineChart.disableScroll()
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.setTouchEnabled(false)
    }

    class IndexVH(view: View): RecyclerView.ViewHolder(view) {
        val tvSymbol = view.indexitem_symbol
        val tvPrice = view.indexitem_price
        val tvDayHigh = view.indexitem_dayhigh
        val tvDayLow = view.indexitem_daylow
        val tvVolume = view.indexitem_volume
        val tvOpen = view.indexitem_open
        val chart = view.indexitem_chart
    }
}

class StockAdapter(val context: Context?, val items: List<StockInfo>): RecyclerView.Adapter<StockAdapter.StockVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockVH =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_dashboard_stockitem, parent,false)
                    .let { StockVH(it) }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StockVH, position: Int) {
        holder.tvSymbol.text = items[position].symbol
        holder.tvFullName.text = items[position].fullName
        holder.tvPrice.text = items[position].price.toString()
        holder.btnInfo.text = items[position].change.toString()

    }

    class StockVH(view: View): RecyclerView.ViewHolder(view) {
        val tvSymbol = view.stockitem_symbol
        val tvFullName = view.stockitem_fullname
        val tvPrice = view.stockitem_price
        val btnInfo = view.stockitem_btn
    }
}

