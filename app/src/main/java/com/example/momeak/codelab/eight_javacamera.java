package com.example.momeak.codelab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class eight_javacamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    public static final int VIEW_MODE_RGBA = 1;
    public static final int VIEW_MODE_HIST = 2;
    public static final int VIEW_MODE_CANNY = 3;
    public static final int VIEW_MODE_SEPIA = 4;
    public static final int VIEW_MODE_SOBEL = 5;
    public static final int VIEW_MODE_ZOOM = 6;
    public static final int VIEW_MODE_PIXELIZE = 7;
    public static final int VIEW_MODE_POSTERIZE = 8;
    @BindView(R.id.btn_take3)
    ImageView btnTake3;
    @BindView(R.id.iv_show2)
    QMUIRadiusImageView ivShow2;
    private static final int COMPLETED = 0;
    @BindView(R.id.javaCameraView)
    JavaCameraView javaCameraView;
    @BindView(R.id.fold)
    ImageView fold;
    private Mat mRgba;
    private Mat mGray;
    private int s;
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier = null; //级联分类器
    private int absoluteFaceSize = 0;
    private Handler handler;
    private InputStream is;
    private File mCascadeFile;
    private int id;

    private Mat image;
    private Bitmap bitmaps;
    private boolean bool = false;
    private  File filePic;
    private  String fileName;

    private void initializeOpenCVDependencies() {
        try {
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2); //OpenCV的人脸模型文件： haarcascade_frontalface_alt2.xml
            mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            // 加载cascadeClassifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error loading cascade", e);
        }
        // 显示
        openCvCameraView.enableView();
    }

    public eight_javacamera() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.eight_javacamera);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        MyApp appState = ((MyApp) getApplicationContext());
        s = appState.getScore();
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView);
        openCvCameraView.setCameraIndex(s); //摄像头索引        -1/0：后置双摄     1：前置
        openCvCameraView.setCvCameraViewListener(this);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        }
        initializeOpenCVDependencies();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba(); //RGBA
        mGray = inputFrame.gray(); //单通道灰度图
        if (s == 1) {
            Core.flip(mRgba, mRgba, 1);
            Core.flip(mGray, mGray, 1);
        }

        if (absoluteFaceSize == 0) {
            int heights = mGray.rows();
            if (Math.round(heights * 0.2f) > 0) {
                absoluteFaceSize = Math.round(heights * 0.2f);
            }
        }
        if (bool == true) {
            Thread t = new Thread() {
                public void run() {
                    bitmaps = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(mRgba, bitmaps);
                    bool = false;
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handlers.sendMessage(msg);

                }
            };
            t.start();
        }
        //检测并显示
        MatOfRect faces = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mGray, faces, 1.1, 3, 1, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            for (int i = 0; i < facesArray.length; i++) {    //用框标记
                Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 0, 0, 255), 3);
            }
        }
        return mRgba;
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                if (bitmaps != null) {
                    saveBitmaps();
                }
            }
        }
    };

    /**
     * 保存图片任务
     */
    public void saveBitmaps() {
        Bitmap bitmap;
        if (s == 1) {
            bitmap = createPhotos(bitmaps, 90);
        }else {
            bitmap = createPhotos(bitmaps, -90);
           Matrix m = new Matrix();
            m.setScale(-1, -1);//水平翻转
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            //生成的翻转后的bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
        }
        ivShow2.setImageBitmap(Bitmap_compress.compressScaleBitmap(bitmap));
        saveBitmaps(bitmap);
    }
    public  void saveBitmaps(Bitmap bitmap) {
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            fileName = "/IMG_" + timeStamp + ".jpg";
            filePic = new File(appDir, fileName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (
                IOException e) {
            e.printStackTrace();
            return;
        }
        // 其次把文件插入到系统图库
        String path = filePic.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), path, fileName, null);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(filePic);
        intent.setData(uri);
        this.sendBroadcast(intent);
    }
    public static Bitmap createPhotos(Bitmap bitmap, int sum) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            try {
                m.setRotate(sum, bitmap.getWidth() / 2, bitmap.getHeight() / 2);//90就是我们需要选择的90度
                Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                bitmap.recycle();
                bitmap = bmp2;
            } catch (Exception ex) {
                System.out.print("创建图片失败！" + ex);
            }
        }
        return bitmap;
    }

    @OnClick({R.id.btn_take3, R.id.fold, R.id.iv_show2})
    public void onViewClicked(View view) {
        MyApp appState = ((MyApp) getApplicationContext());
        switch (view.getId()) {
            case R.id.btn_take3:
                bool = true;
                break;
            case R.id.fold:
                this.finish();
                break;
            case R.id.iv_show2:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intent);
                break;
        }
    }
}
