package com.xiaoxuan.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xiaoxuan on 2016/12/10 0010.
 * 网络工具
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
