package com.gdou.www.gdouaqualib;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gdou.www.gdouaqualib.utils.GsonUtil;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Veyron on 2017/1/14
 * Function：
 */
public class MyApplication extends Application{
    private static RequestQueue queues ;
    private int value;

    public Map<String, Object> getMap() {
        return map;
    }
    public Map<String, Object> getMap2() {
        return map2;
    }


    public void setMap2(Map<String, Object> map2) {
        this.map2 = map2;
    }
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


    public Map<String, Object> map;
    public Map<String, Object> map2;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        setValue(0);
        Log.e("TAG", "MyApplication");
        getJsonForArticleUrl();
        getJsonAllListUrl();
    }

    public void getJsonAllListUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://123.207.126.233/fish/GetAllList?check=EXAA";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析
                        map2 = GsonUtil.toMap(GsonUtil.parseJson(s));
                        Log.e("TAG", "Map...123" + map2.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
                request.setTag("GetUrl");
                MyApplication.getHttpQueues().add(request);
            }
        }).start();
    }

    public void getJsonForArticleUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://123.207.126.233/fish/GetAllTable?check=EXAA";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析
                        map = GsonUtil.toMap(GsonUtil.parseJson(s));
                        Log.e("TAG", "Map...mmm"+map.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
                request.setTag("GetUrl");
                MyApplication.getHttpQueues().add(request);
            }
        }).start();
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }




}

