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
import com.car.rent.vo.OrderVo;

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



public class HistoryOrderActivity extends Activity   {

	private ListView myorderViewList = null;

	private List<OrderVo> myorderList = new ArrayList<OrderVo>();
	
	private OrderVo selMyOrder;
	private SharedPreferences mSharedPreferences;
	private String login_name;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		
		mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		login_name = mSharedPreferences.getString("login_name", null);
		
		
		
		myorderViewList = (ListView) findViewById(R.id.historyorder_list);
		
		
		initView();
		
		myorderViewList.setOnItemClickListener(new MyListItemListener());
		
	}

	private void initView() {
		myorderList.clear();
		String url = HttpUtil.BASE_URL +"/servlet/ClientOrderServlet?action=5&username="+login_name;
		String result = HttpUtil.queryStringForGet(url);
		try {
			JSONArray tables = new JSONArray(result);
			
			for(int i=0;i<tables.length();i++)
			{
				JSONObject obj_tmp = tables.getJSONObject(i);
				OrderVo myorderVo = new OrderVo();
				myorderVo.setId(obj_tmp.getInt("id"));
				myorderVo.setCartype(obj_tmp.getString("cartype"));
				myorderVo.setCarno(obj_tmp.getString("carno"));
				myorderVo.setStarttime(obj_tmp.getString("startTime"));
				myorderVo.setEndtime(obj_tmp.getString("endTime"));
				myorderVo.setOrderday(obj_tmp.getInt("totalDay"));
				myorderVo.setTotalprice(obj_tmp.getInt("totalPrice"));
				myorderVo.setOstate(obj_tmp.getInt("state"));
				
				myorderList.add(myorderVo);
			}
			myorderViewList.setAdapter(new Myadapter(HistoryOrderActivity.this , myorderList));
			
			
		} catch (JSONException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();	
			Log.e("mobile", "ת�����ݳ���");
		}
		
	}

	public final class MyListItemListener implements OnItemClickListener{
   		AlertDialog dialog;
   		public void onItemClick(AdapterView<?> view, View arg1,
			final int position, long arg3) {
   			
   			
   			selMyOrder= myorderList.get(position);
   			if(selMyOrder.getOstate()!=2){
   				AlertDialog.Builder builder = new AlertDialog.Builder(
   						HistoryOrderActivity.this);
   				builder.setItems(R.array.myorder_choice,
   						new DialogInterface.OnClickListener() {
   							public void onClick(DialogInterface dialog, int which) {

   								switch (which) {
   								case 0: //
   									
   									String url = HttpUtil.BASE_URL +"/servlet/ClientOrderServlet?action=4&id="+String.valueOf(selMyOrder.getId());
   									HttpUtil.queryStringForGet(url);
   									Toast.makeText(HistoryOrderActivity.this, "ȡ���ɹ�", 10).show();
   									initView();
   									break;
   								
   								}
   								dialog.dismiss();
   							}
   						});

   				dialog = builder.create();
   				dialog.show();
   			}
			
   			
   			
   			
			
   			
   		}
    }
	
	
	
	private class Myadapter extends BaseAdapter {
		private Context c;
		private List<OrderVo> list;

		public Myadapter(Context c, List<OrderVo> list) {
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
			View v = LayoutInflater.from(c).inflate(R.layout.item_myorder,
					null);

			
			TextView tv1 = (TextView) v.findViewById(R.id.myorderCarNo);
			TextView tv2 = (TextView) v.findViewById(R.id.myorderDate);
			TextView tv3 = (TextView) v.findViewById(R.id.myorderPrice);
			TextView tv4 = (TextView) v.findViewById(R.id.myorderState);
			
			
			tv1.setText(list.get(position).getCarno()+":"+list.get(position).getCartype());
			
			tv2.setText("ʱ�Σ�"+list.get(position).getStarttime()+"��"+list.get(position).getEndtime());
			tv3.setText("�۸�"+list.get(position).getTotalprice()+"Ԫ");
			if(list.get(position).getOstate()==1){
				tv4.setText("������");
			}else if(list.get(position).getOstate()==2){
				tv4.setText("Ԥ�����");
			}else if(list.get(position).getOstate()==0){
				tv4.setText("Ԥ��ʧ�ܣ���ɾ��");
			}
			
			return v;
		}

	}
}
