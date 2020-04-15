package com.example.stockproject.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.stockproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView listView;
    private ArrayList<StockModel> stockModelArrayList = new ArrayList<StockModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView = root.findViewById(R.id.listView);
        String most_active_sticker = "https://financialmodelingprep.com/api/v3/stock/actives";
        new JsonTask().execute(most_active_sticker);
        return root;
    }

    public class JsonTask extends AsyncTask<String, Void, String>  {
        String data = "";
        @Override
        protected String doInBackground(String...params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
                String line  = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jsonArray = jsonObj.getJSONArray("mostActiveStock");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    StockModel stockModel = new StockModel();
                    stockModel.setTicker(temp.getString("ticker"));
                    stockModel.setLast_price(temp.getString("price"));
                    stockModel.setChange(temp.getString("changes"));
                    Log.d("DEBUG", stockModel.getTicker());
                    stockModelArrayList.add(stockModel);
                    StockAdapter stockAdapter = new StockAdapter(getActivity(), stockModelArrayList);
                    listView.setAdapter(stockAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}