package com.example.momeak.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

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
    private static final String TAG = "OCVSample::Activity";

    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    @BindView(R.id.imageView29)
    ImageView imageView29;
    @BindView(R.id.imageView30)
    ImageView imageView30;
    @BindView(R.id.imageView34)
    ImageView imageView34;
    @BindView(R.id.imageView35)
    ImageView imageView35;
    @BindView(R.id.imageView36)
    ImageView imageView36;
    @BindView(R.id.imageView37)
    ImageView imageView37;
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.fold2)
    ImageView fold2;
    @BindView(R.id.head)
    QMUIRadiusImageView head;
    @BindView(R.id.imageView11)
    ImageView imageView11;
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
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.heads)
    QMUIRadiusImageView heads;
    @BindView(R.id.imageView31)
    ImageView imageView31;
    @BindView(R.id.imageView32)
    ImageView imageView32;
    @BindView(R.id.imageView33)
    ImageView imageView33;
    @BindView(R.id.textView5)
    TextView textView5;

    //调用照相机返回图片文件
    private File tempFile;
    private Bitmap headbit;
    private Bitmap headsbit;
    private int id;
    private static final int COMPLETED = 0;
    private static int numberOfFaceDetected;
    private static FaceDetector.Face[] myFace;
    QMUITipDialog tipDialog;
    private File filePic;
    private String fileName;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    System.loadLibrary("native-lib");
                    System.loadLibrary("opencv_java3");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_statics);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = Uri.fromFile(tempFile);
                        try {
                            headbit = Bitmap_compress.getBitmapFormUri(this, contentUri);
                            head.setImageBitmap(headbit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            headbit = Bitmap_compress.getBitmapFormUri(this, Uri.fromFile(tempFile));
                            head.setImageBitmap(headbit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    try {
                        headbit = Bitmap_compress.getBitmapFormUri(this, uri);
                        head.setImageBitmap(headbit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void saveBitmaps(Bitmap bitmap) {
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

    @OnClick({R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.fold2, R.id.textView5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fold2:
                this.finish();
                break;
            case R.id.imageView12:
                showListPopups(imageView12, 1);
                break;
            case R.id.imageView11:
                if (headbit != null) {

                    tipDialog = new QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("正在加载")
                            .create();
                    tipDialog.show();
                    Thread t = new Thread() {
                        public void run() {
                            switch (id) {
                                case 1:
                                    headsbit = Bitmap_face.drawFaces(headbit);
                                    break;
                                case 2:
                                    headsbit = Bitmap_dispose.handleImagePix(headbit);
                                    break;
                                case 3:
                                    headsbit = Bitmap_dispose.handleImagePix2(headbit);
                                    break;
                                case 4:
                                    headsbit = Bitmap_dispose.handleImagePix3(headbit);
                                    break;
                                case 5:
                                    headsbit = Bitmap_dispose.handleImagePix4(headbit);
                                    break;
                                case 6:
                                    headsbit = Bitmap_dispose.handleImagePix5(headbit);
                                    break;
                                case 7:
                                    headsbit = Bitmap_dispose.handleImagePix6(headbit);
                                    break;
                                case 8:
                                    headsbit = Bitmap_dispose.handleImagePix7(headbit);
                                    break;
                                case 9:
                                    headsbit = Bitmap_dispose.handleImagePix8(headbit);
                                    break;
                                case 10:
                                    headsbit = Bitmap_dispose.handleImagePix9(headbit);
                                    break;
                                case 11:
                                    headsbit = Bitmap_dispose.handleImagePix10(headbit);
                                    break;
                                case 15:
                                    headsbit = Bitmap_dispose.fast(headbit);
                                    break;
                                case 16:
                                    headsbit = Bitmap_dispose.star(headbit);
                                    break;
                                case 17:
                                    headsbit = Bitmap_dispose.sift(headbit);
                                    break;
                                case 18:
                                    headsbit = Bitmap_dispose.surf(headbit);
                                    break;
                                case 19:
                                    headsbit = Bitmap_dispose.orb(headbit);
                                    break;
                                case 20:
                                    headsbit = Bitmap_dispose.brisk(headbit);
                                    break;
                                case 21:
                                    headsbit = Bitmap_dispose.Harris(headbit);
                                    break;
                                case 22:
                                    headsbit = Bitmap_dispose.Shit(headbit);
                                    break;
                                case 30:
                                    headsbit = Bitmap_dispose.enhance(headbit);
                                    break;
                                case 31:
                                    headsbit = Bitmap_dispose.enhance1(headbit);
                                    break;
                                case 32:
                                    headsbit = Bitmap_dispose.enhance2(headbit);
                                    break;
                                case 33:
                                    headsbit = Bitmap_dispose.enhance3(headbit);
                                    break;
                                case 34:
                                    headsbit = Bitmap_dispose.enhance4(headbit);
                                    break;
                                case 35:
                                    headsbit = Bitmap_dispose.enhance5(headbit);
                                    break;
                                case 36:
                                    headsbit = Bitmap_dispose.enhance6(headbit);
                                    break;
                                case 37:
                                    headsbit = Bitmap_dispose.enhance7(headbit);
                                    break;
                                case 38:
                                    headsbit = Bitmap_dispose.enhance8(headbit);
                                    break;
                                case 39:
                                    headsbit = Bitmap_dispose.enhance9(headbit);
                                    break;
                                case 40:
                                    headsbit = Bitmap_dispose.enhance10(headbit);
                                    break;
                            }
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handlers.sendMessage(msg);

                        }
                    };
                    t.start();
                }
                break;
            case R.id.imageView13:
                saveBitmaps(headsbit);
                Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intents);
                break;
        }
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                // try
                // {
                if (id == 1) {
                    Bitmap bitmap = headbit.copy(Bitmap.Config.RGB_565, true);
                    int numberOfFace = 5;
                    FaceDetector myFaceDetect;
                    int imageWidth = bitmap.getWidth();
                    int imageHeight = bitmap.getHeight();
                    myFace = new FaceDetector.Face[numberOfFace];
                    myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
                    numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);
                    for (int i = 0; i < numberOfFaceDetected; i++) {
                        FaceDetector.Face face = myFace[i];
                        PointF myMidPoint = new PointF();
                        face.getMidPoint(myMidPoint);
                        float myEyesDistance = face.eyesDistance();
                        int x = (int) (myMidPoint.x - myEyesDistance * 1.3);
                        int y = (int) (myMidPoint.y - myEyesDistance * 1.8);
                        int winth = (int) (myEyesDistance * 2.6);
                        int height = (int) (myEyesDistance * 3.6);
                        if (x >= 0 && y >= 0 && (x + winth) <= imageWidth && (y + height) <= imageHeight) {
                            switch (i) {
                                case 0:
                                    Bitmap bmp1 = Bitmap.createBitmap(bitmap, x, y, winth, height);
                                    imageView33.setImageBitmap(bmp1);
                                    break;
                                case 1:
                                    Bitmap bmp2 = Bitmap.createBitmap(bitmap, x, y, winth, height);
                                    imageView34.setImageBitmap(bmp2);
                                    break;
                                case 2:
                                    Bitmap bmp3 = Bitmap.createBitmap(bitmap, x, y, winth, height);
                                    imageView35.setImageBitmap(bmp3);
                                    break;
                                case 3:
                                    Bitmap bmp4 = Bitmap.createBitmap(bitmap, x, y, winth, height);
                                    imageView36.setImageBitmap(bmp4);
                                    break;
                                case 4:
                                    Bitmap bmp5 = Bitmap.createBitmap(bitmap, x, y, winth, height);
                                    imageView37.setImageBitmap(bmp5);
                                    break;
                            }
                        }
                    }
                }
                // }catch (Exception e)
                // {

                //  }
                heads.setImageBitmap(headsbit);
                tipDialog.dismiss();
            }
        }
    };

}
