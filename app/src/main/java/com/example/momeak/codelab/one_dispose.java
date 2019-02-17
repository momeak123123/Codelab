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

public class one_dispose extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.fold)
    ImageView fold;
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
    private one_picture mstatic1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        txt.setText("处理");
        switchs.setVisibility(View.GONE);
        switchtxt.setVisibility(View.GONE);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.navbar1);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.picture_processing, "图像处理").setActiveColorResource(R.color.coloronclice))
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
        mstatic1 = one_picture.newInstance();
        transaction.replace(R.id.layout1, mstatic1);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mstatic1 == null) {
                    mstatic1 = one_picture.newInstance();
                }
                transaction.replace(R.id.layout1, mstatic1);
                headtxt.setText("图像处理");
                headtxts.setText("基于android原生的图像处理");
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

    @OnClick(R.id.fold)
    public void onViewClicked() {
        this.finish();
    }
}
