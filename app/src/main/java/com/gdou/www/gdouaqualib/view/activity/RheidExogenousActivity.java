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

import com.gdou.www.gdouaqualib.MyApplication;
import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.Constants;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.ToastUtil;

import java.util.Map;
import java.util.Set;


/**
 * 外源性有毒软体动物
 */
public class RheidExogenousActivity extends AppCompatActivity implements View.OnTouchListener{
    private LinearLayout wai_mabi,wai_fuxie,wai_lostmemory,wai_nerve;
    public Map<String, Object> map;
    public Set<String> set;
    private MyApplication app;

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
        setContentView(R.layout.activity_rheid_exogenous);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("外源性有毒软体动物");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wai_mabi = (LinearLayout)findViewById(R.id.wai_mabi);
        wai_fuxie = (LinearLayout)findViewById(R.id.wai_fuxie);
        wai_lostmemory = (LinearLayout)findViewById(R.id.wai_lost_memory);
        wai_nerve = (LinearLayout)findViewById(R.id.wai_nerve);

        wai_mabi.setOnTouchListener(this);
        wai_fuxie.setOnTouchListener(this);
        wai_lostmemory.setOnTouchListener(this);
        wai_nerve.setOnTouchListener(this);

        app = (MyApplication)getApplication();
        map = app.getMap();
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
                switch(key){
                    case "wai_mabi":
                        if (set.contains("麻痹性毒素贝类")){
                            String burl = map.get("麻痹性毒素贝类").toString().replace("\"","");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidExogenousActivity.this,DetailsActivity.class);
                            intent0.putExtra("flag",0);
                            intent0.putExtra("title","麻痹性毒素");
                            intent0.putExtra("url", Constants.AURL+burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidExogenousActivity.this)
                                            .toBundle());
                        }else{
                            ToastUtil.show(RheidExogenousActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "wai_fuxie":
                        if (set.contains("腹泻性毒素贝类")){
                            String burl = map.get("腹泻性毒素贝类").toString().replace("\"","");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidExogenousActivity.this,DetailsActivity.class);
                            intent0.putExtra("flag",0);
                            intent0.putExtra("title","腹泻性毒素");
                            intent0.putExtra("url", Constants.AURL+burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidExogenousActivity.this)
                                            .toBundle());
                        }else{
                            ToastUtil.show(RheidExogenousActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "wai_lost_memory":
                        if (set.contains("记忆丧失毒素贝类")){
                            String burl = map.get("记忆丧失毒素贝类").toString().replace("\"","");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidExogenousActivity.this,DetailsActivity.class);
                            intent0.putExtra("flag",0);
                            intent0.putExtra("title","记忆丧失毒素");
                            intent0.putExtra("url", Constants.AURL+burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidExogenousActivity.this)
                                            .toBundle());
                        }else{
                            ToastUtil.show(RheidExogenousActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "wai_nerve":
                        if (set.contains("神经性毒素贝类")){
                            String burl = map.get("神经性毒素贝类").toString().replace("\"","");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidExogenousActivity.this,DetailsActivity.class);
                            intent0.putExtra("flag",0);
                            intent0.putExtra("title","神经性毒素");
                            intent0.putExtra("url", Constants.AURL+burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidExogenousActivity.this)
                                            .toBundle());
                        }else{
                            ToastUtil.show(RheidExogenousActivity.this, "服务器出问题，请稍候...");
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