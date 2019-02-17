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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class there_contrast extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.imb14)
    ImageButton imb14;
    @BindView(R.id.imbtxt14)
    TextView imbtxt14;
    @BindView(R.id.ima14)
    ImageView ima14;
    @BindView(R.id.imb12)
    ImageButton imb12;
    @BindView(R.id.imbtxt12)
    TextView imbtxt12;
    @BindView(R.id.ima12)
    ImageView ima12;
    @BindView(R.id.imb10)
    ImageButton imb10;
    @BindView(R.id.imb7)
    ImageButton imb7;
    @BindView(R.id.imb8)
    ImageButton imb8;
    @BindView(R.id.imb9)
    ImageButton imb9;
    @BindView(R.id.imb11)
    ImageButton imb11;
    @BindView(R.id.imbtxt11)
    TextView imbtxt11;
    @BindView(R.id.ima11)
    ImageView ima11;
    @BindView(R.id.imb13)
    ImageButton imb13;
    @BindView(R.id.imbtxt13)
    TextView imbtxt13;
    @BindView(R.id.ima13)
    ImageView ima13;
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
     /*  switchs.setVisibility(View.GONE);
        switchtxt.setVisibility(View.GONE);
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
        imbtxt5.setVisibility(View.GONE);
        imb6.setVisibility(View.GONE);
        ima6.setVisibility(View.GONE);
        imbtxt6.setVisibility(View.GONE);*/
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
        imb11.setVisibility(View.GONE);
        ima11.setVisibility(View.GONE);
        imbtxt11.setVisibility(View.GONE);
        imb12.setVisibility(View.GONE);
        ima12.setVisibility(View.GONE);
        imbtxt12.setVisibility(View.GONE);
        imb13.setVisibility(View.GONE);
        ima13.setVisibility(View.GONE);
        imbtxt13.setVisibility(View.GONE);
        imb14.setVisibility(View.GONE);
        ima14.setVisibility(View.GONE);
        imbtxt14.setVisibility(View.GONE);

        imbtxt1.setText("像素值对比");
        imbtxt2.setText("直方图对比");
        imbtxt3.setText("欧氏距离对比");
        imbtxt4.setText("均值Hash算法对比");
        imbtxt5.setText("pHash算法对比");
        imbtxt6.setText("SIFT算法对比");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static there_contrast newInstance() {
        Bundle args = new Bundle();
        there_contrast fragment = new there_contrast();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imb10, R.id.imb7, R.id.imb8, R.id.imb9, R.id.imb5, R.id.imb6, R.id.imb3, R.id.imb4, R.id.imb1, R.id.imb2, R.id.imb11, R.id.imb12, R.id.imb13, R.id.imb14})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imb14:
                Intent intent14 = new Intent(getContext(), there_statics.class);
                intent14.putExtra("id", 14);
                startActivity(intent14);
                break;
            case R.id.imb13:
                Intent intent13 = new Intent(getContext(), there_statics.class);
                intent13.putExtra("id", 13);
                startActivity(intent13);
                break;
            case R.id.imb12:
                Intent intent12 = new Intent(getContext(), there_statics.class);
                intent12.putExtra("id", 12);
                startActivity(intent12);
                break;
            case R.id.imb11:
                Intent intent11 = new Intent(getContext(), there_statics.class);
                intent11.putExtra("id", 11);
                startActivity(intent11);
                break;
            case R.id.imb10:
                Intent intent10 = new Intent(getContext(), there_statics.class);
                intent10.putExtra("id", 10);
                startActivity(intent10);
                break;
            case R.id.imb9:
                Intent intent9 = new Intent(getContext(), there_statics.class);
                intent9.putExtra("id", 9);
                startActivity(intent9);
                break;
            case R.id.imb8:
                Intent intent8 = new Intent(getContext(), there_statics.class);
                intent8.putExtra("id", 8);
                startActivity(intent8);
                break;
            case R.id.imb7:
                Intent intent7 = new Intent(getContext(), there_statics.class);
                intent7.putExtra("id", 7);
                startActivity(intent7);
                break;
            case R.id.imb6:
                Intent intent6 = new Intent(getContext(), there_statics.class);
                intent6.putExtra("id", 6);
                startActivity(intent6);
                break;
            case R.id.imb5:
                Intent intent5 = new Intent(getContext(), there_statics.class);
                intent5.putExtra("id", 5);
                startActivity(intent5);
                break;
            case R.id.imb4:
                Intent intent4 = new Intent(getContext(), there_statics.class);
                intent4.putExtra("id", 4);
                startActivity(intent4);
                break;
            case R.id.imb3:
                Intent intent3 = new Intent(getContext(), there_statics.class);
                intent3.putExtra("id", 3);
                startActivity(intent3);
                break;
            case R.id.imb2:
                Intent intent2 = new Intent(getContext(), there_statics.class);
                intent2.putExtra("id", 2);
                startActivity(intent2);
                break;
            case R.id.imb1:
                Intent intent1 = new Intent(getContext(), there_statics.class);
                intent1.putExtra("id", 1);
                startActivity(intent1);
                break;

        }
    }
}
