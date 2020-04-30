package com.example.stockproject.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.markushi.ui.CircleButton
import com.example.stockproject.R
import com.example.stockproject.model.IndexInfo
import com.example.stockproject.model.StockInfo
import com.example.stockproject.utils.toBigNumberString
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_dashboard_indexitem.view.*
import kotlinx.android.synthetic.main.item_dashboard_stockitem.view.*


class DashboardFragment : Fragment() {

    private lateinit var stockList: RecyclerView
    private lateinit var indexList: RecyclerView
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel = activity?.run {
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        indexList = root.findViewById(R.id.dashboard_indexlist)
        stockList = root.findViewById(R.id.dashboard_stocklist)

        // Observe Stock Info List
        stockList.layoutManager = LinearLayoutManager(context)
        stockList.isNestedScrollingEnabled = false
        indexList.layoutManager = LinearLayoutManager(context)
        indexList.isNestedScrollingEnabled = false

        dashboardViewModel.stockInfo.observe(this, Observer { updateStockList() })
        dashboardViewModel.mode.observe(this, Observer { updateStockList() })

        dashboardViewModel.indexInfo.observe(this, Observer {
            indexList.adapter = IndexAdapter(context, it)
        })
        root.findViewById<CircleButton>(R.id.btn_addstock).setOnClickListener(addStockOnClickListener)
        root.findViewById<CircleButton>(R.id.btn_deletestock).setOnClickListener(deleteStockOnClickListener)
        return root
    }

    private fun updateStockList() {
        val info = dashboardViewModel.stockInfo.value ?: emptyList()
        val mode = dashboardViewModel.mode.value ?: 0
        stockList.adapter = StockAdapter(context!!, info, mode, modeChangeOnClickListener)
    }

    private val modeChangeOnClickListener: View.OnClickListener = View.OnClickListener {
        val value = ((dashboardViewModel.mode.value ?: 0) + 1) % 3
        dashboardViewModel.mode.postValue(value)
    }

    private val addStockOnClickListener: View.OnClickListener = View.OnClickListener {
        val dialog = AddStockDialogFragment()
        dialog.setPositiveCallback { text -> dashboardViewModel.addStockToList(text) }
        dialog.show(fragmentManager, "test")
    }

    private val deleteStockOnClickListener: View.OnClickListener = View.OnClickListener {
        dashboardViewModel.deleteLastStock()
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
        val item = items[position]
        holder.tvDayHigh.text = item.dayHigh.toString()
        holder.tvDayLow.text = item.dayLow.toString()
        holder.tvSymbol.text = item.index
        holder.tvPrice.text = item.price.toString()
        holder.tvOpen.text = item.open.toString()
        holder.tvVolume.text = item.volume.toBigNumberString()
        inflateLineChart(holder.chart, item.datapoint)
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

class StockAdapter(
        val context: Context,
        val items: List<StockInfo>,
        val mode: Int,
        val modeChangeListener: View.OnClickListener
): RecyclerView.Adapter<StockAdapter.StockVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockVH =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_dashboard_stockitem, parent,false)
                    .let { StockVH(it) }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StockVH, position: Int) {
        val item = items[position]
        holder.tvSymbol.text = item.symbol
        holder.tvFullName.text = item.fullName
        holder.tvPrice.text = item.price.toString()
        val green = ContextCompat.getColor(context, R.color.robinhoodGreen)
        val red = ContextCompat.getColor(context, R.color.robinhoodRed)
        holder.tvPrice.setTextColor(if (item.change >= 0) green else red)
        holder.btnInfo.text = when(mode) {
            0 -> item.volume.toBigNumberString()
            1 -> (if (item.change>0) "+" else "") + item.change.toString()
            else -> item.changePercent
        }

//        holder.btnInfo.setbackgrount()
        holder.btnInfo.setOnClickListener(modeChangeListener)
    }

    class StockVH(view: View): RecyclerView.ViewHolder(view) {
        val tvSymbol = view.stockitem_symbol
        val tvFullName = view.stockitem_fullname
        val tvPrice = view.stockitem_price
        val btnInfo = view.stockitem_btn
    }
}

