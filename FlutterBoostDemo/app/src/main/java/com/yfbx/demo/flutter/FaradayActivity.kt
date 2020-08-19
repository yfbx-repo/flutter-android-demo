package com.yfbx.demo.flutter

import android.content.Context
import android.content.Intent
import com.idlefish.flutterboost.containers.BoostFlutterActivity


class FaradayActivity : BoostFlutterActivity() {


    companion object {
        fun buildIntent(context: Context, url: String, params: Map<String, Any>?, backgroundMode: String): Intent {
            val serializableMap = SerializableMap()
            params?.let {
                serializableMap.map = it
            }
            val intent = Intent(context, FaradayActivity::class.java)
            intent.putExtra(EXTRA_BACKGROUND_MODE, backgroundMode)
            intent.putExtra(EXTRA_DESTROY_ENGINE_WITH_ACTIVITY, false)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(EXTRA_PARAMS, serializableMap)
            return intent
        }
    }


}
