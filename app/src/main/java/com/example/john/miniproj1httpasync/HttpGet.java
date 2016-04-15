package com.example.john.miniproj1httpasync;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by John on 15/4/2016.
 */
public class HttpGet {
    private String url;
    private ArrayList<Results> mResults = new ArrayList<>();
    private Callback callback;

    public HttpGet(Callback callback, String url) {
        this.callback = callback;
        this.url = url;
    }

    public void getData() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;

                if (params == null) {
                    return null;
                }

                try {
                    URL url = new URL(params[0]);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();

                    StringBuilder stringBuilder = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }

                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("results");

                    for (int i = 0; i<results.length(); i++) {
                        JSONObject toilet = results.getJSONObject(i);
                        String name = toilet.getString("name");
                        String address = toilet.getString("address");
                        String distance = toilet.getString("distance");
                        Results resultsToilet = new Results(name, address, distance);
                        mResults.add(resultsToilet);
                    }

                    Toilet toilet = new Toilet(mResults);
                    callback.success(toilet);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(url);
    }
}