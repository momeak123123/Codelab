package com.example.momeak.codelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class one_static_state extends Fragment {
    Unbinder unbinder;
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
    @BindView(R.id.imb10)
    ImageButton imb10;
    @BindView(R.id.imb7)
    ImageButton imb7;
    @BindView(R.id.imb8)
    ImageButton imb8;
    @BindView(R.id.imb9)
    ImageButton imb9;
    @BindView(R.id.imb5)
    ImageButton imb5;
    @BindView(R.id.imb3)
    ImageButton imb3;
    @BindView(R.id.imb4)
    ImageButton imb4;
    @BindView(R.id.imb1)
    ImageButton imb1;
    @BindView(R.id.imb2)
    ImageButton imb2;
    @BindView(R.id.ima3)
    ImageView ima3;
    @BindView(R.id.imbtxt3)
    TextView imbtxt3;
    @BindView(R.id.ima4)
    ImageView ima4;
    @BindView(R.id.imbtxt4)
    TextView imbtxt4;
    @BindView(R.id.imbtxt10)
    TextView imbtxt10;
    @BindView(R.id.ima10)
    ImageView ima10;
    @BindView(R.id.imbtxt7)
    TextView imbtxt7;
    @BindView(R.id.ima7)
    ImageView ima7;
    @BindView(R.id.imbtxt8)
    TextView imbtxt8;
    @BindView(R.id.ima8)
    ImageView ima8;
    @BindView(R.id.imbtxt9)
    TextView imbtxt9;
    @BindView(R.id.ima9)
    ImageView ima9;
    @BindView(R.id.imageView9)
    ImageView imageView9;
    @BindView(R.id.ima5)
    ImageView ima5;
    @BindView(R.id.imbtxt5)
    TextView imbtxt5;
    @BindView(R.id.imb6)
    ImageButton imb6;
    @BindView(R.id.ima6)
    ImageView ima6;
    @BindView(R.id.imbtxt6)
    TextView imbtxt6;
    @BindView(R.id.ima2)
    ImageView ima2;
    @BindView(R.id.imbtxt2)
    TextView imbtxt2;
    @BindView(R.id.imbtxt1)
    TextView imbtxt1;
    @BindView(R.id.ima1)
    ImageView ima1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_layouts, container, false);
        unbinder = ButterKnife.bind(this, view);
    /*
        imb1.setVisibility(View.GONE);
        ima1.setVisibility(View.GONE);
        imbtxt1.setVisibility(View.GONE);
        imb2.setVisibility(View.GONE);
        ima2.setVisibility(View.GONE);
        imbtxt2.setVisibility(View.GONE);
        imb3.setVisibility(View.GONE);
        ima3.setVisibility(View.GONE);
        imbtxt3.setVisibility(View.GONE);
        imb4.setVisibility(View.GONE);
        ima4.setVisibility(View.GONE);
        imbtxt4.setVisibility(View.GONE);
        imb5.setVisibility(View.GONE);
        ima5.setVisibility(View.GONE);
        imbtxt5.setVisibility(View.GONE); */
        imb6.setVisibility(View.GONE);
        ima6.setVisibility(View.GONE);
        imbtxt6.setVisibility(View.GONE);
        imb7.setVisibility(View.GONE);
        ima7.setVisibility(View.GONE);
        imbtxt7.setVisibility(View.GONE);
        imb8.setVisibility(View.GONE);
        ima8.setVisibility(View.GONE);
        imbtxt8.setVisibility(View.GONE);
        imb9.setVisibility(View.GONE);
        ima9.setVisibility(View.GONE);
        imbtxt9.setVisibility(View.GONE);
        imb10.setVisibility(View.GONE);
        ima10.setVisibility(View.GONE);
        imbtxt10.setVisibility(View.GONE);
        switchs.setVisibility(View.GONE);
        switchtxt.setVisibility(View.GONE);

        headtxt.setText("图像处理");
        headtxts.setText("基于android原生的图像处理");
        imbtxt1.setText("基本处理");
        imbtxt2.setText("色调处理");
        imbtxt3.setText("底片效果");
        imbtxt4.setText("怀旧效果");
        imbtxt5.setText("浮雕效果");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static one_static_state newInstance() {
        Bundle args = new Bundle();
        one_static_state fragment = new one_static_state();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imb10, R.id.imb7, R.id.imb8, R.id.imb9, R.id.switchs, R.id.imb5, R.id.imb6, R.id.imb3, R.id.imb4, R.id.imb1, R.id.imb2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switchs:
                break;
            case R.id.imb10:
                break;
            case R.id.imb9:
                break;
            case R.id.imb8:
                break;
            case R.id.imb7:
                break;
            case R.id.imb6:
                break;
            case R.id.imb5:
                Intent intent5 = new Intent(getContext(), statics.class);
                intent5.putExtra("id", 14);
                startActivity(intent5);
                break;
            case R.id.imb4:
                Intent intent4 = new Intent(getContext(), statics.class);
                intent4.putExtra("id", 13);
                startActivity(intent4);
                break;
            case R.id.imb3:
                Intent intent3 = new Intent(getContext(), statics.class);
                intent3.putExtra("id", 12);
                startActivity(intent3);
                break;
            case R.id.imb2:
                Intent intent2 = new Intent(getContext(), one_statics2.class);
                intent2.putExtra("id", 11);
                startActivity(intent2);
                break;
            case R.id.imb1:
                Intent intent1 = new Intent(getContext(), one_statics2.class);
                intent1.putExtra("id", 10);
                startActivity(intent1);
                break;

        }
    }
}
