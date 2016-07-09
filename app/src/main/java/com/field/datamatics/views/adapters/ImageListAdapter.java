package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.field.datamatics.R;
import com.field.datamatics.models.MediaItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by anoop on 27/9/15.
 */
public class ImageListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater = null;
    private File[] imageFiles;
    private int width;

    public ImageListAdapter(Activity activity, int width) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.width = width;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageFiles == null ? 0 : imageFiles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imageFiles[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        ImageView iv_image;
    }

    public void setData(File[] images) {
        imageFiles = images;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        View rowView = convertView;
        int pos = position;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.sublist_image_list, null);

        }
        holder = new Holder();
        holder.iv_image = (ImageView) rowView.findViewById(R.id.iv_image);
        holder.iv_image.setMinimumWidth(width);
        holder.iv_image.setMinimumHeight((int) (width));
        rowView.setMinimumWidth(width);
        rowView.setMinimumHeight((int) (width));

//        Glide.with(activity)
//                .load(imageFiles[pos])
//                .dontTransform()
//                // .override(512, 512)
//                .centerCrop()
//                .thumbnail(0.5f)
//                .crossFade()
//                .placeholder(R.drawable.placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(holder.iv_image);

        Picasso.with(activity)
                .load(imageFiles[pos])
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_image);

        return rowView;
    }
}


