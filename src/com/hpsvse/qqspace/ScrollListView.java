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
		//����ListView�Ƿ񻬶�����
		//deltaX,deltaYΪ��������������Ϊ-����������Ϊ+
		if(deltaY < 0){
			//���������游�����ĸ߶�
			mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
			//���»���һ�β���
			mImageView.requestLayout();
		}else{
			if(mImageView.getHeight() > mImageViewHeight){
				//���������游�����ĸ߶�
				mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
				//���»���һ�β���
				mImageView.requestLayout();
			}
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}
	
	/**
	 * listView������ʱ�����
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View heared = (View) mImageView.getParent();
		int top = heared.getTop();
		//ֻ�е�ͼƬ�Ŵ��˲���ȥ��СͼƬ
		if(top < 0 && mImageView.getHeight() > mImageViewHeight){
			mImageView.getLayoutParams().height = mImageView.getHeight() + top;
			//�����ָ�ϻ��ķ���bug
			//�����Ϊ�˱�֤view�����븸��ͼ���������ҵľ��벻��
			heared.layout(heared.getLeft(), 0, heared.getRight(), heared.getHeight());
			mImageView.requestLayout();
		}
		
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		//����̧�ֺ���ͼƬ���л�ԭ
		if(action == MotionEvent.ACTION_UP){
			//�Զ��嶯�������ֺ�ͼƬ��ԭ��һ�����ɵ�Ч��
			ResetAnimation animation = new ResetAnimation(mImageView, mImageViewHeight);
			animation.setDuration(300);
			mImageView.startAnimation(animation);
		}
		return super.onTouchEvent(ev);
	}
	
	public class ResetAnimation extends Animation{
		
		private ImageView iv;
		private int targetHeight;//��ʼ��ʱ�ĸ߶�
		private int currentHeight;//������ĸ߶�
		private int extraHeight;//�����ĸ߶Ȳ���������˶��٣�
		
		public ResetAnimation(ImageView iv,int targetHeight){
			this.iv = iv;
			this.targetHeight = targetHeight;
			this.currentHeight = iv.getHeight();
			this.extraHeight = currentHeight - targetHeight;
		}
		
		//ִ�е�ʱ����������᲻�ϵĵ���
		//interpolatedTime ��ֵ�������ǹ��೤ʱ��ִ��һ�Σ���Χ��0-1
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			
			iv.getLayoutParams().height = (int)(currentHeight - interpolatedTime*extraHeight);
			iv.requestLayout();
			super.applyTransformation(interpolatedTime, t);
		}
		
		
	}
	
	
	//Ҫ������ImageView
	public void setZoomImageView(ImageView iv){
		mImageView = iv;
	}
	
}
