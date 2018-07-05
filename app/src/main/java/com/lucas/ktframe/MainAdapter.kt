package com.lucas.ktframe

import com.lucas.frame.base.adapter.BaseQuickAdapter
import com.lucas.frame.base.adapter.BaseViewHolder

class MainAdapter(id:Int):BaseQuickAdapter<String,BaseViewHolder>(id) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.v_text,item)
    }
}