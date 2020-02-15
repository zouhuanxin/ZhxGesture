# ZhxGesture
一个手势库，帮助用户不需要再去写频繁的手势算法，支持放大，缩小，上滑，下滑，左滑，右滑。用户只需要去监听自己想要监听的手指即可，和监听单击事件一样方便。并且手势库支持上下滑动的一个惯性摩擦滑动回调，你也只需要去相应方法中获取即可。

### 库引入
Step 1. Add the JitPack repository to your build file 
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.zouhuanxin:ZhxGesture:1.1'
	}
```

### 手势操作使用
-手势放大 缩小
callback 返回的是缩放大小的高度与宽度 正常来说应该是相等返回的 所以随意使用哪个变量 
然后也返回了三种状态给用户做具体判断使用
```
PointMagnifyShrinkUtils.getInstance().MagnifyShrink(m4I1).setEventCallBack(new PointMagnifyShrinkUtils.EventCallBack() {
            @Override
            public void callback(int w, int h) {
                UploadImage(w,h);
            }

            @Override
            public void down(int dx1, int dy1, int dx2, int dy2) {

            }

            @Override
            public void move(int mx1, int my1, int mx2, int my2) {

            }

            @Override
            public void up(int ux1, int uy1, int ux2, int uy2) {
                ViewGroup.LayoutParams params = m4I1.getLayoutParams();
                if (params.width < initw || params.height <inith){
                    params.width = initw;
                    params.height = inith;
                    m4I1.setLayoutParams(params);
                }else{
                    width = params.width;
                    height = params.height;
                }
            }
        });
```
-手势上滑 下滑 左滑 右滑
这里总共有来个类型的接口
一种是move 
一种是up
up中返回了一个摩擦因数 意思是 惯性滚动距离值 这个回调会在up回调后面
其他四种回调 俩种类型一致 分别对应上滑 下滑 左滑 右滑 用户在相应的里面做处理就可以了
```
  PointSlideUtils.getInstance().Slide(m5L1).setEventMoveCallBack(new PointSlideUtils.EventMoveCallBack() {
            @Override
            public void TopSlide(int value) {
                m5S1.setScrollY(value);
            }

            @Override
            public void BottomSlide(int value) {
                m5S1.setScrollY(value);
            }

            @Override
            public void LeftSlide(int value) {

            }

            @Override
            public void RightSlide(int value) {

            }
        }).setEventUpCallBack(new PointSlideUtils.EventUpCallBack() {
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

            }
        });
```

我们将一直更新下去，谢谢。 

喜欢的话点个star，如果有需要请联系qq 634448817。


