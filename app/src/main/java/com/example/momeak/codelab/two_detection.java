package com.example.momeak.codelab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class two_detection extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {


    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.fold)
    ImageView fold;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.headtxt)
    TextView headtxt;
    @BindView(R.id.headtxts)
    TextView headtxts;
    @BindView(R.id.switchtxt)
    TextView switchtxt;
    @BindView(R.id.switchs)
    Switch switchs;
    @BindView(R.id.navbar1)
    BottomNavigationBar navbar1;
    @BindView(R.id.layout1)
    NestedScrollView layout1;
    private ArrayList<Fragment> fragments;
    private two_people mstatic;
    private two_image mdynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        txt.setText("检测");
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.navbar1);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.people_detection, "人物检测").setActiveColorResource(R.color.coloronclice))
                .addItem(new BottomNavigationItem(R.drawable.image_detection, "图像检测").setActiveColorResource(R.color.coloronclice))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mstatic = two_people.newInstance();
        headtxt.setText("人物检测");
        headtxts.setText("基于opencv的人物检测处理");
        MyApp application = ((MyApp) this.getApplicationContext());
        if (application.getScore() == 0) {
            switchtxt.setText("摄像头状态:后置");
            switchs.setChecked(false);
        } else {
            switchtxt.setText("摄像头状态:前置");
            switchs.setChecked(true);
        }
        transaction.replace(R.id.layout1, mstatic);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MyApp application = ((MyApp) this.getApplicationContext());
        switch (position) {
            case 0:
                headtxt.setText("人物检测");
                headtxts.setText("基于opencv的人物检测处理");
                if (application.getScore() == 0) {
                    switchtxt.setText("摄像头状态:后置");
                    switchs.setChecked(false);
                } else {
                    switchtxt.setText("摄像头状态:前置");
                    switchs.setChecked(true);
                }
                if (mstatic == null) {
                    mstatic = two_people.newInstance();
                }
                transaction.replace(R.id.layout1, mstatic);
                break;
            case 1:
                headtxt.setText("图像检测");
                headtxts.setText("基于opencv的图像检测处理");
                if (application.getScore() == 0) {
                    switchtxt.setText("摄像头状态:后置");
                    switchs.setChecked(false);
                } else {
                    switchtxt.setText("摄像头状态:前置");
                    switchs.setChecked(true);
                }
                if (mdynamic == null) {
                    mdynamic = two_image.newInstance();
                }
                transaction.replace(R.id.layout1, mdynamic);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @OnClick({R.id.fold, R.id.switchs})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.fold:
                this.finish();
                break;
            case R.id.switchs:
                MyApp application = ((MyApp) this.getApplicationContext());
                if (switchs.isChecked()) {
                    switchtxt.setText("摄像头状态:前置");
                    application.setScore(1);
                } else {
                    switchtxt.setText("摄像头状态:后置");
                    application.setScore(0);
                }
                break;
        }
    }
}
