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

public class eight_activity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.fold)
    ImageView fold;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.view5)
    View view5;
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
    private eight_layout mstatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        txt.setText("相机");
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.navbar1);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.camera, "相机").setActiveColorResource(R.color.coloronclice))
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
        headtxt.setText("相机");
        headtxts.setText("基于android以及opencv的相机");
        MyApp application = ((MyApp) this.getApplicationContext());
        if (application.getScore() == 0) {
            switchtxt.setText("摄像头状态:后置");
            switchs.setChecked(false);
        } else {
            switchtxt.setText("摄像头状态:前置");
            switchs.setChecked(true);
        }
        mstatic = eight_layout.newInstance();
        transaction.replace(R.id.layout1, mstatic);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mstatic == null) {
                    mstatic = eight_layout.newInstance();
                }
                transaction.replace(R.id.layout1, mstatic);
                headtxt.setText("相机");
                headtxts.setText("基于android以及opencv的相机");
                MyApp application = ((MyApp) this.getApplicationContext());
                if (application.getScore() == 0) {
                    switchtxt.setText("摄像头状态:后置");
                    switchs.setChecked(false);
                } else {
                    switchtxt.setText("摄像头状态:前置");
                    switchs.setChecked(true);
                }
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
