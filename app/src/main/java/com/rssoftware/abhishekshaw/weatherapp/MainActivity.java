package com.rssoftware.abhishekshaw.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final String API_ENDPOINT_URL = "http://api.openweathermap.org/data/2.5/weather?q=[city]&appid=2de143494c0b295cca9337e1e96b00e0";

    EditText mEditTextCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextCity = (EditText) findViewById(R.id.editTextCity);
    }

    public void OnGetWeatherClick(View v){

         String cityName=mEditTextCity.getText().toString().trim();
         String requestURL=API_ENDPOINT_URL;
         requestURL = requestURL.replace("[city]",cityName);
         Log.i("info", requestURL);

        SharedPreferences pref = getSharedPreferences("SessionData", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("cityName", cityName);
        edit.commit();


        sendWeatherRequest(requestURL,cityName);
    }

    public void sendWeatherRequest(String requestURL,String cityName) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String requestURL=params[0];
                String cityName=params[1];

                HttpURLConnection conn=null;
                try {
                    URL url = new URL(requestURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    InputStream stream = conn.getInputStream();
                    // Put output stream into a String
                    InputStreamReader inputStreamReader = new InputStreamReader(stream);
                    BufferedReader br = new BufferedReader(inputStreamReader);
                    String result="";
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        result += line;
                    }
                    br.close();

                    System.out.println(result);
                    conn.disconnect();
                    return result;
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("result", result);
                callWeatherReportActivity(result);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(requestURL, cityName);
    }

    public void callWeatherReportFragment(String result){

        Fragment fragment = new WeatherReport();

        Bundle args=new Bundle();
        args.putString("result", result);

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rootContainer, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("SessionData", MODE_PRIVATE);
        String cityName = pref.getString("cityName","");
        mEditTextCity.setText(cityName);
    }

    public void callWeatherReportActivity(String result){

        Intent intent=new Intent(this,WeatherReportActivity.class);
        intent.putExtra("weatherJSON",result);
        startActivity(intent);

    }

}
