package com.example.momeak.codelab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.bytedeco.javacpp.opencv_imgproc.CV_COMP_CORREL;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class four_statics_two extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    @BindView(R.id.head)
    QMUIRadiusImageView head;
    @BindView(R.id.head2)
    QMUIRadiusImageView head2;
    @BindView(R.id.m2)
    QMUIRadiusImageView m2;
    @BindView(R.id.m3)
    QMUIRadiusImageView m3;
    @BindView(R.id.m4)
    QMUIRadiusImageView m4;
    @BindView(R.id.n2)
    QMUIRadiusImageView n2;
    @BindView(R.id.n3)
    QMUIRadiusImageView n3;
    @BindView(R.id.n4)
    QMUIRadiusImageView n4;
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.fold2)
    ImageView fold2;
    @BindView(R.id.m1)
    QMUIRadiusImageView m1;
    @BindView(R.id.n1)
    QMUIRadiusImageView n1;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.imageView13)
    ImageView imageView13;
    @BindView(R.id.textView8)
    TextView textView8;

    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private Bitmap photoBmp;
    private int headint;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;
    private static final String RESULT_FORMAT = "00.0%";
    private int id;
    private Bitmap headbit1;
    private Bitmap headbit2;
    private Bitmap headbit3;
    private Bitmap headbit4;
    private Bitmap headbit5;
    private Bitmap headbit10;
    private Bitmap headbit11;
    private Bitmap headbit12;
    private Bitmap headbit13;
    private int data;
    private int s;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.four_statics_one);
        ButterKnife.bind(this);
        FileInputStream fis;
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        MyApp appState = ((MyApp) getApplicationContext());
        s = appState.getScoret();
        switch (id) {
            case 40:
                n1.setVisibility(View.GONE);
                n2.setVisibility(View.GONE);
                n3.setVisibility(View.GONE);
                n4.setVisibility(View.GONE);
                m1.setVisibility(View.GONE);
                m2.setVisibility(View.GONE);
                m3.setVisibility(View.GONE);
                m4.setVisibility(View.GONE);
                if (s == 1) {
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "head.jpg");
                        headbit = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit.isRecycled())
                            headbit.recycle();
                        detectFace(bitmapt);
                        headbit = drawFace(bitmapt);
                        head.setImageBitmap(headbit);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "head1.jpg");
                        headbit1 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit1.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit1.isRecycled())
                            headbit1.recycle();
                        detectFace(bitmapt);
                        headbit1 = drawFace(bitmapt);
                        head2.setImageBitmap(headbit1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 41:
                head2.setVisibility(View.GONE);
                m1.setVisibility(View.GONE);
                m2.setVisibility(View.GONE);
                m3.setVisibility(View.GONE);
                m4.setVisibility(View.GONE);
                if (s == 1) {
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "head.jpg");
                        headbit = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit.isRecycled())
                            headbit.recycle();
                        detectFace(bitmapt);
                        headbit = drawFace(bitmapt);
                        head.setImageBitmap(headbit);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n1.jpg");
                        headbit10 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit10.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit10.isRecycled())
                            headbit10.recycle();
                        detectFace(bitmapt);
                        headbit10 = drawFace(bitmapt);
                        n1.setImageBitmap(headbit10);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n2.jpg");
                        headbit11 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit11.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit11.isRecycled())
                            headbit11.recycle();
                        detectFace(bitmapt);
                        headbit11 = drawFace(bitmapt);
                        n2.setImageBitmap(headbit11);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n3.jpg");
                        headbit12 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit12.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit12.isRecycled())
                            headbit12.recycle();
                        detectFace(bitmapt);
                        headbit12 = drawFace(bitmapt);
                        n3.setImageBitmap(headbit12);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n4.jpg");
                        headbit13 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit13.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit13.isRecycled())
                            headbit13.recycle();
                        detectFace(bitmapt);
                        headbit13 = drawFace(bitmapt);
                        n4.setImageBitmap(headbit13);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 42:
                head.setVisibility(View.GONE);
                head2.setVisibility(View.GONE);
                if (s == 1) {
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "m1.jpg");
                        headbit2 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit2.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit2.isRecycled())
                            headbit2.recycle();
                        detectFace(bitmapt);
                        headbit2 = drawFace(bitmapt);
                        m1.setImageBitmap(headbit2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "m2.jpg");
                        headbit3 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit3.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit3.isRecycled())
                            headbit3.recycle();
                        detectFace(bitmapt);
                        headbit3 = drawFace(bitmapt);
                        m2.setImageBitmap(headbit3);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "m3.jpg");
                        headbit4 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit4.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit4.isRecycled())
                            headbit4.recycle();
                        detectFace(bitmapt);
                        headbit4 = drawFace(bitmapt);
                        m3.setImageBitmap(headbit4);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "m4.jpg");
                        headbit5 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit5.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit5.isRecycled())
                            headbit5.recycle();
                        detectFace(bitmapt);
                        headbit5 = drawFace(bitmapt);
                        m4.setImageBitmap(headbit5);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n1.jpg");
                        headbit10 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit10.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit10.isRecycled())
                            headbit10.recycle();
                        detectFace(bitmapt);
                        headbit10 = drawFace(bitmapt);
                        n1.setImageBitmap(headbit10);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n2.jpg");
                        headbit11 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit11.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit11.isRecycled())
                            headbit11.recycle();
                        detectFace(bitmapt);
                        headbit11 = drawFace(bitmapt);
                        n2.setImageBitmap(headbit11);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n3.jpg");
                        headbit12 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit12.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit12.isRecycled())
                            headbit12.recycle();
                        detectFace(bitmapt);
                        headbit12 = drawFace(bitmapt);
                        n3.setImageBitmap(headbit12);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fis = new FileInputStream(getFilesDir().getAbsolutePath() + "n4.jpg");
                        headbit13 = BitmapFactory.decodeStream(fis);
                        Bitmap bitmapt = headbit13.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit13.isRecycled())
                            headbit13.recycle();
                        detectFace(bitmapt);
                        headbit13 = drawFace(bitmapt);
                        n4.setImageBitmap(headbit13);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 43:

                break;
            case 44:

                break;
            case 45:

                break;
            case 46:

                break;
        }


    }

    //android FaceDetector人脸检测
    private void detectFace(Bitmap bitmap) {
        int numberOfFace = 12;
        FaceDetector myFaceDetect;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        myFace = new FaceDetector.Face[numberOfFace];
        myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
        numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);

    }

    //人脸画框
    private Bitmap drawFace(Bitmap bitmap) {
        Bitmap bmp = bitmap;
        for (int i = 0; i < numberOfFaceDetected; i++) {
            FaceDetector.Face face = myFace[i];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            float myEyesDistance = face.eyesDistance();
            bmp = Bitmap.createBitmap(bitmap, (int) (myMidPoint.x - myEyesDistance * 1.8), (int) (myMidPoint.y - myEyesDistance * 2.2), (int) (myEyesDistance * 3.6), (int) (myEyesDistance * 4.4));
        }
        return bmp;
    }

    public static Double hist(Bitmap bm1, Bitmap bm2) {
        //  Mat转bitmap
        //   Bitmap bitmap= Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        //   Utils.matToBitmap(mat, bitmap);


        //  bitmap转Mat
        Bitmap bmp2 = bm1.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Bitmap bmp3 = bm2.copy(Bitmap.Config.RGB_565, true);
        Mat img2 = new Mat(bmp3.getHeight(), bmp3.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp3, img2);

        List<Mat> listImage1 = new ArrayList<>();
        List<Mat> listImage2 = new ArrayList<>();

        Mat hsv_img1 = new Mat();
        Mat hsv_img2 = new Mat();

        cvtColor(img1, hsv_img1, COLOR_BGR2HSV);
        cvtColor(img2, hsv_img2, COLOR_BGR2HSV);

        listImage1.add(hsv_img1);
        listImage2.add(hsv_img2);

        Mat hist_img1 = new Mat();
        Mat hist_img2 = new Mat();

        MatOfFloat ranges = new MatOfFloat(0, 255);
        MatOfInt histSize = new MatOfInt(50);
        MatOfInt channels = new MatOfInt(0);

        Imgproc.calcHist(listImage1, channels, new Mat(), hist_img1, histSize, ranges);
        Imgproc.calcHist(listImage2, channels, new Mat(), hist_img2, histSize, ranges);

        Core.normalize(hist_img1, hist_img1, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(hist_img2, hist_img2, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        double results = Imgproc.compareHist(hist_img1, hist_img2, CV_COMP_CORREL);

        return results;
    }


    private void showLongMessageDialog(String txt) {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("标题")
                .setMessage(txt)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick({R.id.head, R.id.head2, R.id.m2, R.id.m3, R.id.m4, R.id.n2, R.id.n3, R.id.n4, R.id.fold2, R.id.m1, R.id.n1, R.id.imageView13})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head:
                Intent intent1 = new Intent(this, four_javacamera.class);
                intent1.putExtra("data", 1);
                intent1.putExtra("id", id);
                startActivity(intent1);
                this.finish();
                break;
            case R.id.head2:
                Intent intent2 = new Intent(this, four_javacamera.class);
                intent2.putExtra("data", 2);
                intent2.putExtra("id", id);
                startActivity(intent2);
                this.finish();
                break;
            case R.id.fold2:
                this.finish();
                break;
            case R.id.m2:
                Intent intent4 = new Intent(this, four_javacamera.class);
                intent4.putExtra("data", 4);
                intent4.putExtra("id", id);
                startActivity(intent4);
                this.finish();
                break;
            case R.id.m3:
                Intent intent5 = new Intent(this, four_javacamera.class);
                intent5.putExtra("data", 5);
                intent5.putExtra("id", id);
                startActivity(intent5);
                this.finish();
                break;
            case R.id.m4:
                Intent intent6 = new Intent(this, four_javacamera.class);
                intent6.putExtra("data", 6);
                intent6.putExtra("id", id);
                startActivity(intent6);
                this.finish();
                break;
            case R.id.m1:
                Intent intent3 = new Intent(this, four_javacamera.class);
                intent3.putExtra("data", 3);
                intent3.putExtra("id", id);
                startActivity(intent3);
                this.finish();
                break;
            case R.id.n1:
                Intent intent7 = new Intent(this, four_javacamera.class);
                intent7.putExtra("data", 7);
                intent7.putExtra("id", id);
                startActivity(intent7);
                this.finish();
                break;
            case R.id.n2:
                Intent intent8 = new Intent(this, four_javacamera.class);
                intent8.putExtra("data", 8);
                intent8.putExtra("id", id);
                startActivity(intent8);
                this.finish();
                break;
            case R.id.n3:
                Intent intent9 = new Intent(this, four_javacamera.class);
                intent9.putExtra("data", 9);
                intent9.putExtra("id", id);
                startActivity(intent9);
                this.finish();
                break;
            case R.id.n4:
                Intent intent10 = new Intent(this, four_javacamera.class);
                intent10.putExtra("data", 10);
                intent10.putExtra("id", id);
                startActivity(intent10);
                this.finish();
                break;
            case R.id.imageView13:
                DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
                switch (id) {
                    case 40:
                        String one = df.format(hist(headbit, headbit1));
                        showLongMessageDialog("相似度为： " + one);
                        break;
                    case 41:
                        String two = df.format(hist(headbit, headbit10));
                        String two1 = df.format(hist(headbit, headbit11));
                        String two2 = df.format(hist(headbit, headbit12));
                        String two3 = df.format(hist(headbit, headbit13));
                        showLongMessageDialog("1:n1的相似度为： " + two + "\n1:n2的相似度为： " + two1 + "\n1:n3的相似度为： " + two2 + "\n1:n4的相似度为： " + two3);
                        break;
                    case 42:
                        String there = df.format(hist(headbit2, headbit10));
                        String there1 = df.format(hist(headbit2, headbit11));
                        String there2 = df.format(hist(headbit2, headbit12));
                        String there3 = df.format(hist(headbit2, headbit13));
                        String four = df.format(hist(headbit3, headbit10));
                        String four1 = df.format(hist(headbit3, headbit11));
                        String four2 = df.format(hist(headbit3, headbit12));
                        String four3 = df.format(hist(headbit3, headbit13));
                        String five = df.format(hist(headbit4, headbit10));
                        String five1 = df.format(hist(headbit4, headbit11));
                        String five2 = df.format(hist(headbit4, headbit12));
                        String five3 = df.format(hist(headbit4, headbit13));
                        String six = df.format(hist(headbit5, headbit10));
                        String six1 = df.format(hist(headbit5, headbit11));
                        String six2 = df.format(hist(headbit5, headbit12));
                        String six3 = df.format(hist(headbit5, headbit13));
                        showLongMessageDialog("m1:n1的相似度为： " + there + "\nm1:n2的相似度为： " + there1 + "\nm1:n3的相似度为： " + there2 + "\nm1:n4的相似度为： " + there3 + "\nm2:n1的相似度为： " + four + "\nm2:n2的相似度为： " + four1 + "\nm2:n3的相似度为： " + four2 + "\nm2:n4的相似度为： " + four3 + "\nm3:n1的相似度为： " + five + "\nm3:n2的相似度为： " + five1 + "\nm3:n3的相似度为： " + five2 + "\nm3:n4的相似度为： " + five3 + "\nm4:n1的相似度为： " + six + "\nm4:n2的相似度为： " + six1 + "\nm4:n3的相似度为： " + six2 + "\nm4:n4的相似度为： " + six3);
                        break;
                    case 43:

                        break;
                    case 44:

                        break;
                    case 45:

                        break;
                    case 46:

                        break;
                }
                for (int i = 0; i < 10; i++) {
                    switch (i) {
                        case 0:
                            file = new File(getFilesDir().getAbsolutePath() + "head.jpg");
                            break;
                        case 1:
                            file = new File(getFilesDir().getAbsolutePath() + "head1.jpg");
                            break;
                        case 2:
                            file = new File(getFilesDir().getAbsolutePath() + "n1.jpg");
                            break;
                        case 3:
                            file = new File(getFilesDir().getAbsolutePath() + "n2.jpg");
                            break;
                        case 4:
                            file = new File(getFilesDir().getAbsolutePath() + "n3.jpg");
                            break;
                        case 5:
                            file = new File(getFilesDir().getAbsolutePath() + "n4.jpg");
                            break;
                        case 6:
                            file = new File(getFilesDir().getAbsolutePath() + "m1.jpg");
                            break;
                        case 7:
                            file = new File(getFilesDir().getAbsolutePath() + "m2.jpg");
                            break;
                        case 8:
                            file = new File(getFilesDir().getAbsolutePath() + "m3.jpg");
                            break;
                        case 9:
                            file = new File(getFilesDir().getAbsolutePath() + "m4.jpg");
                            break;
                    }
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                }
                break;
        }
    }
}
