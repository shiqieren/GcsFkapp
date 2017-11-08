package com.gcs.jyfk.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.gcs.jyfk.ui.holder.BaseHolder;

import java.util.List;


/**
 * adapter基类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {


    public List<T> data;

    public MyBaseAdapter(List<T> data) {
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();// 增加加载更多布局数量

    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // 子类可以重写此方法来更改返回的布局类型
    public int getInnerType(int position) {
        return 0;// 默认就是普通类型
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            // 1. 加载布局文件convertView = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.news_items, null);
            // 2. 初始化控件 findViewById
            // 3. 打一个标记tag convertView.setTag(holder);
            holder = getHolder(position);// 子类返回具体对象

            //convertView.setTag(R.id.tag_first,holder);//绑定holder 缓存的复用

            //convertView.setTag(R.id.tag_second, T);//绑定数据
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        holder.setData(getItem(position));


        return holder.getRootView();
    }


    // 返回当前页面的holder对象, 必须子类实现
    public abstract BaseHolder<T> getHolder(int position);


    //获取当前集合大小
    public int getListSize() {
        return data.size();
    }

}
