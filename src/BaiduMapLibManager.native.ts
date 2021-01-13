import { NativeEventEmitter, NativeModules } from 'react-native';
import type { BaiduMapSdkEvents } from './BaiduMapSdkEvents';
import type { Listener } from './Types';

const { BaiduMapLib } = NativeModules;
// const Prefix = WeChatSdkModule.WeChatSdk;

let baiduMapLibManager: BaiduMapLibManager | undefined;
const BaiduMaoLibEvent = new NativeEventEmitter(BaiduMapLib);

export default class BaiduMapLibManager {
  private _listeners = new Map<string, Listener>();

  static instance(): BaiduMapLibManager {
    if (baiduMapLibManager == null) {
      baiduMapLibManager = new BaiduMapLibManager();
    }
    return baiduMapLibManager;
  }
  initSDK(ak: string): Promise<boolean> {
    return BaiduMapLib.initSDK(ak);
  }

  getCurrentPosition(): Promise<boolean> {
    return BaiduMapLib.getCurrentPosition();
  }

  destroy() {
    this.removeAllListener();
  }
  addListener<EventType extends keyof BaiduMapSdkEvents>(
    event: EventType,
    listener: BaiduMapSdkEvents[EventType]
  ) {
    if (!this._listeners.has(event)) {
      this._listeners.set(event, listener);
      BaiduMaoLibEvent.addListener(event, listener);
    }
  }
  removeListener<EventType extends keyof BaiduMapSdkEvents>(
    event: EventType,
    listener: BaiduMapSdkEvents[EventType]
  ) {
    if (this._listeners.has(event)) {
      this._listeners.delete(event);
      BaiduMaoLibEvent.removeListener(event, listener);
    }
  }
  removeAllListener() {
    this._listeners.forEach((value, key) => {
      this._listeners.delete(key);
      BaiduMaoLibEvent.removeListener(key, value);
    });
    this._listeners.clear();
  }
}
