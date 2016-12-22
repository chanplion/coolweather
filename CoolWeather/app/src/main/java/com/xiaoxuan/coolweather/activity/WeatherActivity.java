package com.xiaoxuan.coolweather.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaoxuan.coolweather.R;
import com.xiaoxuan.coolweather.common.Common;
import com.xiaoxuan.coolweather.gson.Forecast;
import com.xiaoxuan.coolweather.gson.Weather;
import com.xiaoxuan.coolweather.mvp.presenter.IWeatherPresenter;
import com.xiaoxuan.coolweather.mvp.view.IWeatherView;
import com.xiaoxuan.coolweather.mvp.impl.WeatherPresenterImpl;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiaoxuan on 2016/12/13 0013.
 */

public class WeatherActivity extends AppCompatActivity implements IWeatherView{
    @Bind(R.id.iv_bing_pic)
    ImageView ivBingPic;
    @Bind(R.id.drawerLayout)
    public DrawerLayout drawerLayout;
    @Bind(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.btn_select)
    Button btnSelect;
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

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private IWeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        loadBackground();//加载背景图
        combineBgWithStatusBar();//融合背景图和状态栏
        String weatherString = pref.getString("weather", null);
        final String initWeatherId = getIntent().getStringExtra("weather_id");//获取从选择区域得来的原始的weatherId值
        final String weatherId = presenter.getCacheOrRequest(weatherString, initWeatherId);//若已有缓存则直接读取，没有则通过原始的id值获取网络数据
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestWeather(weatherId);//刷新天气数据
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);//打开侧边栏菜单
            }
        });
    }

    //加载背景图,先读缓存,缓存没有则到网络去加载
    private void loadBackground() {
        presenter = new WeatherPresenterImpl(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);//初始化
        editor = pref.edit();
        String bgImg = pref.getString("bg_img", null);
        if (bgImg == null) {
            presenter.loadBackground(Common.requestBgImg);
        } else {
            Glide.with(this).load(bgImg).into(ivBingPic);
        }
    }

    //融合背景图和状态栏
    private void combineBgWithStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //网络获取背景图片后先缓存后显示
    @Override
    public void loadBackground(final String bgPic) {
        editor.putString("bg_img", bgPic);
        editor.apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(WeatherActivity.this).load(bgPic).into(ivBingPic);
            }
        });
    }

    //网络中的天气数据
    @Override
    public void showWeatherInfo(final Weather weather, final String responseText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weather != null && "ok".equals(weather.status)) {
                    editor.putString("weather", responseText);
                    editor.apply();
                    showWeatherView(weather);
                } else {
                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                }
                closeRefresh();
            }
        });
    }

    //请求数据失败
    @Override
    public void errorWeatherInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                closeRefresh();
            }
        });
    }

    //加载图片错误信息
    @Override
    public void loadError(IOException e) {
        Log.e("BingPicError", e.getMessage());
    }

    //关闭刷新
    public void closeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    //设置Scrollbar不可见
    @Override
    public void inVisibleScrollBar() {
        scrollviewWeather.setVisibility(View.INVISIBLE);
    }

    //把数据显示到页面上
    @Override
    public void showWeatherView(Weather weather) {
        titleCity.setText(weather.basic.cityName);//城市名称
        titleUpdateTime.setText(weather.basic.update.updateTime.split(" ")[1]);//刷新时间
        textDegree.setText(weather.now.temperature + "℃");//温度
        textWeatherInfo.setText(weather.now.more.info);//天气情况（多云、晴朗、阵雨）
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
        textComfort.setText(comfort);//舒适度
        textCarWash.setText(carWash);//洗车指数
        textSport.setText(sport);//运动建议
        scrollviewWeather.setVisibility(View.VISIBLE);
    }

}
