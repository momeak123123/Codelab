package com.example.momeak.codelab;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.media.FaceDetector;
import android.media.ThumbnailUtils;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.CV_COMP_CORREL;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Bitmap_dispose {
    private static double results;
    private static double found;
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTRE,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN,
        CENTER;
    }

    /**
     * 底片
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix(Bitmap mBitmapSrc) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int r, g, b, a;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        mBitmapSrc.getPixels(imgPixels, 0, width, 0, 0, width, height);
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
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix2(Bitmap mBitmapSrc) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int r, g, b, a, newR, newG, newB;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        mBitmapSrc.getPixels(imgPixels, 0, width, 0, 0, width, height);
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
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix3(Bitmap mBitmapSrc) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int r, g, b, a, cr, cg, cb;
        int[] imgPixels = new int[width * height];
        int[] newImgPixels = new int[width * height];
        mBitmapSrc.getPixels(imgPixels, 0, width, 0, 0, width, height);
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

    /**
     * 复古
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix4(Bitmap mBitmapSrc) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        mBitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 冰冻
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix5(Bitmap mBitmapSrc) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        mBitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                //获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                //红色
                newColor = pixR - pixG - pixB;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newR = newColor;
                //绿色
                newColor = pixG - pixB - pixR;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newG = newColor;
                //蓝色
                newColor = pixB - pixG - pixR;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newB = newColor;
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 倒影
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix6(Bitmap mBitmapSrc) {
        final int reflectionGap = 4;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(mBitmapSrc, 0,
                height / 2, width, height / 2, matrix, false);
        Bitmap bitmap = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBitmapSrc, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                mBitmapSrc.getHeight(), 0, bitmap.getHeight()
                + reflectionGap, 0x70FFFFFF, 0x00FFFFFF,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmap.getHeight()
                + reflectionGap, paint);
        return bitmap;
    }

    /**
     * 黑白
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix7(Bitmap mBitmapSrc) {
        int mBitmapWidth;
        int mBitmapHeight;

        mBitmapWidth = mBitmapSrc.getWidth();
        mBitmapHeight = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Bitmap.Config.ARGB_8888);
        int iPixel;
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmapSrc.getPixel(i, j);

                int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
                        .blue(curr_color)) / 3;
                if (avg >= 100) {
                    iPixel = 255;
                } else {
                    iPixel = 0;
                }
                int modify_color = Color.argb(255, iPixel, iPixel, iPixel);

                bitmap.setPixel(i, j, modify_color);
            }
        }
        return bitmap;
    }

    /**
     * 油画
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix8(Bitmap mBitmapSrc) {
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapSrc.getWidth(),
                mBitmapSrc.getHeight(), Bitmap.Config.RGB_565);
        int color = 0;
        int Radio = 0;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();

        Random rnd = new Random();
        int iModel = 10;
        int i = width - iModel;
        while (i > 1) {
            int j = height - iModel;
            while (j > 1) {
                int iPos = rnd.nextInt(100000) % iModel;
                color = mBitmapSrc.getPixel(i + iPos, j + iPos);
                bmpReturn.setPixel(i, j, color);
                j = j - 1;
            }
            i = i - 1;
        }
        return bmpReturn;
    }

    /**
     * 高斯模糊
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix9(Bitmap mBitmapSrc) {
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        mBitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * 锐化
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap handleImagePix10(Bitmap mBitmapSrc) {
        int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        mBitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 灰度化
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance(Bitmap mBitmapSrc) {
        //  bitmap转Mat
        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Mat mat_gray = new Mat();
        Imgproc.cvtColor(img1, mat_gray, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Bitmap bitmaps = Bitmap.createBitmap(mat_gray.cols(), mat_gray.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_gray, bitmaps);

        return bitmaps;
    }

    /**
     * 直方图均衡化
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance1(Bitmap mBitmapSrc) {

        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        List<Mat> mv = new ArrayList<Mat>();
        Core.split(img1, mv);
        for (int i = 0; i < 3; i++) {
            Imgproc.equalizeHist(mv.get(i), mv.get(i));
        }
        Core.merge(mv, img1);

        Bitmap bitmaps = Bitmap.createBitmap(img1.cols(), img1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img1, bitmaps);

        return bitmaps;
    }


    /**
     * 二值化
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance2(Bitmap mBitmapSrc) {
        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Mat mat_gray = new Mat();
        Imgproc.cvtColor(img1, mat_gray, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Imgproc.threshold(mat_gray, mat_gray, 127, 255, Imgproc.THRESH_OTSU);

        Bitmap bitmaps = Bitmap.createBitmap(mat_gray.cols(), mat_gray.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_gray, bitmaps);

        return bitmaps;
    }

    /**
     * Canny算子边缘检测
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance3(Bitmap mBitmapSrc) {
        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Mat mat_gray = new Mat();
        Imgproc.cvtColor(img1, mat_gray, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Imgproc.Canny(mat_gray, mat_gray, 80, 90, 3, false); // Canny边缘检测

        Bitmap bitmaps = Bitmap.createBitmap(mat_gray.cols(), mat_gray.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_gray, bitmaps);

        return bitmaps;
    }

    /**
     * Sobel处理
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance4(Bitmap mBitmapSrc) {
        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Mat mat_gray = new Mat();
        Imgproc.cvtColor(img1, mat_gray, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        Imgproc.Sobel(mat_gray, grad_x, CvType.CV_16S, 1, 0, 3, 1, 0);
        // 计算垂直方向梯度
        Imgproc.Sobel(mat_gray, grad_y, CvType.CV_16S, 0, 1, 3, 1, 0);
        // 计算两个方向上的梯度的绝对值
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        // 计算结果梯度
        Core.addWeighted(mat_gray, 0.5, abs_grad_y, 0.5, 1, mat_gray);

        Bitmap bitmaps = Bitmap.createBitmap(mat_gray.cols(), mat_gray.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_gray, bitmaps);

        return bitmaps;
    }

    /**
     * 拉普拉斯算子
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance5(Bitmap mBitmapSrc) {
        Bitmap bmp2 = mBitmapSrc.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp2.getHeight(), bmp2.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp2, img1);

        Mat dst = img1.clone();
        Imgproc.GaussianBlur(img1, dst, new Size(3, 3), 0);

        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2GRAY);

        Imgproc.Laplacian(dst, dst, -1, 3, 1, 0, Core.BORDER_DEFAULT);

        Bitmap bitmaps = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmaps);

        return bitmaps;
    }

    /**
     * 平滑处理
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance6(Bitmap mBitmapSrc) {
        int w = mBitmapSrc.getWidth();
        int h = mBitmapSrc.getHeight();
        int[] data = new int[w * h];
        mBitmapSrc.getPixels(data, 0, w, 0, 0, w, h);
        int[] resultData = new int[w * h];
        try {
            resultData = filter(data, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap newBitmap = Bitmap.createBitmap(resultData, w, h, Bitmap.Config.ARGB_8888);
        return newBitmap;
    }

    private static int[] filter(int[] data, int width, int height) throws Exception {
        int filterData[] = new int[data.length];
        int min = 10000;
        int max = -10000;
        if (data.length != width * height) return filterData;
        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (i == 0 || i == 1 || i == height - 1 || i == height - 2 || j == 0 || j == 1 || j == width - 1 || j == width - 2) {
                        filterData[i * width + j] = data[i * width + j];
                    } else {
                        double average;             //中心的九个像素点
                        average = (data[i * width + j] + data[i * width + j - 1] + data[i * width + j + 1]
                                + data[(i - 1) * width + j] + data[(i - 1) * width + j - 1] + data[(i - 1) * width + j + 1]
                                + data[(i + 1) * width + j] + data[(i + 1) * width + j - 1] + data[(i + 1) * width + j + 1]) / 9;
                        filterData[i * width + j] = (int) (average);
                    }
                    if (filterData[i * width + j] < min)
                        min = filterData[i * width + j];
                    if (filterData[i * width + j] > max)
                        max = filterData[i * width + j];
                }
            }
            for (int i = 0; i < width * height; i++) {
                filterData[i] = (filterData[i] - min) * 255 / (max - min);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return filterData;
    }

    /**
     * 均值滤波
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance7(Bitmap mBitmapSrc) {
        int filterWidth = 30;
        int filterHeight = 30;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        mBitmapSrc.getPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }

    /**
     * 中值滤波
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance8(Bitmap mBitmapSrc) {
        int filterWidth = 30;
        int filterHeight = 30;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        mBitmapSrc.getPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }

    /**
     * 高斯差分算法边缘检测
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance9(Bitmap mBitmapSrc) {
        Mat src = new Mat(mBitmapSrc.getHeight(), mBitmapSrc.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(mBitmapSrc, src);
        Mat grayMat = new Mat();
        Mat blur1 = new Mat();
        Mat blur2 = new Mat();


        // 原图置灰
        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);


        // 以两个不同的模糊半径对图像做模糊处理
        Imgproc.GaussianBlur(grayMat, blur1, new Size(15, 15), 5);
        Imgproc.GaussianBlur(grayMat, blur2, new Size(21, 21), 5);


        // 将两幅模糊后的图像相减
        Mat diff = new Mat();
        Core.absdiff(blur1, blur2, diff);


        // 反转二值阈值化
        Core.multiply(diff, new Scalar(100), diff);
        Imgproc.threshold(diff, diff, 50, 255, Imgproc.THRESH_BINARY_INV);


        // Mat转Bitmap
        Bitmap bitmap = Bitmap.createBitmap(diff.cols(), diff.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(diff, bitmap);

        return bitmap;
    }

    /**
     * 轮廓检测
     *
     * @param mBitmapSrc
     * @return
     */
    public static Bitmap enhance10(Bitmap mBitmapSrc) {
        Mat src = new Mat(mBitmapSrc.getHeight(), mBitmapSrc.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(mBitmapSrc, src);
        Mat grayMat = new Mat();
        Mat cannyEdges = new Mat();

        // 原图置灰
        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Canny(grayMat, cannyEdges, 10, 100);

        Mat hierarchy = new Mat();
        // 保存轮廓
        ArrayList<MatOfPoint> contourList = new ArrayList<>();

        // 检测轮廓
        Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // 画出轮廓
        Mat contours = new Mat();
        contours.create(cannyEdges.rows(), cannyEdges.cols(), CvType.CV_8UC3);
        Random r = new Random();
        for (int i = 0; i < contourList.size(); i++) {
            Imgproc.drawContours(contours, contourList, i, new Scalar(r.nextInt(255), r.nextInt(255), r.nextInt(255), -1));
        }

        // Mat转Bitmap
        Bitmap bitmap = Bitmap.createBitmap(contours.cols(), contours.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(contours, bitmap);

        return bitmap;
    }

    /**
     * 像素值对比
     *
     * @param bm1,bm2
     * @return
     */
    public static Double similarity(Bitmap bm1, Bitmap bm2) {
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

    private static Double percent(int divisor, int dividend) {
        final double value = divisor * 1.0 / dividend;
        return value;
    }

    /**
     * 直方图对比
     *
     * @param bm1,bm2
     * @return
     */
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

    /**
     * 欧式距离对比
     *
     * @param bm1,bm2
     * @return
     */
    public static double similar(Bitmap bm1, Bitmap bm2) {
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
            }

        }
        found = Math.pow(rs, 0.5);
        return found;
    }

    /**
     * hash算法对比
     *
     * @param bm1,bm2
     * @return
     */
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
    public static Bitmap Harris(Bitmap bm1) {

        Bitmap bmp1 = bm1.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp1.getHeight(), bmp1.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp1, img1);

        Mat img1s = new Mat();

        cvtColor(img1, img1s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Harriss(img1s.getNativeObjAddr(), img1.getNativeObjAddr());

        Bitmap bitmap1 = Bitmap.createBitmap(img1.cols(), img1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img1, bitmap1);

        return bitmap1;
    }

    public static native void Harriss(long RGr, long Rgba);

    public static Bitmap Shit(Bitmap bm1) {

        Bitmap bmp1 = bm1.copy(Bitmap.Config.RGB_565, true);
        Mat img1 = new Mat(bmp1.getHeight(), bmp1.getWidth(), CvType.CV_8UC2, new Scalar(0));
        Utils.bitmapToMat(bmp1, img1);

        Mat img1s = new Mat();

        cvtColor(img1, img1s, Imgproc.COLOR_BGRA2GRAY, 1); // 转为灰度单通道Mat

        Shits(img1s.getNativeObjAddr(), img1.getNativeObjAddr());

        Bitmap bitmap1 = Bitmap.createBitmap(img1.cols(), img1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img1, bitmap1);

        return bitmap1;
    }

    public static native void Shits(long RGr, long Rgba);
}
