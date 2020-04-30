package com.example.stockproject.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
    private Spinner spinner;
    private String most_active_sticker = "https://financialmodelingprep.com/api/v3/stock/actives";
    private String most_gain_stock = "https://financialmodelingprep.com/api/v3/stock/gainers";
    private String most_loser_stock = "https://financialmodelingprep.com/api/v3/stock/losers";
    private ArrayList<StockModel> stockModelArrayList = new ArrayList<StockModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView = root.findViewById(R.id.listView);
        spinner = root.findViewById(R.id.spinner);
        spinner.setSelection(0);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.list_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Log.d("DEBUG", selected);
                new JsonTask().execute(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        return root;
    }

    public class JsonTask extends AsyncTask<String, Void, String>  {
        String data = "";
        URL url;
        @Override
        protected String doInBackground(String...params) {
            try {
                if (params[0].equals("Most Active Stock Companies")) {
                    url = new URL(most_active_sticker);
                } else if (params[0].equals("Most Gainer Stock Companies")) {
                   url = new URL(most_gain_stock);
                } else if (params[0].equals("Most Loser Stock Companies")) {
                    url = new URL(most_loser_stock);
                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
                String line  = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                Log.d("DEBUG", data);
                httpURLConnection.disconnect();
                inputStream.close();
                bufferedReader.close();
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
                JSONArray jsonArray = null;
//                JSONArray jsonArray = jsonObj.getJSONArray("mostActiveStock");
                if (url.toString().endsWith("actives")) {
                    jsonArray = jsonObj.getJSONArray("mostActiveStock");
                } else if (url.toString().endsWith("gainers")) {
                    jsonArray = jsonObj.getJSONArray("mostGainerStock");
                } else if (url.toString().endsWith("losers")) {
                    jsonArray = jsonObj.getJSONArray("mostLoserStock");
                } else {
                    return;
                }
                Log.d("DEBUG", jsonArray.toString());
                stockModelArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    StockModel stockModel = new StockModel();
                    stockModel.setTicker(temp.getString("ticker"));
                    stockModel.setLast_price(temp.getString("price"));
                    stockModel.setChange(temp.getString("changes"));
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