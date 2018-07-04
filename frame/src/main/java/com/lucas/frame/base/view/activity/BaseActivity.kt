package com.lucas.frame.base.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
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
import com.lucas.frame.helper.CommentHelper
import com.lucas.frame.utils.UIUtil
import com.squareup.otto.Bus
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.io.Serializable
import com.jude.swipbackhelper.SwipeListener
import com.jude.swipbackhelper.SwipeBackHelper




/**
 * @package     com.lucas.frame.base.view
 * @author      lucas
 * @date        2018/6/30
 * @version     V1.0
 * @describe    所有activity应该集成该类
 */
abstract class BaseActivity : RxAppCompatActivity(), CommentHelper {

    var mBus: Bus = FrameApp.INSTANCE.bus
    var mFrameApp: FrameApp? = null
    var mToolBar: Toolbar? = null
    var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnim()
        initSwipeBack()
        if (registerBus())
            mBus.register(this)
        onCreateInit()
        val layoutID = getLayoutID()
        if (layoutID != 0)
            setContentView(layoutID)
        mFrameApp = (application as FrameApp)
        initComment()
        initView()
        initData()
        initEvent()
    }

    //初始化侧滑关闭
    private fun initSwipeBack() {
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//get current instance
                .setSwipeBackEnable(true)//on-off
                .setSwipeEdge(200)//set the touch area。200 mean only the left 200px of screen can touch to begin swipe.
                .setSwipeEdgePercent(0.2f)//0.2 mean left 20% of screen can touch to begin swipe.
                .setSwipeSensitivity(0.5f)//sensitiveness of the gesture。0:slow  1:sensitive
                .setScrimColor(R.color.frame_blue_color)//color of Scrim below the activity
                .setClosePercent(0.8f)//close activity when swipe over this
                .setSwipeRelateEnable(false)//if should move together with the following Activity
                .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true)
                .setDisallowInterceptTouchEvent(true)//your view can hand the events first.default false;
                .addListener(object : SwipeListener {
                    override fun onScroll(percent: Float, px: Int) {}

                    override fun onEdgeTouch() {}

                    override fun onScrollToClose() {}
                })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    //初始化activity打开与关闭动画
    private fun initAnim() {
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransitionMode()) {
                TransitionMode.LEFT -> overridePendingTransition(R.anim.frame_left_in, R.anim.frame_left_out)
                TransitionMode.RIGHT -> overridePendingTransition(R.anim.frame_right_in, R.anim.frame_right_out)
                TransitionMode.FINISH -> overridePendingTransition(R.anim.frame_right_in, R.anim.frame_right_finish)
                TransitionMode.TOP -> overridePendingTransition(R.anim.frame_top_in, R.anim.frame_top_out)
                TransitionMode.BOTTOM -> overridePendingTransition(R.anim.frame_bottom_in, R.anim.frame_bottom_out)
                TransitionMode.SCALE -> overridePendingTransition(R.anim.frame_scale_in, R.anim.frame_scale_out)
                TransitionMode.FADE -> overridePendingTransition(R.anim.frame_fade_in, R.anim.frame_fade_out)
            }
        }
    }

    open fun onCreateInit() {}

    open fun initComment() {
        //查找控件
        mToolBar = findViewById(R.id.frame_toolbar)
    }

    abstract fun initView()
    abstract fun initData()
    open fun initEvent() {}
    //布局id
    abstract fun getLayoutID(): Int
    //注册事件
    open fun registerBus() = false
    //Activity是否添加动画
    open fun toggleOverridePendingTransition() = true
    //Activity动画类型
    open fun getOverridePendingTransitionMode() = TransitionMode.RIGHT
    enum class TransitionMode{
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, FINISH
    }

    //打开一个activity
    //打开一个activity并且传入参数 格式 ：key=value,key=value...
    //是否返回数据
    fun <A : Activity> openActivity(a: Class<A>, params: String, serializableBean: Any?, isResult: Boolean, requestCode: Int) {
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
                mToolBar?.setNavigationIcon(R.mipmap.frame_ic_back)
            } else {
                mToolBar?.setNavigationIcon(leftRes)
            }
            mToolBar?.setNavigationOnClickListener {
                finish()
            }
        }
        if (!TextUtils.isEmpty(title)) {
            val textView = TextView(this)
            textView.id = R.id.frame_toolbar_title
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
            rightImg.id = R.id.frame_toolbar_right
            rightImg.setPadding(dp15, 0, dp15, 0)
            rightImg.setImageResource(rightRes)
            rightImg.setOnClickListener(rightClickListener)
            mToolBar?.addView(rightImg, rightParams)
        }
        if (rightRes is String) {
            val textView = TextView(this)
            textView.id = R.id.frame_toolbar_right
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
        SwipeBackHelper.onDestroy(this)
        if (registerBus())
            mBus.unregister(this)
    }

   override fun finish() {
        super.finish()
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransitionMode()) {
                TransitionMode.LEFT -> overridePendingTransition(R.anim.frame_left_in, R.anim.frame_left_out)
                TransitionMode.RIGHT -> overridePendingTransition(R.anim.frame_right_in, R.anim.frame_right_out)
                TransitionMode.FINISH -> overridePendingTransition(R.anim.frame_right_in, R.anim.frame_right_finish)
                TransitionMode.TOP -> overridePendingTransition(R.anim.frame_top_in, R.anim.frame_top_out)
                TransitionMode.BOTTOM -> overridePendingTransition(R.anim.frame_bottom_in, R.anim.frame_bottom_out)
                TransitionMode.SCALE -> overridePendingTransition(R.anim.frame_scale_in, R.anim.frame_scale_out)
                TransitionMode.FADE -> overridePendingTransition(R.anim.frame_fade_in, R.anim.frame_fade_out)
            }
        }
    }


}