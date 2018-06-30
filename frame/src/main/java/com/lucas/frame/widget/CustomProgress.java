package com.lucas.frame.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucas.frame.R;
import com.lucas.frame.utils.DisplayUtil;
import com.lucas.frame.widget.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;


public class CustomProgress extends Dialog {

    private Context mContext;

    private NumberProgressBar mProgressBar;

    public CustomProgress(Context context) {
        this(context, R.style.frame_Custom_Progress);
        mContext = context;
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     */
    public void show(CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        setTitle("");
        setContentView(R.layout.frame_custom_progress);
        if (TextUtils.isEmpty(message)) {
            findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
        }
        //设置取消的按钮和显示对话框
        setCancelableBtnAndShow(cancelable, cancelListener);
    }


    /**
     * 显示弹出对话框
     *
     * @param content           显示的内容
     * @param title             显示的标题
     * @param cancelBtnListener 取消按钮的监听器
     * @param ensureBtnListener 确认按钮的监听器
     * @param cancelable        是否可以触摸外部让对话框消失
     * @param cancelListener    触摸外部让对话框消失的监听器
     */
    public void show(String content,
                     String title,
                     View.OnClickListener cancelBtnListener,
                     View.OnClickListener ensureBtnListener,
                     boolean cancelable,
                     OnCancelListener cancelListener) {
        show(content, title, "", "", cancelBtnListener, ensureBtnListener, cancelable, cancelListener);
    }


    /**
     * 显示弹出对话框
     *
     * @param content           显示的内容
     * @param title             显示的标题
     * @param cancelBtnListener 取消按钮的监听器
     * @param ensureBtnListener 确认按钮的监听器
     * @param cancelable        是否可以触摸外部让对话框消失
     * @param cancelListener    触摸外部让对话框消失的监听器
     */
    public void show(String content,
                     String title,
                     String cancelText, String ensureText,
                     View.OnClickListener cancelBtnListener,
                     View.OnClickListener ensureBtnListener,
                     boolean cancelable,
                     OnCancelListener cancelListener) {
        setTitle("");
        setContentView(R.layout.frame_custom_alert_dialog);
        TextView cancelBtn = (TextView) findViewById(R.id.tv_custom_alert_dialog_cancel);
        TextView ensureBtn = (TextView) findViewById(R.id.tv_custom_alert_dialog_ensure);
        //设置确定和取消的按钮
        if (!TextUtils.isEmpty(cancelText)) {
            cancelBtn.setText(cancelText);
        }

        if (!TextUtils.isEmpty(ensureText)) {
            ensureBtn.setText(ensureText);
        }

        TextView tvContent = (TextView) findViewById(R.id.ll_custom_alert_dialog_content);
        TextView tvTitle = (TextView) findViewById(R.id.tv_custom_alert_dialog_title);

        //设置title
        setCustomTitle(title, tvTitle);

        //添加内容的显示
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(content);
        }

        //设置点击事件
        setCustomClick(cancelBtnListener, ensureBtnListener, cancelable, cancelListener, cancelBtn, ensureBtn);
    }


    /**
     * 下载的弹出框
     *
     * @param title             title
     * @param cancelBtnListener 取消按钮
     * @param cancelable        是否可以点击外部取消
     * @param cancelListener    点击外部取消的事件
     */
    public void showDownLoad(String title, View.OnClickListener cancelBtnListener, boolean cancelable,
                             OnCancelListener cancelListener) {
        setTitle("");
        setContentView(R.layout.frame_custom_download_view);
        TextView downloadTitle = (TextView) findViewById(R.id.tv_download_title);
        mProgressBar = (NumberProgressBar) findViewById(R.id.npb_download_progress);
        TextView cancelText = (TextView) findViewById(R.id.tv_download_cancel);

        if (!TextUtils.isEmpty(title)) {
            downloadTitle.setText(title);
        }

        if (cancelBtnListener != null) {
            cancelText.setOnClickListener(cancelBtnListener);
        }

        // 按返回键是否取消
        setCancelableBtnAndShow(cancelable, cancelListener);
    }

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        if (mProgressBar != null) {
            mProgressBar.setProgress(progress);
        }
    }


    /**
     * 设置定义的点击事件
     *
     * @param cancelBtnListener 取消的监听
     * @param ensureBtnListener 确认的监听
     * @param cancelable        是否可以点击外部取消
     * @param cancelListener    点击外部取消的监听
     * @param cancelBtn         取消的控件
     * @param ensureBtn         确定的控件
     */
    private void setCustomClick(View.OnClickListener cancelBtnListener, View.OnClickListener ensureBtnListener, boolean cancelable, OnCancelListener cancelListener, TextView cancelBtn, TextView ensureBtn) {
        //设置点击事件
        if (cancelBtnListener != null) {
            cancelBtn.setOnClickListener(cancelBtnListener);
        }

        if (ensureBtnListener != null) {
            ensureBtn.setOnClickListener(ensureBtnListener);
        }
        // 按返回键是否取消
        setCancelableBtnAndShow(cancelable, cancelListener);
    }


    /**
     * 设置title
     *
     * @param title   文本内容
     * @param tvTitle 需要设置的textView
     */
    private void setCustomTitle(String title, TextView tvTitle) {
        //设置title
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            if (!tvTitle.isShown()) {
                tvTitle.setVisibility(View.VISIBLE);
            }
            tvTitle.setText(title);
        }
    }

    /**
     * 设置只有确定按钮的提示
     *
     * @param content           内容
     * @param title             标题
     * @param ensureBtnListener 确定按钮
     * @param cancelable        是否可以点击外部消失
     * @param cancelListener    消失的监听
     */
    public void show(String content,
                     String title, String ensureText,
                     View.OnClickListener ensureBtnListener,
                     boolean cancelable,
                     OnCancelListener cancelListener) {
        setTitle("");
        setContentView(R.layout.frame_custom_alert_dialog_sure);

        TextView ensureBtn = (TextView) findViewById(R.id.tv_custom_alert_dialog_ensure_ensure);
        //设置确定和取消的按钮
        TextView tvContent = (TextView) findViewById(R.id.ll_custom_alert_dialog_ensure_content);
        TextView tvTitle = (TextView) findViewById(R.id.tv_custom_alert_dialog_ensure_title);

        //设置title
        setCustomTitle(title, tvTitle);

        //添加内容的显示
        tvContent.setText(content);

        if (!TextUtils.isEmpty(ensureText)) {
            ensureBtn.setText(ensureText);
        }

        if (ensureBtnListener != null) {
            ensureBtn.setOnClickListener(ensureBtnListener);
        }
        // 按返回键是否取消
        setCancelableBtnAndShow(cancelable, cancelListener);
    }

    public void show(String title, View.OnClickListener itemListener, ArrayList<String> dates,
                     boolean cancelable,
                     OnCancelListener cancelListener) {
        setTitle("");
        setContentView(R.layout.frame_custom_phone_select_view);
        View view;
        LinearLayout layout = (LinearLayout) findViewById(R.id.rg_custom_device_selector_layout);
        TextView textView;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 44));
        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 0.2f));
        lineParam.leftMargin = DisplayUtil.dip2px(mContext, 15);
        lineParam.rightMargin = DisplayUtil.dip2px(mContext, 15);
        for (int i = 0; i < dates.size(); i++) {
            textView = new TextView(mContext);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setId(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DisplayUtil.dip2px(mContext, 16));
            textView.setTextColor(mContext.getResources().getColor(R.color.frame_text_color));
            textView.setText(dates.get(i));
            textView.setPadding(DisplayUtil.dip2px(mContext, 15), 0, DisplayUtil.dip2px(mContext, 15), 0);
            textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_selector_bg_layout));
            layout.addView(textView, params);

            if (itemListener != null) {
                textView.setOnClickListener(itemListener);
            }

            //添加分割线
            view = new View(mContext);
            view.setBackgroundResource(R.color.frame_line_color);
            layout.addView(view, lineParam);
        }

        TextView tvTitle = (TextView) findViewById(R.id.tv_custom_device_selector_title);

        setCustomTitle(title, tvTitle);

        setCancelableBtnAndShow(cancelable, cancelListener);
    }

    /**
     * 设置只有确定按钮的提示
     *
     * @param content           内容
     * @param title             标题
     * @param ensureBtnListener 确定按钮
     * @param cancelable        是否可以点击外部消失
     * @param cancelListener    消失的监听
     */
    public void show(String content,
                     String title,
                     View.OnClickListener ensureBtnListener,
                     boolean cancelable,
                     OnCancelListener cancelListener) {
        show(content, title, "", ensureBtnListener, cancelable, cancelListener);
    }


    /**
     * 设置取消按钮的事件和显示对话框
     *
     * @param cancelable     是否可以点击返回按键
     * @param cancelListener 返回按钮的监听器
     */
    private void setCancelableBtnAndShow(boolean cancelable, OnCancelListener cancelListener) {
        // 按返回键是否取消
        setCancelable(cancelable);
        // 监听返回键处理
        setOnCancelListener(cancelListener);
        // 设置居中
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        show();
    }
}
