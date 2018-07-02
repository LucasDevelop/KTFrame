package com.lucas.frame.loading;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lucas.frame.R;


public class VaryViewHelperController {

    private IVaryViewHelper helper;

    public VaryViewHelperController(View view) {
        this(new VaryViewHelper(view));
    }

    public VaryViewHelperController(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showNetworkError(View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.frame_view_pager_error);
        TextView againBtn = layout.findViewById(R.id.tv_view_pager_error_load);
        if (null != onClickListener) {
            againBtn.setOnClickListener(onClickListener);
        }
        helper.showLayout(layout);
    }

    public void showEmpty(String emptyMsg, int color) {
        View layout = helper.inflate(R.layout.frame_view_pager_no_data);
        TextView textView = layout.findViewById(R.id.tv_view_pager_no_data_content);
        if (!TextUtils.isEmpty(emptyMsg)) {
            textView.setText(emptyMsg);
        }
        if (color != 0){
            textView.setBackgroundColor(color);
        }
        helper.showLayout(layout);
    }

    public void showEmpty(String emptyMsg) {
       showEmpty(emptyMsg,0);
    }

    public void showLoading() {
        View layout = helper.inflate(R.layout.frame_view_pager_loading);
        helper.showLayout(layout);
    }

    public void restore() {
        helper.restoreView();
    }
}
