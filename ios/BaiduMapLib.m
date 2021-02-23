#import "BaiduMapLib.h"
#import <BMKLocationkit/BMKLocationComponent.h>
@interface BaiduMapLib()<BMKLocationAuthDelegate, BMKLocationManagerDelegate>
@property(strong, nonatomic)BMKLocationManager *locationManager;
@end

@implementation BaiduMapLib

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[OnGetCurrentLocation,OnError,OnCheckPermissionState];
}

RCT_EXPORT_METHOD(initSDK:(NSString *)ak
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    [[BMKLocationAuth sharedInstance] checkPermisionWithKey:ak authDelegate:self];
    resolve([NSNumber numberWithBool:YES]);
}

RCT_EXPORT_METHOD(getCurrentPosition:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    _locationManager = nil;
    //初始化实例
    _locationManager = [[BMKLocationManager alloc] init];
    //设置delegate
    _locationManager.delegate = self;
    //设置返回位置的坐标系类型
    _locationManager.coordinateType = BMKLocationCoordinateTypeBMK09LL;
    //设置距离过滤参数
    _locationManager.distanceFilter = kCLDistanceFilterNone;
    //设置预期精度参数
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    //设置应用位置类型
    _locationManager.activityType = CLActivityTypeAutomotiveNavigation;
    //设置是否自动停止位置更新
    _locationManager.pausesLocationUpdatesAutomatically = NO;
    //设置是否允许后台定位
    //_locationManager.allowsBackgroundLocationUpdates = YES;
    //设置位置获取超时时间
    _locationManager.locationTimeout = 10;
    //设置获取地址信息超时时间
    _locationManager.reGeocodeTimeout = 10;
    
    [_locationManager requestLocationWithReGeocode:YES withNetworkState:YES completionBlock:^(BMKLocation * _Nullable location, BMKLocationNetworkState state, NSError * _Nullable error) {
        if (error)
        {
            NSMutableDictionary *body = [NSMutableDictionary new];
            body[@"code"] = [NSNumber numberWithLong:error.code];
            body[@"description"] = error.localizedDescription;
            [self sendEventWithName:OnError body:body];
            return;
        }
        if (location&&location.rgcData) {//得到定位信息，添加annotation
            
            NSMutableDictionary *body = [NSMutableDictionary new];
            body[@"country"] = [location.rgcData country];
            body[@"countryCode"] = [location.rgcData countryCode];
            body[@"province"] = [location.rgcData province];
            body[@"city"] = [location.rgcData city];
            body[@"district"] = [location.rgcData district];
            body[@"town"] = [location.rgcData town];
            body[@"street"] = [location.rgcData street];
            body[@"streetNumber"] = [location.rgcData streetNumber];
            body[@"cityCode"] = [location.rgcData cityCode];
            body[@"adCode"] = [location.rgcData adCode];
            body[@"locationDescribe"] = [location.rgcData locationDescribe];
            body[@"latitude"] = [NSNumber numberWithDouble:[[location location] coordinate].latitude];
            body[@"longitude"] = [NSNumber numberWithDouble:[[location location] coordinate].longitude];
            [self sendEventWithName:OnGetCurrentLocation body:body];
            
            
        }else{
            NSMutableDictionary *body = [NSMutableDictionary new];
            body[@"code"] = [NSNumber numberWithInt:-1];
            body[@"description"] = @"未知异常";
            [self sendEventWithName:OnError body:body];
            
        }
    }];
    resolve([NSNumber numberWithBool:YES]);
}
#pragma  BMKLocationAuthDelegate
-(void)onCheckPermissionState:(BMKLocationAuthErrorCode)iError{
    
    [self sendEventWithName:OnCheckPermissionState body:[NSNumber numberWithInt:iError]];
}
#pragma  BMKLocationManagerDelegate
-(void)BMKLocationManager:(BMKLocationManager *)manager didUpdateLocation:(BMKLocation *)location orError:(NSError *)error{
    if (error)
    {
        NSLog(@"BMKLocationManager locError:{%ld - %@};", (long)error.code, error.localizedDescription);
    }
    if (location) {//得到定位信息，添加annotation

        if (location.rgcData) {
            NSLog(@"rgc = %@:l%f,longitude:%f",[location.rgcData city],[location location].coordinate.latitude,[location location].coordinate.longitude);
        }
        
    }
}
@end
