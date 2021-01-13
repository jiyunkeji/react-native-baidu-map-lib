package com.reactnativebaidumaplib;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

public class BaiduMapLibModule extends ReactContextBaseJavaModule {

  public static final String Key_Pref = "BaiduMapLib";
  public static final String Value_Pref = "com.reactnativebaidumaplib";

  public LocationClient mLocationClient = null;
  private MyLocationListener myListener = new MyLocationListener(this.getReactApplicationContext(),new LocatonCallBack() {
    @Override
    public void stopLocation() {

      if(mLocationClient!=null){
        mLocationClient.stop();
      }
    }
  });

  public BaiduMapLibModule(ReactApplicationContext context) {
    super(context);
  }

  @Override
  public String getName() {
    return "BaiduMapLib";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(Key_Pref, Value_Pref);
    return constants;
  }

  public boolean canOverrideExistingModule() {
    return true;
  }

  @ReactMethod
  public void initSDK(String ak, Promise promise) {
    try {
      promise.resolve(true);
    } catch (Exception exception) {
      promise.reject(exception);
    }
  }

  @ReactMethod
  public void getCurrentPosition(Promise promise) {
    try {

      mLocationClient = null;
      mLocationClient = new LocationClient(getReactApplicationContext());
      //声明LocationClient类
      mLocationClient.registerLocationListener(myListener);
      LocationClientOption option = new LocationClientOption();

      option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

      option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

//      option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

      option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

      option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

      option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

      option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

      option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

      option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

      option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
      option.setIsNeedAddress(true);
      //可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

      mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
      mLocationClient.start();
      promise.resolve(true);
    } catch (Exception exception) {
      promise.reject(exception);
    }
  }
}

 class MyLocationListener extends BDAbstractLocationListener{
  private  LocatonCallBack callBack;
  private ReactApplicationContext context;
  public  MyLocationListener(ReactApplicationContext context,LocatonCallBack callBack){
    this.callBack = callBack;
    this.context = context;
  }
  @Override
  public void onReceiveLocation(BDLocation location){

    WritableMap map = Arguments.createMap();
    map.putString("country", location.getCountry());
    map.putString("countryCode", location.getCountryCode());
    map.putString("province", location.getProvince());
    map.putString("city", location.getCity());
    map.putString("district", location.getDistrict());
    map.putString("town", location.getTown());
    map.putString("street", location.getStreet());
    map.putString("streetNumber", location.getStreetNumber());
    map.putString("cityCode", location.getCityCode());
    map.putString("adCode", location.getAdCode());
    map.putString("locationDescribe", location.getLocationDescribe());
    map.putDouble("latitude", location.getLatitude());
    map.putDouble("longitude", location.getLongitude());

    EventEmitManager.emit(this.context,EventEmitManager.Event_On_Get_CurrentLocation,map);
    if(this.callBack!=null){
      this.callBack.stopLocation();
    }
  }
}

interface LocatonCallBack{
  public  void stopLocation();
}
