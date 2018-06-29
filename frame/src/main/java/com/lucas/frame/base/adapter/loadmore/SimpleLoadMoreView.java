package com.lucas.frame.base.adapter.loadmore;


import com.lucas.frame.R;

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.frame_view_adapter_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
