package com.car.rent;

import java.io.InputStream;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import com.car.rent.util.HttpUtil;
import com.car.rent.vo.CarVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;


import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




public class CarLookActivity extends Activity   {
	 
	private ListView carViewList = null;
	private ListAdapter adapter = null;
	
	private List<CarVo> carList = new ArrayList<CarVo>();
	private CarVo selCar;
	private SharedPreferences mSharedPreferences;

	private String login_name;
	
	private DisplayImageOptions options;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_look);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		
		
		
		carViewList = (ListView) findViewById(R.id.carlook_list);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error).cacheInMemory()
		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		
		
		initView();
		
		
		
		
	}

	private void initView() {
		String url = HttpUtil.BASE_URL +"/servlet/ClientCarServlet?action=1";
		String result = HttpUtil.queryStringForGet(url);
		try {
			JSONArray tables = new JSONArray(result);
			
			for(int i=0;i<tables.length();i++)
			{
				JSONObject obj_tmp = tables.getJSONObject(i);
				CarVo carVo = new CarVo();
				carVo.setNo(obj_tmp.getString("no"));
				carVo.setCtype(obj_tmp.getString("type"));
				carVo.setCproducer(obj_tmp.getString("producer"));
				carVo.setCprice(obj_tmp.getInt("price"));
				carVo.setCyajin(obj_tmp.getInt("yajin"));
				carVo.setCpic(obj_tmp.getString("pic"));
				carVo.setCnote(obj_tmp.getString("note"));
				
				carList.add(carVo);
			}
			carViewList.setAdapter(new Myadapter(CarLookActivity.this , carList));
			
			
		} catch (JSONException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();	
			Log.e("mobile", "转换数据出错");
		}
		
	}

	
	
	
	
	private class Myadapter extends BaseAdapter {
		private Context c;
		private List<CarVo> list;

		public Myadapter(Context c, List<CarVo> list) {
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

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = LayoutInflater.from(c).inflate(R.layout.item_car,
					null);

			ImageView iv = (ImageView) v.findViewById(R.id.car_pic);
			
			TextView tv1 = (TextView) v.findViewById(R.id.car_name);
			TextView tv2 = (TextView) v.findViewById(R.id.car_price);
			TextView tv3 = (TextView) v.findViewById(R.id.car_note);
			Button btn1 = (Button) v.findViewById(R.id.carDetailBtn);
			Button btn2 = (Button) v.findViewById(R.id.carOrderBtn);
			//TextView tv2 = (TextView) v.findViewById(R.id.freind_list_row_state);
			
			
			tv1.setText(list.get(position).getCtype());	
			tv2.setText(String.valueOf(list.get(position).getCprice())+"元/天" );
			tv3.setText(list.get(position).getCnote() );
			
			imageLoader.displayImage(HttpUtil.BASE_URL
					+ list.get(position).getCpic(), iv, options);
			
			/*try {
				URL url = new URL(HttpUtil.BASE_URL 
						+ list.get(position).getCpic());
				// URL url = new URL("http://10.0.2.2:8080/Lvyou/pic/"+pic);
				HttpURLConnection htc = (HttpURLConnection) url
						.openConnection();
				InputStream in = htc.getInputStream();
				Bitmap bit = BitmapFactory.decodeStream(in);
				iv.setImageBitmap(bit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent i = new Intent(CarLookActivity.this, CarDetailActivity.class);
					i.putExtra("no", list.get(position).getNo());
					i.putExtra("type", list.get(position).getCtype());
					i.putExtra("producer", list.get(position).getCproducer());
					i.putExtra("price", list.get(position).getCprice());
					i.putExtra("yajin", list.get(position).getCyajin());
					i.putExtra("note", list.get(position).getCnote());
					
					
					startActivity(i);
				}
			});
			
			btn2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
					login_name = mSharedPreferences.getString("login_name", null);
					if(login_name==null){
						Toast.makeText(CarLookActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(CarLookActivity.this, LoginActivity.class);
						startActivity(i);
					}else{
						Intent i = new Intent(CarLookActivity.this, OrderActivity.class);
						i.putExtra("no", list.get(position).getNo());
						
						startActivity(i);
					}
					
				}
			});
			
			return v;
		}

	}
}
