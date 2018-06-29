package com.lucas.frame.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lucas.frame.R;


/**
 * @项目名称 maohao
 * @作者 Allen
 * @日期 2015/12/7 15:09
 * @描述 点击选择拍照或者选择本地的Popup
 */
public class PictureSelectPopup extends PopupWindow {
    private Button bt_takephoto, bt_photo, bt_cancel;
    private View mMenuView;

    public PictureSelectPopup(Activity context, View.OnClickListener itemsOnclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.frame_popup_selectpicture, null);
        bt_takephoto =  mMenuView.findViewById(R.id.bt_select_takephoto);
        bt_photo =  mMenuView.findViewById(R.id.bt_select_photo);
        bt_cancel =  mMenuView.findViewById(R.id.bt_select_cancel);
        //取消按钮
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //设置按钮监听
        bt_photo.setOnClickListener(itemsOnclick);
        bt_takephoto.setOnClickListener(itemsOnclick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        mMenuView.findViewById(R.id.ll_select_pop).startAnimation(AnimationUtils.loadAnimation(context, R.anim
                .popup_photo_enter));
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x30000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.ll_select_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
