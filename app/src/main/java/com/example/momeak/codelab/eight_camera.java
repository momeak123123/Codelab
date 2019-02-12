package com.example.momeak.codelab;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class eight_camera extends AppCompatActivity {
    @BindView(R.id.tv_camera)
    TextureView tvCamera;
    @BindView(R.id.btn_take)
    ImageView btnTake;
    @BindView(R.id.btn_switch)
    ImageView btnSwitch;
    @BindView(R.id.iv_show)
    QMUIRadiusImageView ivShow;
    @BindView(R.id.btn_delay)
    ImageView btnDelay;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.delaytxt)
    TextView delaytxt;
    private Bitmap rotateBitmap;
    /**
     * 相机权限请求标识
     */
    private static final int REQUEST_CAMERA_CODE = 0x100;
    /**
     * 后置摄像头
     */
    private static final int CAMERA_FRONT = CameraCharacteristics.LENS_FACING_FRONT;
    /**
     * 前置摄像头
     */
    private static final int CAMERA_BACK = CameraCharacteristics.LENS_FACING_BACK;
    /**
     * 无延时
     */
    private static final int DELAY_ZERO = 0;
    /**
     * 延时3秒
     */
    private static final int DELAY_THREE = 3;
    /**
     * 延时10秒
     */
    private static final int DELAY_TEN = 10;
    /**
     * 拍照按钮
     */
    private ImageView mBtnTake;
    /**
     * 返回
     */
    private ImageView fold;
    /**
     * 图片
     */
    private ImageView mImageView;
    /**
     * 切换摄像头
     */
    private ImageView mBtnSwitch;
    /**
     * 切换摄像头
     */
    private ImageView mBtnDelay;
    /**
     * 照相机ID，标识前置后置
     */
    private String mCameraId;
    private int CameraId = 1;
    /**
     * 相机尺寸
     */
    private Size mCaptureSize;
    /**
     * 图像读取者
     */
    private ImageReader mImageReader;
    /**
     * 图像主线程Handler
     */
    private Handler mCameraHandler;
    /**
     * 相机设备
     */
    private CameraDevice mCameraDevice;
    /**
     * 预览大小
     */
    private Size mPreviewSize;
    /**
     * 相机请求
     */
    private CaptureRequest.Builder mCameraCaptureBuilder;
    /**
     * 相机拍照捕获会话
     */
    private CameraCaptureSession mCameraCaptureSession;
    /**
     * 相机管理者
     */
    private CameraManager mCameraManager;
    /**
     * 延时秒数
     */
    private int mDelayTime = DELAY_ZERO;
    /**
     * 闪光模式
     */
    private int mFlashMode = 0;
    /**
     * 相机设备状态回调
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            // 打开
            mCameraDevice = camera;
            if (null != mPreviewSize && tvCamera.isAvailable()) {
                // 开始预览
                takePreview();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            // 断开连接
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            // 异常
            camera.close();
            mCameraDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.eight_camera);
        ButterKnife.bind(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        float scaledDensity = dm.scaledDensity;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        int height = dm.heightPixels;
        int width = dm.widthPixels;
       TextView fold = findViewById(R.id.headtxt);
        fold.setText("height:"+height/scaledDensity+"width"+width/scaledDensity+"/"+densityDpi);
       /* ViewTreeObserver vto = tvCamera.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //手机宽高
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int height = dm.heightPixels;
                int width = dm.widthPixels;
                double a = (double) width / 1080;
                double c = (double) 2000 * a;
                ViewGroup.LayoutParams layoutParams = tvCamera.getLayoutParams();
                //设置宽度
                layoutParams.width = width;
                //设置高度
                layoutParams.height = (int)c;
                tvCamera.setLayoutParams(layoutParams);
            }
        });*/
    }

    private Bitmap compressScaleBitmap(Bitmap bit) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.1f, 0.1f);
        Bitmap bm = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                bit.getHeight(), matrix, true);
        return bm;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextureListener();
    }

    /**
     * 设置Texture监听
     */
    private void setTextureListener() {
        tvCamera.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                // SurfaceTexture可用
                // 设置相机参数并打开相机
                setUpCamera(width, height);
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // SurfaceTexture大小改变
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                // SurfaceTexture 销毁
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // SurfaceTexture 更新
            }
        });
    }

    /**
     * 打开相机
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void openCamera() {
        // 此处ImageReader用于拍照所需
        setupImageReader();
        // 获取照相机管理者
        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
                return;
            }
            // 打开相机
            mCameraManager.openCamera(mCameraId, mStateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CAMERA_CODE == requestCode) {
            // 权限允许
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mCameraManager.openCamera(mCameraId, mStateCallback, mCameraHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                // 权限拒绝
                Toast.makeText(this, "无权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置相机参数
     *
     * @param width  宽度
     * @param height 高度
     */
    private void setUpCamera(int width, int height) {
        // 创建Handler
        mCameraHandler = new Handler(Looper.getMainLooper());
        // 获取摄像头的管理者
        CameraManager cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            // 遍历所有摄像头
            for (String cameraId : cameraManager.getCameraIdList()) {
                // 相机特性
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                // 获取摄像头是前置还是后置
                Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                // 此处默认打开前置摄像头
                if (null != facing && CAMERA_FRONT == facing) continue;
                // 获取StreamConfigurationMap，管理摄像头支持的所有输出格式和尺寸
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                // 根据TextureView的尺寸设置预览尺寸
                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                // 获取相机支持的最大拍照尺寸
                mCaptureSize = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                            @Override
                            public int compare(Size lhs, Size rhs) {
                                return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                            }
                        });
                // 为摄像头赋值
                setupImageReader();
                mCameraId = cameraId;
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置ImageReader
     */
    private void setupImageReader() {
        mImageReader = ImageReader.newInstance(mCaptureSize.getWidth(), mCaptureSize.getHeight(), ImageFormat.JPEG, 1);
        // 设置图像可用监听
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 获取图片
                final Image image = reader.acquireNextImage();
                // 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 获取字节缓冲区
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        // 创建数组之前调用此方法，恢复默认设置
                        buffer.rewind();
                        // 创建与缓冲区内容大小相同的数组
                        byte[] bytes = new byte[buffer.remaining()];
                        // 从缓冲区存入字节数组,读取完成之后position在末尾
                        buffer.get(bytes);
                        // 获取Bitmap图像
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        rotateBitmap = bitmap;
                        // 显示
                        if (bitmap != null) {
                            Bitmap bitmap1 = compressScaleBitmap(rotateBitmap);
                            if (CameraId == 1) {
                                bitmap1 = createPhotos(bitmap1, 90);
                            } else {
                                bitmap1 = createPhotos(bitmap1, -90);
                                Matrix m = new Matrix();
                                m.setScale(-1, 1);//水平翻转
                                int w = bitmap1.getWidth();
                                int h = bitmap1.getHeight();
                                //生成的翻转后的bitmap
                                bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, w, h, m, true);
                            }
                            mImageView.setImageBitmap(bitmap1);
                            image.close();

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    saveBitmaps();
                                }
                            });
                            thread.start();
                        }
                    }
                });
            }

        }, mCameraHandler);
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


    /**
     * 选择SizeMap中大于并且最接近width和height的size
     *
     * @param sizeMap 可选的尺寸
     * @param width   宽
     * @param height  高
     * @return 最接近width和height的size
     */
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    //显示列表浮层
    private void showListPopups(View v, int preferredDirection) {
        String[] array = new String[]{
                "延时拍摄: 3秒",
                "延时拍摄: 10秒",
                "延时拍摄: 关闭",
        };
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(array)));
        final QMUIListPopup mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);
        mListPopup.create(QMUIDisplayHelper.dp2px(this, 150), QMUIDisplayHelper.dp2px(this, 200),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        delayTake(i);
                        mListPopup.dismiss();
                    }
                });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(preferredDirection);
        mListPopup.show(v);
    }

    /**
     * 延时拍摄
     */
    private void delayTake(int sed) {
        switch (sed) {
            case DELAY_ZERO:
                mDelayTime = DELAY_THREE;
                delaytxt.setText("3秒");
                Toast.makeText(this, "延时拍摄: 3秒", Toast.LENGTH_SHORT).show();

                break;
            case DELAY_THREE:
                mDelayTime = DELAY_TEN;
                delaytxt.setText("10秒");
                Toast.makeText(this, "延时拍摄: 10秒", Toast.LENGTH_SHORT).show();
                break;
            case DELAY_TEN:
                mDelayTime = DELAY_ZERO;
                delaytxt.setText("关闭");
                Toast.makeText(this, "延时拍摄: 关闭", Toast.LENGTH_SHORT).show();
                break;
        }
        if (mDelayTime == DELAY_ZERO) {
            takePicture();
        } else {
            // 定时器
            new CountDownTimer(mDelayTime * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    delaytxt.setText((millisUntilFinished / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    takePicture();
                }
            }.start();
        }
    }

    /**
     * 切换前后摄像头
     */
    private void switchCamera() {
        if (String.valueOf(CAMERA_FRONT).equals(mCameraId)) {
            // 后置
            mCameraId = String.valueOf(CAMERA_BACK);
        } else if (String.valueOf(CAMERA_BACK).equals(mCameraId)) {
            // 前置
            mCameraId = String.valueOf(CAMERA_FRONT);
        }
        closeCamera();
        reOpenCamera();
    }

    /**
     * 重新打开摄像头
     */
    private void reOpenCamera() {
        if (tvCamera.isAvailable()) {
            openCamera();
        } else {
            setTextureListener();
        }
    }

    /**
     * 关闭摄像头
     */
    private void closeCamera() {
        if (null != mCameraCaptureSession) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    /**
     * 预览
     */
    private void takePreview() {
        // 获取SurfaceTexture
        SurfaceTexture surfaceTexture = tvCamera.getSurfaceTexture();
        // 设置默认的缓冲大小
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        // 创建Surface
        Surface previewSurface = new Surface(surfaceTexture);
        try {
            // 创建预览请求
            mCameraCaptureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将previewSurface添加到预览请求中
            mCameraCaptureBuilder.addTarget(previewSurface);
            // 创建会话
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        // 配置
                        CaptureRequest captureRequest = mCameraCaptureBuilder.build();
                        // 設置session
                        mCameraCaptureSession = session;
                        // 设置重复预览请求
                        mCameraCaptureSession.setRepeatingRequest(captureRequest, null, mCameraHandler);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    // 配置失败
                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        try {
            // 设置触发
            mCameraCaptureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            mCameraCaptureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 设置自动曝光模式
            mCameraCaptureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mCameraCaptureBuilder.addTarget(mImageReader.getSurface());
            // 捕获静态图像
            mCameraCaptureSession.capture(mCameraCaptureBuilder.build(), null, mCameraHandler);
        } catch (CameraAccessException e) {
            Toast.makeText(this, "异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    /**
     * 保存图片任务
     */
    public void saveBitmaps() {
        Bitmap bitmap;
        if (CameraId == 1) {
            bitmap = createPhotos(rotateBitmap, 90);
        } else {
            bitmap = createPhotos(rotateBitmap, -90);
            Matrix m = new Matrix();
            m.setScale(-1, 1);//水平翻转
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            //生成的翻转后的bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
        }
        SaveBitmap save = new SaveBitmap();
        save.saveBitmaps(bitmap);
    }

    @OnClick({R.id.btn_take, R.id.btn_switch, R.id.iv_show, R.id.btn_delay, R.id.imageView6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take:
                takePicture();
                break;
            case R.id.btn_switch:
                if (CameraId == 1) {
                    CameraId = 0;
                } else {
                    CameraId = 1;
                }
                switchCamera();
                break;
            case R.id.iv_show:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intent);
                break;
            case R.id.btn_delay:
                showListPopups(mBtnDelay, 1);
                break;
            case R.id.imageView6:
                Intent intent6 = new Intent(eight_camera.this, MainActivity.class);
                startActivity(intent6);
                break;
        }
    }
}
