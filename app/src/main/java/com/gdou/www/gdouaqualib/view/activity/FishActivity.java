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

public class FishActivity extends AppCompatActivity implements View.OnTouchListener {
    private LinearLayout duyulei,ciduyulei,pifuyulei;
    public Map<String, Object> mMap;
    public Map<String, Object> map;
    public Set<String> set;


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
        setContentView(R.layout.activity_fish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("有毒鱼类");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        duyulei = (LinearLayout)findViewById(R.id.duyulei);
        ciduyulei = (LinearLayout)findViewById(R.id.ciduyulei);
        pifuyulei = (LinearLayout)findViewById(R.id.pifu_ny_duyulei);

        pifuyulei.setOnTouchListener(this);
        duyulei.setOnTouchListener(this);
        ciduyulei.setOnTouchListener(this);

       // app = (MyApplication)getApplication();

        map = netWorkMap.getInstance().getMapList();
        set = map.keySet();


        mMap = GsonUtil.toMap(GsonUtil
                .parseJson(netWorkMap.getInstance().getMapTree().get("海洋有毒鱼类").toString()));


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
                if (key.contentEquals("duyulei")){
                    Intent intent = new Intent(FishActivity.this,SimpleFishActivity.class);
                    intent.putExtra("flag",1);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(FishActivity.this).toBundle());
                }else if (key.contentEquals("ciduyulei")){
                     Intent intent = new Intent(FishActivity.this,ThornFishAcitvity.class);
                     intent.putExtra("flag",1);
                     startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(FishActivity.this).toBundle());
                } else if(key.contentEquals("pifu_ny_duyulei")){
                    Map<String,Object> map1;
                    map1 = GsonUtil.toMap(GsonUtil
                            .parseJson(mMap.get("皮肤粘液毒鱼类").toString()));
                        //开启一个activity
                        Intent intent = new Intent(FishActivity.this,ParticularActivity.class);
                        intent.putExtra("flag",2);
                        intent.putExtra("title","皮肤粘液毒鱼类");
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(FishActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map1));
                    }else{
                        ToastUtil.show(FishActivity.this, "服务器出问题，请稍候...");
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
