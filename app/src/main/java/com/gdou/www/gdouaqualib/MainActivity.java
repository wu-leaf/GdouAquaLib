package com.gdou.www.gdouaqualib;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
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

import com.gdou.www.gdouaqualib.utils.DensityUtil;
import com.gdou.www.gdouaqualib.utils.MLog;
import com.gdou.www.gdouaqualib.utils.ToastUtil;
import com.gdou.www.gdouaqualib.view.activity.AboutActivity;
import com.gdou.www.gdouaqualib.view.activity.CoelenteronActivity;
import com.gdou.www.gdouaqualib.view.activity.DetailsActivity;
import com.gdou.www.gdouaqualib.view.activity.EchinodermActivity;
import com.gdou.www.gdouaqualib.view.activity.SettingActivity;
import com.gdou.www.gdouaqualib.view.activity.UserGuideActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnTouchListener{

    private LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    private SearchView searchView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<ImageView> imageViews;
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
            "豚毒鱼类",
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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置窗口可以扩散到屏幕外面

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                            case R.id.action_settings:
                                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intent_setting);
                                return true;
                            case R.id.action_about:
                                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                                intent_about.putExtra("flag",2);
                                startActivity(intent_about,
                                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                return true;
                            case R.id.action_guide:
                                Intent intent_guide = new Intent(MainActivity.this, UserGuideActivity.class);
                                startActivity(intent_guide);
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

        SearchManager searchManager =
                (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ToastUtil.show(MainActivity.this,"知道了");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    //LinearLayout的touch事件
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
                MLog.d("点击了该事件"+layout.getTag());
                layout.startAnimation(animDwon);
                animUp.setFillAfter(true);
                String key = v.getTag().toString();
                switch(key){
                    case "jipi":
                        Intent intent1 = new Intent(MainActivity.this,EchinodermActivity.class);
                        intent1.putExtra("flag", 0);
                        startActivity(intent1,
                                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                        .toBundle());
                        break;
                    case "haimian":
                        Intent intent2 = new Intent(MainActivity.this,DetailsActivity.class);
                        intent2.putExtra("flag",0);
                        startActivity(intent2,
                                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                        .toBundle());
                        break;
                    case "qiangchang":
                        Intent intent3 = new Intent(MainActivity.this, CoelenteronActivity.class);
                        intent3.putExtra("flag",1);
                        startActivity(intent3,
                                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
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
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://手指按下
                            Log.e(TAG,"onTouch==手指按下");
                            handler.removeCallbacksAndMessages(null);
                            break;

                        case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                            break;
                        case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                            Log.e(TAG,"onTouch==事件取消");
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                        case MotionEvent.ACTION_UP://手指离开
                            Log.e(TAG,"onTouch==手指离开");
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG,"点击事件");
                    int position = (int) v.getTag()%imageViews.size();
                    String text = imageDescriptions[position];
                    //Toast.makeText(MainActivity.this, "text=="+text, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                    intent.putExtra("flag",2);
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                                    .toBundle());
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
}
