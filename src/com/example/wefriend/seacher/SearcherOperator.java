package com.example.wefriend.seacher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.android.wefriend.bean.IntrestePoint;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRouteAddrResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.wcsmsc.SmsAplication;

/******
 * 搜索类
 * @author asus
 *
 */
public class SearcherOperator {
	
	private MKSearch mkSearch; //用于位置检索、周边检索、范围检索、公交检索、驾乘检索、步行检索
	private OnGetTransitRouteResult ogtrresult;
	private BDLocation   bdLocation;
	
	public SearcherOperator(Context context){
		mkSearch = new MKSearch();
		SmsAplication app =(SmsAplication) context.getApplicationContext();
		mkSearch.init(app.mBMapManager, new MKSearchListener(){
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
	            	if(ogtrresult!=null){
	    				int n = res.getNumPlan();
	    				ArrayList<IntrestePoint> intrestePoints = new ArrayList<IntrestePoint>();
	    				for(int i = 0 ; i<n;i++){
	    					MKTransitRoutePlan mktr = res.getPlan(i);
	    					
	    					MKLine mkLine = mktr.getLine(0);
	    					
	    					IntrestePoint intrestePoint =new IntrestePoint();
	    					intrestePoint.setGeoPoint(mktr.getEnd());
	    					intrestePoint.setObjPoint(res.getEnd().pt);
	    					intrestePoint.setTitle(mkLine.getTitle());
	    					intrestePoint.setAddress(mkLine.getGetOffStop().name);
	    					intrestePoint.setNum(mkLine.getNumViaStops());
	    					intrestePoint.setDistance(mkLine.getDistance());
	    					intrestePoints.add(intrestePoint);
	    				}
	            		ogtrresult.onGetTransitRouteResult(intrestePoints, error);
	            	}
	            }
	            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
	                    int error) {
	            }
				@Override
				public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onGetShareUrlResult(MKShareUrlResult arg0,
						int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onGetSuggestionResult(MKSuggestionResult arg0,
						int arg1) {
					// TODO Auto-generated method stub
					
				}
		
	});}
	
	/************
	 * 获取目的地信息
	 * @param startName
	 * @param endName
	 * @param ogtrr
	 */
	public void getBusStationInfo(String startName ,String endName,OnGetTransitRouteResult ogtrr){
		MKPlanNode start = new MKPlanNode();
		start.name = startName;
		MKPlanNode end = new MKPlanNode();
		end.name = endName;
		getBuStationInfo(start, end,ogtrr);
	}
	
	
	public void getBuStationInfo(MKPlanNode start, MKPlanNode end,OnGetTransitRouteResult ogtrr){
		this.ogtrresult = ogtrr;
		mkSearch.transitSearch("上海", start,end);
	}
	
	public void getBuStationInfo(String  endName,OnGetTransitRouteResult ogtrr){
		MKPlanNode mkPlanNode = new MKPlanNode();
		GeoPoint geoPoint = new GeoPoint((int)(bdLocation.getLatitude()*1e6), (int)(bdLocation.getLongitude()*1e6));
		mkPlanNode.pt = geoPoint;
		 
		MKPlanNode end = new MKPlanNode();
		end.name = endName;
		getBuStationInfo(mkPlanNode, end, ogtrr);
	}
	
	public void setLocationBDLocation(BDLocation bdLocation){
		this.bdLocation = bdLocation;
		
	}
	public interface OnGetTransitRouteResult  {
		public void onGetTransitRouteResult(List<IntrestePoint> intrestePoints,int error);
	}
	
	
	
}
