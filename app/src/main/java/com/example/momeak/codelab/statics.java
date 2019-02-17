package com.example.momeak.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.opencv.core.CvType.CV_8U;


public class statics extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    @BindView(R.id.ima)
    ImageView ima;
    @BindView(R.id.imb)
    ImageView imb;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.head)
    QMUIRadiusImageView head;
    @BindView(R.id.heads)
    QMUIRadiusImageView heads;
    @BindView(R.id.imageView11)
    ImageView imageView11;
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.fold2)
    ImageView fold2;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.imageView12)
    ImageView imageView12;
    @BindView(R.id.imageView13)
    ImageView imageView13;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    //调用照相机返回图片文件
    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private int id;
    private File filePic;
    private String fileName;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_statics);
        ButterKnife.bind(this);
        ima.setVisibility(View.GONE);
        imb.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
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
                }
                break;
        }
    }

    @OnClick({R.id.head, R.id.heads, R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.close, R.id.fold2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head:
                if (headbit != null) {
                    ima.setVisibility(View.VISIBLE);
                    imb.setVisibility(View.VISIBLE);
                    close.setVisibility(View.VISIBLE);
                    imb.setImageBitmap(headbit);
                }
                break;
            case R.id.heads:
                if (headsbit != null) {
                    ima.setVisibility(View.VISIBLE);
                    imb.setVisibility(View.VISIBLE);
                    close.setVisibility(View.VISIBLE);
                    imb.setImageBitmap(headsbit);
                }
                break;
            case R.id.close:
                ima.setVisibility(View.GONE);
                imb.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                break;
            case R.id.fold2:
                this.finish();
                break;
            case R.id.imageView11:
                showListPopups(imageView11, 1);
                break;
            case R.id.imageView12:
                switch (id) {
                    case 2:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix(headbit));
                        break;
                    case 3:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix2(headbit));
                        break;
                    case 4:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix3(headbit));
                        break;
                    case 5:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix4(headbit));
                        break;
                    case 6:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix5(headbit));
                        break;
                    case 7:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix6(headbit));
                        break;
                    case 8:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix7(headbit));
                        break;
                    case 9:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix8(headbit));
                        break;
                    case 10:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix9(headbit));
                        break;
                    case 11:
                        heads.setImageBitmap(Bitmap_dispose.handleImagePix10(headbit));
                        break;
                    case 30:
                        heads.setImageBitmap(Bitmap_dispose.enhance(headbit));
                        break;
                    case 31:
                        heads.setImageBitmap(Bitmap_dispose.enhance1(headbit));
                        break;
                    case 32:
                        heads.setImageBitmap(Bitmap_dispose.enhance2(headbit));
                        break;
                    case 33:
                        heads.setImageBitmap(Bitmap_dispose.enhance3(headbit));
                        break;
                    case 34:
                        heads.setImageBitmap(Bitmap_dispose.enhance4(headbit));
                        break;
                    case 35:
                        heads.setImageBitmap(Bitmap_dispose.enhance5(headbit));
                        break;
                    case 36:
                        heads.setImageBitmap(Bitmap_dispose.enhance6(headbit));
                        break;
                    case 37:
                        heads.setImageBitmap(Bitmap_dispose.enhance7(headbit));
                        break;
                    case 38:
                        heads.setImageBitmap(Bitmap_dispose.enhance8(headbit));
                        break;
                    case 39:
                        heads.setImageBitmap(Bitmap_dispose.enhance9(headbit));
                        break;
                    case 40:
                        heads.setImageBitmap(Bitmap_dispose.enhance10(headbit));
                        break;
                }
                break;
            case R.id.imageView13:
                Bitmap_save save = new Bitmap_save();
                save.saveBitmaps(headsbit);
                Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intents);
                break;
        }
    }


}
