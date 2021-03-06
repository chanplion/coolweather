package com.xiaoxuan.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxuan on 2016/12/13 0013.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }

}
