package com.example.map;
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
 
import com.example.wcsmsc.R;

public class InfoMationActivity extends Activity{
     
	private TextView textView ;
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
		textView = (TextView)findViewById(R.id.texinfor);
		 
		findViewById(R.id.start).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(true){
					Intent intent = new Intent(InfoMationActivity.this,WcService.class);
					intent.putExtra("INDEX", 0);
					startService(intent);
				}
				
				
			}
		});
		
		findViewById(R.id.start2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(true){
					Intent intent = new Intent(InfoMationActivity.this,WcService.class);
					intent.putExtra("INDEX", 1);
					startService(intent);
				}
			}
		});
       findViewById(R.id.start3).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(true){
					Intent intent = new Intent(InfoMationActivity.this,WcService.class);
					intent.putExtra("INDEX", 2);
					startService(intent);
				}
			}
		});
		
		
		findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InfoMationActivity.this,WcService.class);
				stopService(intent);
				 
			}
		});
	}




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}
	
	
    private class MyBroadcastReceiver extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String str = intent.getStringExtra("NND");
				textView.setText("离目的地："+str+"米");
			}
			  
			  
		  }
	
}
