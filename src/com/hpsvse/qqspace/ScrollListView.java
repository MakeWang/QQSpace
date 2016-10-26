package com.hpsvse.qqspace;

import android.content.Context;
import android.preference.PreferenceActivity.Header;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

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
