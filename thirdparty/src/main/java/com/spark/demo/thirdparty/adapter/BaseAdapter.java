package com.spark.demo.thirdparty.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类adapter
 *
 * @author spark
 * @date 2015-6-25
 */
public abstract class  BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context context;
    protected List<T> list = null;
    protected List<ViewHolder<T>> holders = new ArrayList<ViewHolder<T>>();
    protected int position; // 当前getView 走到的 position

    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null && position >= 0 && position < list.size() ? list
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder<T> holder;
        this.position  = position;
        if (convertView == null) {
            convertView = createView(context, parent);
            holder = createViewHolder();
            convertView.setTag(holder);
            holder.init(context, convertView);
            holders.add(holder);
        }
        else {
            holder = (ViewHolder<T>) convertView.getTag();
        }
        T data = list.get(position);
        holder.position = position;
        holder.size = list.size();
        holder.update(context, data);
        return convertView;
    }

    public abstract View createView(Context context, ViewGroup parent);

    public abstract ViewHolder<T> createViewHolder();

    /**
     * 保存当前Item的view
     */
    public static abstract class ViewHolder<T> {
        public int position;

        public int size;

        public abstract void init(Context context, View convertView);

        public abstract void update(Context context, T data);
    }
}