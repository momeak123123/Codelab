package com.example.momeak.codelab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
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
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCalcHist;

import org.bytedeco.javacpp.opencv_core;
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

import static org.bytedeco.javacpp.opencv_core.CV_HIST_ARRAY;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_COMP_CORREL;
import static org.bytedeco.javacpp.opencv_imgproc.cvCompareHist;
import static org.bytedeco.javacpp.opencv_imgproc.cvNormalizeHist;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.cvtColor;


public class there_statics extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static String sum;
    private static double results;
    private static double found;
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
    private File filePic;
    private String fileName;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;
    private String appDir;
    private static final String RESULT_FORMAT = "00.0%";
    private Bitmap headbits;
    private Bitmap headsbits;
    private int size = 32;
    private int smallerSize = 8;
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

    static {
        System.loadLibrary("native-lib");
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void saveBitmap(Bitmap bitmap, String name) {
        String appDir = getFilesDir().getAbsolutePath()+ name + ".jpg";
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
                switch (id) {
                    case 30:
                        saveBitmap(headbit, "head");
                        saveBitmap(headsbit, "heads");
                        String head = getFilesDir().getAbsolutePath()+ "head.jpg";
                        String heads = getFilesDir().getAbsolutePath()+ "heads.jpg";
                        double result = CmpPic(head, heads);
                        String one = df.format(result);
                        similarity.setText("相似度为： " + one);
                        break;
                    case 31:
                            similarity(headbit, headsbit);
                            similarity.setText("相似度为： " + sum);
                        break;
                    case 32:
                        hist(headbit, headsbit);
                        String there = df.format(results);
                        similarity.setText("相似度为： " + there);
                        break;
                    case 33:
                        similar(headbit, headsbit);
                        String foul = df.format(1 / (found + 1));
                        similarity.setText("相似度为： " + foul);
                        break;
                    case 34:
                        int five;
                        String fives;
                        five = shae(headbit, headsbit);
                        if (five < 12) {
                            fives = df.format((11 - five) / 10);
                        } else {
                            fives = df.format(0);
                        }
                        similarity.setText("相似度为： " + fives);
                        break;
                    case 35:

                        break;
                    case 36:

                        break;
                }

                break;
        }
    }

    public double CmpPic(String srcPath, String desPath) {
        int l_bins = 20;
        int hist_size[] = {l_bins};

        float v_ranges[] = {0, 100};
        float ranges[][] = {v_ranges};

        opencv_core.IplImage Image1 = cvLoadImage(srcPath, CV_LOAD_IMAGE_GRAYSCALE);
        opencv_core.IplImage Image2 = cvLoadImage(desPath, CV_LOAD_IMAGE_GRAYSCALE);

        opencv_core.IplImage imageArr1[] = {Image1};
        opencv_core.IplImage imageArr2[] = {Image2};

        opencv_core.CvHistogram Histogram1 = opencv_core.CvHistogram.create(1, hist_size,
                CV_HIST_ARRAY, ranges, 1);
        opencv_core.CvHistogram Histogram2 = opencv_core.CvHistogram.create(1, hist_size,
                CV_HIST_ARRAY, ranges, 1);

        cvCalcHist(imageArr1, Histogram1, 0, null);
        cvCalcHist(imageArr2, Histogram2, 0, null);

        cvNormalizeHist(Histogram1, 100.0);
        cvNormalizeHist(Histogram2, 100.0);

        return cvCompareHist(Histogram1, Histogram2, CV_COMP_CORREL);
    }


    public static String similarity(Bitmap bm1, Bitmap bm2) {
        Bitmap bm_one = bm1;
        Bitmap bm_two = bm2;
        //保存图片所有像素个数的数组，图片宽×高
        int[] pixels_one = new int[bm_one.getWidth() * bm_one.getHeight()];
        int[] pixels_two = new int[bm_two.getWidth() * bm_two.getHeight()];
        //获取每个像素的RGB值
        bm_one.getPixels(pixels_one, 0, bm_one.getWidth(), 0, 0, bm_one.getWidth(), bm_one.getHeight());
        bm_two.getPixels(pixels_two, 0, bm_two.getWidth(), 0, 0, bm_two.getWidth(), bm_two.getHeight());
        //如果图片一个像素大于图片2的像素，就用像素少的作为循环条件。避免报错
        int t = 0;
        int f = 0;
        if (pixels_one.length >= pixels_two.length) {
            //对每一个像素的RGB值进行比较
            for (int i = 0; i < pixels_two.length; i++) {
                int clr_one = pixels_one[i];
                int clr_two = pixels_two[i];
                //RGB值一样就加一（以便算百分比）
                if (clr_one == clr_two) {
                    t++;
                } else {
                    f++;
                }
            }
        } else {
            for (int i = 0; i < pixels_one.length; i++) {
                int clr_one = pixels_one[i];
                int clr_two = pixels_two[i];
                if (clr_one == clr_two) {
                    t++;
                } else {
                    f++;
                }
            }

        }
        return percent(t, t + f);
    }

    private static String percent(int divisor, int dividend) {
        final double value = divisor * 1.0 / dividend;
        DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
        sum = df.format(value);
        return sum;
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

        results = Imgproc.compareHist(hist_img1, hist_img2, CV_COMP_CORREL);

        return results;
    }

    public double similar(Bitmap bm1, Bitmap bm2) {
        Bitmap bm_one = bm1;
        Bitmap bm_two = bm2;
        //保存图片所有像素个数的数组，图片宽×高
        int[] pixels_one = new int[bm_one.getWidth() * bm_one.getHeight()];
        int[] pixels_two = new int[bm_two.getWidth() * bm_two.getHeight()];
        //获取每个像素的RGB值
        bm_one.getPixels(pixels_one, 0, bm_one.getWidth(), 0, 0, bm_one.getWidth(), bm_one.getHeight());
        bm_two.getPixels(pixels_two, 0, bm_two.getWidth(), 0, 0, bm_two.getWidth(), bm_two.getHeight());
        double rs = 0;
        for (int i = 0; i < pixels_one.length; i++) {
            try {
                rs += Math.pow(pixels_one[i] - pixels_two[i], 2);
            } catch (Exception e) {
                String txt = "对比失败，请重新上传图片";
                showMessagePositiveDialog(txt);
            }

        }
        found = Math.pow(rs, 0.5);
        return found;
    }

    public static int shae(Bitmap bm1, Bitmap bm2) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bm1, 8, 8);
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bm2, 8, 8);

        Bitmap bmp1 = bitmap1.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp1.getHeight(), bmp1.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp1, img1);
        Bitmap bmp2 = bitmap2.copy(Bitmap.Config.RGB_565, true);
        Mat img2 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img2);

        Mat img1s = new Mat();
        Mat img2s = new Mat();
        cvtColor(img1, img1s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat
        cvtColor(img2, img2s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        bitmap1 = Bitmap.createBitmap(img1s.cols(), img1s.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img1s, bitmap1);
        bitmap2 = Bitmap.createBitmap(img2s.cols(), img2s.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img2s, bitmap2);

        int a = getAvg(bitmap1);
        int b = getAvg(bitmap2);

        String c = binaryString2hexString(getBinary(bitmap1, a));
        String d = binaryString2hexString(getBinary(bitmap2, b));

        char[] s1s = c.toCharArray();
        char[] s2s = d.toCharArray();
        int diffNum = 0;
        for (int i = 0; i < s1s.length; i++) {
            if (s1s[i] != s2s[i]) {
                diffNum++;
            }
        }
        return diffNum;
    }

    public static int getAvg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);

        int avgPixel = 0;
        for (int pixel : pixels) {
            avgPixel += pixel;
        }
        return avgPixel / pixels.length;
    }

    public static String getBinary(Bitmap img, int average) {
        StringBuilder sb = new StringBuilder();

        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int original = pixels[width * i + j];
                if (original >= average) {
                    pixels[width * i + j] = 1;
                } else {
                    pixels[width * i + j] = 0;
                }
                sb.append(pixels[width * i + j]);
            }
        }
        return sb.toString();
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuilder sb = new StringBuilder();
        int iTmp;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            sb.append(Integer.toHexString(iTmp));
        }
        return sb.toString();
    }

    public void pshap(Bitmap bm1, Bitmap bm2) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bm1, 8, 8);
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bm2, 8, 8);

        Bitmap bmp1 = bitmap1.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp1.getHeight(), bmp1.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp1, img1);
        Bitmap bmp2 = bitmap2.copy(Bitmap.Config.RGB_565, true);
        Mat img2 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img2);

        Mat img1s = new Mat();
        Mat img2s = new Mat();
        cvtColor(img1, img1s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat
        cvtColor(img2, img2s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

    }

}
