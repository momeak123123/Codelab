package com.example.momeak.codelab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;

public class there_baidu_api extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.there_baidu_api);

    }

    private void showLongMessageDialog(String txt) {
        int mCurrentDialogStyle = R.style.QMUI_Dialog;
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("标题")
                .setMessage(txt)
                .addAction("复制", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
}
