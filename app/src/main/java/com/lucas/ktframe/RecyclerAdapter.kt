package com.lucas.ktframe

import com.lucas.frame.base.adapter.BaseQuickAdapter
import com.lucas.frame.base.adapter.BaseViewHolder

class RecyclerAdapter(id:Int):BaseQuickAdapter<ListBean.DataBean.DatasBean,BaseViewHolder>(id) {
    override fun convert(helper: BaseViewHolder, item: ListBean.DataBean.DatasBean) {
        helper.setText(R.id.v_text,item.title)
    }

}