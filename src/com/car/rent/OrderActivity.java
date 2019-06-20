package com.car.rent;

import java.io.InputStream;


import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import com.car.rent.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
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
public class OrderActivity extends Activity {

	private EditText duringEdit;

	private Button startPicker;
	private TextView endTextView;

	private Button cancelBtn;
	private Button saveBtn;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;



	static final int SDATE_DIALOG_ID = 0;
	static final int EDATE_DIALOG_ID = 1;

	private String selcarno;
	
	
	
	private ListView haveOrderViewList = null;
	private List<String> haveOrderList = new ArrayList<String>();

	private SharedPreferences mSharedPreferences;
	private String login_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			selcarno = intent.getStringExtra("no");
		}
		//.makeText(OrderActivity.this, selcarno, 20).show();
		mSharedPreferences = getSharedPreferences("SharedPreferences",
				Context.MODE_PRIVATE);
		login_name = mSharedPreferences.getString("login_name", null);
		
		
		saveBtn = (Button) findViewById(R.id.orderSaveBtn);
		cancelBtn = (Button) findViewById(R.id.orderCloseBtn);
		duringEdit = (EditText) findViewById(R.id.order_during);
		startPicker = (Button) findViewById(R.id.order_startdate);
		endTextView = (TextView) findViewById(R.id.order_end_date_txt);

		startPicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(SDATE_DIALOG_ID);
			}
		});

		// 获取现在的时间
		final Calendar c = Calendar.getInstance();
		startYear = c.get(Calendar.YEAR);
		startMonth = c.get(Calendar.MONTH);
		startDay = c.get(Calendar.DAY_OF_MONTH);

		endYear = c.get(Calendar.YEAR);
		endMonth = c.get(Calendar.MONTH);
		endDay = c.get(Calendar.DAY_OF_MONTH);

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		haveOrderViewList = (ListView) findViewById(R.id.allhaveorder_list);
		initView2();
	}

	public void initView2() {
		haveOrderList.clear();
		String url = HttpUtil.BASE_URL
				+ "/servlet/ClientOrderServlet?action=2&no=" + selcarno;
		String result = HttpUtil.queryStringForGet(url);
		try {
			JSONArray tables = new JSONArray(result);
			for (int i = 0; i < tables.length(); i++) {
				JSONObject obj_tmp = tables.getJSONObject(i);
				haveOrderList.add(obj_tmp.getString("haveorderdate"));
			}

			haveOrderViewList.setAdapter(new Myadapter(OrderActivity.this,
					haveOrderList));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("mobile", "转换数据出错");
		}

	}

	
	private void updateenddatedisplay(String time) {
		if (time == "start") {
			if (duringEdit.getText().toString() == null
					|| duringEdit.getText().toString().equals(""))
				return;
			int ordernum = Integer.parseInt(duringEdit.getText().toString());
			
			String formatStartDate = dateFormat(startYear, startMonth, startDay);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date dd = null;
			try {
				dd = format.parse(formatStartDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dd);
				calendar.add(Calendar.DAY_OF_MONTH, ordernum);
				calendar.add(Calendar.MONTH, 1);
				String endtext = format.format(calendar.getTime());
				endTextView.setText(endtext);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	private void updatedisplay(String time) {
		if (time == "start") {
			startPicker.setText(new StringBuilder()
					.append(startYear)
					.append("-")
					.append((startMonth + 1) < 10 ? "0" + (startMonth + 1)
							: (startMonth + 1)).append("-")
					.append((startDay < 10) ? "0" + startDay : startDay));
			;
		} else {
			
		}

	}
	private String dateFormat(int year, int month, int day) {

		String str_date;
		String str_year, str_month, str_day;

		str_year = String.valueOf(year);

		if (month < 10) {
			str_month = "0" + String.valueOf(month);
		} else {
			str_month = String.valueOf(month);
		}

		if (day < 10) {
			str_day = "0" + String.valueOf(day);
		} else {
			str_day = String.valueOf(day);
		}

		str_date = str_year + "-" + str_month + "-" + str_day;
		return str_date;
	}

	private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startYear = year;
			startMonth = monthOfYear;
			startDay = dayOfMonth;
			updatedisplay("start");
			updateenddatedisplay("start");
		}

	};


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SDATE_DIALOG_ID:
			return new DatePickerDialog(this, sDateSetListener, startYear,
					startMonth, startDay);
		
		}
		return null;
	}

	public void order_save(View v) {
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
	    String nowdate = sdf.format(date);
	    if(startPicker.getText().toString().compareTo(nowdate)<0){
	    	Toast.makeText(OrderActivity.this, "预定起始时间不能小于当前日期", 10).show();
	    	return;
	    }
		
		try {
			String url = HttpUtil.BASE_URL
					+ "/servlet/ClientOrderServlet?action=1" + "&no="
					+ selcarno + "&during="
					+ duringEdit.getText().toString() + "&start_time="
					+ startPicker.getText().toString() + "&end_time="
					+ endTextView.getText().toString() + "&user_name="
					+ login_name;
			String result = HttpUtil.queryStringForGet(url);
			if (result != null & result.equals("success")) {
				Toast.makeText(OrderActivity.this, "预定成功", 10).show();
				
				this.finish();

			} else {
				Toast.makeText(OrderActivity.this, "预定失败,已有人预定", 10).show();
			}

		} catch (Exception e) {
			Toast.makeText(OrderActivity.this, "预定失败", 10).show();
			e.printStackTrace();
		}
	}

	

	private class Myadapter extends BaseAdapter {
		private Context c;
		private List<String> list;

		public Myadapter(Context c, List<String> list) {
			this.c = c;
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = LayoutInflater.from(c).inflate(R.layout.item_haveorder,
					null);

			TextView tv1 = (TextView) v.findViewById(R.id.haveorderinfo);

			tv1.setText("时间段：" + list.get(position));

			// tv2.setText(list.get(position).getFriend_state());

			return v;
		}

	}

}
