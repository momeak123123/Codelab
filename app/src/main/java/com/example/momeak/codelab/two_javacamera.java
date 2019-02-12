package com.example.momeak.codelab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class two_javacamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
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

    private void initializeOpenCVDependencies() {
        try {
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            switch (id) {
                case 1:
                    is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2); //OpenCV的人脸模型文件： haarcascade_frontalface_alt2.xml
                    mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt2.xml");
                    break;
                case 2:
                    is = getResources().openRawResource(R.raw.haarcascade_profileface);
                    mCascadeFile = new File(cascadeDir, "haarcascade_profileface.xml");
                    break;
                case 3:
                    is = getResources().openRawResource(R.raw.haarcascade_eye);
                    mCascadeFile = new File(cascadeDir, "haarcascade_eye.xml");
                case 4:
                    is = getResources().openRawResource(R.raw.haarcascade_upperbody);
                    mCascadeFile = new File(cascadeDir, "haarcascade_upperbody.xml");
                    break;
                case 5:
                    is = getResources().openRawResource(R.raw.haarcascade_lowerbody);
                    mCascadeFile = new File(cascadeDir, "haarcascade_lowerbody.xml");
                    break;
                case 6:
                    is = getResources().openRawResource(R.raw.haarcascade_fullbody);
                    mCascadeFile = new File(cascadeDir, "haarcascade_fullbody.xml");
                    break;
            }
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
        setContentView(R.layout.activity_javacamera);
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
        if(s==1)
        {
            Core.flip(mRgba, mRgba, 1);
            Core.flip(mGray, mGray, 1);
        }

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
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            for (int i = 0; i < facesArray.length; i++) {    //用框标记
                Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 0, 0, 255), 3);
            }
        }
        return mRgba;
    }

    @OnClick({R.id.fold})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold:
                this.finish();
                break;
        }
    }
}
