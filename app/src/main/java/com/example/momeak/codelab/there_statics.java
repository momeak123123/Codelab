package com.example.momeak.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class there_statics extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static double sum;
    private static String results;
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.fold2)
    ImageView fold2;
    @BindView(R.id.head)
    QMUIRadiusImageView head;
    @BindView(R.id.heads)
    QMUIRadiusImageView heads;
    @BindView(R.id.imageView11)
    ImageView imageView11;
    @BindView(R.id.imageView12)
    ImageView imageView12;
    @BindView(R.id.imageView8)
    ImageView imageView8;
    @BindView(R.id.imageView13)
    ImageView imageView13;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.imageView14)
    ImageView imageView14;
    @BindView(R.id.imageView16)
    ImageView imageView16;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.textView13)
    TextView textView13;
    @BindView(R.id.progressBar)
    QMUIProgressBar progressBar;
    @BindView(R.id.textView14)
    TextView textView14;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.textView16)
    TextView textView16;


    private int headint = 0;
    //调用照相机返回图片文件
    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private int id;
    private static final int COMPLETED = 0;
    QMUITipDialog tipDialog;
    private static final String RESULT_FORMAT = "0.0000";
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    System.loadLibrary("native-lib");
                    System.loadLibrary("opencv_java3");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private Bitmap photoBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.but));
        }
        setContentView(R.layout.there_statics);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        String txt = null;
        switch (id) {
            case 1:
                txt = "像素值对比，对每一个像素进行对比，适合查找是否为同一图片";
                break;
            case 2:
                txt = "直方图对比";
                break;
            case 3:
                txt = "欧式距离对比";
                break;
            case 4:
                txt = "余弦相似度对比";
                break;
            case 5:
                txt = "Hash算法对比";
                break;

        }
        textView16.setText(txt);
    }

    /**
     * 判断第一张，第二张图片
     *
     * @param bitmap
     * @return
     * @headint 第几个图片
     */
    private void image(Bitmap bitmap) {

        if (headint == 0) {
            headbit = bitmap;
            head.setImageBitmap(headbit);
        } else {
            if (headint == 1) {
                headsbit = bitmap;
                heads.setImageBitmap(headsbit);
            }
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
                    Uri contentUri = Uri.fromFile(tempFile);
                    if (contentUri != null) {
                        try {
                            photoBmp = Bitmap_compress.getBitmapFormUri(this, contentUri);
                            image(photoBmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        try {
                            photoBmp = Bitmap_compress.getBitmapFormUri(this, uri);
                            image(photoBmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @OnClick({R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.fold2, R.id.textView12, R.id.textView13})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold2:
                this.finish();
                break;
            case R.id.textView12:
                progressBar.setVisibility(View.VISIBLE);
                textView14.setVisibility(View.VISIBLE);
                textView15.setVisibility(View.VISIBLE);
                textView12.setTextColor(Color.parseColor("#1B88EE"));
                textView13.setTextColor(Color.parseColor("#212832"));
                textView16.setVisibility(View.GONE);
                break;
            case R.id.textView13:
                progressBar.setVisibility(View.GONE);
                textView14.setVisibility(View.GONE);
                textView15.setVisibility(View.GONE);
                textView13.setTextColor(Color.parseColor("#1B88EE"));
                textView12.setTextColor(Color.parseColor("#212832"));
                textView16.setVisibility(View.VISIBLE);
                break;
            case R.id.imageView11:
                showListPopups(imageView11, 1);
                headint = 0;
                break;
            case R.id.imageView12:
                showListPopups(imageView12, 1);
                headint = 1;
                break;
            case R.id.imageView13:
                if (headbit != null && headsbit != null) {
                    tipDialog = new QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("正在加载")
                            .create();
                    tipDialog.show();
                    Thread t = new Thread() {
                        public void run() {
                            DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
                            switch (id) {
                                case 1:
                                    sum = Bitmap_dispose.similarity(headbit, headsbit);
                                    results = df.format(sum);
                                    break;
                                case 2:
                                    sum = Bitmap_dispose.hist(headbit, headsbit);
                                    results = df.format(sum);
                                    break;
                                case 3:
                                    sum = Bitmap_dispose.similar(headbit, headsbit);
                                    results = df.format(sum);
                                    break;
                                case 4:
                                    sum = Bitmap_dispose.cosine(headbit, headsbit);
                                    results = df.format(sum);
                                    break;
                                case 5:
                                    sum = Bitmap_dispose.shae(headbit, headsbit);
                                    if (sum < 11) {
                                        results = df.format((10 - sum) / 10);
                                    } else {
                                        results = df.format(0);
                                    }
                                    break;
                            }
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handlers.sendMessage(msg);
                        }
                    };
                    t.start();
                }
                break;
        }
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                double res = (double) Double.parseDouble(results);
                int red = (int) (res * 10000);
                double ret = (double) red / 100;
                progressBar.setMaxValue(10000);
                progressBar.setProgress(red);
                textView14.setText(ret + "%");
                tipDialog.dismiss();
            }
        }
    };

}
