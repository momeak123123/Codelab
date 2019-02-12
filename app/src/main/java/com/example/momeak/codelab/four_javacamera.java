package com.example.momeak.codelab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class four_javacamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    @BindView(R.id.javaCameraView)
    JavaCameraView javaCameraView;
    @BindView(R.id.fold)
    ImageView fold;
    @BindView(R.id.h8)
    ImageView h8;
    @BindView(R.id.h7)
    ImageView h7;
    @BindView(R.id.h4)
    ImageView h4;
    @BindView(R.id.h5)
    ImageView h5;
    @BindView(R.id.h6)
    ImageView h6;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.h1)
    ImageView h1;
    @BindView(R.id.h2)
    ImageView h2;
    @BindView(R.id.h3)
    ImageView h3;
    @BindView(R.id.textView2)
    TextView textView2;
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
    private Rect rect;
    private int ideat=0;
    private static final int COMPLETED = 0;
    private Bitmap bitmaps;
    private File filePic;
    private String appDir;
    private int data;
    private Rect[] facesArray;
    private int i;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.four_javacamera);
        ButterKnife.bind(this);
        handler = new Handler();
        Intent intent = getIntent();
        data = (int) intent.getSerializableExtra("data");
        id = (int) intent.getSerializableExtra("id");
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView);
        openCvCameraView.setCameraIndex(1); //摄像头索引        -1/0：后置双摄     1：前置
        openCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV init error");
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

        Core.flip(mRgba, mRgba, 0);
        Core.flip(mGray, mGray, 0);

        Mat rotateMat = Imgproc.getRotationMatrix2D(new Point(mRgba.rows() / 2, mRgba.cols() / 2), 90, 1);
        Imgproc.warpAffine(mRgba, mRgba, rotateMat, mRgba.size());
        Mat rotateMat1 = Imgproc.getRotationMatrix2D(new Point(mGray.rows() / 2, mGray.cols() / 2), 90, 1);
        Imgproc.warpAffine(mGray, mGray, rotateMat1, mGray.size());

        if (absoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * 0.2f) > 0) {
                absoluteFaceSize = Math.round(height * 0.2f);
            }
        }
        //检测并显示
        MatOfRect faces = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mGray, faces, 1.1, 3, 1, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        facesArray = faces.toArray();
        if (facesArray.length > 0) {
            for ( i = 0; i < facesArray.length; i++) {    //用框标记
                   // Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 0, 0, 255), 3);
                   //子线程
                    Thread t = new Thread() {
                        public void run() {
                            bitmaps = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(mRgba, bitmaps);
                            ideat++;
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handlers.sendMessage(msg);
                        }
                    };
                    t.start();
            }
        }
        return mRgba;
    }

    //主线程
    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                switch (ideat) {
                    case 1:
                        h1.setImageBitmap(bitmaps);
                        break;
                    case 2:
                        h2.setImageBitmap(bitmaps);
                        break;
                    case 3:
                        h3.setImageBitmap(bitmaps);
                        break;
                    case 4:
                        h4.setImageBitmap(bitmaps);
                        break;
                    case 5:
                        h5.setImageBitmap(bitmaps);
                        break;
                    case 6:
                        h6.setImageBitmap(bitmaps);
                        break;
                    case 7:
                        h7.setImageBitmap(bitmaps);
                        break;
                    case 8:
                        h8.setImageBitmap(bitmaps);
                        break;
                }
            }
        }
    };

    public void saveBitmap(Bitmap bitmap) {
        MyApp appState = ((MyApp) getApplicationContext());
        appState.setScoret(1);
        switch (data)
        {
            case 1:
                appDir = getFilesDir().getAbsolutePath() + "head.jpg";
                break;
            case 2:
                appDir = getFilesDir().getAbsolutePath() + "head1.jpg";
                break;
            case 3:
                appDir = getFilesDir().getAbsolutePath() + "m1.jpg";
                break;
            case 4:
                appDir = getFilesDir().getAbsolutePath() + "m2.jpg";
                break;
            case 5:
                appDir = getFilesDir().getAbsolutePath() + "m3.jpg";
                break;
            case 6:
                appDir = getFilesDir().getAbsolutePath() + "m4.jpg";
                break;
            case 7:
                appDir = getFilesDir().getAbsolutePath() + "n1.jpg";
                break;
            case 8:
                appDir = getFilesDir().getAbsolutePath() + "n2.jpg";
                break;
            case 9:
                appDir = getFilesDir().getAbsolutePath() + "n3.jpg";
                break;
            case 10:
                appDir = getFilesDir().getAbsolutePath() + "n4.jpg";
                break;
        }
        try {
            filePic = new File(appDir);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Intent intent1 = new Intent(this, four_statics_two.class);
            intent1.putExtra("id", id);
            startActivity(intent1);
            this.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.h8, R.id.h7, R.id.h4, R.id.h5, R.id.h6, R.id.h1, R.id.h2, R.id.h3, R.id.fold})
    public void onViewClicked(View view) {
        Bitmap bitmap;
        switch (view.getId()) {
            case R.id.h8:
                h8.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h8.getDrawingCache());
                h8.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h7:
                h7.setDrawingCacheEnabled(true);
                 bitmap = Bitmap.createBitmap(h7.getDrawingCache());
                h7.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h4:
                h4.setDrawingCacheEnabled(true);
                 bitmap = Bitmap.createBitmap(h4.getDrawingCache());
                h4.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h5:
                h5.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h5.getDrawingCache());
                h5.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h6:
                h6.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h6.getDrawingCache());
                h6.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h1:
                h1.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h1.getDrawingCache());
                h1.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h2:
                h2.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h2.getDrawingCache());
                h2.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.h3:
                h3.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(h3.getDrawingCache());
                h3.setDrawingCacheEnabled(false);
                saveBitmap(bitmap);
                break;
            case R.id.fold:
                this.finish();
                break;
        }
    }
}
