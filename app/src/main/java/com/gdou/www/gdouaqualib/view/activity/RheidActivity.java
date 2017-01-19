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
 * 有毒软体动物
 * 包括 概述+外源性+内源性
 */
public class RheidActivity extends AppCompatActivity implements View.OnTouchListener{
    private LinearLayout rt_wai,rt_nei,rt_gaishu;

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
        setContentView(R.layout.activity_rheid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("有毒软体动物");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rt_wai = (LinearLayout)findViewById(R.id.ruanti_wai);
        rt_nei = (LinearLayout)findViewById(R.id.ruanti_nei);
        rt_gaishu = (LinearLayout)findViewById(R.id.ruanti_wai_gaishu);

        rt_wai.setOnTouchListener(this);
        rt_nei.setOnTouchListener(this);
        rt_gaishu.setOnTouchListener(this);

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
                switch (key){
                    case "ruanti_wai":
                        Intent intent4 = new Intent(RheidActivity.this,RheidExogenousActivity.class);
                        intent4.putExtra("flag", 2);
                        startActivity(intent4,
                                ActivityOptions.makeSceneTransitionAnimation(RheidActivity.this).toBundle());
                        break;
                    case "ruanti_wai_gaishu":
                        if (set.contains("有毒海洋软体动物概述")) {
                            String burl = map.get("有毒海洋软体动物概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "有毒软体动物");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(RheidActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "ruanti_nei":
                        if (set.contains("内源性毒素贝类")) {
                            String burl = map.get("内源性毒素贝类").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(RheidActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "内源性毒素");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(RheidActivity.this)
                                            .toBundle());
                        } else {
                            ToastUtil.show(RheidActivity.this, "服务器出问题，请稍候...");
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
