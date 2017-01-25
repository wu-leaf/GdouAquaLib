package com.gdou.www.gdouaqualib.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gdou.www.gdouaqualib.R;
import com.gdou.www.gdouaqualib.entity.netWorkMap;
import com.gdou.www.gdouaqualib.utils.ActivityCollector;
import com.gdou.www.gdouaqualib.utils.GsonUtil;
import com.gdou.www.gdouaqualib.utils.MessageEvent;
import com.gdou.www.gdouaqualib.utils.ToastUtil;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Bind(R.id.zname)
    TextInputEditText mZname;//中文名
    @Bind(R.id.xname)
    TextInputEditText mXname;//学名
    @Bind(R.id.sname)
    TextInputEditText mSname;//俗名
    @Bind(R.id.spinner_tsystem)
    Spinner mSpinnerTsystem;//分类系统
    @Bind(R.id.spinner_yd_type)
    Spinner mSpinnerYdType;//有毒类型
    @Bind(R.id.area)
    TextInputEditText mArea;//区域
    @Bind(R.id.feature)
    TextInputEditText mFeature;//特性
    @Bind(R.id.habit)
    TextInputEditText mHabit;//习性
    @Bind(R.id.symptom)
    TextInputEditText mSymptom;//中毒症状
    @Bind(R.id.btn_search)
    Button mBtnSearch;
    @Bind(R.id.btn_reset)
    Button mBtnReset;


    private String tsystem;
    private String yd_type;
    private boolean flag;
    private String urlstr;

    private Map<String,Object> mapList;
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
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("检索引擎");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinnerTsystem.setOnItemSelectedListener(this);
        mSpinnerYdType.setOnItemSelectedListener(this);

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

    @OnClick({R.id.btn_search, R.id.btn_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                /**
                 * 1判断输入是否全空,全空则提示，否则执行检索
                 */
                if (checkInput()){
                   ToastUtil.show(SearchActivity.this, "执行检索..." + urlstr);
                    getDataFromPost();

                }else if (!checkInput()){
                    ToastUtil.show(SearchActivity.this,"请至少输入一项");
                }
                break;
            case R.id.btn_reset:
                allReset();
                break;
        }
    }



    private void getDataFromPost() {
        //使用okhttp  post请求
        //http://123.207.126.233/fish/GetAllTable?check=EXAA
        //"http://123.207.126.233/fish/getdb/DoSearch"
        /**
         *  .addParams("zname", mZname.getText().toString())
         .addParams("xname", mXname.getText().toString())
         .addParams("sname",mSname.getText().toString())
         .addParams("tsystem", tsystem)
         .addParams("yd_type", yd_type)
         .addParams("area", mArea.getText().toString())
         .addParams("feature", mFeature.getText().toString())
         .addParams("habit", mHabit.getText().toString())
         .addParams("symptom", mSymptom.getText().toString())
         */
        OkHttpUtils.get()
                .url("http://123.207.126.233/fish/GetAllTable")
                .addParams("check","EXAA")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "OkHttpUtils-onError:"+e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response) {
                      Log.e("TAG", "OkHttpUtils-onResponse:" + response.toString());
                        /**
                         * 1.把response封装成map
                         * 2.把数据丢给ParticularActivity
                         */
                        mapList = GsonUtil.toMap(GsonUtil.parseJson(response));
                        //打开检索结果页面。
                        goToResultForSearch(mapList);
                    }
                });

    }

    private void goToResultForSearch(Map<String, Object> list) {
        Intent intent = new Intent(SearchActivity.this,ParticularActivity.class);
        intent.putExtra("flag",2);
        intent.putExtra("title","检索结果");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this).toBundle());
        EventBus.getDefault().postSticky(new MessageEvent(mapList));
    }
    private void allReset() {
        mZname.setText("");
        mXname.setText("");
        mSname.setText("");
        mArea.setText("");
        mFeature.setText("");
        mHabit.setText("");
        mSymptom.setText("");

        mSpinnerTsystem.setSelection(0);
        mSpinnerYdType.setSelection(0);
    }

    private boolean checkInput() {

     urlstr = mZname.getText().toString()
             +mXname.getText().toString()
             +mSname.getText().toString()
             +tsystem
             +yd_type
             +mArea.getText().toString()
             +mFeature.getText().toString()
             +mHabit.getText().toString()
             +mSymptom.getText().toString();
     if (urlstr.toString().isEmpty()){
         flag = false;
     }else {
         flag = true;
     }
        return flag;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //判断是哪个spinner
        if (parent.getTag().toString().contains("tsystem")){
                tsystem =SearchActivity.this.getResources().getStringArray(R.array.tsystem)[position];

            }else if (parent.getTag().toString().contains("yd_type")){
                yd_type =SearchActivity.this.getResources().getStringArray(R.array.yd_type)[position];
        }
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
