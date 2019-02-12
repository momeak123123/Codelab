package com.example.momeak.codelab;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener{
    @BindView(R.id.head)
    QMUIRadiusImageView head;
    private int heigth;
    private int width;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.SET_WALLPAPER,

    };
    private static final int PERMISSION_REQUESTED = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        heigth = dm.heightPixels;
        width = dm.widthPixels;
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (heigth / density);// 屏幕高度(dp)
        fragmentManager = getSupportFragmentManager();
        initMenuFragment();
        File savePath = new File(getExternalFilesDir(null).getPath() + "/facestrat.jpg");
        if (savePath.exists()) {
            head.setImageURI(Uri.fromFile(savePath));
        } else {

        }

    }
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.d60));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject("取消选择");
        close.setResource(R.drawable.icn_close);

        MenuObject addFr = new MenuObject("添加人脸数据");
        addFr.setResource(R.drawable.icn_2);

        MenuObject check = new MenuObject("检查更新");
        check.setResource(R.drawable.icn_3);

        MenuObject blocks = new MenuObject("快速分享");
        blocks.setResource(R.drawable.icn_4);

        MenuObject addFrv = new MenuObject("关闭软件");
        addFrv.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(addFr);
        menuObjects.add(check);
        menuObjects.add(blocks);
        menuObjects.add(addFrv);
        return menuObjects;
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //  Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                break;
            case 1:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, face.class);
                startActivity(intent);
                break;
            case 2:

                break;
            case 3:
                showSimpleBottomSheetGrid();
                break;
            case 4:
                System.exit(0);
                break;
        }
    }
    @OnClick({R.id.set, R.id.head, R.id.textView, R.id.ima_one, R.id.ima_one_im, R.id.ima_one_txt, R.id.ima_two, R.id.ima_two_im, R.id.ima_two_txt, R.id.ima_there, R.id.ima_there_im, R.id.ima_there_txt, R.id.ima_four, R.id.ima_four_im, R.id.ima_four_txt, R.id.ima_five, R.id.ima_five_im, R.id.ima_five_txt, R.id.ima_six, R.id.ima_six_im, R.id.ima_six_txt, R.id.ima_seven, R.id.ima_seven_im, R.id.ima_seven_txt, R.id.ima_eight, R.id.ima_eight_im, R.id.ima_eight_txt, R.id.ima_nine, R.id.ima_nine_im, R.id.ima_nine_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set:
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                break;
            case R.id.head:
            case R.id.textView:

                break;
            case R.id.ima_one:
            case R.id.ima_one_im:
            case R.id.ima_one_txt:
                Intent intent1 = new Intent(MainActivity.this, one_dispose.class);
                startActivity(intent1);
                break;
            case R.id.ima_two:
            case R.id.ima_two_im:
            case R.id.ima_two_txt:
                Intent intent2 = new Intent(MainActivity.this, two_detection.class);
                startActivity(intent2);
                break;
            case R.id.ima_there:
            case R.id.ima_there_im:
            case R.id.ima_there_txt:
                Intent intent3 = new Intent(MainActivity.this, there_recognition.class);
                startActivity(intent3);
                break;
            case R.id.ima_four:
            case R.id.ima_four_im:
            case R.id.ima_four_txt:
                Intent intent4 = new Intent(MainActivity.this, four_test.class);
                startActivity(intent4);
                break;
            case R.id.ima_five:
            case R.id.ima_five_im:
            case R.id.ima_five_txt:

                break;
            case R.id.ima_six:
            case R.id.ima_six_im:
            case R.id.ima_six_txt:

                break;
            case R.id.ima_seven:
            case R.id.ima_seven_im:
            case R.id.ima_seven_txt:

                break;
            case R.id.ima_eight:
            case R.id.ima_eight_im:
            case R.id.ima_eight_txt:
                Intent intent8 = new Intent(MainActivity.this, eight_camera.class);
                startActivity(intent8);
                break;
            case R.id.ima_nine:
            case R.id.ima_nine_im:
            case R.id.ima_nine_txt:

                break;
        }
    }

    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        new QMUIBottomSheet.BottomGridSheetBuilder(this)
                .addItem(R.drawable.wx, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.pyq, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.wb, "分享到微博", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.qq, "分享到私信", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                Toast.makeText(MainActivity.this, "分享到微信", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                Toast.makeText(MainActivity.this, "分享到朋友圈", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_WEIBO:
                                Toast.makeText(MainActivity.this, "分享到微博", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_CHAT:
                                Toast.makeText(MainActivity.this, "分享到私信", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .build().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    /**
     * 检查权限
     *
     * @param
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        //获取权限列表
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            //list.toarray将集合转化为数组
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSION_REQUESTED);
        }
    }


    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSION_REQUESTED) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                showMissingPermissionDialog();              //显示提示信息
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public native String stringFromJNI();
}
