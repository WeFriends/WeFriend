package com.example.map;

 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.wcsmsc.R;
/******
 * 获取查询结果的显示适配器
 * @author asus
 *
 */
public class ContactsInfoAdapter extends BaseExpandableListAdapter{
	
	private MKTransitRouteResult arg;
	private Context context;
	private MKTransitRoutePlan mk;
	 
	
	
	
	public ContactsInfoAdapter(MKTransitRouteResult arg,
			Context context) {
		super();
		this.arg = arg;
		this.context = context;
		
	}

	
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(context);
		int index = childPosition/3;
		MKLine mkLine =mk.getLine(index);
		if(childPosition%2==1&&index==0){
			MKPoiInfo mkPoiInfo2 = mkLine.getGetOffStop();
			textView.setText("当前位置："+"经过"+mkLine.getNumViaStops()+"站至"+mkPoiInfo2.name);
		}else if(childPosition%2==0&&(childPosition/2)!=1){
			MKPoiInfo mkPoiInfo  = mkLine.getGetOnStop();
			MKPoiInfo mkPoiInfo2 = mkLine.getGetOffStop();
			int postion =(int)DistanceUtil.getDistance(mk.getStart(),mkPoiInfo.pt);
			textView.setText("步行"+postion+"米"+mkPoiInfo.name);
		}else if(childPosition ==arg.getPlan(groupPosition).getNumRoute()+arg.getPlan(groupPosition).getNumLines()-1) {
			MKPoiInfo mkPoiInfo2 = mkLine.getGetOffStop();
			int postion =(int)DistanceUtil.getDistance(mk.getEnd(),mkPoiInfo2.pt);
			textView.setText(mkPoiInfo2.name+"步行"+postion+"米"+"终点站");
		}else{
			
		}
		textView.setTag(mk);
		return textView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return arg.getPlan(groupPosition).getNumRoute()+arg.getPlan(groupPosition).getNumLines();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return arg.getPlan(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return arg.getNumPlan();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView ==null){
			convertView =LayoutInflater.from(context).inflate(R.layout.intem_parent,null);
		}
		TextView txt=(TextView)convertView.findViewById(R.id.txt1);
		mk = arg.getPlan(groupPosition);
		txt.setText(""+mk.getContent().replace("_"," → "));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
