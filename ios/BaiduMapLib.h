#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

#define OnGetCurrentLocation @"onGetCurrentLocation"
#define OnError @"onError"
#define OnCheckPermissionState @"onCheckPermissionState"
@interface BaiduMapLib : RCTEventEmitter  : RCTEventEmitter <RCTBridgeModule>

@end
