export interface Loaction {
  country?: string;
  countryCode?: string;
  province?: string;
  city?: string;
  district?: string;
  town?: string;
  street?: string;
  streetNumber?: string;
  cityCode?: string;
  adCode?: string;
  locationDescribe?: string;
  latitude?: number;
  longitude?: number;
}
export interface BaiDuMapError {
  code?: number;
  description?: string;
}
export enum LocationAuthErrorCode {
  BMKLocationAuthErrorUnknown = -1, ///< 未知错误
  BMKLocationAuthErrorSuccess = 0, ///< 鉴权成功
  BMKLocationAuthErrorNetworkFailed = 1, ///< 因网络鉴权失败
  BMKLocationAuthErrorFailed = 2, ///< KEY非法鉴权失败
}
export type Listener = (...args: any[]) => any;
