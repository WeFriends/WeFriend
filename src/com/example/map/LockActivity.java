package com.example.map;

 
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.example.map.WcService.LoacationCallBack;
import com.example.wcsmsc.R;
import com.example.wcsmsc.SmsAplication;

public class LockActivity extends Activity implements LoacationCallBack{

	private KeyguardLock mKeyguardLock;
	
	private MapView mMapView = null;	// µØÍ¼View
	private MapController mMapController = null;
	private MyLocationOverlay myLocationOverlay = null; //¶¨Î»Í¼²ã

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_lock);
		initMapView();
//		KeyguardManager kManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
//		mKeyguardLock = kManager.newKeyguardLock("unLock"); 
//		mKeyguardLock.disableKeyguard();
		
		Log.v("WC","LockActivity:OnCreat");
		findViewById(R.id.lock).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	
	private void initMapView(){
		SmsAplication app = (SmsAplication)this.getApplication();
		if (app.mBMapManager == null) {
	            app.mBMapManager = new BMapManager(getApplicationContext());
	      }
		mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(false);
        WcService wcService = WcService.getInstance();
        MKTransitRouteResult mkrr =wcService.getMkrr();
        mMapController.animateTo(mkrr.getEnd().pt);
        TransitOverlay routeOverlay = new TransitOverlay(LockActivity.this, mMapView);
        MKTransitRoutePlan mkr= mkrr.getPlan(0);
	    routeOverlay.setData(mkr);
	    mMapView.getOverlays().clear();
	    mMapView.getOverlays().add(routeOverlay);
	    mMapView.getController().animateTo(wcService.getGeoPointobj());
	    myLocationOverlay= new MyLocationOverlay(mMapView);
	    mMapView.getOverlays().add(myLocationOverlay);
	    BDLocation location =wcService.getBdLocation();
	    LocationData locData =new LocationData();
		locData.latitude  = location.getLatitude();
        locData.longitude = location.getLongitude();
        locData.accuracy  = location.getRadius();
        locData.direction = location.getDerect();
	    myLocationOverlay.setData(locData);
	    mMapView.setVisibility(View.VISIBLE);
	    mMapView.refresh();
	   
	   
	}
	 
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		WcService wcService = WcService.getInstance();
		LocationData locData =new LocationData();
		BDLocation location =wcService.getBdLocation();
		locData.latitude  = location.getLatitude();
	    locData.longitude = location.getLongitude();
	    locData.accuracy  = location.getRadius();
	    locData.direction = location.getDerect();
		myLocationOverlay.setData(locData);
		wcService.setLoacationCallBack(this);
		
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode ==KeyEvent.KEYCODE_BACK||keyCode==KeyEvent.KEYCODE_HOME){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 WcService wcService = WcService.getInstance();
		 wcService.setLoacationCallBack(null);
		 
	}


	@Override
	public void callBackPosition(BDLocation location) {
		// TODO Auto-generated method stub 
	 LocationData locData =new LocationData();
	 locData.latitude  = location.getLatitude();
     locData.longitude = location.getLongitude();
     locData.accuracy  = location.getRadius();
     locData.direction = location.getDerect();
     myLocationOverlay.setData(locData);
     mMapView.refresh();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 mMapView.destroy();
		super.onDestroy();
	}
	
	
	
}
