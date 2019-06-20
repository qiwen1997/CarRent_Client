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
public class CarDetailActivity extends Activity {

	private TextView noTxt;
	private TextView typeTxt;
	private TextView producerTxt;
	private TextView priceTxt;
	private TextView yajinTxt, noteTxt;

	private String carno, cartype, carproducer,  carnote;
	private int carprice, caryajin;
	
	UserVo memberVo;
	private SharedPreferences mSharedPreferences;
	private String login_name;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_detail);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			
			carno = intent.getStringExtra("no");
			cartype = intent.getStringExtra("type");
			carproducer = intent.getStringExtra("producer");
			carprice = intent.getIntExtra("price", 0);
			caryajin = intent.getIntExtra("yajin", 0);
			carnote = intent.getStringExtra("note");

		}
		noTxt = (TextView) findViewById(R.id.car_detail_no);
		typeTxt = (TextView) findViewById(R.id.car_detail_type);
		producerTxt = (TextView) findViewById(R.id.car_detail_producer);
		priceTxt = (TextView) findViewById(R.id.car_detail_price);
		yajinTxt = (TextView) findViewById(R.id.car_detail_yajin);
		noteTxt = (TextView) findViewById(R.id.car_detail_note);
		
		noTxt.setText(carno);
		typeTxt.setText(cartype);
		producerTxt.setText(carproducer);
		priceTxt.setText(carprice+"Ôª/ÈÕ");
		yajinTxt.setText(caryajin+"Ôª");
		noteTxt.setText(carnote);
	}

	

}
