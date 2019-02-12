package com.example.momeak.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class statics extends AppCompatActivity {
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
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
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.fold2)
    ImageView fold2;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.imageView12)
    ImageView imageView12;
    @BindView(R.id.imageView13)
    ImageView imageView13;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    //调用照相机返回图片文件
    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private int id;
    private File filePic;
    private String fileName;
    private int numberOfFaceDetected;
    private FaceDetector.Face[] myFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_statics);
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
            txt = "图片未检测到人脸，请重新上传";
            showMessagePositiveDialog(txt);

        }

    }
    //人脸画框
    private Bitmap drawFace(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        // canvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);
        for (int i = 0; i < numberOfFaceDetected; i++) {
            FaceDetector.Face face = myFace[i];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            float myEyesDistance = face.eyesDistance();
            canvas.drawRect((int) (myMidPoint.x - myEyesDistance * 1.8),
                    (int) (myMidPoint.y - myEyesDistance * 2.2),
                    (int) (myMidPoint.x + myEyesDistance * 1.8),
                    (int) (myMidPoint.y + myEyesDistance * 2.2), myPaint);
        }
        return bitmap;
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


        /**
         * 底片
         *
         * @param bitmap
         * @return
         */
    public static Bitmap handleImagePix(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r, g, b, a;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        bitmap.getPixels(imgPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < imgPixels.length; i++) {
            r = Color.red(imgPixels[i]);
            g = Color.green(imgPixels[i]);
            b = Color.blue(imgPixels[i]);
            a = Color.alpha(imgPixels[i]);

            r = limit(255 - r);
            g = limit(255 - g);
            b = limit(255 - b);

            newImgPixels[i] = Color.argb(a, r, g, b);
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newBitmap.setPixels(newImgPixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }
    /**
     * 限制argb取值范围
     *
     * @param value
     * @return
     */
    public static int limit(int value) {
        if (value > 255) {
            return 255;
        } else if (value < 0) {
            return 0;
        }
        return value;
    }



    /**
     * 怀旧
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleImagePix2(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r, g, b, a, newR, newG, newB;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        bitmap.getPixels(imgPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < imgPixels.length; i++) {
            r = Color.red(imgPixels[i]);
            g = Color.green(imgPixels[i]);
            b = Color.blue(imgPixels[i]);
            a = Color.alpha(imgPixels[i]);

            newR = limit((int) (0.393 * r + 0.769 * g + 0.189 * b));
            newG = limit((int) (0.249 * r + 0.686 * g + 0.168 * b));
            newB = limit((int) (0.272 * r + 0.534 * g + 0.131 * b));

            newImgPixels[i] = Color.argb(a, newR, newG, newB);
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newBitmap.setPixels(newImgPixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 浮雕
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleImagePix3(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r, g, b, a, cr, cg, cb;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        bitmap.getPixels(imgPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < imgPixels.length - 1; i++) {
            r = Color.red(imgPixels[i]);
            g = Color.green(imgPixels[i]);
            b = Color.blue(imgPixels[i]);
            a = Color.alpha(imgPixels[i]);

            cr = Color.red(imgPixels[i + 1]);
            cg = Color.green(imgPixels[i + 1]);
            cb = Color.blue(imgPixels[i + 1]);

            r = limit(cr - r + 127);
            g = limit(cg - g + 127);
            b = limit(cb - b + 127);

            newImgPixels[i] = Color.argb(a, r, g, b);
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newBitmap.setPixels(newImgPixels, 0, width, 0, 0, width, height);
        return newBitmap;
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

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//发起剪切动作
        intent.setDataAndType(uri, "image/*");//设置剪切图片的uri和类型
        intent.putExtra("crop", "true");//剪切动作的信号
        intent.putExtra("aspectX", 3);//x和y是否等比缩放
        intent.putExtra("aspectY", 4);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 400);//剪切后图片的尺寸
        intent.putExtra("return-data", true);//是否把剪切后的图片通过data返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", true);  //关闭面部识别

        //设置剪切的图片保存位置
        Uri cropUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/zxy/image/crop.png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = Uri.fromFile(tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    headbit = bundle.getParcelable("data");
                    head.setImageBitmap(headbit);
                }
                break;
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
                showListPopups(imageView11,1);
                break;
            case R.id.imageView12:
                switch (id) {
                    case 12:
                        handleImagePix(headbit);
                        headsbit = handleImagePix(headbit).copy(Bitmap.Config.RGB_565, true);
                        heads.setImageBitmap(headsbit);
                        break;
                    case 13:
                        handleImagePix2(headbit);
                        headsbit = handleImagePix2(headbit).copy(Bitmap.Config.RGB_565, true);
                        heads.setImageBitmap(headsbit);
                        break;
                    case 14:
                        handleImagePix3(headbit);
                        headsbit = handleImagePix3(headbit).copy(Bitmap.Config.RGB_565, true);
                        heads.setImageBitmap(headsbit);
                        break;
                    case 20:
                        headsbit = headbit.copy(Bitmap.Config.RGB_565, true);
                        if (!headbit.isRecycled())
                            headbit.recycle();
                        detectFace(headsbit);
                        drawFace(headsbit);
                        heads.setImageBitmap(drawFace(headsbit));
                        break;
                }


                break;
            case R.id.imageView13:
                SaveBitmap save = new SaveBitmap();
                save.saveBitmaps(headsbit);
                Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intents);
                break;
        }
    }

}
