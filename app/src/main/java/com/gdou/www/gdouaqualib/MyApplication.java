package com.gdou.www.gdouaqualib;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gdou.www.gdouaqualib.entity.netWorkMap;
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



    public Map<String, Object> mapList;
    public Map<String, Object> mapTree;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        setValue(0);
        Log.e("TAG", "MyApplication");
        getJsonForArticleUrl();//mapList
        getJsonAllTreeUrl();//mapTree
    }

    public void getJsonAllTreeUrl() {//得到mapTree
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://123.207.126.233/fish/GetAllList?check=EXAA";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析
                        mapTree = GsonUtil.toMap(GsonUtil.parseJson(s));
                        Log.e("TAG", "Map...123" + mapTree.toString());
                        netWorkMap.getInstance().setMapTree(mapTree);
                        Log.e("TAG", "activeInfo_MapTree" + netWorkMap.getInstance().getMapTree().toString());
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

    public void getJsonForArticleUrl() {//得到mapList
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://123.207.126.233/fish/GetAllTable?check=EXAA";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析
                        mapList = GsonUtil.toMap(GsonUtil.parseJson(s));
                        Log.e("TAG", "Map...mmm"+mapList.toString());
                        netWorkMap.getInstance().setMapList(mapList);
                        Log.e("TAG", "activeInfo_MapList" + netWorkMap.getInstance().getMapList().toString());
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

