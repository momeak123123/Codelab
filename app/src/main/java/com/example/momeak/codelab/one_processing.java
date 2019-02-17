package com.example.momeak.codelab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class one_processing extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.seekBar2)
    SeekBar seekBar2;
    @BindView(R.id.seekBar3)
    SeekBar seekBar3;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.imageView11)
    ImageView imageView11;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.twos)
    TextView twos;
    @BindView(R.id.there)
    TextView there;
    @BindView(R.id.theres)
    TextView theres;
    @BindView(R.id.ones)
    TextView ones;
    @BindView(R.id.one)
    TextView one;
    private ColorMatrix hueMatrix;
    private ColorMatrix saturationMatrix;
    private ColorMatrix lumMatrix;
    private File tempFile;
    private Bitmap headbit;
    private int id;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.one_statics);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, 0.0f);//红
        hueMatrix.setRotate(1, 0.0f);//绿
        hueMatrix.setRotate(2, 0.0f);//蓝
        saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(1.0f);
        lumMatrix = new ColorMatrix();
        lumMatrix.setScale(1.0f, 1.0f, 1.0f, 1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
            seekBar.setMax(3600);
            seekBar.setProgress(1800);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(headbit==null)
                    {
                        showMessagePositiveDialog();
                    }
                    else {
                        float hue = (float) (progress - 1800) / 10;
                        hueMatrix = new ColorMatrix();
                        hueMatrix.setRotate(0, hue);//红
                        hueMatrix.setRotate(1, hue);//绿
                        hueMatrix.setRotate(2, hue);//蓝
                        one.setText(hue + "");
                        seekers();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "开始拖动");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "结束拖动");
                }
            });
            seekBar2.setMax(200);
            seekBar2.setProgress(100);
            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(headbit==null)
                    {
                        showMessagePositiveDialog();
                    }
                    else {
                        float saturation = (float) progress / 100;
                        saturationMatrix = new ColorMatrix();
                        saturationMatrix.setSaturation(saturation);
                        two.setText(saturation + "");
                        seekers();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "开始拖动");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "结束拖动");
                }
            });
            seekBar3.setMax(200);
            seekBar3.setProgress(100);
            seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(headbit==null)
                    {
                        showMessagePositiveDialog();
                    }
                    else {
                        float lum = (float) progress / 100;
                        lumMatrix = new ColorMatrix();
                        lumMatrix.setScale(lum, lum, lum, 1);
                        there.setText(lum + "");
                        seekers();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "开始拖动");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.e("xyh", "onStartTrackingTouch: " + "结束拖动");
                }
            });
    }

     //seekBar调整效果显示
    private void seekers() {
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);
        bmp = Bitmap.createBitmap(headbit.getWidth(), headbit.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(headbit, 0, 0, paint);
        head.setImageBitmap(bmp);
    }
    private void showMessagePositiveDialog() {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage("图片没有上传，请先上传图片")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    //显示列表浮层
    public void showListPopups(View v, int preferredDirection) {

        String[] array = new String[]{
                "拍照",
                "相册",
        };
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array)));
        final QMUIListPopup mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);
        mListPopup.create(QMUIDisplayHelper.dp2px(this, 150), QMUIDisplayHelper.dp2px(this, 200),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                           getPicFromCamera();
                        } else {
                          getPicFromAlbm();
                        }
                        mListPopup.dismiss();
                    }
                });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(preferredDirection);
        mListPopup.show(v);
    }

    /**
     * 从相机获取图片
     */
    public void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    public void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = Uri.fromFile(tempFile);
                        try {
                            headbit = Bitmap_compress.getBitmapFormUri(this, contentUri);
                            head.setImageBitmap(headbit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            headbit = Bitmap_compress.getBitmapFormUri(this, Uri.fromFile(tempFile));
                            head.setImageBitmap(headbit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                        seekBar3.setProgress(100);
                        seekBar2.setProgress(100);
                        seekBar.setProgress(1800);

                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    try {
                        headbit = Bitmap_compress.getBitmapFormUri(this, uri);
                        head.setImageBitmap(headbit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        seekBar3.setProgress(100);
                        seekBar2.setProgress(100);
                        seekBar.setProgress(1800);
                }
                break;
        }
    }


    @OnClick({R.id.fold2, R.id.imageView11, R.id.imageView13})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold2:
                this.finish();
                break;
            case R.id.imageView11:
                showListPopups(imageView11,1);
                break;
            case R.id.imageView13:
                if(bmp!=null)
                {
                    Bitmap_save save = new Bitmap_save();
                    save.saveBitmaps(bmp);
                    Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                    startActivity(intents);
                }
                else
                {
                    showMessagePositiveDialog();
                }
                break;
        }
    }
}

