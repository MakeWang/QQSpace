# QQSpace
模仿QQ空间图片拉伸

![](https://github.com/MakeWang/QQSpace/blob/master/image/GIF.gif)
直接上代码</br>
#MainActivity</br>
```java
public class MainActivity extends Activity {
	
	private ScrollListView mScrollListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mScrollListView = (ScrollListView) findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {
						"1111111111111111111111111",
						"2222222222222222222222222",
						"3333333333333333333333333",
						"444444444444444444444444",
						"5555555555555555555555555",
						"66666666666666666666666",});
		
		
		View head = View.inflate(this, R.layout.topimg_main, null);
		ImageView iv = (ImageView) head.findViewById(R.id.layout_header_image);
		mScrollListView.setZoomImageView(iv);
		mScrollListView.addHeaderView(head);
		mScrollListView.setAdapter(adapter);
		
	}

}
```

#ScrollListView </br>
```java
public class ScrollListView extends ListView{
	
	private ImageView mImageView;
	private int mImageViewHeight;
	
	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.size_default_height);
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		//监听ListView是否滑动过度
		//deltaX,deltaY为增量，下拉过度为-；上拉过度为+
		if(deltaY < 0){
			//设置最外面父容器的高度
			mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
			//重新绘制一次布局
			mImageView.requestLayout();
		}else{
			if(mImageView.getHeight() > mImageViewHeight){
				//设置最外面父容器的高度
				mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
				//重新绘制一次布局
				mImageView.requestLayout();
			}
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}
	
	/**
	 * listView滑动的时候监听
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View heared = (View) mImageView.getParent();
		int top = heared.getTop();
		//只有当图片放大了才能去缩小图片
		if(top < 0 && mImageView.getHeight() > mImageViewHeight){
			mImageView.getLayoutParams().height = mImageView.getHeight() + top;
			//解决手指上滑的幅度bug
			//这个是为了保证view在中与父视图的上左下右的距离不变
			heared.layout(heared.getLeft(), 0, heared.getRight(), heared.getHeight());
			mImageView.requestLayout();
		}
		
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		//监听抬手后让图片进行还原
		if(action == MotionEvent.ACTION_UP){
			//自定义动画，放手后图片还原有一个过渡的效果
			ResetAnimation animation = new ResetAnimation(mImageView, mImageViewHeight);
			animation.setDuration(300);
			mImageView.startAnimation(animation);
		}
		return super.onTouchEvent(ev);
	}
	
	public class ResetAnimation extends Animation{
		
		private ImageView iv;
		private int targetHeight;//初始化时的高度
		private int currentHeight;//拉升后的高度
		private int extraHeight;//拉升的高度差（就是拉升了多少）
		
		public ResetAnimation(ImageView iv,int targetHeight){
			this.iv = iv;
			this.targetHeight = targetHeight;
			this.currentHeight = iv.getHeight();
			this.extraHeight = currentHeight - targetHeight;
		}
		
		//执行的时候这个方法会不断的调用
		//interpolatedTime 差值器，就是过多长时间执行一次，范围是0-1
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			
			iv.getLayoutParams().height = (int)(currentHeight - interpolatedTime*extraHeight);
			iv.requestLayout();
			super.applyTransformation(interpolatedTime, t);
		}
		
		
	}
	
	
	//要拉升的ImageView
	public void setZoomImageView(ImageView iv){
		mImageView = iv;
	}
	
}

```
#activity_main.xml</br>
```java
<LinearLayout
     xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    >

    <com.hpsvse.qqspace.ScrollListView 
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        ></com.hpsvse.qqspace.ScrollListView>
    
    
</LinearLayout>
```

#topimg_main.xml</br>
```java
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
	<ImageView 
	    android:id="@+id/layout_header_image"
	    android:layout_width="match_parent"
	    android:layout_height="@dimen/size_default_height"
	    android:scaleType="centerCrop"
	    android:src="@drawable/img_header4"
	    />
	<ImageView 
	    android:id="@+id/iv_icon"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:src="@drawable/head"
	    android:layout_alignParentBottom="true"
	    android:layout_marginBottom="40dp"
	    android:layout_alignParentLeft="true"
	    android:layout_marginLeft="40dp"
	    />
	
</RelativeLayout>
```
