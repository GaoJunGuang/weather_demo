package com.gjg.learn.weather_demo;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * 作者： ${高俊光}
 * 时间： 2016/7/19   22：49.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this,"7f95bf87342d58243e5a5ce0bfda6b1b");
    }
}
