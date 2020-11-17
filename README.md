# Android 与 Flutter 混合开发

### Android 与 Flutter 混合开发
1. 新建Android项目   
2. 新建Flutter Module(可以在Android项目外任意目录)   
3. Android项目关联Flutter Module,这一步有两中选择    

- Module依赖    

在Android项目`settings.gradle`中添加:
```
setBinding(new Binding([gradle: this]))
evaluate(new File('../flutter_module/.android/include_flutter.groovy'))
```
如果Flutter Module与Android项目不在同一目录下，可以写Flutter Module的绝对地址。    
编译之后会在Android项目中生成`flutter` Module,与正常Android Module类似。
在Android项目App目录下的build.gradle中添加依赖：
```
dependencies {
    ...
    implementation project(':flutter')
}
```
- Maven库依赖    
使用flutter命令，将Flutter Module生成aar,采用maven库的方式依赖，在Flutter Module执行：
```
> flutter build aar --build-number 1.0.0

//由于默认会生成所有版本的aar(debug/release/profile),执行时间比较长，可以添加命令参数，只生成一个版本，如只生成debug版本：
> flutter build aar --no-release --no-profile --build-number 1.0.0
```
执行完成后，命令执行结果中有maven库配置提示。    

框架搭建完成，两边通过`MethodChannel`进行通信，可以实现相互跳转、数据传递、方法桥接等操作。


    
### 混合开发框架 

#### 一、 [g_faraday](https://github.com/gfaraday/g_faraday)
    
  [g_faraday 说明文档](https://github.com/gfaraday/g_faraday)
      


#### 二、[闲鱼 Flutter Boost](https://github.com/alibaba/flutter_boost)    

目前好像不维护了    

- 集成步骤    

1. 在FlutterModule中添加FlutterBoost依赖：    
```
  flutter_boost:
    git:
      url: 'https://github.com/alibaba/flutter_boost.git'
      ref: 'v1.17.1-hotfixes'
```
`pub get`之后在Android项目中会生成`flutter_boost` Module    

2. 在Android项目中依赖`flutter_boost`:    
```
dependencies {
    ...
    implementation project(':flutter')
    implementation project(':flutter_boost')
}
```
3. 在Android项目中初始化FlutterBoost
```
//在Application中初始化
fun init(app: Application) {
    val platform: Platform = FlutterBoost.ConfigBuilder(app, router)
            .isDebug(true)
            .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
            .renderMode(FlutterView.RenderMode.texture)
            .build()
    FlutterBoost.instance().init(platform)
    }


private val router = INativeRouter { context, url, urlParams, requestCode, exts ->
    val params = urlParams ?: mutableMapOf()
    exts?.let { params.putAll(it) }

    //原生Scheme协议可以自定义
    if (url.startsWith("native://")) {
        //跳转原生页面
        //原生页面用Scheme的方式跳转，需要在AndroidManifest中为目标页面注册Scheme
        val intent = Intent(Intent.ACTION_VIEW)
        val serializableMap = BoostFlutterActivity.SerializableMap()
        serializableMap.map = params
        intent.putExtra("params", serializableMap)
        intent.data = Uri.parse(url)
        if (context is Activity) {
            context.startActivityForResult(intent, requestCode)
        } else {
            context.startActivity(intent)
        }
    } else {
        //跳转Flutter页面
        //BoostFlutterActivity 需要在Manifest中注册，也可以继承该Activity进行部分自定义操作
        val intent = BoostFlutterActivity.withNewEngine().url(url).params(urlParams).backgroundMode(BoostFlutterActivity.BackgroundMode.opaque).build(context);
        if (context is Activity) {
           context.startActivityForResult(intent,requestCode);
        } else {
          context.startActivity(intent);
        }
    }
}
```
4. 在FlutterModule中初始化FlutterBoost
```
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    //注册路由
    FlutterBoost.singleton.registerPageBuilders({
      'flutter_page_1': (pageName, params, _) => FlutterPage1(params),
      'flutter_page_2': (pageName, params, _) => FlutterPage2(params),
    });
  }
}  
```
框架搭建完成。

- Android 与 Flutter 交互

1. Android跳转Flutter,携带参数，并等待返回数据
```
//Android,startActivityForResult
openFlutter("flutter_page_1", mapOf("data" to "data form native")) { _, data ->
    //取返回值
    val map = data?.getSerializableExtra(IFlutterViewContainer.RESULT_KEY) as? Map<*, *>
    println("Flutter 返回的数据：${map}")
}

//Flutter,关闭时返回数据
FlutterBoost.singleton.closeCurrent(result: {
    'data': 'data form flutter page1',
});    
```

2. Flutter跳转Android携带参数，并等待返回数据
```
//Flutter
FlutterBoost.singleton.open("native://native_page_1",urlParams: {"data": "data from flutter page1"}).then((map) {
    print("原生返回给Flutter的数据:$map");
});

//Android 返回数据给Flutter
val result = hashMapOf<String, Any>("data" to "dara form native page1")
setResult(Activity.RESULT_OK, Intent().apply {
    putExtra(IFlutterViewContainer.RESULT_KEY, result)
})
finish()

```

3. Flutter跳转Flutter携带参数，并等待返回数据

```
//Fluuter Page 1
 FlutterBoost.singleton.open("flutter_page_2",urlParams: {"data": "data from flutter page1"}).then((map) {
    print("flutter_page_2返回给flutter_page_1的数据：$map");
});

//Fluuter Page 2,关闭时返回数据
FlutterBoost.singleton.closeCurrent(result: {
    'data': 'data form flutter page2',
});    
```



