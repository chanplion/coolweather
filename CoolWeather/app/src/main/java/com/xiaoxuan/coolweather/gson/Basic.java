package com.xiaoxuan.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxuan on 2016/12/13 0013.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
}
