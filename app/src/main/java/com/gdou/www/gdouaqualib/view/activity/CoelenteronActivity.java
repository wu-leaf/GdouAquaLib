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
import com.gdou.www.gdouaqualib.entity.netWorkMap;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.GsonUtil;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

//腔肠动物
public class CoelenteronActivity extends AppCompatActivity implements View.OnTouchListener{
    private LinearLayout qc_sm,qc_sh,qc_sx;
    public Map<String,Object> mMap;
    public Set<String> mSet;
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
        setContentView(R.layout.activity_coelenteron);

        //EventBus.getDefault().register(this);//注册事件

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("有毒腔肠动物");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qc_sm = (LinearLayout)findViewById(R.id.qc_sm);
        qc_sh = (LinearLayout)findViewById(R.id.qc_sh);
        qc_sx = (LinearLayout)findViewById(R.id.qc_sx);

        qc_sx.setOnTouchListener(this);
        qc_sh.setOnTouchListener(this);
        qc_sm.setOnTouchListener(this);

        //app = (MyApplication)getApplication();

        mMap = GsonUtil.toMap(GsonUtil
                .parseJson(netWorkMap.getInstance().getMapTree().get("海洋有毒腔肠动物").toString()));


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
            /**
             * 思路：在开启的新页面，遍历map，把里面的keyset转成数组，填充到适配器，
             * 然后 控件设置适配器，还有点击事件。
             */
                switch (key){
                    case "qc_sm":
                        Map<String,Object> map1;
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("钵水母纲").toString()));
                      //  Log.e("TAG","钵水母纲："+ map1.toString());
                        //Log.e("TAG", "钵水母纲：" + map1.keySet().toString());


                        //开启一个activity
                        Intent intent = new Intent(CoelenteronActivity.this,ParticularActivity.class);
                        intent.putExtra("flag",2);
                        intent.putExtra("title","钵水母纲");
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CoelenteronActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map1));
                        break;
                    case "qc_sh":
                        Map<String,Object> map2;
                        map2 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("珊瑚虫纲").toString()));
                        //  Log.e("TAG","钵水母纲："+ map1.toString());
                        //Log.e("TAG", "钵水母纲：" + map1.keySet().toString());
                        //开启一个activity
                        Intent intent2 = new Intent(CoelenteronActivity.this,ParticularActivity.class);
                        intent2.putExtra("flag",2);
                        intent2.putExtra("title","珊瑚虫纲");
                        startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(CoelenteronActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map2));
                        break;
                    case "qc_sx":
                        Map<String,Object> map3;
                        map3 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("水螅虫纲").toString()));
                        //  Log.e("TAG","钵水母纲："+ map1.toString());
                        //Log.e("TAG", "钵水母纲：" + map1.keySet().toString());
                        //开启一个activity
                        Intent intent3 = new Intent(CoelenteronActivity.this,ParticularActivity.class);
                        intent3.putExtra("flag",2);
                        intent3.putExtra("title","水螅虫纲");
                        startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(CoelenteronActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map3));
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
       // EventBus.getDefault().unregister(this);
        ActivityCollector.removeActivity(this);
    }
}

