package com.yfbx.demo.flutter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.Platform
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.idlefish.flutterboost.interfaces.INativeRouter
import com.yfbx.helper.launchFor
import io.flutter.embedding.android.FlutterView

/**
 * Author: Edward
 * Date: 2020-08-19
 * Description:
 */
object Flutter {


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

        if (url.startsWith("native://")) {
            openNative(context, url, urlParams, requestCode)
        } else {
            openFlutter(context, url, urlParams, requestCode)
        }
    }

    private fun openFlutter(context: Context, url: String, params: Map<String, Any>, requestCode: Int) {
        val intent = FaradayActivity.buildIntent(context, url, params, BoostFlutterActivity.BackgroundMode.opaque.name)
        if (context is Activity) {
            context.startActivityForResult(intent, requestCode)
        } else {
            context.startActivity(intent)
        }
    }

    private fun openNative(context: Context, url: String, params: Map<String, Any>, requestCode: Int) {
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
    }
}


fun FragmentActivity.openFlutter(url: String, params: Map<String, Any>? = null, listener: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
    val intent = FaradayActivity.buildIntent(this, url, params, BoostFlutterActivity.BackgroundMode.opaque.name)
    launchFor(intent) { result ->
        listener?.invoke(result.resultCode, result.data)
    }
}