package com.example.locallogsample;

import six.locallogcore.LocalLogApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;

public class MainActivity extends Activity implements OnClickListener{
	LinearBase move;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		
		LocalLogApi.initLocalLogEngine(this);
		
		move = (LinearBase)findViewById(R.id.move);
		move.setOnClickListener(LocalLogApi.getLocalLogListener());
//		findViewById(R.id.test1).setOnClickListener(this);
//		findViewById(R.id.test2).setOnClickListener(this);
	}
	Animation animation;
	@Override
	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		int id = v.getId();
//		if(id == R.id.test1){
//			move.start();
////			animation = AnimationUtils.loadAnimation(this,R.anim.from_bottom_in);
////			animation.setAnimationListener(new AnimationListener() {
////				
////				@Override
////				public void onAnimationStart(Animation animation) {
////					// TODO Auto-generated method stub
////					Log.e("aa", "onAnimationStart");
////				}
////				
////				@Override
////				public void onAnimationRepeat(Animation animation) {
////					// TODO Auto-generated method stub
////					
////				}
////				
////				@Override
////				public void onAnimationEnd(Animation animation) {
////					// TODO Auto-generated method stub
////					Log.e("aa", "onAnimationEnd");
////				}
////			});
////			
////			v.startAnimation(animation);
//		}else if(R.id.test2 == id){
//			move.end();
////			Animation anim = findViewById(R.id.test1).getAnimation();
////			if(null != anim){
////				anim.cancel();
////				v.setAnimation(null);
////				anim = null;
////			}
////			findViewById(R.id.test1).clearAnimation();
//		}
	}

}
