package com.example.momeak.codelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class four_data extends AppCompatActivity {

    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.fold4)
    ImageView fold4;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.headtxt)
    TextView headtxt;
    @BindView(R.id.headtxts)
    TextView headtxts;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.add)
    ImageView add;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String namet;
    private String pictures;
    private int id;
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.four_data);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView list = (ListView) findViewById(R.id.list);
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
        sharedPreferencesHelper = new SharedPreferencesHelper(four_data.this, "people");
        // sharedPreferencesHelper.clear();

        if(sharedPreferencesHelper.contain("data")==true)
        {
            int data = Integer.parseInt(sharedPreferencesHelper.getSharedPreference("data", "").toString().trim());
            for(int i=0;i<data;i++) {
                String picture = i+"0.jpg";
                String names =  sharedPreferencesHelper.getSharedPreference(picture, "").toString().trim();
                File savePath = new File(getFilesDir().getAbsolutePath() + picture);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("head", Uri.fromFile(savePath));
                map.put("name", names);
                datalist.add(map);
            }

            list.setAdapter(new SimpleAdapter(this, datalist, R.layout.four_list, new String[]{"imb", "head", "name"}, new int[]{R.id.imb, R.id.head, R.id.name}));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    finish(i);
                }

            });
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // TODO Auto-generated method stub
                    id=i;
                    pictures = i+"0.jpg";
                    namet = sharedPreferencesHelper.getSharedPreference(pictures, "").toString().trim();
                    showMessageNegativeDialog();
                    return true;
                }
            });
        }
    }

    public void finish(int i)
    {
        Intent intent = new Intent(four_data.this, four_face.class);
        intent.putExtra("id", i);
        intent.putExtra("add", 1);
        startActivity(intent);
        this.finish();
    }

    private void showMessageNegativeDialog() {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("是否删除"+namet+"的信息数据?")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0,"删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        tipDialog = new QMUITipDialog.Builder(four_data.this)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在删除")
                                .create();
                        tipDialog.show();
                        sharedPreferencesHelper.remove(pictures);
                        String data = sharedPreferencesHelper.getSharedPreference("data", "").toString().trim();
                        int datas = Integer.parseInt(data);
                        data= String.valueOf(datas-1);
                        sharedPreferencesHelper.put("data", data);
                        for(int i=0;i<10;i++)
                        {
                            String picture= id+i+".jpg";
                            File file = new File(getFilesDir().getAbsolutePath() + picture);
                            if (file.exists() && file.isFile())
                            {
                                file.delete();
                            }
                        }
                        Intent intent = new Intent(four_data.this, four_data.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick({R.id.fold4, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold4:
                Intent intent = new Intent(four_data.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.add:
                Intent intent1 = new Intent(four_data.this, four_face.class);
                intent1.putExtra("id", 0);
                intent1.putExtra("add", 0);
                startActivity(intent1);
                this.finish();
                break;
        }
    }
}
