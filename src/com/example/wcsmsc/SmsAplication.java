package com.example.wcsmsc;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
 
import com.example.map.InfoMationActivity;
import com.example.map.WcService;

public class SmsAplication extends Application{
	 public BMapManager mBMapManager = null;
	 public static final String strKey = "yYuMO2sfFuE0atYmcm2Iwqux";
	  
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initEngineManager(this);
		
	}


	public void initEngineManager(Context context) {
	        if (mBMapManager == null) {
	            mBMapManager = new BMapManager(context);
	        }

	        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
	            Toast.makeText(SmsAplication.this.getBaseContext(), 
	                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
	        }
		}
	
	  class MyGeneralListener implements MKGeneralListener {
	        
	        @Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(SmsAplication.this.getBaseContext(), "您的网络出错啦！",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(SmsAplication.this.getBaseContext(), "输入正确的检索条件！",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	        	//非零值表示key验证未通过
	             
	        }
	    }
	  
	 
	  
	  
}
