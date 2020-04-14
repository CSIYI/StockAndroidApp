package com.example.stockproject.ui.notifications;

import android.os.AsyncTask;
import android.util.Log;

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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Search extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "get something : I have entered");
        String rlt = "";
        try {
            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            JSONParser parser = new JSONParser();
            GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
            url.put("query", "Taylor Swift");
            url.put("limit", "10");
            url.put("indent", "true");
            url.put("key", "AIzaSyC0jKuv7DeRBhLBSlNeITBmb7zco46vQ7U");
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
            JSONArray elements = (JSONArray) response.get("itemListElement");
            for (Object element : elements) {
                System.out.println(JsonPath.read(element, "$.result.name").toString());
                rlt += JsonPath.read(element, "$.result.name").toString();
                Log.d(TAG, "get something : "+ JsonPath.read(element, "$.result.name").toString());
            }
        } catch (Exception ex) {
            Log.d(TAG, "get something : but I get errors ->" + ex);
            ex.printStackTrace();
        }
        return rlt;
    }
}
