package com.example.momeak.codelab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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

public class four_face extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

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
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.javaCameraView2)
    JavaCameraView javaCameraView;
    @BindView(R.id.imb)
    ImageView imb;
    private File tempFile;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Bitmap bitmap;
    private QMUITipDialog tipDialog;
    private File filePic;
    private int id;
    private int add;
    private String appDir;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String picture;
    private String names;
    private String pictures;
    private String data;
    private static final String TAG = "OCVSample::Activity";
    private Mat mRgba;
    private Mat mGray;
    private int s;
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier = null; //级联分类器
    private int absoluteFaceSize = 0;
    private Handler handler;
    private InputStream is;
    private File mCascadeFile;
    private Rect[] facesArray;
    private int i;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;
    private Bitmap bitmaps;
    private static final int COMPLETED = 0;
    private boolean pan =false;
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
        setContentView(R.layout.four_face);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        add = (int) intent.getSerializableExtra("add");
        javaCameraView.setVisibility(View.GONE);
        handler = new Handler();
        openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView2);
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
        sharedPreferencesHelper = new SharedPreferencesHelper(four_face.this, "people");
        if (add == 1) {
            picture = id + "0.jpg";
            names = sharedPreferencesHelper.getSharedPreference(picture, "").toString().trim();
            File savePath = new File(getFilesDir().getAbsolutePath() + picture);
            if (savePath.exists()) {
                head.setImageURI(Uri.fromFile(savePath));
                name.setText(names);
            } else {
                showMessagePositiveDialog();
            }
        } else {
            if (add == 0) {
                if (sharedPreferencesHelper.contain("data") == true) {
                    data = sharedPreferencesHelper.getSharedPreference("data", "").toString().trim();
                    pictures = data + "0.jpg";
                    int datas = Integer.parseInt(data);
                    data = String.valueOf(datas + 1);

                } else {
                    data = "1";
                    pictures = "00.jpg";
                }
            }
        }
    }


    //弹窗
    private void showMessagePositiveDialog() {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage("照片打开失败,请重新打开")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Intent intent1 = new Intent();
                        intent1.setClass(four_face.this, four_data.class);
                        startActivity(intent1);
                    }
                })
                .create(mCurrentDialogStyle).show();
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
            cascadeClassifier.detectMultiScale(mGray, faces, 1.1, 10, 1, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        facesArray = faces.toArray();
        if (facesArray.length > 0) {
            for ( i = 0; i < facesArray.length; i++) {    //用框标记
                if (i == 0) {
                    // Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 0, 0, 255), 3);
                    //子线程
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
    //主线程
    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                javaCameraView.setVisibility(View.GONE);
                head.setVisibility(View.VISIBLE);
                numberOfFaceDetected=Bitmap_face.detectFace(bitmaps);
                if(numberOfFaceDetected==0)
                {
                    String txt = "照片检测失败，请重新上传";
                    showMessagePositiveDialogd(txt);

                }
                else
                {
                    head.setImageBitmap(bitmaps);
                    saveBitmap(bitmaps);
                }

            }
        }
    };

    public void saveBitmap(Bitmap bitmap) {
        if (add == 1) {
            appDir = getFilesDir().getAbsolutePath() + picture;
        } else {
            if (add == 0) {
                appDir = getFilesDir().getAbsolutePath() + pictures;
            }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditTextDialog() {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setPlaceholder("在此输入您的昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            name.setText(text);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(four_face.this, "请填入昵称", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick({R.id.but, R.id.imageView10, R.id.ok, R.id.imb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView10:
                Intent intent = new Intent();
                intent.setClass(four_face.this, four_data.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.but:
                javaCameraView.setVisibility(View.VISIBLE);
                head.setVisibility(View.GONE);
                break;
            case R.id.imb:
                showEditTextDialog();
                break;
            case R.id.ok:
                if (add == 1) {
                    sharedPreferencesHelper.put(picture, name.getText().toString().trim());
                    Intent intent1 = new Intent();
                    intent1.setClass(four_face.this, four_data.class);
                    startActivity(intent1);
                    this.finish();
                } else {
                    if (add == 0) {
                        if (name.getText().toString() != null) {
                            sharedPreferencesHelper.put(pictures, name.getText().toString().trim());
                            sharedPreferencesHelper.put("data", data);
                            Intent intent1 = new Intent();
                            intent1.setClass(four_face.this, four_data.class);
                            startActivity(intent1);
                            this.finish();
                        } else {
                            String txt = "请输入姓名";
                            showMessagePositiveDialogd(txt);
                        }
                    }
                }
                break;
        }
    }

    //弹窗
    private void showMessagePositiveDialogd(String txt) {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(txt)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
}
