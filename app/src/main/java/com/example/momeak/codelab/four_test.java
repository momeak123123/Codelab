package com.example.momeak.codelab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class four_test extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.txt)
    TextView txt;
    private ArrayList<Fragment> fragments;
    private four_one one;
    private four_two two;
    private four_there there;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        txt.setText("测试");
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.navbar1);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.statics, "静态").setActiveColorResource(R.color.coloronclice))
                .addItem(new BottomNavigationItem(R.drawable.dynamic, "动态").setActiveColorResource(R.color.coloronclice))
                .addItem(new BottomNavigationItem(R.drawable.compare, "N:N").setActiveColorResource(R.color.coloronclice))
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
        one = four_one.newInstance();
        transaction.replace(R.id.layout1, one);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (one == null) {
                    one = four_one.newInstance();
                }
                transaction.replace(R.id.layout1, one);
                break;
            case 1:
                if (two == null) {
                    two = four_two.newInstance();
                }
                transaction.replace(R.id.layout1, two);
                break;
            case 2:
                if (there == null) {
                    there = four_there.newInstance();
                }
                transaction.replace(R.id.layout1, there);
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

