package com.yin.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initEvent()
  }

  private fun initEvent() {
    btn_wx.setOnClickListener {
      WXActivity.startActivity(this)
    }
    btn_qq.setOnClickListener {
      QQActivity.startActivity(this)
    }
    btn_wb.setOnClickListener {
      WBActivity.startActivity(this)
    }
  }


}
