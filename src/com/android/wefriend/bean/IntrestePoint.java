package com.android.wefriend.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class IntrestePoint implements Parcelable{
	private String    address;
	private GeoPoint  geoPoint;//下车的地点
	private GeoPoint  objPoint;//到目的地的地点
	private String    choose;
	private String    title; //公交车名称
	private int       num;
	private long      distance;
	
	public static final Parcelable.Creator<IntrestePoint> CREATOR = new Creator<IntrestePoint>() { 
		         @Override 
		         public IntrestePoint createFromParcel(Parcel source) {  
		             String str = source.readString();
		             IntrestePoint test = new IntrestePoint();
		             return test; 
		         }
		         @Override
		         public IntrestePoint[] newArray(int size) {
		             return new IntrestePoint[size];
		         } 
 
		         
		         
	};

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	 
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	public String getChoose() {
		return choose;
	}
	public void setChoose(String choose) {
		this.choose = choose;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public long getDistance() {
		
		return (long) DistanceUtil.getDistance(objPoint,geoPoint);
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public String getInfoContent(){
		
		return "乘坐公交"+title+"经过"+num+"站"+"到"+address+"离目的地的还有"+getDistance();
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public GeoPoint getObjPoint() {
		return objPoint;
	}
	public void setObjPoint(GeoPoint objPoint) {
		this.objPoint = objPoint;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeSerializable(address);
		arg0.writeString(title);
		arg0.writeInt(geoPoint.getLatitudeE6());
		arg0.writeInt(geoPoint.getLongitudeE6());
	}
	
	
	
}
