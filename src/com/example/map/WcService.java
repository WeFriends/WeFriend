package com.example.map;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class WcService extends Service{
	private LocationClient mLocClient;
	private BDLocation bdLocation;
	private GeoPoint geoPointobj;
	private double dm=500;
	private int    time = 1000*10;
	private static WcService loacationService ;
	private LocationListenner ltl;
    private Intent   lockintent ;
    private boolean  is = true;
    private LoacationCallBack loacationCallBack;
    private int m;
    private long last ;
    private NotificationMesg notificationMesg; 
    private GeoPoint g[] = new GeoPoint[20];
    public static final  String   s[] ={"南京东路","人民广场","汶水路站"};
    private int index = 0;
    
	public static WcService getInstance(){
		
		return loacationService ;
	}
	
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
	    lockintent = new Intent(this,LockActivity.class);
	    lockintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.loacationService= this;
		ltl = new LocationListenner();
		 
		
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("WC","服务被杀了");
		unregisterReceiver(mScreenOnReceiver);
		mLocClient.unRegisterLocationListener(ltl);
		loacationService = null;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		this.index =intent.getIntExtra("INDEX",0);
		//Log.v("WC","LKLLJ<NMHMMMMMMMMMM::"+index);
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
	 * @author asus
	 *
	 */
	public class LocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			GeoPoint geoPoint = new GeoPoint(0, 0);
			geoPoint.setLatitudeE6((int)(arg0.getLatitude()*1e6));
			geoPoint.setLongitudeE6((int)(arg0.getLongitude()*1e6));
			 
			
			mLocClient.getLocOption().setScanSpan(time);
			long m = System.currentTimeMillis()-last;
			d =DistanceUtil.getDistance(geoPoint, geoPointobj);
			if(d<4000){
				time = 1000*10;
			}else{
				time =1000*60;
			}
			if(notificationMesg!=null)notificationMesg.noteInfor(d+"");
			Log.v("WC",m+":"+d+"KKKKKKK:::"+geoPointobj.getLatitudeE6()+":"+geoPointobj.getLongitudeE6());
			last = System.currentTimeMillis();
			Intent intent = new Intent();
			intent.putExtra("NND", s[index]+""+d);
			intent.setAction("WC.WCC.WCCC");
			sendBroadcast(intent);
			if(d<=WcService.this.m){
				 PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
				 WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
				 mWakelock.acquire();
				 mWakelock.release();
				 Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
				 vibrator.vibrate(new long[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},-1);
			    //  vibrator.vibrate(1000*100);
			 }
			 
			 
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/****
	 * 开始设置提醒
	 * @param arg0
	 */
	public void start(MKTransitRouteResult arg0){
		//this.mkrr = arg0;
		KeyguardManager kManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
	    KeyguardManager.KeyguardLock mKeyguardLock = kManager.newKeyguardLock("unLock"); 
		mKeyguardLock.disableKeyguard();
		g[0] = new GeoPoint((int)(31.242839*1e6), (int)(121.490053*1e6));
		g[1] = new GeoPoint((int)(31.238196*1e6), (int)(121.482219*1e6));
		g[2] = new GeoPoint((int)(31.299718*1e6), (int)(121.456401*1e6));
		geoPointobj =  g[index];
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(ltl);
		LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);//打开gps
	    option.setPriority(LocationClientOption.NetWorkFirst); 
	    option.setCoorType("bd09ll");     //设置坐标类型
	    option.setScanSpan(time);
	    mLocClient.setLocOption(option);
	    is = true ;
		mLocClient.start();
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
    /******
     * 
     * @return
     */
	public BDLocation getBdLocation() {
		return bdLocation;
	}
	
	/****
	 * 
	 * @param loacationService
	 */
	public static void setLoacationService(WcService loacationService) {
		WcService.loacationService = loacationService;
	}
    
	

	public void setLoacationCallBack(LoacationCallBack loacationCallBack) {
		this.loacationCallBack = loacationCallBack;
	}



	public interface LoacationCallBack{
		public void callBackPosition(BDLocation bdLocation);
	}
	
	
	public interface NotificationMesg{
		public void noteInfor(String str);
	}
	
	
	
	public NotificationMesg getNotificationMesg() {
		return notificationMesg;
	}

	public void setNotificationMesg(NotificationMesg notificationMesg) {
		this.notificationMesg = notificationMesg;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		geoPointobj = g[index];
	}
	
	
}
