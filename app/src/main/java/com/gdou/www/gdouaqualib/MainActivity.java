package com.gdou.www.gdouaqualib;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gdou.www.gdouaqualib.entity.netWorkMap;
import com.gdou.www.gdouaqualib.entity.version;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.ChechNetwork;
import com.gdou.www.gdouaqualib.utils.Constants;
import com.gdou.www.gdouaqualib.utils.DensityUtil;
import com.gdou.www.gdouaqualib.utils.GsonUtil;
import com.gdou.www.gdouaqualib.utils.HtmlUtil;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.MessageEvent;
import com.gdou.www.gdouaqualib.utils.RefreshMapEvent;
import com.gdou.www.gdouaqualib.utils.ToastUtil;
import com.gdou.www.gdouaqualib.utils.VersionCheck;
import com.gdou.www.gdouaqualib.view.activity.AboutActivity;
import com.gdou.www.gdouaqualib.view.activity.CoelenteronActivity;
import com.gdou.www.gdouaqualib.view.activity.DetailsActivity;
import com.gdou.www.gdouaqualib.view.activity.EchinodermActivity;
import com.gdou.www.gdouaqualib.view.activity.FishActivity;
import com.gdou.www.gdouaqualib.view.activity.ParticularActivity;
import com.gdou.www.gdouaqualib.view.activity.RheidActivity;
import com.gdou.www.gdouaqualib.view.activity.SearchActivity;
import com.gdou.www.gdouaqualib.view.activity.SettingActivity;
import com.gdou.www.gdouaqualib.view.activity.UserGuideActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    int newVersionCode;
    int now_VersionCode;


    public Map<String, Object> map;
    public Set<String> set;

    private LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<ImageView> imageViews;

    private LinearLayout lily;//特意给snackbar使用的
    long ExitTime;

    version mVersion;
    private MyApplication app;

    private NavigationView navigationView;

    // 图片资源ID
    private final int[] imageIds = {
            R.drawable.pro1,
            R.drawable.pro2,
            R.drawable.pro3,
            R.drawable.pro4,
            R.drawable.pro5,
            R.drawable.pro6,
            R.drawable.pro7};
    /**
     * 上一次高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;

    // 图片标题集合
    private final String[] imageDescriptions = {
            "有毒鱼类",
            "有毒软体动物",
            "有毒腔肠动物",
            "有毒爬行动物",
            "有毒节肢动物",
            "有毒棘皮动物",
            "有毒海绵动物"
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item = viewpager.getCurrentItem()+1;
            viewpager.setCurrentItem(item);

            //延迟发消息
            handler.sendEmptyMessageDelayed(0,4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);//注册

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置窗口可以扩散到屏幕外面

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

        lily = (LinearLayout)findViewById(R.id.lily);

///////////////////////////////////////////////////////////////////////////////
        //ViewPager的使用
        //1.在布局文件中定义ViewPager
        //2.在代码中实例化ViewPager
        //3.准备数据
        imageViews = new ArrayList<>();
        for (int i = 0 ;i < imageIds.length;i++){

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);

            //添加到集合中
            imageViews.add(imageView);

            //添加点-----------------------
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
//            point.setImageResource(R.drawable.point_selector);
            //在代码中设置的都是像数-问题，在所有的手机上都是8个像数
            //把8px当成是dp-->px
            int width = DensityUtil.dip2px(this, 8);
            //Toast.makeText(MainActivity.this, "width==" + width, Toast.LENGTH_SHORT).show();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            if(i==0){
                point.setEnabled(true); //显示红色
            }else{
                point.setEnabled(false);//显示灰色
                params.leftMargin = width;
            }


            point.setLayoutParams(params);

            ll_point_group.addView(point);
        }
        //4.设置适配器(PagerAdapter)-item布局-绑定数据

        viewpager.setAdapter(new MyPagerAdapter());
        //设置监听ViewPager页面的改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置中间位置
        int item = Integer.MAX_VALUE/2 - Integer.MAX_VALUE/2%imageViews.size();//要保证imageViews的整数倍


        viewpager.setCurrentItem(item);

        tv_title.setText(imageDescriptions[prePosition]);

        //发消息
        handler.sendEmptyMessageDelayed(0,3000);

/////////////////////////////////////////////////////////////////////////////

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        // ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle =new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,
                R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_home_description_format);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //NavigationView是左侧侧滑菜单
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);//设置左侧导航抽屉
        }

        layout1 = (LinearLayout)findViewById(R.id.linear1);
        layout2 = (LinearLayout)findViewById(R.id.linear2);
        layout3 = (LinearLayout)findViewById(R.id.linear3);
        layout4 = (LinearLayout)findViewById(R.id.linear4);
        layout5 = (LinearLayout)findViewById(R.id.linear5);
        layout6 = (LinearLayout)findViewById(R.id.linear6);
        layout7 = (LinearLayout)findViewById(R.id.linear7);

        layout1.setOnTouchListener(this);
        layout2.setOnTouchListener(this);
        layout3.setOnTouchListener(this);
        layout4.setOnTouchListener(this);
        layout5.setOnTouchListener(this);
        layout6.setOnTouchListener(this);
        layout7.setOnTouchListener(this);

        ActivityCollector.addActivity(this);
        app = (MyApplication)getApplication();

        if (ChechNetwork.isNetworkConnected(this)){
            //app.getJsonAllListUrl();
            //app.getJsonForArticleUrl();
            Log.e("TAG","有网络。。。");
        }
        checkIfNewVersion();

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void messageEventBus(RefreshMapEvent event){
        if (event.mFLAG){
            Log.e("TAG","eventbus..."+event.mFLAG);
            map = netWorkMap.getInstance().getMapList();
        }
    }

    private void checkIfNewVersion() {

        goToCheckNewVersion();
        now_VersionCode = VersionCheck.getNowVerCode(MainActivity.this);

        Log.e("TAG","now_VersionCode "+ now_VersionCode );

        newVersionCode = app.getValue();

        map = netWorkMap.getInstance().getMapList();

        Log.e("TAG","new_VersionCode "+ newVersionCode);

        if (VersionCheck.isNewVersion(newVersionCode,now_VersionCode)){

            //弹出对话框
            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("软件更新")
                    .setMessage("有新版本")
                            // 设置内容
                    .setPositiveButton("更新",// 设置确定按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    VersionCheck.goUpdate(MainActivity.this);
                                    ToastUtil.show(MainActivity.this, "正在更新");
                                }
                            })
                    .setNegativeButton("暂不更新",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 点击"取消"按钮之后退出程序
                                    //finish();
                                }
                            }).create();// 创建
            // 显示对话框
            dialog.show();
        }
    }

    private void goToCheckNewVersion() {
        //检查服务器的app版本号,解析这个:http://each.ac.cn/atmosphere.json，获得versionCode
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://119.29.78.145/gdouaqualib.json";
                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("TAG", s);
                        List<version> versionList = parseJSONWithGsonForVersion(s);
                        for (version ver : versionList){
                            if (ver != null){
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
        Log.e("TAG", mVersion.getVersionCode()+"  versionModel");
        int newVersionCode = mVersion.getVersionCode();
        Log.e("TAG", "newVersionCode  " + newVersionCode);
        app = (MyApplication)getApplication();
        app.setValue(newVersionCode);
    }
    private  List<version> parseJSONWithGsonForVersion(String jsonData) {
        Gson gson = new Gson();
        List<version> versList = gson.fromJson(jsonData,
                new TypeToken<List<version>>() {}.getType());
        return versList;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                mDrawerLayout.closeDrawers();
                                return true;
                          /*  case R.id.action_others:
                                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                                intent_setting.putExtra("flag",2);
                                startActivity(intent_setting, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                return true;*/
                            case R.id.action_about:
                                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                                intent_about.putExtra("flag",2);
                                startActivity(intent_about,
                                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                return true;
                            case R.id.action_update:
                                checkIfNewVersion();
                                if (!VersionCheck.isNewVersion(newVersionCode,now_VersionCode)){
                                    ToastUtil.show(MainActivity.this,"已是最新版本");
                                }
                                return true;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

       /* SearchManager searchManager =
                (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public boolean onQueryTextSubmit(String query) {
        ToastUtil.show(MainActivity.this,"知道了");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }*/


    //LinearLayout的touch事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        map = netWorkMap.getInstance().getMapList();
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
                MLog.d("点击了该事件"+layout.getTag());
                if (map != null){
                    set = map.keySet();// 取得里面的key的集合
                    layout.startAnimation(animDwon);
                    animUp.setFillAfter(true);
                    String key = v.getTag().toString();
                    switch(key){
                        case "paxing":
                            Map<String,Object> mappx;
                            mappx = netWorkMap.getInstance().getMapTree();
                            if (mappx.keySet().contains("海洋有毒爬行类动物")) {
                                mappx = GsonUtil.toMap(GsonUtil
                                        .parseJson(netWorkMap.getInstance()
                                                .getMapTree().get("海洋有毒爬行类动物").toString()));
                                //开启一个activity
                                Intent intent = new Intent(MainActivity.this,ParticularActivity.class);
                                intent.putExtra("flag",2);
                                intent.putExtra("title","有毒爬行类动物");
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                EventBus.getDefault().postSticky(new MessageEvent(mappx));

                            } else {
                                ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                            }
                            break;
                        case "jipi":
                            Intent intent1 = new Intent(MainActivity.this,EchinodermActivity.class);
                            intent1.putExtra("flag", 0);
                            startActivity(intent1,
                                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                            .toBundle());
                            break;
                        case "haimian":
                            if (set != null && set.contains("海洋有毒海绵动物概述")){
                                String burl = map.get("海洋有毒海绵动物概述").toString().replace("\"","");
                                Log.e("TAG", burl);
                                Intent intent0 = new Intent(MainActivity.this,DetailsActivity.class);
                                intent0.putExtra("flag",0);
                                intent0.putExtra("title", "有毒海绵动物");
                                intent0.putExtra("url", "http://www.csdn.net/article/2017-01-16/2826688");
                                //intent0.putExtra("url", Constants.AURL+burl);
                                startActivity(intent0,
                                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                .toBundle());
                            }else{
                                ToastUtil.show(MainActivity.this,"服务器出问题，请稍候...");
                            }
                            break;
                        case "qiangchang":
                            Intent intent3 = new Intent(MainActivity.this, CoelenteronActivity.class);
                            intent3.putExtra("flag",2);
                            startActivity(intent3,
                                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                            break;
                        case "ruanti":
                            Intent intent4 = new Intent(MainActivity.this,RheidActivity.class);
                            intent4.putExtra("flag",2);
                            startActivity(intent4,
                                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                            break;
                        case "yulei":
                            Intent intent5 = new Intent(MainActivity.this, FishActivity.class);
                            intent5.putExtra("flag", 2);
                            // intent5.putEx
                            startActivity(intent5,
                                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                            break;
                        case "jiezhi":
                            Map<String,Object> mapzj;
                            mapzj = netWorkMap.getInstance().getMapTree();
                            if (mapzj.keySet().contains("海洋有毒节肢动物")) {
                                mapzj = GsonUtil.toMap(GsonUtil
                                        .parseJson(netWorkMap.getInstance()
                                                .getMapTree().get("海洋有毒节肢动物").toString()));
                                //开启一个activity
                                Intent intent = new Intent(MainActivity.this,ParticularActivity.class);
                                intent.putExtra("flag",2);
                                intent.putExtra("title","有毒节肢动物");
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                EventBus.getDefault().postSticky(new MessageEvent(mapzj));

                            } else {
                                ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                            }
                            break;

                    }

                }else {
                    ToastUtil.show(MainActivity.this,"请检查网络连接");
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



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法
         * @param position 当前页面的位置
         * @param positionOffset 滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中了的时候回调
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position%imageViews.size();
            Log.e(TAG, "onPageSelected==" + realPosition);
            //设置对应页面的文本信息
            tv_title.setText(imageDescriptions[realPosition]);

            //把上一个高亮的设置默认-灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //当前的设置为高亮-红色
            ll_point_group.getChildAt(realPosition).setEnabled(true);

            prePosition = realPosition;



        }

        /**
         当页面滚动状态变化的时候回调这个方法
         静止->滑动
         滑动-->静止
         静止-->拖拽

         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_DRAGGING){
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
                Log.e(TAG,"SCROLL_STATE_DRAGGING-------------------");
            }else if(state == ViewPager.SCROLL_STATE_SETTLING){
                Log.e(TAG,"SCROLL_STATE_SETTLING-----------------");

            }else if(state == ViewPager.SCROLL_STATE_IDLE&&isDragging){
                isDragging = false;
                Log.e(TAG,"SCROLL_STATE_IDLE------------");
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0,4000);
            }

        }
    }

    class MyPagerAdapter extends PagerAdapter {


        /**
         * 得到图片的总数
         * @return
         */
        @Override
        public int getCount() {
//            return imageViews.size();
            return Integer.MAX_VALUE;
        }

        /**
         * 相当于getView方法
         * @param container ViewPager自身
         * @param position 当前实例化页面的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            int realPosition = position%imageViews.size();

            final ImageView imageView =  imageViews.get(realPosition);
            container.addView(imageView);//添加到ViewPager中
            Log.e(TAG, "instantiateItem==" + realPosition + ",---imageView==" + imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://手指按下
                            Log.e(TAG, "onTouch==手指按下");
                            handler.removeCallbacksAndMessages(null);
                            break;

                        case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                            break;
                        case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                            Log.e(TAG, "onTouch==事件取消");
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                        case MotionEvent.ACTION_UP://手指离开
                            Log.e(TAG, "onTouch==手指离开");
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map = netWorkMap.getInstance().getMapList();

                    Log.e(TAG, "点击事件");
                    if (map != null) {
                        set = map.keySet();
                        int position = (int) v.getTag() % imageViews.size();
                        String text = imageDescriptions[position];
                        //  Toast.makeText(MainActivity.this, "text=="+text, Toast.LENGTH_SHORT).show();
                        switch (text) {
                            case "有毒鱼类":
                                if (set.contains("海洋有毒鱼类概述")) {
                                    String burl = map.get("海洋有毒鱼类概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒鱼类");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                               }
                                break;
                            case "有毒软体动物":
                                if (set.contains("有毒海洋软体动物概述")) {
                                    String burl = map.get("有毒海洋软体动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒软体动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                            case "有毒腔肠动物":
                                if (set.contains("海洋有毒腔肠动物概述")) {
                                    String burl = map.get("海洋有毒腔肠动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒腔肠动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                            case "有毒爬行动物":
                                if (set.contains("海洋有毒爬行动物概述")) {
                                    String burl = map.get("海洋有毒爬行动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒爬行动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                            case "有毒节肢动物":
                                if (set.contains("海洋有毒节肢动物概述")) {
                                    String burl = map.get("海洋有毒节肢动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒节肢动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                            case "有毒棘皮动物":
                                if (set.contains("海洋有毒棘皮动物概述")) {
                                    String burl = map.get("海洋有毒棘皮动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒棘皮动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                            case "有毒海绵动物":
                                if (set.contains("海洋有毒海绵动物概述")) {
                                    String burl = map.get("海洋有毒海绵动物概述").toString().replace("\"", "");
                                    Log.e("TAG", burl);
                                    Intent intent0 = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent0.putExtra("flag", 0);
                                    intent0.putExtra("title", "有毒海绵动物");
                                    intent0.putExtra("url", Constants.AURL + burl);
                                    startActivity(intent0,
                                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                                    .toBundle());
                                } else {
                                    ToastUtil.show(MainActivity.this, "服务器出问题，请稍候...");
                                }
                                break;
                        }
                    }else{
                        ToastUtil.show(MainActivity.this,"请检查网络连接");
                    }
                }
            });

            return imageView;
        }

        /**
         * 比较view和object是否同一个实例
         * @param view 页面
         * @param object  这个方法instantiateItem返回的结果
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//            if(view == object){
//                return true;
//            }else{
//                return  false;
//            }
            return view == object;
        }
        /**
         * 释放资源
         * @param container viewpager
         * @param position 要释放的位置
         * @param object 要释放的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            Log.e(TAG, "destroyItem==" + position + ",---object==" + object);
            container.removeView((View) object);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (System.currentTimeMillis() - this.ExitTime > 2000) {
            //Toast.makeText(this, "再按一下，退出应用", Toast.LENGTH_SHORT).show();
            //先判断侧滑菜单是否打开，是就关闭，否就OK
                if (mDrawerLayout.isDrawerOpen(navigationView)){
                    mDrawerLayout.closeDrawers();
                }else{
                    Snackbar.make(lily,"再按一次，退出程序", Snackbar.LENGTH_SHORT).show();
                    this.ExitTime = System.currentTimeMillis();
                }
        } else {
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ActivityCollector.finishAll();
    }
}
