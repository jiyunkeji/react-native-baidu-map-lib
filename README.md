# react-native-baidu-map-lib

baidu map

## Installation

```sh
npm install react-native-baidu-map-lib
```

## Usage

```js
import BaiduMapLib from "react-native-baidu-map-lib";

// ...
使用TS开发
目前只支持定位
定位SDK版本号 V2.0.0
初始化
    if (!Utils.isAndroid()) {
        let isSuccess = await BaiduMapLibManager.instance().initSDK(
          Config.baiduIosAK,
        );
        if (isSuccess) {
          console.log('isSuccess');
        }
      } else {
        let isSuccess = await RequestAndroidPermission.requestLocationPermission();
        if (isSuccess) {
          console.log('isSuccess');
        }
      }


    BaiduMapLibManager.instance().addListener(
      'onGetCurrentLocation',
      async (location: Loaction) => {
        this.loadSchoolList(location.latitude, location.longitude);
        await Storage.setCity(location.city);
        await Storage.setLatitude(location.latitude);
        await Storage.setLongitude(location.longitude);
      },
    );

    componentWillUnmount() {
    BaiduMapLibManager.instance().destroy();
    }

    
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
