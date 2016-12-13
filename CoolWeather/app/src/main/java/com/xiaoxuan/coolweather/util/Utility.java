package com.xiaoxuan.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.xiaoxuan.coolweather.gson.Weather;
import com.xiaoxuan.coolweather.model.City;
import com.xiaoxuan.coolweather.model.County;
import com.xiaoxuan.coolweather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaoxuan on 2016/12/10 0010.
 * JSON解析
 */

public class Utility {

    /**
     * 解析省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allPronvices = new JSONArray(response);
                for (int i = 0; i < allPronvices.length(); i++) {
                    JSONObject provinceObject = allPronvices.getJSONObject(i);
                    Province province = new Province();
                    province.setProviceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析省市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析省县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountryName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 返回Weather类的Json数据
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject weatherObject = new JSONObject(response);
            JSONArray weatherArray = weatherObject.getJSONArray("HeWeather");
            String weatherString = weatherArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherString, Weather.class);
        } catch (Exception ex) {
            Log.e("WEATHER_ERROR", ex.getMessage());
        }
        return null;
    }
}
