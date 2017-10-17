package com.gcs.jyfk.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.gcs.jyfk.R;
import com.gcs.jyfk.ui.bean.BankType;
import com.gcs.jyfk.utils.UIUtils;

/**
 * Created by lyw on 2017/8/2.
 */

public class PopuWebListHolder extends BaseHolder<BankType> {



    private TextView tvName;
    private TextView tvVisiblecode;

    @Override
    public View initView() {
        // 1. 加载布局
        View view = UIUtils.inflate(R.layout.list_allcustorselect_item);
        // 2. 初始化控件
        tvVisiblecode = (TextView) view.findViewById(R.id.tvVisible_code);
        tvName = (TextView) view.findViewById(R.id.visit_name);



        return view;
    }

    @Override
    public void refreshView(BankType data) {
        tvName.setText(data.getBankName().toString());
        tvVisiblecode.setText(String.valueOf(data.getBankCode()));
    }


}
