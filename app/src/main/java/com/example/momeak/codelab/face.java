package com.example.momeak.codelab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class face extends AppCompatActivity {

    @BindView(R.id.head)
    QMUIRadiusImageView head;
    @BindView(R.id.imageView10)
    ImageView imageView10;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.but)
    QMUIRadiusImageView but;
    private File tempFile;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Bitmap bitmap;
    private QMUITipDialog tipDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_face);
        ButterKnife.bind(this);
        File savePath = new File(getExternalFilesDir(null).getPath() + "/facestrat.jpg");
        if (savePath.exists()) {
            head.setImageURI(Uri.fromFile(savePath));
        } else {
            getPicFromCamera();
        }
    }
    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    Uri contentUri = Uri.fromFile(tempFile);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                        head.setImageBitmap(compressScaleBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    //生成缩略图
    private Bitmap compressScaleBitmap(Bitmap bit) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.2f, 0.2f);
        Bitmap bm = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                bit.getHeight(), matrix, true);
        return bm;
    }

    @OnClick({R.id.but, R.id.imageView10, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView10:
                this.finish();
                break;
            case R.id.but:
                getPicFromCamera();
                break;
            case R.id.ok:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SaveBitmap save = new SaveBitmap();
                        save.saveBitmaps(bitmap);
                        tipDialog.dismiss();
                        Intent intent1 = new Intent();
                        intent1.setClass(face.this, MainActivity.class);
                        startActivity(intent1);
                    }
                });
                thread.start();
                tipDialog = new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载")
                        .create();
                tipDialog.show();
                break;
        }
    }
}
