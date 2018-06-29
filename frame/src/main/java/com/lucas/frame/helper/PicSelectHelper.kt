package com.lucas.frame.helper

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.Toast
import com.lucas.frame.FrameApp
import com.lucas.frame.R
import com.lucas.frame.data.bean.PhotoBean
import com.lucas.frame.popup.PictureSelectPopup
import com.lucas.frame.utils.CommonUtil
import com.yanzhenjie.album.Album
import java.io.File
import java.util.*

/**
 * @file       PicSelectHelper.kt
 * @brief      冲相册获取图片或者获取拍摄图片
 * @author     lucas
 * @date       2018/4/17 0017
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class PicSelectHelper {
    //请求识别码
    val CODE_GALLERY_REQUEST = 0x010
    val CODE_CAMERA_REQUEST = 0x011
    var IMAGE_FILE_NAME: String? = null
    val mData = ArrayList<PhotoBean>()
    var popup: PictureSelectPopup? = null

    fun showSelectPicPopup(activity: Activity, maxPic: Int) {
        if (popup == null)
            popup = PictureSelectPopup(activity, {
                when (it.id) {
                    R.id.bt_select_photo -> {
                        getPicFormAlbum(activity, maxPic)
                        popup?.dismiss()
                    }
                    R.id.bt_select_takephoto -> {
                        choseHeadImageFromCameraCapture(activity)
                        popup?.dismiss()
                    }
                }
            })
        popup?.showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    fun hidePopup() {
        popup?.dismiss()
    }

    //从相册获取图片
     fun getPicFormAlbum(activity: Activity, maxPic: Int = 1) {
        IMAGE_FILE_NAME = "pic_" + System.currentTimeMillis() + ".jpg"
        Album.startAlbum(activity, CODE_GALLERY_REQUEST, maxPic,
                ContextCompat.getColor(activity, R.color.frame_colorPrimary),
                ContextCompat.getColor(activity, R.color.frame_colorPrimaryDark))
    }

    // 启动手机相机拍摄照片作为头像
     fun choseHeadImageFromCameraCapture(activity: Activity) {
        IMAGE_FILE_NAME = "pic_" + System.currentTimeMillis() + ".jpg"
        val intentFromCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)))
        }
        activity.startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST)
    }

    //回调
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent, result: (data: ArrayList<PhotoBean>) -> Unit) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            return
        }
        var data: Uri? = null
        when (requestCode) {
            CODE_GALLERY_REQUEST -> {
                // 拿到用户选择的图片路径List：
                val pathList = Album.parseResult(intent)
                for (path in pathList) {
                    mData.add(PhotoBean(path))
                }
                result(mData)
                mData.clear()
            }
            CODE_CAMERA_REQUEST -> {
                if (hasSdcard()) {
                    val tempFile = File(Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME)
                    data = Uri.fromFile(tempFile)
                } else {
                    Toast.makeText(FrameApp.INSTANCE, "没有SDCard!", Toast.LENGTH_LONG).show()
                }
                val path = CommonUtil.getPathByUri4kitkat(FrameApp.INSTANCE, data)
                mData.add(PhotoBean(path))
                result(mData)
                mData.clear()
            }
        }//                data = intent.getData();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    private fun hasSdcard(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }
}