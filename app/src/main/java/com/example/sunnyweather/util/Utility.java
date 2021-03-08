package com.example.sunnyweather.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sunnyweather.db.City;
import com.example.sunnyweather.db.County;
import com.example.sunnyweather.db.Province;
import com.example.sunnyweather.gson.Weather;
import com.google.gson.Gson;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {                   //TextUtils不能通过new来创建，它的方法都是static类型的，可以直接调用，
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
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
     * 解析和处理服务器返回的市级数据
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
     * 解析和处理服务器返回的县级数据
     */

    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCountries = new JSONArray(response);
                for (int i = 0; i < allCountries.length(); i++) {
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countryObject.getString("name"));
                    county.setWeatherId(countryObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


//    提供了handleProvincesResponse()	、handleCitiesResponse() 、handleCountiesResponse()	这3个方法，
//    分别用于解析和处理服务器返回的省级、市级 和县级数据。处理的方式都是类似的，先使用JSONArray和JSONObject将数据解析出来，
//    然后组 装成实体类对象，再调用save()	方法将数据存储到数据库当中。




    public	static Weather handleWeatherResponse(String	response)	{
        try	{
            JSONObject	jsonObject	=	new	JSONObject(response);
            JSONArray	jsonArray	=	jsonObject.getJSONArray("HeWeather");
            String	weatherContent	=	jsonArray.getJSONObject(0).toString();
            return	new Gson().fromJson(weatherContent,	Weather.class); //只需要通过调用fromJson()方法就能直接将JSON数据转换成Weather对象了
        }	catch	(Exception	e)	{
            e.printStackTrace();
        }
        return	null;
    }


}