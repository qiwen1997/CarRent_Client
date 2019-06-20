package com.car.rent;

import java.io.InputStream;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.car.rent.util.HttpUtil;
import com.car.rent.vo.UserVo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 
 */
public class PersonalActivity extends Activity {

	private TextView nameTxt;
	private TextView passwordTxt;
	private TextView realnameTxt;
	private TextView certifyTxt;
	private TextView phoneTxt, emailTxt;

	UserVo memberVo;
	private SharedPreferences mSharedPreferences;
	private String login_name;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		nameTxt = (TextView) findViewById(R.id.personal_name);
		passwordTxt = (TextView) findViewById(R.id.personal_password);
		realnameTxt = (TextView) findViewById(R.id.personal_realname);
		certifyTxt = (TextView) findViewById(R.id.personal_certify);
		phoneTxt = (TextView) findViewById(R.id.personal_phone);
		emailTxt = (TextView) findViewById(R.id.personal_email);
		memberVo = new UserVo();
		
		mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		login_name = mSharedPreferences.getString("login_name", null);
		
		
		initView();
	}

	public void initView() {

		String url = HttpUtil.BASE_URL
				+ "/servlet/ClientMemberServlet?action=1&username=" + login_name;
		String result = HttpUtil.queryStringForGet(url);
		System.out.println(result);
		try {

			JSONArray tables = new JSONArray(result);

			JSONObject obj_tmp = tables.getJSONObject(0);

			memberVo.setName(obj_tmp.getString("name"));
			memberVo.setPassword(obj_tmp.getString("password"));
			memberVo.setRealname(obj_tmp.getString("realname"));
			memberVo.setCertifyNo(obj_tmp.getString("certify"));
			memberVo.setPhone(obj_tmp.getString("phone"));
			memberVo.setEmail(obj_tmp.getString("email"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("mobile", "转换数据出错");
		}

		nameTxt.setText(memberVo.getName());
		passwordTxt.setText(memberVo.getPassword());
		realnameTxt.setText(memberVo.getRealname());
		certifyTxt.setText(memberVo.getCertifyNo());
		phoneTxt.setText(memberVo.getPhone());
		emailTxt.setText(memberVo.getEmail());

	}
	@Override
	public void onResume(){
		super.onResume();
		initView();
	}
	
	public void modify_personal(View v){
		Intent i = new Intent(PersonalActivity.this, PersonalModifyActivity.class);
	
		
		startActivity(i);
		
	}

}
