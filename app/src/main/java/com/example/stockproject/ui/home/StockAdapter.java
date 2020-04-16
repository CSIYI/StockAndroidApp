package com.example.stockproject.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stockproject.R;

import java.util.ArrayList;

public class StockAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<StockModel> stockModelArrayList;

    public StockAdapter(Context context, ArrayList<StockModel> stockModelArrayList) {
        this.context = context;
        this.stockModelArrayList = stockModelArrayList;
    }
    @Override
    public int getCount() {
        return stockModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, null, true);

            viewHolder.stock_ticker = (TextView) convertView.findViewById(R.id.list_item1);
            viewHolder.stock_lastPrice = (TextView) convertView.findViewById(R.id.list_item2);
            viewHolder.stock_change = (TextView) convertView.findViewById(R.id.list_item3);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.stock_ticker.setText(stockModelArrayList.get(position).getTicker());
        viewHolder.stock_lastPrice.setText(stockModelArrayList.get(position).getLast_price());
        viewHolder.stock_change.setText(stockModelArrayList.get(position).getChange());

        if (stockModelArrayList.get(position).getChange().charAt(0) == '-') {
            viewHolder.stock_ticker.setTextColor(Color.RED);
            viewHolder.stock_lastPrice.setTextColor(Color.RED);
            viewHolder.stock_change.setTextColor(Color.RED);
        }

        return convertView;
    }

    private class ViewHolder {
        protected TextView stock_ticker, stock_lastPrice, stock_change;
    }
}
