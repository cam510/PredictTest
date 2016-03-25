package com.example.cam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.categorytest.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by cam on 3/21/16.
 */
public class AppAdapter extends BaseAdapter{

    private ArrayList<PackageVO> mAppList;
    private Context context;
    private LayoutInflater mInflater;

    public AppAdapter() {
    }

    public AppAdapter(ArrayList<PackageVO> mAppList, Context context) {
        this.mAppList = mAppList;
        this.context = context;
        mInflater = mInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mAppList == null) {
            return 0;
        }
        return mAppList.size();
    }

    @Override
    public PackageVO getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.item_app, null);
            holder = new ViewHolder();
            holder.mIcon = (ImageView) convertView.findViewById(R.id.im_app_icon);
            holder.mAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageVO p = mAppList.get(position);
//        holder.mIcon.setImageDrawable(p.icon);
        holder.mAppName.setText("" + p.appname);

        return convertView;
    }

    class ViewHolder {
        ImageView mIcon;
        TextView mAppName;
    }
}
