package com.example.locallogsample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class LinearBase extends LinearLayout{
	
	public LinearBase(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	public LinearBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	Scroller mScroller;
	public void start(){
		mScroller = new Scroller(this.getContext(),new AnticipateOvershootInterpolator());
		mScroller.startScroll(0,0,
				200,
				200,
				5000);
		postInvalidate();
	}
	public void end(){
		if(null != mScroller){
			mScroller.abortAnimation();
			mScroller = null;
		}
	}
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if(null != mScroller){
			if (mScroller.computeScrollOffset()) {
				Log.e("aa", "移动中");
				postInvalidate();
			}else{
				Log.e("aa", "结束移动");
			}
		}
	}
	
}
