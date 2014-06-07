package com.example.wcsmsc;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.wefriend.activity.InfoMationActivity;
import com.android.wefriend.activity.WcService;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
 

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
	                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
	        }
		}
	
	  class MyGeneralListener implements MKGeneralListener {
	        
	        @Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(SmsAplication.this.getBaseContext(), "���������������",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(SmsAplication.this.getBaseContext(), "������ȷ�ļ���������",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	        	//����ֵ��ʾkey��֤δͨ��
	             
	        }
	    }
	  
	 
	  
	  
}
