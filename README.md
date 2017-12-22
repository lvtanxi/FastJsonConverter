#### 目录
[![](https://jitpack.io/v/lvtanxi/FastJsonConverter.svg)](https://jitpack.io/#lvtanxi/FastJsonConverter)


## 引入

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```
 implementation 'com.github.lvtanxi:FastJsonConverter:lasetversion'
```
## 用法
```
 new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
```
