package com.xiaoxuan.coolweather.mvp.view;

import com.xiaoxuan.coolweather.gson.Weather;

import java.io.IOException;

/**
 * Created by xiaoxuan on 2016/12/21 0021.
 */

public interface IWeatherView {

    void errorWeatherInfo(); //请求天气数据失败

    void showWeatherInfo(Weather weather,String responseText);//显示数据

    void loadError(IOException e); //加载图片错误

    void loadBackground(String bgPic);//加载背景图

    void inVisibleScrollBar();//设置Scrollbar不可见

    void showWeatherView(Weather weather);//把数据显示到页面上
}
