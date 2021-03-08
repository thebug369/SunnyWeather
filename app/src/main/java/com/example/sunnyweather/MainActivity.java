package com.example.sunnyweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs	=	PreferenceManager.getDefaultSharedPreferences(this);
        if	(prefs.getString("weather",	null)	!=	null)	{           //缓存数据的判断  getString里的null为默认值，即一开始什么都不存在所使用的默认值，P220
            Intent intent	=	new	Intent(this,	WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

//    在onCreate()	方法的一开始先从SharedPreferences文件中读取缓存数据，如果 不为null	就说明之前已经请求过天气数据了，
//    那么就没必要让用户再次选择城市，而是直接跳 转到WeatherActivity即可。