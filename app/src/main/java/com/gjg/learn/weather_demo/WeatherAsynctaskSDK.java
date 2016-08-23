package com.gjg.learn.weather_demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 作者： ${高俊光}
 * 时间： 2016/7/18   23：32.
 */
public class WeatherAsynctaskSDK {


    private WeatherDB weatherDB;
    private Context mContext;


    public WeatherAsynctaskSDK(WeatherDB weatherDB, Context context) {
        this.weatherDB = weatherDB;
        this.mContext = context;
    }



    void initView(){

    }



    /**
     * 解析返回的json数据
     *
     * @param responseString
     * @return
     */
    public void jsonResult(String responseString) {

        CityWeather weatherToday = new CityWeather();
        try {

            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);

            String isOk = jsonObject1.getString("status");

            Log.i("sdkdemo", "http返回值：" + isOk);

            if (isOk.equals("ok")) {

                //今日天气状况
                weatherToday = todayWeather(jsonObject1);
                //将数据保存到数据表中
                weatherDB.saveCityWeather(weatherToday);

                //未来三天的天气状况
                JSONArray jsonArray1 = jsonObject1.getJSONArray("daily_forecast");
                for (int i = 1; i < 4; i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
                    CityWeather cityWeather = forestWeather(jsonObject2);

                    //将数据保存到数据表中
                    weatherDB.saveCityWeather(cityWeather);
                }
            } else {
                Toast.makeText(mContext, "请确保输入的城市名称是否正确", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今天的天气状况
     *
     * @param jsonObject1
     * @return
     * @throws JSONException
     */
    private CityWeather todayWeather(JSONObject jsonObject1) {

        CityWeather weatherToday = new CityWeather();
        try {
            //天气状况基本信息
            JSONObject jsonBasic = jsonObject1.getJSONObject("basic");

            //城市名称
            String city_name = jsonBasic.getString("city");
            //城市id
            String city_id = jsonBasic.getString("id");

            //更新时间
            JSONObject jsonDate = jsonBasic.getJSONObject("update");
            //当地日期
            String date = jsonDate.getString("loc").substring(0, 11);

            //周几
            String week = WeekFind(date);

            //将信息保存在CityWeather实例中
            weatherToday.setCityName(city_name);
            weatherToday.setCityID(city_id);
            weatherToday.setDate(date);
            weatherToday.setWeek(week);



            //实况天气
            JSONObject jsonNow = jsonObject1.getJSONObject("now");
            //天气状况
            JSONObject jsonStation = jsonNow.getJSONObject("cond");
            //天气状况代码
            String sta_code = jsonStation.getString("code");
            //天气状况描述
            String station = jsonStation.getString("txt");
            //Log.i("sdkdemo", "天气状况"+station);
            //温度
            String tempature = jsonNow.getString("tmp");
            //风力风向
            JSONObject jsonWind = jsonNow.getJSONObject("wind");
            //风向
            String windDir = jsonWind.getString("dir");
            //风力
            String windSc = jsonWind.getString("sc");
            weatherToday.setStation(station);
            weatherToday.setStationCode(sta_code);
            weatherToday.setTemperature(tempature);
            weatherToday.setWindDir(windDir);
            weatherToday.setWindSc(windSc);



            //空气质量，仅限国内部分城市
            JSONObject jsonAir = jsonObject1.getJSONObject("aqi");
            JSONObject jsonCity = jsonAir.getJSONObject("city");
            //PM2.5 1小时平均值(ug/m³)
            String pm = jsonCity.getString("pm25");
            //空气质量类别
            String airQ = jsonCity.getString("qlty");

            weatherToday.setPM(pm);
            weatherToday.setAirQlty(airQ);
            weatherToday.setIsToday(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherToday;
    }

    /**
     * 获取未来三天的天气状况
     *
     * @param jsonObject2
     * @return
     */
    public CityWeather forestWeather(JSONObject jsonObject2) {

        CityWeather weatherForest = new CityWeather();

        try {

            //日期
            String date = jsonObject2.getString("date");

            //周几
            String week = WeekFind(date);

            //天气状况（这里主要取白天）
            JSONObject jsonStation = jsonObject2.getJSONObject("cond");
            String sta_code = jsonStation.getString("code_d");
            String sta_txt = jsonStation.getString("txt_d");

            //温度
            JSONObject jsonTmp = jsonObject2.getJSONObject("tmp");
            String tmpMax = jsonTmp.getString("max");
            String tmpMin = jsonTmp.getString("min");

            //风力风向
            JSONObject jsonWind = jsonObject2.getJSONObject("wind");
            //风力
            String windSc = jsonWind.getString("sc");
            //风向
            String windDir = jsonWind.getString("dir");

            weatherForest.setIsToday(false);
            weatherForest.setDate(date);
            weatherForest.setWeek(week);
            weatherForest.setStation(sta_txt);
            weatherForest.setStationCode(sta_code);
            weatherForest.setTemperature(tmpMin + "~" + tmpMax);
            weatherForest.setWindSc(windSc);
            weatherForest.setWindDir(windDir);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherForest;
    }

    /**
     * 找出对应的周几
     *
     * @param dateTime
     * @return
     */
    public String WeekFind(String dateTime) {

        String week = "";

        //dateTime = dateTime.substring(0, 10);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //Log.i("sdkdemo", "Calendar"+c);
        try {
            //Date date=format.parse(dateTime);
            c.setTime(format.parse(dateTime));
            /*Log.i("sdkdemo", "-----@@@###----"+c);
            Log.i("sdkdemo", "-----###----"+Calendar.DAY_OF_WEEK);
            Log.i("sdkdemo", "---------"+c.get(Calendar.DAY_OF_WEEK));*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Log.i("sdkdemo", "今天是周日");        //周日
                week = "周日";
                break;
            case 2:                                  //周一
                Log.i("sdkdemo", "今天是周一");
                week = "周一";
                break;
            case 3:                                  //周二
                Log.i("sdkdemo", "今天是周二");
                week = "周二";
                break;
            case 4:                                  //周三
                Log.i("sdkdemo", "今天是周三");
                week = "周三";
                break;
            case 5:                                  //周四
                Log.i("sdkdemo", "今天是周四");
                week = "周四";
                break;
            case 6:                                  //周五
                Log.i("sdkdemo", "今天是周五");
                week = "周五";
                break;
            case 7:                                  //周六
                Log.i("sdkdemo", "今天是周六");
                week = "周六";
                break;
        }
        return week;
    }
}
