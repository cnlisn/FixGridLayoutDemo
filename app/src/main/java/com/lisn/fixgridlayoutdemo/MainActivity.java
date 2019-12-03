package com.lisn.fixgridlayoutdemo;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lisn.rxpermissionlibrary.permissions.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Author: LiShan
 * Time: 2019-12-03
 * Description: 根据控件宽高自动换行布局
 */
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private FixGridLayout fixGridLayout;
    private Context mContext;
    private int width;
    private int height;
    private int fglwidth;
    private int fglheight;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        //异常捕获
        CrashUtil.getInstance().init(mContext);

        //获取应用读写权限
        RxPermissions rxPermissions = new RxPermissions(this);
        Observable<Boolean> request = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        request.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e(TAG, "accept: aBoolean =" + aBoolean);
            }
        });


        fixGridLayout = findViewById(R.id.fgl);
        RadioButton rb_1 = findViewById(R.id.rb_1);
        RadioButton rb_4 = findViewById(R.id.rb_4);
        RadioButton rb_6 = findViewById(R.id.rb_6);
        RadioButton rb_9 = findViewById(R.id.rb_9);

        rb_1.setOnCheckedChangeListener(this);
        rb_4.setOnCheckedChangeListener(this);
        rb_6.setOnCheckedChangeListener(this);
        rb_9.setOnCheckedChangeListener(this);

        ScreenInfo screenInfo = new ScreenInfo(MainActivity.this);
        width = screenInfo.getWidth();
        height = screenInfo.getHeight();
        Log.e(TAG, "onCreate:screen width=" + width + " height=" + height);

        fixGridLayout.post(new Runnable() {
            @Override
            public void run() {
                fglwidth = fixGridLayout.getWidth();
                fglheight = fixGridLayout.getHeight();
                Log.e(TAG, "控件宽高: fglwidth=" + fglwidth + " fglheight=" + fglheight);

                //初始四分屏
                fixGridLayout.setmCellHeight(fglheight / 2 - 2);
                fixGridLayout.setmCellWidth(fglwidth / 2 - 2);
            }
        });

        for (int i = 0; i < 9; i++) {
            View imageView = View.inflate(mContext, R.layout.layout, null);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "点击了第" + finalI + "个窗口", Toast.LENGTH_SHORT).show();
                }
            });
            fixGridLayout.addView(imageView);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) return;

        if (buttonView.getId() == R.id.rb_1) {                      //一分屏
            fixGridLayout.setmCellHeight(fglheight - 2);
            fixGridLayout.setmCellWidth(fglwidth - 2);
        } else if (buttonView.getId() == R.id.rb_4) {               //四分屏
            fixGridLayout.setmCellHeight(fglheight / 2 - 2);
            fixGridLayout.setmCellWidth(fglwidth / 2 - 2);
        } else if (buttonView.getId() == R.id.rb_6) {               //六分屏
            fixGridLayout.setmCellHeight(fglheight / 3 - 3);
            fixGridLayout.setmCellWidth(fglwidth / 2 - 2);
        } else if (buttonView.getId() == R.id.rb_9) {               //九分屏
            fixGridLayout.setmCellHeight(fglheight / 3 - 3);
            fixGridLayout.setmCellWidth(fglwidth / 3 - 3);
        }
    }
}
