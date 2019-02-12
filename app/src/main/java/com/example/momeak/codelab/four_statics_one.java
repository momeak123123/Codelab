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

public class four_statics_one extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.four_statics_one);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
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
                break;
            case 41:
                head2.setVisibility(View.GONE);
                m1.setVisibility(View.GONE);
                m2.setVisibility(View.GONE);
                m3.setVisibility(View.GONE);
                m4.setVisibility(View.GONE);
                break;
            case 42:
                head.setVisibility(View.GONE);
                head2.setVisibility(View.GONE);
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
                    Uri contentUri = Uri.fromFile(tempFile);
                    if (contentUri != null) {
                        try {
                            photoBmp = getBitmapFormUri(this, contentUri);
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
                            photoBmp = getBitmapFormUri(this, uri);
                            image(photoBmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
        }
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 判断第一张，第二张图片
     *
     * @return
     * @headint 第几个图片
     */
    private void image(Bitmap bitmap) {
        Bitmap bitmaps = bitmap.copy(Bitmap.Config.RGB_565, true);
        if (!bitmap.isRecycled())
            bitmap.recycle();
        detectFace(bitmaps);
        switch (headint) {
            case 0:
                headbit = drawFace(bitmaps);
                head.setImageBitmap(headbit);
                break;
            case 1:
                headbit1 = drawFace(bitmaps);
                head2.setImageBitmap(headbit1);
                break;
            case 2:
                headbit2 = drawFace(bitmaps);
                m1.setImageBitmap(headbit2);
                break;
            case 3:
                headbit3 = drawFace(bitmaps);
                m2.setImageBitmap(headbit3);
                break;
            case 4:
                headbit4 = drawFace(bitmaps);
                m3.setImageBitmap(headbit4);
                break;
            case 5:
                headbit5 = drawFace(bitmaps);
                m4.setImageBitmap(headbit5);
                break;
            case 6:
                headbit10 = drawFace(bitmaps);
                n1.setImageBitmap(headbit10);
                break;
            case 7:
                headbit11 = drawFace(bitmaps);
                n2.setImageBitmap(headbit11);
                break;
            case 8:
                headbit12 = drawFace(bitmaps);
                n3.setImageBitmap(headbit12);
                break;
            case 9:
                headbit13 = drawFace(bitmaps);
                n4.setImageBitmap(headbit13);
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
        if (myFace == null) {
            String txt;
            if (headint == 0) {
                txt = "第一张图片未检测到人脸，请重新上传";
                showMessagePositiveDialog(txt);
            } else {
                txt = "第二张图片未检测到人脸，请重新上传";
                showMessagePositiveDialog(txt);
            }
        }

    }

    //人脸画框
    private Bitmap drawFace(Bitmap bitmap) {
        Bitmap bmp = null;
        for (int i = 0; i < numberOfFaceDetected; i++) {
            FaceDetector.Face face = myFace[i];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            float myEyesDistance = face.eyesDistance();
            bmp = Bitmap.createBitmap(bitmap, (int) (myMidPoint.x - myEyesDistance * 1.8), (int) (myMidPoint.y - myEyesDistance * 2.2), (int) (myEyesDistance * 3.6), (int) (myEyesDistance * 4.4));
        }
        return bmp;
    }

    //弹窗
    private void showMessagePositiveDialog(String txt) {
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
                showListPopups(head, 1);
                headint = 0;
                break;
            case R.id.head2:
                showListPopups(head2, 1);
                headint = 1;
                break;
            case R.id.fold2:
                this.finish();
                break;
            case R.id.m2:
                showListPopups(m2, 1);
                headint = 3;
                break;
            case R.id.m3:
                showListPopups(m3, 1);
                headint = 4;
                break;
            case R.id.m4:
                showListPopups(m4, 1);
                headint = 5;
                break;
            case R.id.m1:
                showListPopups(m1, 1);
                headint = 2;
                break;
            case R.id.n1:
                showListPopups(n1, 1);
                headint = 6;
                break;
            case R.id.n2:
                showListPopups(n2, 1);
                headint = 7;
                break;
            case R.id.n3:
                showListPopups(n3, 1);
                headint = 8;
                break;
            case R.id.n4:
                showListPopups(n4, 1);
                headint = 9;
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
                        showLongMessageDialog("m1:n1的相似度为： "+there + "\nm1:n2的相似度为： "+ there1 +  "\nm1:n3的相似度为： " + there2 + "\nm1:n4的相似度为： " + there3 + "\nm2:n1的相似度为： " + four+ "\nm2:n2的相似度为： " + four1 + "\nm2:n3的相似度为： " + four2 + "\nm2:n4的相似度为： "+ four3 + "\nm3:n1的相似度为： " + five  + "\nm3:n2的相似度为： " + five1 + "\nm3:n3的相似度为： " + five2 + "\nm3:n4的相似度为： " + five3 + "\nm4:n1的相似度为： " + six + "\nm4:n2的相似度为： " + six1 + "\nm4:n3的相似度为： " + six2 + "\nm4:n4的相似度为： " + six3);
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
                break;
        }
    }
}
