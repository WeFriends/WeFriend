package com.android.wefriend.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.wefriend.activity.InfoMationActivity;
import com.android.wefriend.bean.IntrestePoint;
import com.example.wcsmsc.R;

public class ListAdapter extends BaseAdapter{
	
	private InfoMationActivity activity;
	private ArrayList<IntrestePoint> data = new ArrayList<IntrestePoint>();
	private LayoutInflater inflater;
	private ViewHolder holder;
	
	public ListAdapter(InfoMationActivity activity,
			ArrayList<IntrestePoint> data) {
		super();
		this.activity = activity;
		this.data = data;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.adapter_item, null);
			holder.name  = (TextView)arg1.findViewById(R.id.item_name);
			holder.start  = (TextView)arg1.findViewById(R.id.item_start);
			holder.cancel  = (TextView)arg1.findViewById(R.id.item_cancel);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder)arg1.getTag();
		}
		
		holder.name.setText(data.get(arg0).getAddress());
		return arg1;
	}

	private class ViewHolder{
		private TextView name;
		private TextView start;
		private TextView cancel;
	}
}
