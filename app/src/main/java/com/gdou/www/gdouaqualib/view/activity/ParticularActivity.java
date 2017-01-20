package com.gdou.www.gdouaqualib.view.activity;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdou.www.gdouaqualib.MyApplication;
import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.Constants;
import com.gdou.www.gdouaqualib.utils.MessageEvent;
import com.gdou.www.gdouaqualib.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public final class ParticularActivity extends ListActivity {

    public Map<String, Object> map;//非目录树结构
    public Map<String,Object> mMap;
    public Set<String> mSet;
    private MyApplication app;

    //private ProgressBar progressBar;
        /**
         * 装数据
         */
        private LinkedList<String> mListItems;
        private PullToRefreshListView mPullRefreshListView;
        private ArrayAdapter<String> mAdapter;

        private TextView tv_title;
        private String title;


        private String[] mStrings;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            int flag = getIntent().getExtras().getInt("flag");
            title = getIntent().getExtras().getString("title");

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

            setContentView(R.layout.activity_particular);
            EventBus.getDefault().register(this);//注册事件

            app = (MyApplication)getApplication();
            map =app.getMap();//非目录树结构的map



            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_title.setText(title);

            //progressBar = (ProgressBar)findViewById(R.id.progress_bar);


            //实例化控件
            mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

            // Set a listener to be invoked when the list should be refreshed.
            //设置下拉刷新的监听
//        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                //得到当前刷新的时间
//                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//                // Update the LastUpdatedLabel
//                //设置更新时间
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//                // Do work to refresh the list here.
//                new GetDataTask().execute();
//            }
//        });

            // Set a listener to be invoked when the list should be refreshed.
            mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

                /**
                 * 下拉刷新
                 * @param refreshView
                 */
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    //得到当前刷新的时间
                    String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                    // Update the LastUpdatedLabel
                    //设置更新时间
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                    Toast.makeText(ParticularActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();

                    new GetDataTask().execute();
                }

                /**
                 * 上拉刷新
                 * @param refreshView
                 */
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    //得到当前刷新的时间
                    String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                    // Update the LastUpdatedLabel
                    //设置更新时间
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                    Toast.makeText(ParticularActivity.this, "上拉刷新!", Toast.LENGTH_SHORT).show();
                    new GetDataTask().execute();
                }

            });

            // Add an end-of-list listener
            //设置监听最后一条
            mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                @Override
                public void onLastItemVisible() {
                    Toast.makeText(ParticularActivity.this, "滑动到最后一条了!", Toast.LENGTH_SHORT).show();
                }
            });






            //得到ListView
            final ListView listview = mPullRefreshListView.getRefreshableView();



            mListItems = new LinkedList<String>();
            mListItems.addAll(Arrays.asList(mStrings));

            //创建适配器
            mAdapter = new ArrayAdapter<String>(this, R.layout.text_item_layout, mListItems);

            /**
             * Add Sound Event Listener
             * 添加刷新事件并且发出声音
             */
            SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
            soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
            soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
            soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
            mPullRefreshListView.setOnPullEventListener(soundListener);

            // You can also just use setListAdapter(mAdapter) or
            // mPullRefreshListView.setAdapter(mAdapter)
            //设置适配器
            listview.setAdapter(mAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.test_item);
                    Log.e("TAG", textView.getText().toString());
                    String key = textView.getText().toString();
                    if (mSet.contains(key)){
                        String burl = map.get(key).toString().replace("\"","");
                        Log.e("TAG", burl);
                        Intent intent0 = new Intent(ParticularActivity.this,DetailsActivity.class);
                        intent0.putExtra("flag",0);
                        intent0.putExtra("title",key);
                        intent0.putExtra("url", Constants.AURL+burl);
                        startActivity(intent0,
                                ActivityOptions.makeSceneTransitionAnimation(ParticularActivity.this)
                                        .toBundle());
                    }else{
                        ToastUtil.show(ParticularActivity.this, "服务器出问题，请稍候...");
                    }

                }
            });


            ActivityCollector.addActivity(this);
            //设置上拉刷新或者下拉刷新
//        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }


            @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
            public void messageEventBus(MessageEvent event){
                this.mMap = event.mMap;
               // Log.e("Tag","PPPPPP："+mMap.keySet().toString());
                //已经拿到map对象，遍历拿出来
                int count = this.mMap.keySet().size();
                mStrings = new String[count];
                mSet = mMap.keySet();
                mStrings = mSet.toArray(mStrings);
            }


     private class GetDataTask extends AsyncTask<Void, Void, String[]> {

            @Override
            protected String[] doInBackground(Void... params) {
                // Simulates a background job.
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return mStrings;
            }

            @Override
            protected void onPostExecute(String[] result) {
                if(mPullRefreshListView.getMode()== PullToRefreshBase.Mode.PULL_FROM_START){
                    //下拉刷新
                    mListItems.addFirst("刷新请求到的新数据...");
                }else if(mPullRefreshListView.getMode()==PullToRefreshBase.Mode.PULL_FROM_END){
                    mListItems.addLast("上拉数据请求到了...");
                }

                mAdapter.notifyDataSetChanged();

                // Call onRefreshComplete when the list has been refreshed.
                mPullRefreshListView.onRefreshComplete();

                super.onPostExecute(result);
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        ActivityCollector.removeActivity(this);
    }
}


