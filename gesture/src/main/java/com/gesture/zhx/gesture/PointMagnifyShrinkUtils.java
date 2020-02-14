package com.gesture.zhx.gesture;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手势放大 手势缩小
 * 支持 TextView ImageView
 */
public class PointMagnifyShrinkUtils {
    private int dx1, dy1, dx2, dy2; //按下双指坐标
    private int mx1, my1, mx2, my2; //移动双指坐标
    private int ux1, uy1, ux2, uy2; //抬起双指坐标
    private EventCallBack eventCallBack;

    public void setEventCallBack(EventCallBack eventCallBack) {
        this.eventCallBack = eventCallBack;
    }

    private static class SingClassInstance{
        private static final PointMagnifyShrinkUtils pmu = new PointMagnifyShrinkUtils();
    }

    public PointMagnifyShrinkUtils(){

    }

    public static synchronized PointMagnifyShrinkUtils getInstance(){
        return SingClassInstance.pmu;
    }

    public PointMagnifyShrinkUtils MagnifyShrink(TextView textView){
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    public PointMagnifyShrinkUtils MagnifyShrink(ImageView imageView){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnTouchClick(v,event);
                return true;
            }
        });
        return this;
    }

    private void OnTouchClick(View v,MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                dx1 = (int) event.getX(0);
                dy1 = (int) event.getY(0);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2){
                    dx2 = (int) event.getX(1);
                    dy2 = (int) event.getY(1);
                    if (eventCallBack != null) eventCallBack.down(dx1,dy1,dx2,dy2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2){
                    mx1 = (int) event.getX(0);
                    my1 = (int) event.getY(0);
                    mx2 = (int) event.getX(1);
                    my2 = (int) event.getY(1);
                    if (eventCallBack != null) {
                        int r = (int) (Subtract(mx1,my1,mx2,my2) - Subtract(dx1,dy1,dx2,dy2));
                        int t = (int) Math.sqrt((r*r)/2);
                        if (r < 0){
                            t = t*-1;
                        }
                        eventCallBack.callback(t,t);
                        eventCallBack.move(mx1,my1,mx2,my2);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (eventCallBack != null){
                    eventCallBack.up(ux1,uy1,ux2,uy2);
                }
                break;
        }
    }

    //计算俩点之间的差
    private double Subtract(int x1, int y1, int x2, int y2) {
        int w = x2 - x1;
        int h = y2 - y1;
        if (w < 0) w = w * -1;
        if (h < 0) h = h * -1;
        double res = Math.sqrt(w * w + h * h);
        return res;
    }

    public interface EventCallBack{
        void callback(int w, int h);
        void down(int dx1, int dy1, int dx2, int dy2);
        void move(int mx1, int my1, int mx2, int my2);
        void up(int ux1, int uy1, int ux2, int uy2);
    }
}
