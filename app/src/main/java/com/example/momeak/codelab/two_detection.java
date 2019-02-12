package com.example.momeak.codelab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class two_detection extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.fold)
    ImageView fold;
    @BindView(R.id.navbar1)
    BottomNavigationBar navbar1;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    private ArrayList<Fragment> fragments;
    private two_static_state mstatic;
    private two_dynamic_condition mdynamic;

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
                .addItem(new BottomNavigationItem(R.drawable.statics, "静态").setActiveColorResource(R.color.coloronclice))
                .addItem(new BottomNavigationItem(R.drawable.dynamic, "动态").setActiveColorResource(R.color.coloronclice))
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
        mstatic = two_static_state.newInstance();
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
                    mstatic = two_static_state.newInstance();
                }
                transaction.replace(R.id.layout1, mstatic);
                break;
            case 1:
                if (mdynamic == null) {
                    mdynamic = two_dynamic_condition.newInstance();
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

    @OnClick(R.id.fold)
    public void onViewClicked() {
        this.finish();
    }
}
