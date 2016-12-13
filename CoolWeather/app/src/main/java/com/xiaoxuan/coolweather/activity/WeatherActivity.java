package com.xiaoxuan.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxuan.coolweather.R;
import com.xiaoxuan.coolweather.gson.Forecast;
import com.xiaoxuan.coolweather.gson.Weather;
import com.xiaoxuan.coolweather.util.HttpUtil;
import com.xiaoxuan.coolweather.util.Utility;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaoxuan on 2016/12/13 0013.
 */

public class WeatherActivity extends AppCompatActivity {
    @Bind(R.id.title_city)
    TextView titleCity;
    @Bind(R.id.title_update_time)
    TextView titleUpdateTime;
    @Bind(R.id.text_degree)
    TextView textDegree;
    @Bind(R.id.text_weather_info)
    TextView textWeatherInfo;
    @Bind(R.id.llt_forecast)
    LinearLayout lltForecast;
    @Bind(R.id.txt_aqi)
    TextView txtAqi;
    @Bind(R.id.txt_pm25)
    TextView txtPm25;
    @Bind(R.id.text_comfort)
    TextView textComfort;
    @Bind(R.id.text_car_wash)
    TextView textCarWash;
    @Bind(R.id.text_sport)
    TextView textSport;
    @Bind(R.id.scrollview_weather)
    ScrollView scrollviewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            //直接解析缓存的天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //没有缓存从网上获取数据
            String weatherId = getIntent().getStringExtra("weather_id");
            scrollviewWeather.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WEATHER_SEND_ERROR", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        textDegree.setText(degree);
        textWeatherInfo.setText(weatherInfo);
        lltForecast.removeAllViews();

        for (Forecast forecast : weather.forecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_forecast, lltForecast, false);
            TextView textDate = (TextView) view.findViewById(R.id.text_date);
            TextView textInfo = (TextView) view.findViewById(R.id.text_info);
            TextView textMax = (TextView) view.findViewById(R.id.text_max);
            TextView textMin = (TextView) view.findViewById(R.id.text_min);
            textDate.setText(forecast.date);
            textInfo.setText(forecast.more.info);
            textMax.setText(forecast.temperature.max);
            textMin.setText(forecast.temperature.min);
            lltForecast.addView(view);
        }

        if (weather.aqi != null) {
            txtAqi.setText(weather.aqi.city.aqi);
            txtPm25.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        textComfort.setText(comfort);
        textCarWash.setText(carWash);
        textSport.setText(sport);
        scrollviewWeather.setVisibility(View.VISIBLE);
    }
}
