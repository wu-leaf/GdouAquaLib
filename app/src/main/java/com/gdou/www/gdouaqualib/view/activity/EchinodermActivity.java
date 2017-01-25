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

//棘皮动物
public class EchinodermActivity extends AppCompatActivity implements View.OnTouchListener{

    private LinearLayout jipi_hs,jipi_hd,jipi_hx,jipi_gaishu;
    public Map<String,Object> mMap;
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
        setContentView(R.layout.activity_echinoderm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("有毒棘皮动物");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jipi_hs = (LinearLayout)findViewById(R.id.jipi_hs);
        jipi_hd = (LinearLayout)findViewById(R.id.jipi_hd);
        jipi_hx = (LinearLayout)findViewById(R.id.jipi_hx);
        jipi_gaishu = (LinearLayout)findViewById(R.id.jipi_gaishu);

        jipi_hs.setOnTouchListener(this);
        jipi_hd.setOnTouchListener(this);
        jipi_hx.setOnTouchListener(this);
        jipi_gaishu.setOnTouchListener(this);

        mMap = GsonUtil.toMap(GsonUtil
                .parseJson(netWorkMap.getInstance().getMapTree().get("海洋有毒棘皮动物").toString()));
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
                    case "jipi_gaishu":
                        Map<String,Object> mapList;
                        mapList = netWorkMap.getInstance().getMapList();
                        if (mapList.keySet().contains("海洋有毒棘皮动物概述")){
                            String burl = mapList.get("海洋有毒棘皮动物概述").toString().replace("\"", "");
                            Log.e("TAG", burl);
                            Intent intent0 = new Intent(EchinodermActivity.this, DetailsActivity.class);
                            intent0.putExtra("flag", 0);
                            intent0.putExtra("title", "有毒棘皮动物");
                            intent0.putExtra("url", Constants.AURL + burl);
                            startActivity(intent0,
                                    ActivityOptions.makeSceneTransitionAnimation(EchinodermActivity.this)
                                            .toBundle());
                        }else{
                            ToastUtil.show(EchinodermActivity.this, "服务器出问题，请稍候...");
                        }
                        break;
                    case "jipi_hs":
                        Map<String,Object> map1;
                        map1 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("海参").toString()));
                        //开启一个activity
                        Intent intent = new Intent(EchinodermActivity.this,ParticularActivity.class);
                        intent.putExtra("flag",2);
                        intent.putExtra("title","海参");
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(EchinodermActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map1));
                        break;
                    case "jipi_hd":
                        Map<String,Object> map2;
                        map2 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("海胆").toString()));
                        //开启一个activity
                        Intent intent2 = new Intent(EchinodermActivity.this,ParticularActivity.class);
                        intent2.putExtra("flag",2);
                        intent2.putExtra("title","海胆");
                        startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(EchinodermActivity.this).toBundle());
                        EventBus.getDefault().postSticky(new MessageEvent(map2));
                        break;
                    case "jipi_hx":
                        Map<String,Object> map3;
                        map3 = GsonUtil.toMap(GsonUtil
                                .parseJson(mMap.get("海星").toString()));
                        //  Log.e("TAG","钵水母纲："+ map1.toString());
                        //Log.e("TAG", "钵水母纲：" + map1.keySet().toString());
                        //开启一个activity
                        Intent intent3 = new Intent(EchinodermActivity.this,ParticularActivity.class);
                        intent3.putExtra("flag",2);
                        intent3.putExtra("title","海星");
                        startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(EchinodermActivity.this).toBundle());
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
        ActivityCollector.removeActivity(this);
    }
}
