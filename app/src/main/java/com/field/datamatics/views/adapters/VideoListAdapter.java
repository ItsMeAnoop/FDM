package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.field.datamatics.R;

import java.io.File;

/**
 * Created by anoop on 27/9/15.
 */
public class VideoListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater = null;
    private int width;
    private File[] videoFiles;

    public VideoListAdapter(Activity activity, int width) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.width = width;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return videoFiles == null ? 0 : videoFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return videoFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView iv_image;
        TextView tv_file_name;

    }

    public void setData(File[] images) {
        videoFiles = images;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        View rowView = convertView;
        int pos = position;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.sublist_video_list, null);
            holder = new Holder();
            holder.iv_image = (ImageView) rowView.findViewById(R.id.iv_image);
            holder.tv_file_name = (TextView) rowView.findViewById(R.id.file_name);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.iv_image.setMinimumWidth(width);
        holder.iv_image.setMinimumHeight((int) (width));
        rowView.setMinimumWidth(width);
        rowView.setMinimumHeight((int) (width));

        holder.tv_file_name.setText(videoFiles[position].getName().toString());
       Glide.with(activity)
               .load(videoFiles[pos])
              // .override(150, 150)
               .centerCrop()
               .crossFade()
               .thumbnail(0.5f)
               .diskCacheStrategy(DiskCacheStrategy.NONE)
               .skipMemoryCache(true)
               .placeholder(R.drawable.placeholder).into(holder.iv_image);

        return rowView;
    }
}
