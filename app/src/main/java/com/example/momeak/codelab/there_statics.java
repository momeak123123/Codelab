package com.example.momeak.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.media.ThumbnailUtils;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static com.example.momeak.codelab.Bitmap_dispose.shae;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.CV_COMP_CORREL;
import static org.opencv.imgproc.Imgproc.cvtColor;


public class there_statics extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static double sum;
    private static String results;
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
    @BindView(R.id.imageView12)
    ImageView imageView12;
    @BindView(R.id.similarity)
    TextView similarity;
    private int headint = 0;
    //调用照相机返回图片文件
    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private int id;
    private static final String RESULT_FORMAT = "00.0%";
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

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
        setContentView(R.layout.there_statics);
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

    /**
     * 判断第一张，第二张图片
     *
     * @param bitmap
     * @headint  第几个图片
     * @return
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
                headint = 0;
                break;
            case R.id.imageView12:
                showListPopups(imageView12, 1);
                headint = 1;
                break;
            case R.id.imageView13:
                DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
                final QMUITipDialog tipDialog;
                tipDialog = new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载")
                        .create();
                tipDialog.show();
                switch (id) {
                    case 1:
                        sum=Bitmap_dispose.similarity(headbit, headsbit);
                        results = df.format(sum);
                        similarity.setText("相似度为： " + results);
                        tipDialog.dismiss();
                        break;
                    case 2:
                        sum=Bitmap_dispose.hist(headbit, headsbit);
                        results = df.format(sum);
                        similarity.setText("相似度为： " + results);
                        tipDialog.dismiss();
                        break;
                    case 3:
                        sum=Bitmap_dispose.similar(headbit, headsbit);
                        results = df.format(1 / (sum + 1));
                        similarity.setText("相似度为： " + results);
                        tipDialog.dismiss();
                        break;
                    case 4:
                        sum = Bitmap_dispose.shae(headbit, headsbit);
                        if (sum < 12) {
                            results = df.format((11 - sum) / 10);
                        } else {
                            results = df.format(0);
                        }
                        similarity.setText("相似度为： " + results);
                        tipDialog.dismiss();
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                }
                break;
        }
    }
}
