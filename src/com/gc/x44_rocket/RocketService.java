package com.gc.x44_rocket;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 小火箭服务
 * 
 * @author guochang
 * 
 */
public class RocketService extends Service {

	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;
	private View myView;
	private int startX;
	private int startY;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 可以在任何界面弹出自己的浮窗
		windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		// 获得屏幕宽高
		winWidth = windowManager.getDefaultDisplay().getWidth();
		winHeight = windowManager.getDefaultDisplay().getHeight();

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		// //动画效果
		// params.windowAnimations =
		// com.android.internal.R.style.Animation_Toast;

		// myView 的显示类型为Toast级别，优先级比较低，因此设置为 TYPE_PHONE,电话窗口，用于电话交互，特别是电话呼入
		// （需要权限）android.permission.SYSTEM_ALERT_WINDOW
		// params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;

		// 将重心从中间移至左上方（0,0）
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.setTitle("Toast");
		// "不可触摸"设置删掉,"保持屏幕常亮"关掉
		// params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		// | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		myView = View.inflate(this, R.layout.rocket, null);

		// 加载火箭帧动画AnimationDrawable
		ImageView iv_rocket = (ImageView) myView.findViewById(R.id.iv_rocket1);
		iv_rocket.setBackgroundResource(R.drawable.anim_rocket);
		AnimationDrawable anim = (AnimationDrawable) iv_rocket.getBackground();
		anim.start();

		windowManager.addView(myView, params);

		// 触摸事件
		myView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 获得起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					// 获得此时的坐标
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 获得移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 设置浮窗位置，基于左上方的偏移量
					params.x += dx;
					params.y += dy;

					// 防止坐标偏移出屏幕
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					if (params.x > winWidth - myView.getWidth()) {
						params.x = winWidth - myView.getWidth();
					}

					if (params.y > winHeight - myView.getHeight()) {
						params.y = winHeight - myView.getHeight();
					}

					windowManager.updateViewLayout(myView, params);

					// 从新获得起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_UP:
					// 触发发射火箭的条件区域
					if (params.x > winWidth / 3
							&& params.x < winWidth * 2 / 3 - myView.getWidth()
							&& params.y > winHeight - 1.5 * myView.getHeight()) {
						
						sendRocket();

						// 启动一个栈来存放activity
						Intent intent = new Intent(RocketService.this,
								BackgroundActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int y = msg.arg1;
			params.y = y;
			windowManager.updateViewLayout(myView, params);
		};
	};

	/**
	 * 发射火箭
	 */
	protected void sendRocket() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				// 火箭居中
				params.x = winWidth / 2 - myView.getWidth() / 2;

				// 火箭移动距离
				int pos = winHeight;
				// 循环次数
				int times = 40;
				for (int i = 0; i <= times; i++) {
					try {
						// 休眠，控制火箭速度
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					int y = pos - winHeight / times * i;

					Message msg = Message.obtain();
					msg.arg1 = y;
					mHandler.sendMessage(msg);

				}
			};
		}.start();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (windowManager != null && myView != null) {
			windowManager.removeView(myView);
			myView = null;
		}
	}
}
