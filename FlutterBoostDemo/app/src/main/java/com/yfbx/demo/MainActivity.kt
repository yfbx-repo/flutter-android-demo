package com.yfbx.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idlefish.flutterboost.interfaces.IFlutterViewContainer
import com.yfbx.demo.flutter.openFlutter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //跳转Flutter，携带参数，并获取返回数据
        btn.setOnClickListener {
            openFlutter("flutter_page_1", mapOf("data" to "data form native")) { _, data ->
                //取返回值
                val map = data?.getSerializableExtra(IFlutterViewContainer.RESULT_KEY) as? Map<*, *>
                infoTxt.append("\n Flutter 返回的数据：${map}")
            }
        }
    }
}