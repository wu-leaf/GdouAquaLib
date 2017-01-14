package com.gdou.www.gdouaqualib.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gdou.www.gdouaqualib.MyApplication;
import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.entity.version;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.ToastUtil;
import com.gdou.www.gdouaqualib.utils.VersionCheck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    version mVersion;
    int newVersionCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        int flag = getIntent().getExtras().getInt("flag");
        // 设置不同的动画效果
        switch (flag) {
            case 0:
                getWindow().setEnterTransition(new Explode());
                break;
            case 1:
                getWindow().setEnterTransition(new Slide());
                break;
            case 2:
                getWindow().setEnterTransition(new Fade());
                getWindow().setExitTransition(new Fade());
                break;
            case 3:
                break;
        }
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityCollector.addActivity(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void Check(View view){
        ToastUtil.show(SettingActivity.this, "检查更新");
        goToCheckNewVersion();
        Log.e("TAG", "Check");
        int now_VersionCode = VersionCheck.getNowVerCode(SettingActivity.this);
        Log.e("TAG", now_VersionCode + ";"+newVersionCode);
        if (VersionCheck.isNewVersion(newVersionCode,now_VersionCode)){
            Log.e("TAG", "可以更新");
            ToastUtil.show(SettingActivity.this,"正在更新");
            VersionCheck.goUpdate(SettingActivity.this);
        }else{
            ToastUtil.show(SettingActivity.this,"已是最新版本");
        }

    }

    private void goToCheckNewVersion() {
        //检查服务器的app版本号,解析这个
        // :http://each.ac.cn/atmosphere.json，获得versionCode
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://119.29.78.145/gdouaqualib.json";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("TAG", s);
                        List<version> versionList = parseJSONWithGson(s);
                        for (version ver : versionList){
                            if (ver != null){  //List里面只有一个ver
                                Log.e("TAG", ver.getVersionCode()+";"+ver.getApkUrl());
                                versionModel(ver);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
                request.setTag("testGet");
                MyApplication.getHttpQueues().add(request);
            }
        }).start();
    }

    private void versionModel(version ver) {
        mVersion = ver;
        Log.e("TAG", mVersion.getVersionCode() + "");
        newVersionCode = mVersion.getVersionCode();
    }

    private  List<version> parseJSONWithGson(String jsonData) {
        Gson gson = new Gson();
        List<version> versList = gson.fromJson(jsonData,
                new TypeToken<List<version>>() {}.getType());
        return versList;
    }


    public void Feedback(View view){
        ToastUtil.show(SettingActivity.this, "反馈");
    }
}
