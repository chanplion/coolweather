package com.xiaoxuan.coolweather.mvp.presenter;

import com.xiaoxuan.coolweather.gson.Weather;

/**
 * Created by xiaoxuan on 2016/12/21 0021.
 */

public interface IWeatherPresenter {

     void requestWeather(String weatherId); //请求天气数据

     void loadBackground(String requestBgImg);//加载背景图

     Weather handleWeatherResponse(String response);//解析天气的Json数据

     String getCacheOrRequest(String weatherString, String initWeatherId);//加载天气页面先判断有无缓存，没有则进行网络请求
}
