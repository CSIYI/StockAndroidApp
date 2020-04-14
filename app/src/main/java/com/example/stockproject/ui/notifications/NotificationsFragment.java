package com.example.stockproject.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.stockproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Button button = (Button) root.findViewById(R.id.button_search);
        final EditText edit = (EditText) root.findViewById(R.id.editText_search);
        textView = (TextView) root.findViewById(R.id.textView_search);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "user input : "+ edit.getText().toString());
                new Search().execute(edit.getText().toString());

            }
        });
        return root;
    }

//    public class Search extends AsyncTask<String, Void, String> {

//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d(TAG, "get something : I have entered");
//            String rlt = "";
//            String query = strings[0];
//            try {
//                HttpTransport httpTransport = new NetHttpTransport();
//                HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
//                JSONParser parser = new JSONParser();
//                GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
//                url.put("query", query);
//                url.put("limit", "1");
//                url.put("indent", "true");
//                url.put("key", );
//                HttpRequest request = requestFactory.buildGetRequest(url);
//                HttpResponse httpResponse = request.execute();
//                JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
//                JSONArray elements = (JSONArray) response.get("itemListElement");
//                for (Object element : elements) {
//                    System.out.println(JsonPath.read(element, "$.result.name").toString());
////                    rlt += JsonPath.read(element, "$.result.name").toString()+"\n";
//                    rlt += element.toString();
//                    Log.d(TAG, "get something : "+ element.toString());
//                }
//            } catch (Exception ex) {
//                Log.d(TAG, "get something : but I get errors ->" + ex);
//                ex.printStackTrace();
//            }
//            return rlt;
//        }
//    }

}