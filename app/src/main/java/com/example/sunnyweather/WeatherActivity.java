package com.example.sunnyweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sunnyweather.gson.Forecast;
import com.example.sunnyweather.gson.Weather;
import com.example.sunnyweather.util.HttpUtil;
import com.example.sunnyweather.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private	TextView	titleUpdateTime;
    private	TextView	degreeText;
    private	TextView	weatherInfoText;
    private LinearLayout forecastLayout;
    private	TextView	aqiText;
    private	TextView	pm25Text;
    private	TextView	comfortText;
    private	TextView	carWashText;
    private	TextView	sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weatherLayout	=	(ScrollView)	findViewById(R.id.weather_layout);
        titleCity	=	(TextView)	findViewById(R.id.title_city);
        titleUpdateTime	=	(TextView)	findViewById(R.id.title_update_time);
        degreeText	=	(TextView)	findViewById(R.id.degree_text);
        weatherInfoText	=	(TextView)	findViewById(R.id.weather_info_text);
        forecastLayout	=	(LinearLayout)	findViewById(R.id.forecast_layout);
        aqiText	=	(TextView)	findViewById(R.id.aqi_text);
        pm25Text	=	(TextView)	findViewById(R.id.pm25_text);
        comfortText	=	(TextView)	findViewById(R.id.comfort_text);
        carWashText	=	(TextView)	findViewById(R.id.car_wash_text);
        sportText	=	(TextView)	findViewById(R.id.sport_text);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        if (weatherString!=null){
            //有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            //无缓存时去服务器查询天气
            String	weatherId	=	getIntent().getStringExtra("weather_id");   //第一次肯定是没有缓存的，因 此就会从Intent中取出天气id，并调用requestWeather()方法来从服务器请求天气数据。
            weatherLayout.setVisibility(View.INVISIBLE);    //注 意，请求数据的时候先将ScrollView进行隐藏，不然空数据的界面看上去会很奇怪。
            requestWeather(weatherId);
        }

    }

//    根据天气id请求城市天气信息
    public	void	requestWeather(final	String	weatherId)	{
        Log.d("TAG","~~~~~~~~~~~~");
        String	weatherUrl	=	"http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherId, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,	"获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,	"获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


//    处理并展示Weather实体类中的数据
    private	void	showWeatherInfo(Weather	weather)	{
        String	cityName	=	weather.basic.cityName;
        String	updateTime	=	weather.basic.update.updateTime.split("	")[1];
        String	degree	=	weather.now.temperature	+	"℃";
        String	weatherInfo	=	weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();						//移除其他所有子视图
//        removeAllViews()和removeAllViewsInLayout()都有移除子view的功能。
//        removeAllViewsInLayout() 需要先测量当前的布局, 一旦调用该方法,只能移除已经自身布局中已计算好的所包含的子view
//        removeAllViews() 也调用了removeAllViewsInLayout(), 但是后面还调用了requestLayout(),  这个方法是当View的布局发生改变会调用它来更新当前视图, 移除子View会更加彻底。
        for	(Forecast forecast	:	weather.forecastList)	{
            View	view	=	LayoutInflater.from(this).inflate(R.layout.forecast_item,	forecastLayout,	false);
            TextView	dateText	=	(TextView)	view.findViewById(R.id.date_text);
            TextView	infoText	=	(TextView)	view.findViewById(R.id.info_text);
            TextView	maxText	=	(TextView)	view.findViewById(R.id.max_text);
            TextView	minText	=	(TextView)	view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if	(weather.aqi	!=	null)	{
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String	comfort	=	"舒适度："	+	weather.suggestion.comfort.info;
        String	carWash	=	"洗车指数："	+	weather.suggestion.carWash.info;
        String	sport	=	"运动建议："	+	weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}





















