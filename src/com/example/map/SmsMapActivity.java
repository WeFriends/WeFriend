package com.example.map;
 

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.wcsmsc.R;
import com.example.wcsmsc.SmsAplication;

public class SmsMapActivity extends Activity implements  OnClickListener,OnTouchListener,MKMapViewListener{
    private MapView mMapView = null;
    private MKSearch mSearch = null;    
    private GeoPoint mPoint ;
    private GeoPoint mchosePoint;
    private PostionObject poj;
    
   // 定位相关
 	LocationClient mLocClient;
 	LocationData locData = null;
 	public MyLocationListenner myListener = new MyLocationListenner();
 	boolean isFirstLoc = true;//是否首次定位
 	boolean isRequest = false;//是否手动触发请求定位
 	private MapController mMapController = null;
 	private PopupOverlay   pop  = null;
 	private TextView       txt;
 	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmsAplication app = (SmsAplication)this.getApplication();
		if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
           // app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
		setContentView(R.layout.activity_share_postion);
		//mPoint = getPosition();
		mMapView = (MapView)findViewById(R.id.bmapView);
		mMapView.setOnTouchListener(this);
		mMapView.regMapViewListener(app.mBMapManager, this);
		
        mMapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				android.view.ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(20,20);
				MapView.LayoutParams lll = new MapView.LayoutParams(ll);
				Point point = mMapView.getCenterPixel();
				lll.x=point.x-10;
				lll.y=point.y-20;
		        ImageView imageView = new ImageView(SmsMapActivity.this);
		        imageView.setImageResource(android.R.color.holo_blue_bright);
		        mMapView.addView(imageView,lll );
			}
		});
        
		LocationManager loManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
		Location location = loManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(location != null){
			mPoint = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
		}
		mMapView.addView(new TextView(this));
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		mMapController.setZoom(19);
		mMapController.setCenter(mPoint);
		 
		pop = new PopupOverlay(mMapView,null);
		txt = new TextView(this);
		poj = new PostionObject();
		
		mLocClient = new LocationClient(this);
	    locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        
         
		
		// 初始化搜索模块，注册搜索事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            /**
             * 在此处理poi搜索结果 , 用poioverlay 显示
             */
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                 
            }
            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                    int error) {
            }
            public void onGetTransitRouteResult(MKTransitRouteResult res,
                    int error) {
            }
            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                    int error) {
            }
            /**
             * 在此处理反地理编结果
             */
            public void onGetAddrResult(MKAddrInfo res, int error) {
            	AddrShareOverlay addrOverlay = new AddrShareOverlay(
                		getResources().getDrawable(R.drawable.icon_marka),mMapView , res);
                 mMapView.getOverlays().clear();
                 mMapView.getOverlays().add(addrOverlay);
                 mMapView.refresh();
            	 mSearch.poiRGCShareURLSearch(res.geoPt, "分享地址", res.strAddr);	
            	 poj.name = res.strAddr;
            	 txt.setText(""+poj.name);
     			 txt.setBackgroundResource(R.drawable.bg_input);
     			 pop.showPopup(txt,mchosePoint, 32);
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }
            
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				poj.url = result.url;
			}
        });
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 Intent intent = new Intent();
		 intent.putExtra("OBJ", poj);
		 this.setResult(RESULT_OK, intent);
		 finish();
	}
	
	
	 
	  
	 private class AddrShareOverlay extends ItemizedOverlay {

		  private MKAddrInfo addrInfo ;
		  public AddrShareOverlay(Drawable defaultMarker, MapView mapView , MKAddrInfo addrInfo) {
			super(defaultMarker, mapView);
			this.addrInfo = addrInfo;
			addItem(new OverlayItem(addrInfo.geoPt,addrInfo.strAddr,addrInfo.strAddr));
		}
		  
	 
		  
	  }
	  
	 /**
	     * 定位SDK监听函数
	     */
	public class MyLocationListenner implements BDLocationListener {
	    	
	        @Override
	        public void onReceiveLocation(BDLocation location) {
	            if (location == null)
	                return ;
	           
	            locData.latitude = location.getLatitude();
	            locData.longitude = location.getLongitude();
	            //如果不显示定位精度圈，将accuracy赋值为0即可
	            locData.accuracy = location.getRadius();
	            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
	            locData.direction = location.getDerect();
	            //更新定位数据
	           // myLocationOverlay.setData(locData);
	            //更新图层数据执行刷新后生效
	            mMapView.refresh();
	            Log.v("WC","KJJJJJJJJJJJJJJJJJJJ:"+locData.latitude+":"+locData.longitude);
	            //是手动触发请求或首次定位时，移动到定位点
	            if (isRequest || isFirstLoc){
	            	//移动地图到定位点
	            	Log.d("LocationOverlay", "receive location, animate to it");
	            	mPoint = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
	            	mMapController.animateTo(mPoint);
	            	if(isFirstLoc){
		            	mchosePoint=mPoint;
		            }
	                isRequest = false;
//	                myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
//					requestLocButton.setText("跟随");
//	                mCurBtnType = E_BUTTON_TYPE.FOLLOW;
	            }
	            //首次定位完成
	            isFirstLoc = false;
	            
	        }
	        
	        public void onReceivePoi(BDLocation poiLocation) {
	            if (poiLocation == null){
	                return ;
	            }
	        }
	    }

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
			//.
			break;
		case MotionEvent.ACTION_MOVE:
			pop.hidePop();
			break;
		default:
			break;
		}
		//mchosePoint = mMapView.getMapCenter();
		return false;
	}
	
	public static class PostionObject implements Serializable{
		public String url="";
		public String name="";
		
		
	}


	@Override
	public void onClickMapPoi(MapPoi arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetCurrentMap(Bitmap arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMapAnimationFinish() {
		// TODO Auto-generated method stub
		mchosePoint = mMapView.getMapCenter();
		mSearch.reverseGeocode(mchosePoint);
		
	}
	@Override
	public void onMapLoadFinish() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMapMoveFinish() {
		// TODO Auto-generated method stub
		mchosePoint = mMapView.getMapCenter();
		mSearch.reverseGeocode(mchosePoint);
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
		super.onDestroy();
		 
		
	}
    
	
	
	
}
