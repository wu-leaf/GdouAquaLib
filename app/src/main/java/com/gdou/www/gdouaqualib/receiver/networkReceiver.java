package com.gdou.www.gdouaqualib.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gdou.www.gdouaqualib.MyApplication;
import com.gdou.www.gdouaqualib.entity.netWorkMap;
import com.gdou.www.gdouaqualib.utils.GsonUtil;
import com.gdou.www.gdouaqualib.utils.MessageEvent;
import com.gdou.www.gdouaqualib.utils.RefreshMapEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * Created by Veyron on 2017/1/22.
 * Function：监听网络状态
 */
public class networkReceiver extends BroadcastReceiver {
    public Map<String, Object> map;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
       // NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();

        if (activeInfo == null){
            Log.e("TAG","没网络");


        }else if (activeInfo != null){
            Log.e("TAG", "有网络");
            /**
             * 当监听到网络状态恢复后，重新加载一次两个map对象
             */
            refreshMapList();
            refreshMapTree();
            EventBus.getDefault().post(new RefreshMapEvent(true));//发送更新map消息
        }

    }

    private void refreshMapTree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://123.207.126.233/fish/GetAllList?check=EXAA";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析
                        map = GsonUtil.toMap(GsonUtil.parseJson(s));
                        Log.e("TAG", "refreshMapTree" + map.toString());
                        netWorkMap.getInstance().setMapTree(map);
                        Log.e("TAG", "refreshMapTree" + netWorkMap.getInstance().getMapTree().toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
                request.setTag("GetUrlTree");
                MyApplication.getHttpQueues().add(request);
            }
        }).start();
    }

    private void refreshMapList() {
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
                        Log.e("TAG", "refreshMapList"+map.toString());
                        netWorkMap.getInstance().setMapList(map);
                        Log.e("TAG", "refreshMapList" + netWorkMap.getInstance().getMapList().toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
                request.setTag("GetUrlList");
                MyApplication.getHttpQueues().add(request);
            }
        }).start();
    }
}
