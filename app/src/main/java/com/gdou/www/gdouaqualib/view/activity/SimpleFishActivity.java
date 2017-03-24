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
import com.gdou.www.gdouaqualib.utils.GsonUtil;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.MessageEvent;
import com.gdou.www.gdouaqualib.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Set;
//毒鱼类
public class SimpleFishActivity extends AppCompatActivity implements View.OnTouchListener{
    LinearLayout fish_shj,fish_td,fish_luandu,fish_dd,fish_xqd,fish_gd,fish_zand,fish_sqd,fish_zsd;
    public Map<String, Object> map;
    public Map<String,Object> mMap;
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


        mMap = GsonUtil.toMap(GsonUtil
                .parseJson(netWorkMap.getInstance().getMapTree().get("海洋有毒鱼类").toString()));
        mMap = GsonUtil.toMap(GsonUtil
                .parseJson(mMap.get("毒鱼类").toString()));


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
                Map<String, Object> map1;
                MLog.d("点击了该事件" + layout.getTag());
                layout.startAnimation(animDwon);
                animUp.setFillAfter(true);
                String key = v.getTag().toString();
                switch(key) {
                    case "fish_shj":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("珊瑚礁毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "珊瑚礁毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;

                    case "fish_td":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("豚毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "豚毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_luandu":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("卵毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "卵毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_dd":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("胆毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "胆毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_xqd":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("血清毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "血清毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_gd":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("肝毒鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "肝毒鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_zand":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("易生成组胺毒鱼类（鲭毒鱼类）").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "易生成组胺毒鱼类（鲭毒鱼类）");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_sqd":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("蛇鲭毒鱼类（含蜡脂鱼类）").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "蛇鲭毒鱼类（含蜡脂鱼类）");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
                        } else {
                            ToastUtil.show(SimpleFishActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "fish_zsd":
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("含真鲨毒素鱼类").toString()));
                        if (map1 != null) {
                            //开启一个activity
                            Intent intent = new Intent(SimpleFishActivity.this, ParticularActivity.class);
                            intent.putExtra("flag", 2);
                            intent.putExtra("title", "含真鲨毒素鱼类");
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SimpleFishActivity.this).toBundle());
                            EventBus.getDefault().postSticky(new MessageEvent(map1));
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