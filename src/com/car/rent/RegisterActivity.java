package com.car.rent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.car.rent.util.HttpUtil;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText register_user_name_edit;
	private EditText register_user_password1_edit;
	private EditText register_user_password2_edit;
	private EditText register_realname_edit;
	private EditText register_certify_edit;
	private EditText register_email_edit;
	private EditText register_phone_edit;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		register_user_name_edit = (EditText) findViewById(R.id.register_username);
		register_user_password1_edit = (EditText) findViewById(R.id.register_password1);
		register_user_password2_edit = (EditText) findViewById(R.id.register_password2);
		register_realname_edit = (EditText) findViewById(R.id.register_realname);
		register_certify_edit = (EditText) findViewById(R.id.register_certify);
		register_email_edit = (EditText) findViewById(R.id.register_email);
		register_phone_edit = (EditText) findViewById(R.id.register_phone);

	}

	public void register_system(View v) { // 忘记密码按钮

		if (register_user_password1_edit.getText().toString()
				.equals(register_user_password2_edit.getText().toString())) {
			// 获得用户名称
			String username = register_user_name_edit.getText().toString();
			// 获得密码
			String pwd = register_user_password1_edit.getText().toString();

			String realname = register_realname_edit.getText().toString();
			String certify = register_certify_edit.getText().toString();
			String email = register_email_edit.getText().toString();
			String phone = register_phone_edit.getText().toString();
			
		/*	if(!isMobileNO(phone)){
				Toast.makeText(RegisterActivity.this, "手机格式不对", 10).show();
				return;
			}
			if(!isEmail(email)){
				Toast.makeText(RegisterActivity.this, "邮箱格式不对", 10).show();
				return;
			}
			if(!(isNumeric(certify)&&certify.length()==18)){
				Toast.makeText(RegisterActivity.this, "身份证格式不对", 10).show();
				return;
			}*/
			
			// 获得登录结果
			String result = regsisterServer(username, pwd, realname, certify,
					email, phone);
			if (result != null && result.equals("success")) {
				Toast.makeText(RegisterActivity.this, "注册成功", 10).show();
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				// saveUserMsg(result);
				new AlertDialog.Builder(RegisterActivity.this)

				.setTitle("注册错误").setMessage("用户名帐号或者密码不能注册，\n请确认后再注册！")
						.create().show();
			}
		} else {
			new AlertDialog.Builder(RegisterActivity.this)

			.setTitle("注册错误").setMessage("两次密码应相同，\n请输入后再注册！").create().show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	// 根据用户名称密码查询
	private String regsisterServer(String account, String password,
			String realname, String certify, String email, String phone) {

		String queryString = "account=" + account + "&password=" + password
				+ "&realname=" + realname + "&certify=" + certify + "&email="
				+ email + "&phone=" + phone;

		// url
		String url = HttpUtil.BASE_URL + "servlet/RegisterServlet?action=1&"
				+ queryString;

		// 查询返回结果
		return HttpUtil.queryStringForPost(url);

	}

	// 判断手机格式是否正确
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	// 判断email格式是否正确
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	// 判断是否全是数字
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

}
