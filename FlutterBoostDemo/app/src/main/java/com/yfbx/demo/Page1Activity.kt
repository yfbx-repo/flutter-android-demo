package com.yfbx.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.idlefish.flutterboost.interfaces.IFlutterViewContainer
import kotlinx.android.synthetic.main.activity_page1.*

/**
 * Author: Edward
 * Date: 2020-08-19
 * Description:
 */
class Page1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        //flutter 跳转原生时携带的参数
        val params = intent.getSerializableExtra("params") as? BoostFlutterActivity.SerializableMap
        params?.let { infoTxt.append("flutter 跳转原生时携带的参数: ${it.map}") }

        //返回Flutter,并携带返回数据
        btn.setOnClickListener {
            val result = hashMapOf<String, Any>("data" to "dara form native page1")
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(IFlutterViewContainer.RESULT_KEY, result)
            })
            finish()
        }
    }
}