package com.gcs.jyfk.ui.adapter;

import com.gcs.jyfk.ui.bean.BankType;
import com.gcs.jyfk.ui.holder.BaseHolder;
import com.gcs.jyfk.ui.holder.PopuWebListHolder;

import java.util.List;

/**
 * Created by lyw on 2017/8/2.
 */

public class MyWebListPopuAdapter extends MyBaseAdapter {

    public MyWebListPopuAdapter(List<BankType> data) {
        super(data);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PopuWebListHolder();
    }
}
