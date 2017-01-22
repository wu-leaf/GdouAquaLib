package com.gdou.www.gdouaqualib.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.gdou.www.gdouaqualib.MainActivity;
import com.gdou.www.gdouaqualib.MyApplication;
import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.entity.netWorkMap;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.Constants;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.ToastUtil;

import java.util.Map;
import java.util.Set;

public class SimpleFishActivity extends AppCompatActivity implements View.OnTouchListener{
    LinearLayout fish_shj,fish_td,fish_luandu,fish_dd,fish_xqd,fish_gd,fish_zand,fish_sqd,fish_zsd;
    public Map<String, Object> map;
    public Set<String> set;
   // private MyApplication app;

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
        setContentView(R.layout.activity_simple_fish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一般有毒鱼类");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fish_shj = (LinearLayout)findViewById(R.id.fish_shj);
        fish_td = (LinearLayout)findViewById(R.id.fish_td);
        fish_luandu = (LinearLayout)findViewById(R.id.fish_luandu);
        fish_dd = (LinearLayout)findViewById(R.id.fish_dd);
        fish_xqd = (LinearLayout)findViewById(R.id.fish_xqd);
        fish_gd = (LinearLayout)findViewById(R.id.fish_gd);
        fish_zand = (LinearLayout)findViewById(R.id.fish_zand);
        fish_sqd = (LinearLayout)findViewById(R.id.fish_sqd);
        fish_zsd = (LinearLayout)findViewById(R.id.fish_zsd);

        fish_shj.setOnTouchListener(this);
        fish_td.setOnTouchListener(this);
        fish_luandu.setOnTouchListener(this);
        fish_dd.setOnTouchListener(this);
        fish_xqd.setOnTouchListener(this);
        fish_gd.setOnTouchListener(this);
        fish_zand.setOnTouchListener(this);
        fish_sqd.setOnTouchListener(this);
        fish_zsd.setOnTouchListener(this);

       // app = (MyApplication)getApplication();

        map = netWorkMap.getInstance().getMapList();
        set = map.keySet();

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
    public boolean onTouch(View v, MotionEvent event) {
        //设置Animation
        Animation animDwon = AnimationUtils.loadAnimation(this, R.anim.show_down);
        Animation animUp = AnimationUtils.loadAnimation(this, R.anim.show_up);

        LinearLayout layout = (LinearLayout) v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //layout.startAnimation(animDwon);
                //animDwon.setFillAfter(true);
                break;

            case MotionEvent.ACTION_UP:
                MLog.d("点击了该事件" + layout.getTag());
                layout.startAnimation(animDwon);
                animUp.setFillAfter(true);
                String key = v.getTag().toString();
                switch(key) {
                    case "fish_shj":
                        if (set.contains("珊瑚瞧毒鱼概述")) {
                            String burl = map.get("珊瑚瞧毒鱼概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "珊瑚瞧毒鱼");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;

                    case "fish_td":
                        if (set.contains("鲀毒鱼类概述")) {
                            String burl = map.get("鲀毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "鲀毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                         break;
                    case "fish_luandu":
                        if (set.contains("卵毒鱼类概述")) {
                            String burl = map.get("卵毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "卵毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_dd":
                        if (set.contains("胆毒鱼类概述")) {
                            String burl = map.get("胆毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "胆毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_xqd":
                        if (set.contains("血清毒鱼类概述")) {
                            String burl = map.get("血清毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "血清毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_gd":
                        if (set.contains("肝毒鱼类概述")) {
                            String burl = map.get("肝毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "肝毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_zand":
                        if (set.contains("鲭毒鱼类概述")) {
                            String burl = map.get("鲭毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "鲭毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_sqd":
                        if (set.contains("蛇鲭毒鱼类概述")) {
                            String burl = map.get("蛇鲭毒鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "蛇鲭毒鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_zsd":
                        if (set.contains("含真鲨毒素鱼类概述")) {
                            String burl = map.get("含真鲨毒素鱼类概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(SimpleFishActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "含真鲨毒素鱼类");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;

                }


                break;
            case MotionEvent.ACTION_MOVE:
                //layout.startAnimation(animUp);
                // animUp.setFillAfter(true);
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                //layout.startAnimation(animUp);
                //animUp.setFillAfter(true);
                break;
        }
        return true;    //这时必须返回true，不然 MotionEvent.ACTION_UP 没效果
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}