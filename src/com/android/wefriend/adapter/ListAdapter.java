package com.android.wefriend.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.wefriend.activity.InfoMationActivity;
import com.android.wefriend.activity.WcService;
import com.android.wefriend.bean.IntrestePoint;
import com.example.wcsmsc.R;

public class ListAdapter extends BaseAdapter implements OnClickListener{
	
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
			holder.start  = (Button)arg1.findViewById(R.id.item_start);
			holder.start.setTag(data.get(arg0));
			holder.start.setOnClickListener(this);
			holder.cancel  = (Button)arg1.findViewById(R.id.item_cancel);
			holder.cancel.setOnClickListener(this);
			holder.cancel.setTag(data.get(arg0));
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder)arg1.getTag();
		}
		
		holder.name.setText(data.get(arg0).getInfoContent());
		return arg1;
	}

	private class ViewHolder{
		private TextView name;
		private Button start;
		private Button cancel;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.item_cancel){
			
		}else if(v.getId()==R.id.item_start){
			Intent intent = new Intent(activity, WcService.class);
			IntrestePoint intrestePoint =(IntrestePoint)v.getTag();
			Bundle mBundle = new Bundle();  
            mBundle.putParcelable("BEAN", intrestePoint);  
			intent.putExtra("INTRES", mBundle);
			activity.startService(intent);
		}
	}
}
