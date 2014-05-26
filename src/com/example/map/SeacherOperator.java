package com.example.map;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.example.wcsmsc.R;

public class SeacherOperator implements OnClickListener{
	private MKSearch mkSearch;
	private Button button ;
	private ExpandableListView expandableListView;
	 
	
	
	public SeacherOperator(View layout){
		layout.findViewById(R.id.btn).setOnClickListener(this);
		expandableListView =(ExpandableListView)layout.findViewById(R.id.result);
		
		 
	}
	public void setMkSearch(MKSearch mkSearch) {
		this.mkSearch = mkSearch;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		MKPlanNode m1 = new MKPlanNode();
//		m1.name = "闸北区德必易园";
//		MKPlanNode m2 = new MKPlanNode();
//		m2.name = "闸北区阳景小区";
//		mkSearch.transitSearch("上海", m2, m1);
		Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",Uri.parse("sip:20140030"));
	    v.getContext().startActivity(intent);
	}
	
   /*****
    * 得到获取的交通出行
    * @param arg0
    * @param arg1
    */
	public void TransitRouteResult(MKTransitRouteResult arg0, int arg1){
		if(arg0 == null){
			return ;
		}
		expandableListView.setAdapter(new ContactsInfoAdapter(arg0, expandableListView.getContext()));
	}
	
	
   public void setOccl(OnChildClickListener occl) {
	   expandableListView.setOnChildClickListener(occl);
   }
	
	
	
	
}
