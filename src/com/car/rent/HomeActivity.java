package com.car.rent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnTouchListener {

	private long waitTime = 2000;
	private long touchTime = 0;

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private String login_name;

	private RelativeLayout[] relativeLayouts = new RelativeLayout[4];
	private int[] ids = { R.id.home_model0_layout, R.id.home_model1_layout,
			R.id.home_model2_layout, R.id.home_model3_layout };
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		context = this;
		intiView();

		
	}

	private void intiView() {
		for (int i = 0; i < relativeLayouts.length; i++) {
			relativeLayouts[i] = (RelativeLayout) findViewById(ids[i]);
			relativeLayouts[i].setOnTouchListener(this);
		}
	}

	private void onClick(View v) {
		mSharedPreferences = getSharedPreferences("SharedPreferences",
				Context.MODE_PRIVATE);
		login_name = mSharedPreferences.getString("login_name", null);
		Intent i;
		switch (v.getId()) {
		case R.id.home_model0_layout:
			// Toast.makeText(context, "-------", 0).show();
			i = new Intent(HomeActivity.this, CarLookActivity.class);
			startActivity(i);
			break;
		case R.id.home_model1_layout:
			if (login_name == null) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				i = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(i);
			} else {
				i = new Intent(HomeActivity.this, MyOrderActivity.class);
				startActivity(i);
			}

			break;
		case R.id.home_model2_layout:
			if (login_name == null) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				i = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(i);
			} else {
				i = new Intent(HomeActivity.this, HistoryOrderActivity.class);
				startActivity(i);
			}
			break;
		case R.id.home_model3_layout:
			if (login_name == null) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				i = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(i);
			} else {
				i = new Intent(HomeActivity.this, PersonalActivity.class);
				startActivity(i);
			}
			break;
		}
	}

	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		int action = event.getAction();
		Animation anim = null;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			anim = AnimationUtils.loadAnimation(context, R.anim.scale_in);
			v.startAnimation(anim);
			myAnimationListener.setView(v);
			myAnimationListener.startAnimation();
			anim.setAnimationListener(myAnimationListener);
			break;
		case MotionEvent.ACTION_UP:
			myAnimationListener.stopAnimation();
			anim = AnimationUtils.loadAnimation(context, R.anim.scale_out);
			v.startAnimation(anim);
			anim.setAnimationListener(new MyAnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
					onClick(v);
				}
			});
			break;
		}
		return true;
	}

	private MyAnimationListener myAnimationListener = new MyAnimationListener();

	private class MyAnimationListener implements AnimationListener {

		private View v;

		private boolean isStop;

		private void setView(View v) {
			this.v = v;
		}

		private void stopAnimation() {
			isStop = true;
		}

		private void startAnimation() {
			isStop = false;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (!isStop) {
				Animation anim = AnimationUtils.loadAnimation(context,
						R.anim.scale_in_static);
				v.startAnimation(anim);
				anim.setAnimationListener(this);
			}
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 退出系统
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {

				SharedPreferences mSharedPreferences = getSharedPreferences(
						"SharedPreferences", Context.MODE_PRIVATE);
				mEditor = mSharedPreferences.edit();
				mEditor.clear();
				mEditor.commit();

				this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
