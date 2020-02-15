package com.gesture.zhx.gesture;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 上滑
 * 下滑
 * 左滑
 * 右滑
 * 带摩擦滑动因子算法 算法逐步完善中 建议目前不使用摩擦算法 需要使用可以自行拿按下时间自行计算
 */
public class PointSlideUtils {
    private int dx1, dy1; //按下单指坐标
    private int mx1, my1; //移动单指坐标
    private int ux1, uy1; //抬起单指坐标
    private EventMoveCallBack eventMoveCallBack;
    private EventUpCallBack eventUpCallBack;
    private long DownTime;

    public long getDownTime() {
        return DownTime;
    }

    public PointSlideUtils setEventUpCallBack(EventUpCallBack eventUpCallBack) {
        this.eventUpCallBack = eventUpCallBack;
        return this;
    }

    public PointSlideUtils setEventMoveCallBack(EventMoveCallBack eventMoveCallBack) {
        this.eventMoveCallBack = eventMoveCallBack;
        return this;
    }

    private static class SingClassInstance{
        private static final PointSlideUtils psu = new PointSlideUtils();
    }

    public PointSlideUtils(){

    }

    public static synchronized PointSlideUtils getInstance(){
        return SingClassInstance.psu;
    }

    public PointSlideUtils Slide(TextView textView){
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    public PointSlideUtils Slide(ImageView imageView){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    public PointSlideUtils Slide(LinearLayout linearLayout){
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    public PointSlideUtils Slide(FrameLayout frameLayout){
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    private void OnTouchClick(View v, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    DownTime = System.currentTimeMillis();
                    dx1 = (int) event.getX(0);
                    dy1 = (int) event.getY(0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    mx1 = (int) event.getX(0);
                    my1 = (int) event.getY(0);
                    //上滑 下滑 y
                    int h = my1 - dy1;
                    if (h > 0) {
                        if (eventMoveCallBack != null) eventMoveCallBack.BottomSlide(h);
                    } else {
                        if (eventMoveCallBack != null) eventMoveCallBack.TopSlide(h);
                    }
                    //左滑 右滑 x
                    int w = mx1 - dx1;
                    if (w > 0) {
                        if (eventMoveCallBack != null) eventMoveCallBack.RightSlide(h);
                    } else {
                        if (eventMoveCallBack != null) eventMoveCallBack.LeftSlide(h);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                ux1 = (int) event.getX(0);
                uy1 = (int) event.getY(0);
                //上滑 下滑 y
                int h = uy1 - dy1;
                if (h > 0) {
                    if (eventUpCallBack != null) eventUpCallBack.BottomSlide(h);
                } else {
                    if (eventUpCallBack != null) eventUpCallBack.TopSlide(h);
                }
                PostInertiaTwo(h);
                //左滑 右滑 x
                int w = ux1 - dx1;
                if (w > 0) {
                    if (eventUpCallBack != null) eventUpCallBack.RightSlide(h);
                } else {
                    if (eventUpCallBack != null) eventUpCallBack.LeftSlide(h);
                }
                break;
        }
    }

    public interface EventMoveCallBack{
        void TopSlide(int value);
        void BottomSlide(int value);
        void LeftSlide(int value);
        void RightSlide(int value);
    }

    public interface EventUpCallBack{
        void TopSlide(int value);
        void BottomSlide(int value);
        void LeftSlide(int value);
        void RightSlide(int value);
        @Deprecated
        void Inertia(double value);
    }

    //惯性滚动算法
    private void PostInertia(int jl){
        //得到当前时间
        long time = System.currentTimeMillis() - DownTime;
        //得到当前滑动距离
        //计算出速度
        long sleep = jl / time;
        //速度大于2有滑动 小于2没有
        UploadView(sleep);
    }
    private void UploadView(long sleep){
        while (true){
            try {
                if (sleep <= 2){
                    return;
                }
                double s = sleep * 10;
                if (eventUpCallBack != null) eventUpCallBack.Inertia(s);
                sleep = (long) (sleep * 0.9);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //惯性滚动算法第二版
    private double a = 5000;
    private double tz = 0.016666;
    private double ageI,nowI;
    private void PostInertiaTwo(int jl){
        a = 5000;
        System.out.println("手指滑行距离:"+jl);
        Double time = Chufa(System.currentTimeMillis() - DownTime,1000); //毫秒级
        System.out.println("手指滑行时间:"+time);
        Double sleep = Chufa(jl,time); //平均速度即是最后一秒的瞬时速度
        UploadViewTwo(sleep);
    }
    private void UploadViewTwo(final Double sleep){
        System.out.println("sleep:"+sleep);
        //滑行时间
        double t = Chufa(sleep,a);
        System.out.println("滑行时间："+t);
        //总帧数
        double sumzs = Chufa(t,tz);
        System.out.println("总帧数:"+sumzs);
        //自由滑行长度
        double L = Chufa(sleep*sleep,(2*a));
        System.out.println("自由滑行长度："+L);
        //第一帧长度
        double x1 = sleep*tz - Chufa((a*(tz*tz)),2);
        if (eventUpCallBack != null) eventUpCallBack.Inertia(x1);
        ageI = x1;
        CountDownTimer countDownTimer = new CountDownTimer((long) (17*sumzs),17) {
            @Override
            public void onTick(long millisUntilFinished) {
                nowI = ageI - a*(tz*tz);
                double nowv = Chufa(nowI,tz);
                if (nowv < Chufa(sleep,3)){
                    a = 3000;
                }else if (nowv < Chufa(sleep , 4)){
                    a = 1500;
                }else if (nowv < Chufa(sleep , 5)){
                    a = 750;
                }
                if (eventUpCallBack != null) eventUpCallBack.Inertia(nowI);
                ageI = nowI;
            }

            @Override
            public void onFinish() {
                cancel();
            }
        };
        countDownTimer.start();
    }

    public Double Chufa(double a,double b) {
        //“0.00000000”确定精度
        DecimalFormat dF = new DecimalFormat("0.00000000");
        return Double.parseDouble(dF.format((float)a/b));
    }
}
