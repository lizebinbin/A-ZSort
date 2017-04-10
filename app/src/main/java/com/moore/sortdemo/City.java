package com.moore.sortdemo;

import android.support.annotation.NonNull;

/**
 * Created by MooreLi on 2017/4/7.
 */

public class City implements Comparable<City>{
    private String cityName;
    private String firstLetter;
    private String cityPinYin;

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getCityPinYin() {
        return cityPinYin;
    }

    public void setCityPinYin(String cityPinYin) {
        this.cityPinYin = cityPinYin;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull City o) {
        return this.cityPinYin.compareTo(o.getCityPinYin());
    }
}
