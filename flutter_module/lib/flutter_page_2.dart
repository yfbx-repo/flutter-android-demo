import 'package:flutter/cupertino.dart';
import 'package:flutter_boost/flutter_boost.dart';

class FlutterPage2 extends StatelessWidget {
  final Map<String, dynamic> params;

  FlutterPage2(this.params);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: CupertinoColors.white,
      padding: EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          Center(child: Text("Flutter Page 2")),
          button(
            "close and set result",
            () => close(),
          ),
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

  close() {
    FlutterBoost.singleton.closeCurrent(result: {
      'result': 'data from flutter page2',
    });
  }
}
