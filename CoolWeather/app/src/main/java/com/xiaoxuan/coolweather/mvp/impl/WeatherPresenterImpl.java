package com.xiaoxuan.coolweather.mvp.impl;

import android.util.Log;

import com.xiaoxuan.coolweather.common.Common;
import com.xiaoxuan.coolweather.gson.Weather;
import com.xiaoxuan.coolweather.mvp.model.IWeatherModel;
import com.xiaoxuan.coolweather.mvp.presenter.IWeatherPresenter;
import com.xiaoxuan.coolweather.mvp.view.IWeatherView;
import com.xiaoxuan.coolweather.mvp.model.WeatherModel;
import com.xiaoxuan.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaoxuan on 2016/12/21 0021.
 */

public class WeatherPresenterImpl implements IWeatherPresenter {

    private IWeatherView iView;
    private IWeatherModel iModel;

    public WeatherPresenterImpl(IWeatherView iView) {
        this.iView = iView;
        iModel = new WeatherModel();
    }

    //请求天气数据
    @Override
    public void requestWeather(String weatherId) {
        iModel.requestWeather(weatherId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WEATHER_SEND_ERROR", e.getMessage());
                iView.errorWeatherInfo();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = handleWeatherResponse(responseText);
                iView.showWeatherInfo(weather, responseText);
                loadBackground(Common.requestBgImg);
            }
        });
    }

    //加载背景图
    @Override
    public void loadBackground(String requestBgImg) {
        iModel.loadBackground(requestBgImg, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iView.loadError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bgPic = response.body().string();
                iView.loadBackground(bgPic);
            }
        });
    }

    //解析天气的Json数据
    @Override
    public Weather handleWeatherResponse(String response) {
        return Utility.handleWeatherResponse(response);
    }

    //加载天气页面先判断有无缓存，没有则进行网络请求
    @Override
    public String getCacheOrRequest(String weatherString,String initWeatherId) {
        final String weatherId;
        if (weatherString != null) {
            //直接解析缓存的天气数据
            Weather weather = handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            iView.showWeatherView(weather);//加载缓存数据后显示到页面上
        } else {
            //没有缓存从网上获取数据
            iView.inVisibleScrollBar();
            weatherId = initWeatherId;//weatherId为初始化的id值
            requestWeather(weatherId);
        }
        return weatherId;
    }
}
