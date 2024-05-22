package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondhandtransaction.R;

import com.example.po.Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//我的收藏适配器Adapter类
public class MyCollectionAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Collection> collections = new ArrayList<>();

    HashMap<Integer, View> location = new HashMap<>();

    public MyCollectionAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Collection> collection) {
        this.collections.clear();
        this.collections.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Collection collection = collections.get(position);
        ViewHolder holder = null;
        if (location.get(position) == null) {
            convertView = layoutInflater.inflate(R.layout.layout_my_collection, null);
            holder = new ViewHolder(convertView, collection);
            location.put(position, convertView);
            convertView.setTag(holder);
        } else {
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();

            holder.tvTitle.setText("标题:" + collection.getTitle());
            holder.tvDescription.setText("描述:" + collection.getDescription());
            holder.tvPrice.setText("￥:" + String.valueOf(collection.getPrice()) + "元");
            holder.tvPhone.setText("电话:" + collection.getPhone());
            if (!TextUtils.isEmpty(collection.getTime())) {
                holder.tvTime.setText(collection.getTime());
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvDescription.setVisibility(View.GONE);
            }

            byte[] picture = collection.getPicture();
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            holder.ivCommodity.setImageBitmap(img);
        }
        return convertView;
    }

    //定义静态类,包含每一个item的所有元素
    static class ViewHolder {
        ImageView ivCommodity;
        TextView tvTitle, tvDescription, tvPrice, tvPhone, tvTime;

        public ViewHolder(View itemView, Collection collection) {
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            tvTime = itemView.findViewById(R.id.tv_time);

            tvTitle.setText("标题:" + collection.getTitle());
            tvDescription.setText("描述:" + collection.getDescription());
            tvPrice.setText("￥:" + String.valueOf(collection.getPrice()) + "元");
            tvPhone.setText("电话:" + collection.getPhone());
            if (!TextUtils.isEmpty(collection.getTime())) {
                tvTime.setText(collection.getTime());
                tvTime.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.GONE);
            }

            byte[] picture = collection.getPicture();
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivCommodity.setImageBitmap(img);
        }
    }
}
