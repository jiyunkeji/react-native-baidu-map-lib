import type { Loaction, LocationAuthErrorCode, BaiDuMapError } from './Types';

export type OnGetCurrentLocation = (loaction: Loaction) => void;
export type OnError = (error: BaiDuMapError) => void;
export type OnCheckPermissionState = (code: LocationAuthErrorCode) => void;

export interface BaiduMapSdkEvents {
  onGetCurrentLocation: OnGetCurrentLocation;
  onError: OnError;
  onCheckPermissionState: OnCheckPermissionState;
}
