package com.car.rent;


import com.car.rent.util.HttpUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends Activity {
	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private ArrayAdapter<String> adapter;
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_system);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);
		

	}

	// 登录方法
	private boolean login() {
		// 获得用户名称
		String username = mUser.getText().toString();
		// 获得密码
		String pwd = mPassword.getText().toString();

		// 获得登录结果
		String result = query(username, pwd);

		if (result != null && result.equals("success")) {
			return true;
		} else {
			// saveUserMsg(result);
			return false;
		}
	}

	public void login_mobile_system(View v) {
		if (validate()) {
			if (login()) {
				mSharedPreferences = getSharedPreferences("SharedPreferences",
						Context.MODE_PRIVATE);
				mEditor = mSharedPreferences.edit();
				mEditor.putString("login_name", mUser.getText().toString());
				mEditor.commit();
				
			/*	Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
				startActivity(intent);*/
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
				
				this.finish();
			} else {
				new AlertDialog.Builder(LoginActivity.this).setTitle("登录错误")
						.setMessage("用户名帐号或者密码不能错误，\n请输入后再登录！").create().show();
			}
		}

	}

	public void login_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void login_register(View v) { // 忘记密码按钮

		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);

	}

	// 验证方法
	private boolean validate() {
		String username = mUser.getText().toString();
		if (username.equals("")) {

			showDialog("用户名称是必填项！");
			return false;
		}
		String pwd = mPassword.getText().toString();
		if (pwd.equals("")) {
			showDialog("用户密码是必填项！");
			return false;
		}
		return true;
	}

	private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// 根据用户名称密码查询
	private String query(String account, String password) {
	
		String queryString = "account=" + account + "&password=" + password;
								
		// url
		String url = HttpUtil.BASE_URL + "servlet/LoginServlet?" + queryString;

		// 查询返回结果

		return HttpUtil.queryStringForPost(url);
	}
	
}
