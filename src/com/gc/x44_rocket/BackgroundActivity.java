package com.gc.x44_rocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * ªº˝∑¢…‰ ±±≥æ∞£®‘∆ŒÌ£©
 * 
 * @author guochang
 * 
 */
public class BackgroundActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bg);
		
		ImageView iv_smoke_up = (ImageView) findViewById(R.id.iv_smoke_top);
		ImageView iv_smoke_bottom = (ImageView) findViewById(R.id.iv_smoke_bottom);
		
		//Ω•±‰∂Øª≠
		AlphaAnimation anim1 = new AlphaAnimation(0, 1);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		
		AlphaAnimation anim2 = new AlphaAnimation(0, 1);
		anim2.setDuration(350);
		anim2.setFillAfter(true);
		
		iv_smoke_up.startAnimation(anim2);
		iv_smoke_bottom.startAnimation(anim1);
		
		//—” ±500∫¡√Î∫Û÷¥––Runnable
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 500);
	}
}
