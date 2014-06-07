package com.android.wefriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.wefriend.adapter.ListAdapter;
import com.android.wefriend.bean.IntrestePoint;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKRouteAddrResult;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.example.wcsmsc.R;
import com.example.wefriend.seacher.SearcherOperator;
import com.example.wefriend.seacher.SearcherOperator.OnGetTransitRouteResult;

public class InfoMationActivity extends Activity implements OnClickListener{
//
	private TextView distanceHint,searchBtn,showByHot,showByHistory;
	private EditText stopName;
	private ListView mListView;
	private MyBroadcastReceiver myBroadcastReceiver;
	private SearcherOperator    searcherOperator;//获取查询
	private LocationClient      mLocClient;      //位置服务
	private MyBDLoctionListenner myBDLoctionListenner;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			ArrayList<IntrestePoint> intrestePoints =(ArrayList<IntrestePoint>) msg.obj;
			ListAdapter listAdapter = new ListAdapter(InfoMationActivity.this, intrestePoints);
			mListView.setAdapter(listAdapter);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("WC.WCC.WCCC");
		registerReceiver(myBroadcastReceiver, intentFilter);
		setContentView(R.layout.main_infor);
		initdata();
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
		mLocClient.unRegisterLocationListener(myBDLoctionListenner);
		mLocClient.stop();
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
			String name =stopName.getText().toString();
			if(name!=null){
				searcherOperator.getBuStationInfo(name, onGetTransitRouteResult);
			}
			break;

		default:
			break;
		}
	}
	/***
	 * 初始化搜索
	 */
	private void initdata(){
		searcherOperator = new SearcherOperator(this);
		mLocClient =  new LocationClient(this);
		myBDLoctionListenner =new MyBDLoctionListenner();
		mLocClient.registerLocationListener(myBDLoctionListenner);
		LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(false);//打开gps
	    option.setPriority(LocationClientOption.GpsFirst); 
	    option.setCoorType("bd09ll");     //设置坐标类型
	    option.setPoiDistance(1000);
	    option.setPoiExtraInfo(true); 
	    mLocClient.setLocOption(option);
	    mLocClient.start();
		
	}
	
	
	/****
	 * 搜索得到公交的信息
	 */
	private OnGetTransitRouteResult onGetTransitRouteResult = new OnGetTransitRouteResult() {
		
		@Override
		public void onGetTransitRouteResult(List<IntrestePoint> intrestePoints, int error) {
			// TODO Auto-generated method stub
			 Message message = new Message();
			 message.obj = intrestePoints;
			 handler.sendMessage(message);
			mLocClient.stop();
		}
	};
	
	/****
	 * 定位服务
	 * @author asus
	 *
	 */
	private class MyBDLoctionListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			searcherOperator.setLocationBDLocation(arg0);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
