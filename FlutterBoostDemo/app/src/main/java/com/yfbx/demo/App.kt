package com.yfbx.demo

import android.app.Application
import com.yfbx.demo.flutter.Flutter


/**
 * Author: Edward
 * Date: 2020-08-18
 * Description:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Flutter.init(this)
    }

}