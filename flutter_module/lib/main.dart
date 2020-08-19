import 'package:flutter/cupertino.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:fluttermodule/flutter_page_1.dart';

import 'flutter_page_2.dart';

void main() {
  runApp(MyApp());

  // FlutterBoost.singleton.addBoostNavigatorObserver(TestObserver());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    FlutterBoost.singleton.registerPageBuilders({
      'flutter_page_1': (pageName, params, _) => FlutterPage1(params),
      'flutter_page_2': (pageName, params, _) => FlutterPage2(params),
    });
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoApp(
      builder: FlutterBoost.init(),
      title: 'Flutter Demo',
      home: FlutterPage1({}),
    );
  }
}

class TestObserver extends NavigatorObserver {
  @override
  void didPop(Route route, Route previousRoute) {
    super.didPop(route, previousRoute);
  }

  @override
  void didPush(Route route, Route previousRoute) {
    super.didPush(route, previousRoute);
  }

  @override
  void didRemove(Route route, Route previousRoute) {
    super.didRemove(route, previousRoute);
  }

  @override
  void didReplace({Route newRoute, Route oldRoute}) {
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
  }

  @override
  void didStartUserGesture(Route route, Route previousRoute) {
    super.didStartUserGesture(route, previousRoute);
  }

  @override
  void didStopUserGesture() {
    super.didStopUserGesture();
  }
}
