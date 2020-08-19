import 'package:flutter/cupertino.dart';
import 'package:flutter_boost/flutter_boost.dart';

class FlutterPage1 extends StatefulWidget {
  final Map<String, dynamic> params;

  FlutterPage1(this.params);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<FlutterPage1> {
  var info = "";

  @override
  void initState() {
    super.initState();
    info = "从原生跳转到Flutter时携带的数据：\n${widget.params}";
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: CupertinoColors.white,
      padding: EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          Center(child: Text("Flutter Page1")),
          button(
            "close,并返回数据给原生",
            () => close(),
          ),
          button(
            "open Native,并等待返回数据",
            () => openNativeForResult(),
          ),
          button(
            "open Flutter,并等待返回数据",
            () => openFlutterForResult(),
          ),
          SizedBox(height: 16),
          Text(info),
        ],
      ),
    );
  }

  Widget button(String text, VoidCallback callback) {
    return Container(
      margin: EdgeInsets.only(top: 16),
      child: CupertinoButton(
        color: CupertinoColors.activeBlue,
        child: Text(
          text,
          style: TextStyle(fontSize: 14, color: CupertinoColors.white),
        ),
        onPressed: () => callback.call(),
      ),
    );
  }

  ///
  /// close,并返回数据给原生
  ///
  close() {
    FlutterBoost.singleton.closeCurrent(result: {
      'data': 'data form flutter page1',
    });
  }

  ///
  /// 打开原生页面，并等待返回结果
  ///
  openNativeForResult() {
    FlutterBoost.singleton.open("native://native_page_1",
        urlParams: {"data": "data from flutter page1"}).then((map) {
      info = info + "\n\n原生返回给Flutter的数据：\n$map";
      setState(() {});
    });
  }

  ///
  /// 打开Flutter页面，并等待返回结果
  ///
  openFlutterForResult() {
    FlutterBoost.singleton.open("flutter_page_2",
        urlParams: {"data": "data from flutter page1"}).then((map) {
      info = info + "\n\nflutter_page_2返回给flutter_page_1的数据：\n$map";
      setState(() {});
    });
  }
}
