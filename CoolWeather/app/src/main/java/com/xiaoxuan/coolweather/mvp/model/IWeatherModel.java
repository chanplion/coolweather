package com.xiaoxuan.coolweather.mvp.model;

import okhttp3.Callback;

/**
 * Created by xiaoxuan on 2016/12/21 0021.
 */

public interface IWeatherModel {

    void requestWeather(String weatherId, Callback callback);//请求天气数据

    void loadBackground(String requestBgImg, Callback callback);//加载背景图
}
