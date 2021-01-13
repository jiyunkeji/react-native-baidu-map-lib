package com.reactnativebaidumaplib;

import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class EventEmitManager {

  public static final String Event_On_Get_CurrentLocation = "onGetCurrentLocation";
  public static final String Event_On_Error = "onError";
  public static final String Event_On_Check_Permission_State = "onCheckPermissionState";


  public  static void emit(ReactApplicationContext context, String name, WritableMap params) {
    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(name,params);
  }
}
