package com.xiaoxuan.coolweather.model;

/**
 * Created by xiaoxuan on 2016/12/10 0010.
 */

public class Province {

    private int id;
    private String proviceName;
    private int provinceCode;

    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
