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
 * С�������
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

		// �������κν��浯���Լ��ĸ���
		windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		// �����Ļ���
		winWidth = windowManager.getDefaultDisplay().getWidth();
		winHeight = windowManager.getDefaultDisplay().getHeight();

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		// //����Ч��
		// params.windowAnimations =
		// com.android.internal.R.style.Animation_Toast;

		// myView ����ʾ����ΪToast�������ȼ��Ƚϵͣ��������Ϊ TYPE_PHONE,�绰���ڣ����ڵ绰�������ر��ǵ绰����
		// ����ҪȨ�ޣ�android.permission.SYSTEM_ALERT_WINDOW
		// params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;

		// �����Ĵ��м��������Ϸ���0,0��
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.setTitle("Toast");
		// "���ɴ���"����ɾ��,"������Ļ����"�ص�
		// params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		// | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		myView = View.inflate(this, R.layout.rocket, null);

		// ���ػ��֡����AnimationDrawable
		ImageView iv_rocket = (ImageView) myView.findViewById(R.id.iv_rocket1);
		iv_rocket.setBackgroundResource(R.drawable.anim_rocket);
		AnimationDrawable anim = (AnimationDrawable) iv_rocket.getBackground();
		anim.start();

		windowManager.addView(myView, params);

		// �����¼�
		myView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// ����������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					// ��ô�ʱ������
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// ����ƶ�ƫ����
					int dx = endX - startX;
					int dy = endY - startY;

					// ���ø���λ�ã��������Ϸ���ƫ����
					params.x += dx;
					params.y += dy;

					// ��ֹ����ƫ�Ƴ���Ļ
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

					// ���»���������
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_UP:
					// ��������������������
					if (params.x > winWidth / 3
							&& params.x < winWidth * 2 / 3 - myView.getWidth()
							&& params.y > winHeight - 1.5 * myView.getHeight()) {
						
						sendRocket();

						// ����һ��ջ�����activity
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
	 * ������
	 */
	protected void sendRocket() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				// �������
				params.x = winWidth / 2 - myView.getWidth() / 2;

				// ����ƶ�����
				int pos = winHeight;
				// ѭ������
				int times = 40;
				for (int i = 0; i <= times; i++) {
					try {
						// ���ߣ����ƻ���ٶ�
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
