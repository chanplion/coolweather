package com.xiaoxuan.coolweather.mvp.model;

import com.xiaoxuan.coolweather.common.Common;
import com.xiaoxuan.coolweather.mvp.model.IWeatherModel;
import com.xiaoxuan.coolweather.util.HttpUtil;

import okhttp3.Callback;

/**
 * Created by xiaoxuan on 2016/12/21 0021.
 */

public class WeatherModel implements IWeatherModel {

    @Override
    public void requestWeather(String weatherId, Callback callback) {
        HttpUtil.sendOkHttpRequest(Common.weatherUrlHead + weatherId + Common.weatherUrlRail, callback);
    }

    @Override
    public void loadBackground(String requestBgImg, Callback callback) {
        HttpUtil.sendOkHttpRequest(requestBgImg, callback);
    }
}
