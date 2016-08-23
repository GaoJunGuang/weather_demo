package com.gjg.learn.weather_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //weather_detail.xml
    private TextView txt_weather_detail_city;              //城市（可选择）
    private TextView txt_weather_detail_date;              //日期（可选择）
    private TextView txt_weather_detail_station;           //天气状况（如：晴天）
    private TextView txt_weather_detail_temperature;       //温度
    private TextView txt_weather_detail_windDir;           //风向
    private TextView txt_weather_detail_windSc;            //风力
    private TextView txt_weather_detail_week;              //星期（如：周五）
    private TextView txt_weather_detail_pm;                //PM2.5
    private TextView txt_weather_detail_airquality;        //空气质量
    private ImageView image_weather_detail_icon;           //天气状况图片

    //weather_day.xml
    //weather_day01
    private TextView txt_weather_day_week01;
    private TextView txt_weather_day_date01;
    private TextView txt_weather_day_temperature01;
    private TextView txt_weather_day_station01;
    private TextView txt_weather_day_windDir01;
    private TextView txt_weather_day_windSc01;
    private ImageView image_weather_day_icon01;

    //weather_day02
    private TextView txt_weather_day_week02;
    private TextView txt_weather_day_date02;
    private TextView txt_weather_day_temperature02;
    private TextView txt_weather_day_station02;
    private TextView txt_weather_day_windDir02;
    private TextView txt_weather_day_windSc02;
    private ImageView image_weather_day_icon02;

    //weather_day03
    private TextView txt_weather_day_week03;
    private TextView txt_weather_day_date03;
    private TextView txt_weather_day_temperature03;
    private TextView txt_weather_day_station03;
    private TextView txt_weather_day_windDir03;
    private TextView txt_weather_day_windSc03;
    private ImageView image_weather_day_icon03;

    //查询天气辅助工具类
    private WeatherAsynctaskSDK wasdk;

    //天气列表
    private List<CityWeather> listWeather  = new ArrayList<CityWeather>();
    //WeatherDB
    private WeatherDB weatherDB;

    //默认选择城市
    private String citySelected ;

    //是否是第一次进入程序
    private boolean isFirstIn=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化布局
        init();

        //获取天气信息
        getWeatherInfo();
    }

    /**
     * 初始化布局
     */
    private void init() {

        //weather_detail.xml
        txt_weather_detail_city = (TextView) findViewById(R.id.txt_weather_detail_city);
        txt_weather_detail_date = (TextView) findViewById(R.id.txt_weather_detail_date);
        image_weather_detail_icon = (ImageView) findViewById(R.id.image_weather_detail_icon);
        txt_weather_detail_pm = (TextView) findViewById(R.id.txt_weather_detail_pm);
        txt_weather_detail_station = (TextView) findViewById(R.id.txt_weather_detail_station);
        txt_weather_detail_temperature = (TextView) findViewById(R.id.txt_weather_detail_temperature);
        txt_weather_detail_week = (TextView) findViewById(R.id.txt_weather_detail_week);
        txt_weather_detail_windDir = (TextView) findViewById(R.id.txt_weather_detail_windDir);
        txt_weather_detail_windSc = (TextView) findViewById(R.id.txt_weather_detail_windSc);
        txt_weather_detail_airquality = (TextView) findViewById(R.id.txt_weather_detail_airquality);

        txt_weather_detail_date.setOnClickListener(this);
        txt_weather_detail_city.setOnClickListener(this);

        //weather_day.xml
        //weather_day_01
        LinearLayout layoutWeather01 = (LinearLayout) findViewById(R.id.weather_day01);
        txt_weather_day_date01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon01 = (ImageView) layoutWeather01.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station01 = (TextView) layoutWeather01.findViewById(R.id.txt_weather_day_station);

        //weather_day02
        LinearLayout layoutWeather02 = (LinearLayout) findViewById(R.id.weather_day02);
        txt_weather_day_date02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon02 = (ImageView) layoutWeather02.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station02 = (TextView) layoutWeather02.findViewById(R.id.txt_weather_day_station);

        //weather_day03
        LinearLayout layoutWeather03 = (LinearLayout) findViewById(R.id.weather_day03);
        txt_weather_day_date03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_date);
        txt_weather_day_week03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_week);
        image_weather_day_icon03 = (ImageView) layoutWeather03.findViewById(R.id.image_weather_day_icon);
        txt_weather_day_temperature03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_tempature);
        txt_weather_day_week03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_week);
        txt_weather_day_windDir03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_windDir);
        txt_weather_day_windSc03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_windSc);
        txt_weather_day_station03 = (TextView) layoutWeather03.findViewById(R.id.txt_weather_day_station);
    }



    public void getWeatherInfo() {
        weatherDB = WeatherDB.getInstance(MainActivity.this);


        citySelected= (String) txt_weather_detail_city.getText();

        requestHttp(citySelected);
       // Log.i("sdkdemo", "界面上显示的城市：" + citySelected);

       // int size = listWeather.size();
        //Log.i("sdkdemo", "WeatherActivity中获取的天气列表长度为：" + size);
        //第一次进入程序
       /* if(isFirstIn&&size==0){
            listWeather = wasdk.requestHttp(citySelected);
            isFirstIn=false;
        }*/

    }


    /**
     * @param city 请求的城市
     * @return 天气情况的list结合
     */
    public List<CityWeather> requestHttp(String city) {

        String httpUrl = "http://apis.baidu.com/heweather/weather/free";
        wasdk = new WeatherAsynctaskSDK(weatherDB,this);
        //参数
        Parameters para = new Parameters();
        para.put("city", city);

        /**
         * 接口的参数含义如下
         ApiStoreSDK.execute(
         String url, 						// 接口地址
         String method, 					// 接口方法
         Parameters params, 			// 接口参数
         ApiCallBack callBack) {		// 接口回调方法
         其中方法为”get”或”post”，推荐使用sdk提供的变量ApiStoreSDK.GET和ApiStoreSDK.POST.
         回调方法在UI线程中执行，可以直接修改UI。
         onError 调用时，statusCode参数值对应如下：
         -1 没有检测到当前网络；
         -3 没有进行初始化;
         -4 传参错误
         其他数值是http状态码，或服务器响应的errNum，请查阅响应字符串responseString

         */
        //weatherDB.deleteTable();
        ApiStoreSDK.execute(httpUrl, ApiStoreSDK.GET, para, new ApiCallBack() {
            @Override
            public void onSuccess(int status, String responseString) {
                //Log.i("sdkdemo", "onSuccess");

                //解析数据，获得天气信息，并保存
                wasdk.jsonResult(responseString);
                //Log.i("sdkdemo", responseString);

                listWeather = weatherDB.loadCityWeather();
                Log.i("sdkdemo", "requestHttp方法中：list的长度为--" + listWeather.size());

                //显示天气信息
                showWeatherInfo();

            }

            @Override
            public void onComplete() {
                Log.i("sdkdemo", "onComplete");



            }

            @Override
            public void onError(int status, String responseString, Exception e) {
                Log.i("sdkdemo", "onError, status: " + status);
                Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
                if (status == 1) {
                    Toast.makeText(MainActivity.this, "请检查网络是否可用", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "请确认输入的城市名是否正确", Toast.LENGTH_LONG).show();
                }
            }

        });



        return listWeather;
    }

    /**
     * 显示天气信息
     */
    private void showWeatherInfo() {



        if (listWeather.size() > 0) {

            //保证每次都是最新数据
            int count = listWeather.size();
            //weatherToday
            CityWeather weatherToday = listWeather.get(count-4);
            txt_weather_detail_date.setText(weatherToday.getDate());
            txt_weather_detail_week.setText(weatherToday.getWeek());
            txt_weather_detail_station.setText(weatherToday.getStation());
            txt_weather_detail_temperature.setText(weatherToday.getTemperature() + "℃");
            txt_weather_detail_windDir.setText(weatherToday.getWindDir());
            txt_weather_detail_windSc.setText(weatherToday.getWindSc() + "级");
            txt_weather_detail_pm.setText(weatherToday.getPM());
            txt_weather_detail_airquality.setText(weatherToday.getAirQlty());
            int iconId=selectWeatherIcon(weatherToday.getStation());
            image_weather_detail_icon.setImageResource(iconId);

           // Log.i("sdkdemo", "实际上显示的城市-->" + weatherToday.getCityName());

            CityWeather weatherForest01 = listWeather.get(count-3);
            txt_weather_day_date01.setText(weatherForest01.getDate());
            txt_weather_day_week01.setText(weatherForest01.getWeek());
            txt_weather_day_station01.setText(weatherForest01.getStation());
            txt_weather_day_temperature01.setText(weatherForest01.getTemperature() + "℃");
            txt_weather_day_windDir01.setText(weatherForest01.getWindDir());
            txt_weather_day_windSc01.setText(weatherForest01.getWindSc() + "级");
            int iconId1=selectWeatherIcon(weatherForest01.getStation());
            image_weather_day_icon01.setImageResource(iconId1);

            CityWeather weatherForest02 = listWeather.get(count-2);
            txt_weather_day_date02.setText(weatherForest02.getDate());
            txt_weather_day_week02.setText(weatherForest02.getWeek());
            txt_weather_day_station02.setText(weatherForest02.getStation());
            txt_weather_day_temperature02.setText(weatherForest02.getTemperature() + "℃");
            txt_weather_day_windDir02.setText(weatherForest02.getWindDir());
            txt_weather_day_windSc02.setText(weatherForest02.getWindSc() + "级");
            int iconId2=selectWeatherIcon(weatherForest02.getStation());
            image_weather_day_icon02.setImageResource(iconId2);

            CityWeather weatherForest03 = listWeather.get(count-1);
            txt_weather_day_date03.setText(weatherForest03.getDate());
            txt_weather_day_week03.setText(weatherForest03.getWeek());
            txt_weather_day_station03.setText(weatherForest03.getStation());
            txt_weather_day_temperature03.setText(weatherForest03.getTemperature() + "℃");
            txt_weather_day_windDir03.setText(weatherForest03.getWindDir());
            txt_weather_day_windSc03.setText(weatherForest03.getWindSc() + "级");
            int iconId3=selectWeatherIcon(weatherForest03.getStation());
            image_weather_day_icon03.setImageResource(iconId3);
        } else {
            Toast.makeText(this, "没有天气数据", Toast.LENGTH_SHORT).show();
        }

    }

    public int selectWeatherIcon(String strWeather){
        if(strWeather.contains("晴")){
            return R.drawable.ic_sunny_samll;
        }else if(strWeather.contains("多云")){
            return R.drawable.ic_cloudy_samll;
        }else if(strWeather.contains("浮尘")){
            return R.drawable.ic_dust_samll;
        }else if(strWeather.contains("霾")){
            return R.drawable.ic_haze_samll;
        }else if(strWeather.contains("雾")){
            return R.drawable.ic_fog_samll;
        }else if(strWeather.contains("暴雨")){
            return R.drawable.ic_heavyrain_samll;
        }else if(strWeather.contains("大雪")){
            return R.drawable.ic_heavysnow_samll;
        }else if(strWeather.contains("大雨")){
            return R.drawable.ic_moderraterain_samll;
        }else if(strWeather.contains("小雨")){
            return R.drawable.ic_lightrain_samll;
        }else if(strWeather.contains("阴")){
            return R.drawable.ic_overcast_samll;
        }else if(strWeather.contains("雨夹雪")){
            return R.drawable.ic_rainsnow_samll;
        }else if(strWeather.contains("沙尘暴")){
            return R.drawable.ic_sandstorm_samll;
        }else if(strWeather.equals("阵雨")){
            return R.drawable.ic_shower_samll;
        }else if(strWeather.equals("雷阵雨")){
            return R.drawable.ic_thundeshower_samll;
        }else if(strWeather.contains("中雨")){
            return R.drawable.ic_sleet_samll;
        }else {
            return R.drawable.ic_default_samll;
        }


    }




    /**
     * 城市选择按钮监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_weather_detail_city:             //打开城市选择对话框

                citySelectDialog();

                break;
            case R.id.txt_weather_detail_date:  //打开日期选择对话框（暂且定位到当前时间，有空再完善）
                break;
        }
    }

    /**
     * 城市选择对话框
     */
    //自定义对话框
    public void citySelectDialog() {

        //获取自定义布局
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.weather_city_select_dialog, null);

        //响应对话框中的点击事件
        // （注意，需要findViewById()之前需要加view，表明是自定义view布局中的响应事件）
        final EditText edit = (EditText) view.findViewById(R.id.edt_weather_city_select_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入您要选择的城市");
        //将自定义布局设置到对话框中
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                citySelected = edit.getText().toString();
                Log.i("test", "您选择的城市是：" + citySelected);
                if (!TextUtils.isEmpty(citySelected)) {

                    txt_weather_detail_city.setText(citySelected);
                    getWeatherInfo();

                }else{
                    Toast.makeText(MainActivity.this, "城市名不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "您没有选择新的城市", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                dialog.cancel();
            }
        });

        //所有关于alertdialog的设置，必须在show（）方法之前完成，才能有效果
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
