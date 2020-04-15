package com.example.stockproject.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockproject.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NotificationsFragment extends Fragment {


    private TextView textView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        button = (Button) root.findViewById(R.id.button_search);
        button.setEnabled(false);
        radioGroup=(RadioGroup)root.findViewById(R.id.radioGroup);


        final EditText edit = (EditText) root.findViewById(R.id.editText_search);
        textView = (TextView) root.findViewById(R.id.textView_search);
        textView.setMovementMethod(new ScrollingMovementMethod());
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                Log.d(TAG, "radio button : "+ rb.getText().toString());
                Log.d(TAG, "user input : "+ edit.getText().toString());
                new OkHttpHandler().execute(edit.getText().toString(), rb.getText().toString());

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               button.setEnabled(true);
            }
        });

        return root;
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String>  {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {
            Request request;

            if (params[1].equals("Business Info")){
                request = new Request.Builder()
                        .url("https://financialmodelingprep.com/api/v3/company/profile/"+params[0])
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    String bodyString = body.string();

                    JsonObject jsonObj = new Gson().fromJson(bodyString, JsonObject.class);

                    for (String key : jsonObj.keySet()) {
                        Log.d(TAG, "data key : " + key);
                    }
                    Object profile = jsonObj.get("profile");

                    Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(profile.toString());
                    String prettyJsonString = gsonPretty.toJson(je);
                    prettyJsonString = prettyJsonString.replace("{","");
                    prettyJsonString = prettyJsonString.replace("}", "");
                    prettyJsonString = prettyJsonString.replace("\"", "");

                    return  prettyJsonString;
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (params[1].equals("Ticker")) {
                request = new Request.Builder()
                        .url("https://financialmodelingprep.com/api/v3/search?query="+params[0]+"&limit=1&exchange=NASDAQ")
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    String bodyString = body.string();

                    JsonArray jsonArray = new Gson().fromJson(bodyString, JsonArray.class);
                    return "ticker symbol : " +jsonArray.get(0).getAsJsonObject().get("symbol").toString().replace("\"", "");
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                request = new Request.Builder()
                        .url("https://financialmodelingprep.com/api/v3/stock/real-time-price/"+params[0])
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    String bodyString = body.string();

                    JsonObject jsonObj = new Gson().fromJson(bodyString, JsonObject.class);
                    return "current price : "+jsonObj.get("price").toString().replace("\"", "");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }


}