package com.rssoftware.abhishekshaw.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherReportActivity extends AppCompatActivity {

    TextView mTempratureTextView;
    TextView mHumidityTextView;
    TextView mMainTextView;
    TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_report);

        String weatherJSON=getIntent().getStringExtra("weatherJSON");

        try {
            JSONObject weatherInfo=new JSONObject(weatherJSON);

            JSONObject mainWeatherObj=weatherInfo.getJSONObject("main");

            JSONArray weatherArr=weatherInfo.getJSONArray("weather");

            String temprature=mainWeatherObj.getString("temp");

            String humidity=mainWeatherObj.getString("humidity");


            mTempratureTextView = (TextView) findViewById(R.id.main_temp);

            mHumidityTextView = (TextView) findViewById(R.id.main_humidity);

            mHumidityTextView.setText("Humidity : "+ humidity + "%");
            mTempratureTextView.setText("Temprature : "+ temprature+ "F");

            mMainTextView = (TextView) findViewById(R.id.weather_main);
            mDescriptionTextView = (TextView) findViewById(R.id.weather_description);

            String mainText = weatherArr.getJSONObject(0).getString("main");

            String description = weatherArr.getJSONObject(0).getString("description");

            mMainTextView.setText("Main : " + mainText);
            mDescriptionTextView.setText("Description : " + description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("weatherJSON", weatherJSON);
    }

    public void onGoBackClick(View view){
        finish();
    }
}
