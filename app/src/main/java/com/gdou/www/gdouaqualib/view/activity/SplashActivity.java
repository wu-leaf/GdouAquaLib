package com.gdou.www.gdouaqualib.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdou.www.gdouaqualib.MainActivity;
import com.gdou.www.gdouaqualib.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {


    int[] imgs = new int[]{
            R.mipmap.pic_1,
            R.mipmap.pic_2,
            R.mipmap.pic_1,
            R.mipmap.pic_2,
            R.mipmap.pic_1};


    ImageView ivBg;

    TextView tvSkip;

    private Context mContext;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        //ButterKnife.bind(this);
        mContext = this;
        setContentView(R.layout.activity_splash);
        initViewsAndEvents();
    }





    protected void initViewsAndEvents() {
        ivBg = (ImageView) findViewById(R.id.ivBg);
        tvSkip = (TextView) findViewById(R.id.tvSkip);

        tvSkip.setOnClickListener(this);
        int index = (int) (Math.random() * imgs.length);

        ivBg.setImageResource(imgs[index]);

        timer = new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvSkip.setText(String.format(getResources().getString(R.string.skip), (int) (millisUntilFinished / 1000 + 0.1)));
            }

            @Override
            public void onFinish() {
                tvSkip.setText(String.format(getResources().getString(R.string.skip), 0));
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        };
        timer.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tvSkip){
            if (timer != null)
                timer.cancel();

            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
    }
}
