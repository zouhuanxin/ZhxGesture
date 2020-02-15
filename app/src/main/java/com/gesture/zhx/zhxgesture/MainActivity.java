package com.gesture.zhx.zhxgesture;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gesture.zhx.gesture.PointMagnifyShrinkUtils;
import com.gesture.zhx.gesture.PointSlideUtils;

public class MainActivity extends AppCompatActivity {

    private FrameLayout m1F1;
    private ImageView m1I1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        m1F1 = findViewById(R.id.m1_f1);
        m1I1 = findViewById(R.id.m1_i1);

        PointSlideUtils.getInstance().Slide(m1F1).setEventUpCallBack(new PointSlideUtils.EventUpCallBack() {
            @Override
            public void TopSlide(int value) {

            }

            @Override
            public void BottomSlide(int value) {

            }

            @Override
            public void LeftSlide(int value) {

            }

            @Override
            public void RightSlide(int value) {

            }

            @Override
            public void Inertia(double value) {
                  UploadImage(value);
            }
        });


    }

    private double H;
    private void UploadImage(double h){
        ViewGroup.LayoutParams params = m1I1.getLayoutParams();
        H = params.height;
        params.width = 200;
        params.height = (int) (H + h);
        m1I1.setLayoutParams(params);
    }
}
