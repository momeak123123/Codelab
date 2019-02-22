package com.example.momeak.codelab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.CV_COMP_CORREL;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class five_unlock extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.fold3)
    ImageView fold3;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.test1)
    QMUIRoundButton test1;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.javaCameraView)
    JavaCameraView javaCameraView;
    @BindView(R.id.head3)
    QMUIRadiusImageView head3;
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
    private int ideat = 0;
    private static final int COMPLETED = 0;
    private Bitmap bitmaps;
    private File filePic;
    private String appDir;
    private int data;
    private Rect[] facesArray;
    private int i;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;
    private static final String RESULT_FORMAT = "00.0%";
    private Bitmap bitmap;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private double sums;
    private String name;
    private int z;
    private int j;
    private QMUITipDialog tipDialog;
    private Bitmap bitmaps1;
    private int datas;
    private boolean pan = false;
    private Bitmap bitmapt;
    private Mat image;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.yellow));
        }
        setContentView(R.layout.five_unlock);
        ButterKnife.bind(this);
        javaCameraView.setVisibility(View.GONE);
        handler = new Handler();
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView);
        openCvCameraView.setCameraIndex(1); //摄像头索引        -1/0：后置双摄     1：前置
        openCvCameraView.setCvCameraViewListener(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(five_unlock.this, "people");
        String data = sharedPreferencesHelper.getSharedPreference("data", "").toString().trim();
        datas = Integer.parseInt(data);
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
            cascadeClassifier.detectMultiScale(mGray, faces, 1.1, 9, 1, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        facesArray = faces.toArray();
        if (facesArray.length > 0) {
            for (s = 0; s < facesArray.length; s++) {    //用框标记
                //Imgproc.rectangle(mRgba, facesArray[s].tl(), facesArray[s].br(), new Scalar(255, 0, 0, 255), 3);
                if (s == 0) {
                    Thread t = new Thread() {
                        public void run() {
                            bitmaps = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(mRgba, bitmaps);
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handlers.sendMessage(msg);

                        }
                    };
                    t.start();
                }

            }
        }
        return mRgba;
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                int ddl = 0;
                double sum = 0;
                int ddls = 0;
                int ddlt = 0;
                double sumt = 0;
                String[] pic = {i + "0.jpg", i + "1.jpg", i + "2.jpg", i + "3.jpg", i + "4.jpg", i + "5.jpg", i + "6.jpg", i + "7.jpg", i + "8.jpg", i + "9.jpg"};
                imageView3.setVisibility(View.GONE);
                javaCameraView.setVisibility(View.GONE);
                bitmapt = Bitmap_face.drawFace(bitmaps);
                for (i = 0; i < datas; i++) {
                    String picture = i + "0.jpg";
                    sums = facehist(picture);
                    if (sums > sum) {
                        ddl = i;
                        sum = sums;
                    }
                }
                sumt = sums;
                for (int j = 1; j < 10; j++) {
                    File file = new File(getFilesDir().getAbsolutePath() + pic[j]);
                    if (!file.exists()) {
                        ddlt = j;
                        break;
                    }
                }
                for (int j = 0; j < ddlt; j++) {
                    sums = facehist(pic[j]);
                    if (sums > sumt) {
                        ddls = j;
                        sumt = sums;
                    }
                }

                DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
                String one = df.format(sumt);

                if (sumt > 0.8) {
                    if (sumt < 0.85) {
                        Bitmap_save save = new Bitmap_save();
                        if(bitmaps!=null)
                        {
                            save.saveBitmaps(bitmaps, pic[ddlt]);

                        }
                        else
                        {
                            test1.setText("点击重新识别");
                            textView3.setText("相似度：" + one + "\n识别率过低，无法识别，请确保此人信息已录入");
                        }
                    }
                    head3.setImageBitmap(bitmaps);
                    name = ddl + "0.jpg";
                    String names = sharedPreferencesHelper.getSharedPreference(name, "").toString().trim();
                    test1.setText(names);
                    textView3.setText("相似度：" + one + "\n点击姓名重新识别");

                } else {
                    head3.setImageBitmap(bitmaps);
                    test1.setText("点击重新识别");
                    textView3.setText("相似度：" + one + "\n识别率过低，无法识别，请确保此人信息已录入");
                }
            }
        }
    };

    public Double facehist(String picture) {
        File file = new File(getFilesDir().getAbsolutePath() + picture);
        if (file.exists() && file.isFile()) {
            Bitmap bitmapa = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath() + picture);
            if (pan == true) {
                try {
                    bitmap = bitmapa;
                    sums = Bitmap_dispose.hist(bitmap, bitmaps);
                } catch (Exception e) {
                }
            } else {
                try {
                    bitmap = Bitmap_face.drawFace(bitmapa);
                    sums = Bitmap_dispose.hist(bitmap, bitmapt);
                } catch (Exception e) {
                }
            }

        }
        return sums;
    }


    @OnClick({R.id.fold3, R.id.test1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold3:
                this.finish();
                break;
            case R.id.test1:
                test1.setText("正在识别");
                textView3.setText("请正对手机，确保光线充足");
                imageView3.setVisibility(View.GONE);
                javaCameraView.setVisibility(View.VISIBLE);
                break;
        }
    }

}
