package com.example.map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class WcService extends Service implements Runnable{
	private LocationClient mLocClient;
	 
	private GeoPoint       geoPointobj;//目标距离
	private double    dm=500;             //提醒距离
	private int    time = 1000*10;
	private LocationListenner ltl;
    private Intent   lockintent ;
    private boolean  is = false;
     
    
    private GeoPoint g[] = new GeoPoint[20];
    public static final  String   s[] ={"南京东路","人民广场","汶水路站"};
    private static int index = 0;
    private Thread thread;
	 
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*注册广播*/
		IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
		mScreenOnFilter.setPriority(-888);
		mScreenOnFilter.addAction("android.intent.action.SCREEN_OFF");
	    this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);
	    lockintent = new Intent(this,SmsMapActivity.class);
	    lockintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 
		
		 
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		thread.interrupt();
		Log.v("WC","服务被杀了");
		unregisterReceiver(mScreenOnReceiver);
		mLocClient.unRegisterLocationListener(ltl);
		mLocClient.stop();
	 
		
		 
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		SharedPreferences sp = getSharedPreferences("DATE",Context.MODE_APPEND );
		if(intent!=null){
			this.index =intent.getIntExtra("INDEX",0); 
			Editor editor =sp.edit();
			editor.putInt("Index", index);
			editor.commit();
		}else{
			index = sp.getInt("Index", 0);
		}
		if(!is)
		start(null);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@SuppressLint("NewApi")
	private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			 
 		 
			
		}
		
	};
	
	
	public double d;
	/****
	 * 
	 * @author 定位服务
	 *
	 */
	public class LocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			GeoPoint geoPoint = new GeoPoint(0, 0);
			geoPoint.setLatitudeE6((int)(arg0.getLatitude()*1e6));
			geoPoint.setLongitudeE6((int)(arg0.getLongitude()*1e6));
			d =DistanceUtil.getDistance(geoPoint, geoPointobj);
			Intent intent = new Intent();
			if(arg0.getLocType()== BDLocation.TypeGpsLocation){
				intent.putExtra("NND","GPS"+ s[index]+""+d+":");
			}else if(arg0.getLocType()== BDLocation.TypeNetWorkLocation){
				intent.putExtra("NND", "NETWork"+s[index]+""+d+":");
			}else if(arg0.getLocType()==BDLocation.TypeOffLineLocationNetworkFail){
				intent.putExtra("NND", arg0.getLocType()+"NETWorkEroor"+s[index]+""+d+":" );
			}
			intent.setAction("WC.WCC.WCCC");
			sendBroadcast(intent);
			if(d<=WcService.this.dm){
				 PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
				 WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
				 mWakelock.acquire();
				 mWakelock.release();
				 Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
				// vibrator.vibrate(new long[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},-1);
			     vibrator.vibrate(1000);
			 }
		}
         
		///获取附近的一些基本信息
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
			if(poiLocation==null)return ;
			  StringBuffer sb = new StringBuffer(256);
			 
			GeoPoint geoPoint =new GeoPoint(0, 0);
			  geoPoint .setLatitudeE6((int)(poiLocation.getLatitude()*1e6));
		      geoPoint.setLongitudeE6((int)(poiLocation.getLongitude()*1e6));
				//mLocClient.getLocOption().setScanSpan(time);
				 
				d =DistanceUtil.getDistance(geoPoint, geoPointobj);
	          sb.append("Poi time : ");
	          sb.append(poiLocation.getTime());
	          sb.append("\nerror code : ");
	          sb.append(poiLocation.getLocType());
	          sb.append("\nlatitude : ");
	          sb.append(poiLocation.getLatitude());
	          sb.append("\nlontitude : ");
	          sb.append(poiLocation.getLongitude());
	          sb.append("\nradius : ");
	          sb.append(poiLocation.getRadius());
	          if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
	              sb.append("\naddr : ");
	              sb.append(poiLocation.getAddrStr());
	         } 
	          if(poiLocation.hasPoi()){
	               sb.append("\nPoi:");
	               sb.append(poiLocation.getPoi());
	         }else{             
	               sb.append("noPoi information");
	          }
	          Intent intent = new Intent();
		      intent.putExtra("NND", d+":::::::::::::"+sb.toString());
			  intent.setAction("WC.WCC.WCCC");
			  sendBroadcast(intent);
	          Log.v("WC", ":::::::"+sb.toString());
		}
		
	}
	/****
	 * 开始设置提醒
	 * @param arg0
	 */
	public void start(MKTransitRouteResult arg0){
		//this.mkrr = arg0;
		g[0] = new GeoPoint((int)(31.242839*1e6), (int)(121.490053*1e6));
		g[1] = new GeoPoint((int)(31.238196*1e6), (int)(121.482219*1e6));
		g[2] = new GeoPoint((int)(31.299718*1e6), (int)(121.456401*1e6));
		geoPointobj =  g[index];
		ltl = new LocationListenner();
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(ltl);
		LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(false);//打开gps
	    option.setPriority(LocationClientOption.GpsFirst); 
	    option.setCoorType("bd09ll");     //设置坐标类型
	    option.setPoiDistance(1000);
	    option.setPoiExtraInfo(true); 
	   // option.setScanSpan(time);
	    mLocClient.setLocOption(option);
	   
		thread = new Thread(this);
		thread.start();
	 
	}
	
	public void stop(){
		if (mLocClient != null){
			 mLocClient.stop();
			 is = false;
		}
	
           
	}
    /***
     *
     * @return
     */
	public MKTransitRouteResult getMkrr() {
		return null;
	}
    /****
     * 
     * @return
     */
	public GeoPoint getGeoPointobj() {
		return geoPointobj;
	}
    
	
	 
  
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		geoPointobj = g[index];
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 is = true ;
		while(is){
			try {
				 
				if(mLocClient!=null&&!mLocClient.isStarted())
					mLocClient.start();
				else{
					  mLocClient.requestLocation();
					}
				Thread.sleep(1000*30);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 is = false ;
		}
	}
	
	
}
