package com.android.wefriend.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class IntrestePoint implements Parcelable{
	private String    address;
	private GeoPoint  geoPoint;//�³��ĵص�
	private GeoPoint  objPoint;//��Ŀ�ĵصĵص�
	private String    choose;
	private String    title; //���������
	private int       num;
	private long      distance;
	
	private String latitude;
	private String lontitude;
	private String stopName;
	private String lineName;
	
	
	
	
	public static final Parcelable.Creator<IntrestePoint> CREATOR = new Creator<IntrestePoint>() { 
		         @Override 
		         public IntrestePoint createFromParcel(Parcel source) {  
		              
		             return new IntrestePoint(source); 
		         }
		         @Override
		         public IntrestePoint[] newArray(int size) {
		             return new IntrestePoint[size];
		         } 
 
		         
		         
	};
     
	public IntrestePoint (){
		
	}
	
    IntrestePoint(Parcel source){
		 address = source.readString();
         title   = source.readString();
         GeoPoint g = new GeoPoint(source.readInt(), source.readInt());
         geoPoint = g;
         //Log.v("WC",test.address+"KKK:"+test.geoPoint.getLatitudeE6()+":"+test.geoPoint.getLongitudeE6()+test.title);
	}
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
		
		return "乘坐"+title+"经过"+num+"站"+"到"+address+"下车";
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
		arg0.writeString(address);
		arg0.writeString(title);
		arg0.writeInt(geoPoint.getLatitudeE6());
		arg0.writeInt(geoPoint.getLongitudeE6());
		Log.v("WC","KKK:"+geoPoint.getLatitudeE6()+":"+geoPoint.getLongitudeE6());
	}
	
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLontitude() {
		return lontitude;
	}
	public void setLontitude(String lontitude) {
		this.lontitude = lontitude;
	}
	
	
}
