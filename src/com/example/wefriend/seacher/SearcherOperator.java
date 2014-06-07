package com.example.wefriend.seacher;

import android.content.Context;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.example.wcsmsc.SmsAplication;

/******
 * 搜索类
 * @author asus
 *
 */
public class SearcherOperator {
	
	private MKSearch mkSearch; //用于位置检索、周边检索、范围检索、公交检索、驾乘检索、步行检索
	private OnGetTransitRouteResult ogtrresult;
	
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
	            	if(ogtrresult!=null)ogtrresult.onGetTransitRouteResult(res, error);
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
	
	public interface OnGetTransitRouteResult  {
		public void onGetTransitRouteResult(MKTransitRouteResult res,int error);
	}
	
}
