package com.example.momeak.codelab;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;

public class Bitmap_face {

    private static int numberOfFaceDetected;
    private static FaceDetector.Face[] myFace;
    private boolean pan;

    //android FaceDetector人脸检测
    public static int detectFace(Bitmap bitmap) {
        int numberOfFace = 12;
        FaceDetector myFaceDetect;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        myFace = new FaceDetector.Face[numberOfFace];
        myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
        numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);
        return numberOfFaceDetected;
    }

    //人脸裁剪
    public static Bitmap drawFace(Bitmap bitmap) {
        Bitmap bmp = bitmap;
        int numberOfFace = 12;
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
            if (x > 0 && y > 0 && (x + winth) < imageWidth && (y + height) < imageHeight) {
                bmp = Bitmap.createBitmap(bitmap, x, y, winth, height);
            }
        }
        return bmp;
    }

    //人脸画框
    private Bitmap drawFaces(Bitmap bitmap) {
        int numberOfFace = 12;
        FaceDetector myFaceDetect;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        myFace = new FaceDetector.Face[numberOfFace];
        myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
        numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);
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
            int x = (int) (myMidPoint.x - myEyesDistance * 1.3);
            int y = (int) (myMidPoint.y - myEyesDistance * 1.8);
            int winth = (int) (myEyesDistance * 2.6);
            int height = (int) (myEyesDistance * 3.6);
            if (x > 0 && y > 0 && (x + winth) < imageWidth && (y + height) < imageHeight) {
                canvas.drawRect((int) (myMidPoint.x - myEyesDistance * 1.3),
                        (int) (myMidPoint.y - myEyesDistance * 1.8),
                        (int) (myMidPoint.x + myEyesDistance * 1.3),
                        (int) (myMidPoint.y + myEyesDistance * 1.8), myPaint);
            }
        }
        return bitmap;
    }
}
