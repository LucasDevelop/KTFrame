package com.mmy.frame.base.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.lucas.frame.FrameApp
import com.lucas.frame.R
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.base.mvp.IView
import com.lucas.frame.helper.CommentHelper
import com.lucas.frame.utils.UIUtil
import com.squareup.otto.Bus
import java.io.Serializable


/**
 * @创建者     lucas
 * @创建时间   2017/12/25 0025 13:42
 * @描述          所有activity应该集成该类
 */
abstract class BaseActivity<P : IPresenter<*>> : AppCompatActivity(), IView, CommentHelper {

    lateinit var mPresenter: P
    var mBus: Bus = FrameApp.INSTANCE.bus
    var mFrameApp: FrameApp? = null
    var mToolBar: Toolbar? = null
    var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnim()
        mPresenter = getPresenter()
        if (registerBus())
            mBus.register(this)
        onCreateInit()
        val layoutID = getLayoutID()
        if (layoutID is Int && layoutID != 0)
            setContentView(layoutID)
        if (layoutID is View)
            setContentView(layoutID)
        mFrameApp = (application as FrameApp)
        initComment()
        initView()
        initData()
        initEvent()
    }

    private fun initAnim() {
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransitionMode()) {
                BaseActivity.TransitionMode.LEFT -> overridePendingTransition(R.anim.mow_left_in, R.anim.mow_left_out)
                BaseActivity.TransitionMode.RIGHT -> overridePendingTransition(R.anim.mow_right_in, R.anim.mow_right_out)
                BaseActivity.TransitionMode.FINISH -> overridePendingTransition(R.anim.mow_right_in, R.anim.mow_right_finish)
                BaseActivity.TransitionMode.TOP -> overridePendingTransition(R.anim.mow_top_in, R.anim.mow_top_out)
                BaseActivity.TransitionMode.BOTTOM -> overridePendingTransition(R.anim.mow_bottom_in, R.anim.mow_bottom_out)
                BaseActivity.TransitionMode.SCALE -> overridePendingTransition(R.anim.mow_scale_in, R.anim.mow_scale_out)
                BaseActivity.TransitionMode.FADE -> overridePendingTransition(R.anim.mow_fade_in, R.anim.mow_fade_out)
            }
        }
    }

    open fun onCreateInit() {}

    private fun initComment() {
        //查找控件
        mToolBar = findViewById(R.id.toolbar)
//        loadingDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//        loadingDialog?.titleText = "加载中.."
    }

    abstract fun initView()
    abstract fun initData()
    open fun initEvent() {}
    //布局id
    abstract fun getLayoutID(): Any
    //注册事件
    open fun registerBus() = false
    abstract fun getPresenter():P
    //Activity是否添加动画
    open fun toggleOverridePendingTransition() = true
    //Activity动画类型
    open fun getOverridePendingTransitionMode() =TransitionMode.RIGHT
    enum class TransitionMode{
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, FINISH
    }

    override fun showLoading() {
//        if (!loadingDialog?.isShowing!!)//!!loading为空时会出空指针异常
//            loadingDialog?.show()
    }

    override fun hidLoading() {
//        if (loadingDialog?.isShowing!!) {
//            loadingDialog?.dismiss()
//        }
    }

    override fun <A : Activity> openActivity(a: Class<A>, params: String, serializableBean: Any?, isResult: Boolean, requestCode: Int) {
        val intent = Intent(this, a)
        val list = params.split(",")
        list.forEach {
            val split = it.split("=")
            if (split.size == 2)
                intent.putExtra(split[0].trim(), split[1].trim())
        }
        if (serializableBean != null) {
            val tem: Serializable? = serializableBean as? Serializable
            if (tem != null)
                intent.putExtra("sBean", serializableBean)
        }
        if (!isResult)
            startActivity(intent)
        else
            startActivityForResult(intent, requestCode)
    }

    /**
     * 设置toolbar
     * @param title 中间显示的文本
     * @param isBack 是否显示返回按钮
     * @param textSize 字体大小
     * @param textColor  字体的颜色
     * @param layoutParams 文字参数
     * @param rightRes 右边的资源
     * @param rightClickListener    右边资源的点击事件
     */
    fun setToolbar(title: String, isBack: Boolean = true, rightRes: Any = 0, rightTextColor: Int = 0, rightClickListener: View.OnClickListener? = null, @DrawableRes leftRes: Int = 0, textSize: Float = 18f,
                   textColor: Int = 0, layoutParams: Toolbar.LayoutParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)) {
        if (mToolBar == null) return
        if (isBack) {
            if (leftRes == 0) {
                mToolBar?.setNavigationIcon(R.mipmap.ic_back)
            } else {
                mToolBar?.setNavigationIcon(leftRes)
            }
            mToolBar?.setNavigationOnClickListener {
                finishView()
            }
        }
        if (!TextUtils.isEmpty(title)) {
            val textView = TextView(this)
            textView.id = R.id.toolbar_title
            textView.text = title
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            if (textColor != 0)
                textView.setTextColor(textColor)
            mToolBar?.addView(textView, layoutParams)
        }
        if (rightClickListener == null) return
        val rightParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT)
        //判断是资源还是文本
        val dp15 = UIUtil.dp2px(this, 15)
        if (rightRes is Int) {
            val rightImg = ImageView(this)
            rightImg.id = R.id.toolbar_right
            rightImg.setPadding(dp15, 0, dp15, 0)
            rightImg.setImageResource(rightRes)
            rightImg.setOnClickListener(rightClickListener)
            mToolBar?.addView(rightImg, rightParams)
        }
        if (rightRes is String) {
            val textView = TextView(this)
            textView.id = R.id.toolbar_right
            textView.gravity = Gravity.CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            if (rightTextColor != 0) {
                textView.setTextColor(rightTextColor)
            } else {
                textView.setTextColor(resources.getColor(R.color.frame_title_color))
            }
            textView.setPadding(dp15, 0, dp15, 0)
            textView.text = rightRes
            textView.setOnClickListener(rightClickListener)
            mToolBar?.addView(textView, rightParams)
        }

    }

    /**
     * 优化事件设置
     * 目标：布局ID或者View
     * 作用：给控件添加任何事件
     */
    open fun Array<out Any>.setViewListener(listener: Any) {
        this.forEach {
            when (listener) {
                is View.OnClickListener -> {
                    if (it is Int)
                        findViewById<View>(it).setOnClickListener(listener)
                    (it as? View)?.setOnClickListener(listener)
                }
                is View.OnLongClickListener -> {
                    if (it is Int)
                        findViewById<View>(it).setOnLongClickListener(listener)
                    (it as? View)?.setOnLongClickListener(listener)
                }
                is View.OnTouchListener -> {
                    if (it is Int)
                        findViewById<View>(it).setOnTouchListener(listener)
                    (it as? View)?.setOnTouchListener(listener)
                }
                is RadioGroup.OnCheckedChangeListener -> {
                    if (it is Int)
                        findViewById<RadioGroup>(it).setOnCheckedChangeListener(listener)
                    (it as? RadioGroup)?.setOnCheckedChangeListener(listener)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (registerBus())
            mBus.unregister(this)
    }

   override fun finish() {
        super.finish()
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransitionMode()) {
                BaseActivity.TransitionMode.LEFT -> overridePendingTransition(R.anim.mow_left_in, R.anim.mow_left_out)
                BaseActivity.TransitionMode.RIGHT -> overridePendingTransition(R.anim.mow_right_in, R.anim.mow_right_out)
                BaseActivity.TransitionMode.FINISH -> overridePendingTransition(R.anim.mow_right_in, R.anim.mow_right_finish)
                BaseActivity.TransitionMode.TOP -> overridePendingTransition(R.anim.mow_top_in, R.anim.mow_top_out)
                BaseActivity.TransitionMode.BOTTOM -> overridePendingTransition(R.anim.mow_bottom_in, R.anim.mow_bottom_out)
                BaseActivity.TransitionMode.SCALE -> overridePendingTransition(R.anim.mow_scale_in, R.anim.mow_scale_out)
                BaseActivity.TransitionMode.FADE -> overridePendingTransition(R.anim.mow_fade_in, R.anim.mow_fade_out)
            }
        }
    }


}