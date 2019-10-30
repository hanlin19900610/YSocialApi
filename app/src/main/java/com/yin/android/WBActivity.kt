package com.yin.android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aimymusic.aimysociallib.URL
import com.yin.sociallibrary.Social
import com.yin.sociallibrary.config.PlatformType
import com.yin.sociallibrary.utils.CallbackDataUtil
import kotlinx.android.synthetic.main.activity_wb.*

class WBActivity : AppCompatActivity() {

  companion object {
    fun startActivity(context:Context){
      val intent = Intent(context, WBActivity::class.java)
      intent.putExtra("className", context::class.java.simpleName)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_wb)
    initEvent()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    Social.onActivityResult(requestCode, resultCode, data)
  }


  private fun initEvent() {
    btn_wb_share_img.setOnClickListener {
     Social.share(this, PlatformType.SINA_WEIBO,
        img = createBitmap(),                  // 除了分享文本外，其他分享img都必须传
        text = "测试",
        onSuccess = { type ->
//          toast("$type 分享成功")
        },
        onError = { type, code, msg ->
//          toast("$type:$code == $msg")
        },
        onCancel = { _ -> })
    }
    btn_wb_share_text.setOnClickListener {
     Social.share(this, PlatformType.SINA_WEIBO,
        img = createBitmap(),                  // 除了分享文本外，其他分享img都必须传
        text = "测试",
        onSuccess = { type ->
//          toast("$type 分享成功")
        },
        onError = { type, code, msg ->
//          toast("$type:$code == $msg")
        },
        onCancel = { _ -> })
    }
    btn_wb_share_web.setOnClickListener {
      Social.share(this, PlatformType.SINA_WEIBO,
        img = createBitmap(),                  // 除了分享文本外，其他分享img都必须传
        webUrl = URL.WEB_URL,
        description = "测试",
        title = "测试",
              onSuccess = { type ->
                  Log.e("TAG", "$type 分享成功")
              },
              onError = { type, code, msg ->
                  Log.e("TAG", "$type:$code == $msg")
              },
              onCancel = { _ ->
                  Log.e("TAG", "取消分享")
              })
    }
    btn_wb_share_video.setOnClickListener {
     Social.share(this, PlatformType.SINA_WEIBO,
        img = createBitmap(),                  // 除了分享文本外，其他分享img都必须传
        videoUrl = "file:///android_asset/test.mp4",
        description = "测试",
        title = "测试",
        onSuccess = { type ->
//          toast("$type 分享成功")
        },
        onError = { type, code, msg ->
//          toast("$type:$code == $msg")
        },
        onCancel = { _ -> })
    }
    btn_wb_auth.setOnClickListener {
      exampleForAuth()
    }
  }

  private fun exampleForAuth(){
    if(Social.available4Plat(PlatformType.SINA_WEIBO)){
      Social.auth(this, PlatformType.SINA_WEIBO, onSuccess = {
        type, map ->
//        toast("登录成功")
        map?.let {
          val data = CallbackDataUtil.getID(it, "")
        }
      })
    }else{
//      toast("客户端未安装")
    }
  }

  private fun createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
      color = Color.GREEN
      style = Paint.Style.FILL
      textSize = 100F
    }
    canvas.drawText("qq分享测试", 100f, 200f, paint)
    return bitmap
  }
}
