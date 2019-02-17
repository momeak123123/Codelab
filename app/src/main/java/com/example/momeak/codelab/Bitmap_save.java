package com.example.momeak.codelab;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bitmap_save extends Application {

    private  File filePic;
    private  String fileName;
    public  void saveBitmaps(Bitmap bitmap) {
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

   public void saveBitmaps(Bitmap bitmap, String string) {
       String appDir = getFilesDir().getAbsolutePath();
        try {
            filePic = new File(appDir+ string);
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

}
