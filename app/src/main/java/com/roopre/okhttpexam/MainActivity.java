package com.roopre.okhttpexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Weather> weatherList = new ArrayList<>();
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    WeatherAdapter mAdapter;

    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new HttpAsyncTask().execute("https://goo.gl/eIXu9l");
    }

    class HttpAsyncTask extends AsyncTask<String, Void, String>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String strUrl = params[0];

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();
                Response response = client.newCall(request).execute();
                result = response.body().string();
                //Log.d(TAG, "doInBackground = "+result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            rv = findViewById(R.id.rv);
            linearLayoutManager = new LinearLayoutManager(MainActivity.this);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Weather>>() {}.getType();
            weatherList = gson.fromJson(s, listType);

            mAdapter = new WeatherAdapter(weatherList);
            rv.setLayoutManager(linearLayoutManager);
            rv.setAdapter(mAdapter);

//            try {
//                JSONArray jsonArray = new JSONArray(s);
//
//                for(int i=0;i<jsonArray.length();i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                    Log.d(TAG, jsonObject.optString("country"));
//
//                    String country = jsonObject.optString("country");
//                    String weather = jsonObject.optString("weather");
//                    String temperature = jsonObject.optString("temperature");
//
//                    Weather w = new Weather(country, weather, temperature);
//                    weatherList.add(w);
//                }
//                mAdapter = new WeatherAdapter(weatherList);
//
//                rv.setLayoutManager(linearLayoutManager);
//                rv.setAdapter(mAdapter);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }
    }
}
