package com.gesture.zhx.gesture;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                PostInertia(h);
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
        void Inertia(int value);
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
                if (eventUpCallBack != null) eventUpCallBack.Inertia((int) s);
                sleep = (long) (sleep * 0.9);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
