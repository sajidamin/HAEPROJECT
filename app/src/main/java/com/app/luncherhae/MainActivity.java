package com.app.luncherhae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextClock tClock;
    private TextView getBattery;
    RecyclerAdapter adapter;
    private RecyclerView mList;
    Article article;
    private LinearLayoutManager linearLayoutManager;
    private List<Article> movieList;
    Button icon_drawer_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBattery=findViewById(R.id.getBatteryTV);
        mList = findViewById(R.id.recyclerView);
        tClock =findViewById(R.id.simpleDigitalClock);
        icon_drawer_button=findViewById(R.id.icon_drawer);
        movieList= new ArrayList();
        new MyAsyncTasks().execute("beijing");
        new MyAsyncTasks().execute("berlin");
        new MyAsyncTasks().execute("cardiff");
        new MyAsyncTasks().execute("edinburgh");
        new MyAsyncTasks().execute("london");
        getBatteryPercentage(MainActivity.this);
        getBattery.setText(""+getBatteryPercentage(MainActivity.this));

        icon_drawer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,luncherActivity.class));
            }
        });
    }
    public int getBatteryPercentage(Context context) {

        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }
    class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String current ="";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://weather.bfsah.com/"+params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);
                    }
                    Log.d("this",""+current.length());
                    // return the data to onPostExecute method
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String response) {
            Log.d("this", response.toString());

            try {
                JSONObject mjsonObject;
                mjsonObject = new JSONObject(response);
                article = new Article();
                article.setCityCountry(mjsonObject.getString("country"));
                article.setCityDescription(mjsonObject.getString("description"));
                article.setCityName(mjsonObject.getString("city"));
                article.setCityTemperature(mjsonObject.getString("temperature"));
                movieList.add(article);
                adapter = new RecyclerAdapter(MainActivity.this, movieList);
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mList.setHasFixedSize(true);
                mList.setLayoutManager(linearLayoutManager);
                mList.setAdapter(adapter);
                mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
