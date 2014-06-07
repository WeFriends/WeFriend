package com.android.wefriend.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wcsmsc.R;

public class InfoMationActivity extends Activity implements OnClickListener{
//
	private TextView distanceHint,searchBtn,showByHot,showByHistory;
	private EditText stopName;
	private ListView mListView;
	private MyBroadcastReceiver myBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("WC.WCC.WCCC");
		registerReceiver(myBroadcastReceiver, intentFilter);
		setContentView(R.layout.main_infor);
		initViews();
	}

	private void initViews(){
		distanceHint = (TextView)findViewById(R.id.distance_hint);
		searchBtn = (TextView)findViewById(R.id.search_searchbtn);
		showByHot = (TextView)findViewById(R.id.byHot);
		showByHistory = (TextView)findViewById(R.id.byHistory);
		stopName = (EditText)findViewById(R.id.search_edit);
		mListView = (ListView)findViewById(R.id.searchresult_list);

		showByHot.setOnClickListener(this);
		showByHistory.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
	}
	
	/**
	 * 开启定位服务
	 * */
	private void initService(int type) {
		Intent intent = new Intent(InfoMationActivity.this, WcService.class);
		intent.putExtra("INDEX", type);
		startService(intent);
	}

	/**
	 * 关闭定位服务
	 * */
	private void stopService() {
		Intent intent = new Intent(InfoMationActivity.this, WcService.class);
		stopService(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String str = intent.getStringExtra("NND");
			distanceHint.setText("离目的地：" + str + "米");
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int which = arg0.getId();
		switch (which) {
		case R.id.byHistory:
			showByHistory.setBackgroundColor(Color.GRAY);
			showByHot.setBackgroundColor(Color.WHITE);
			break;
		case R.id.byHot:
			showByHistory.setBackgroundColor(Color.WHITE);
			showByHot.setBackgroundColor(Color.GRAY);
			break;
		case R.id.search_searchbtn:
			
			break;

		default:
			break;
		}
	}

}
