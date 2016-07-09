package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.field.datamatics.R;

import java.io.File;

/**
 * Created by Jith on 10/21/2015.
 */
public class BrochureListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater = null;
    private File[] files;
    private int width;

    public BrochureListAdapter(Activity activity, int width) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.width = width;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return files == null ? 0 : files.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        ImageView iv_image;
        TextView fileName;
    }

    public void setData(File[] images) {
        files = images;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        View rowView = convertView;
        int pos = position;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.sublist_brochure, null);
            holder = new Holder();
            holder.iv_image = (ImageView) rowView.findViewById(R.id.iv_image);
            holder.fileName = (TextView) rowView.findViewById(R.id.file_name);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.iv_image.setMinimumWidth(width);
        holder.iv_image.setMinimumHeight((int) (width));
        holder.fileName.setText(files[position].getName());
        rowView.setMinimumWidth(width);
        rowView.setMinimumHeight((int) (width));
        String fileName = files[position].getName().toLowerCase();
        int imageId = R.drawable.ic_doc;
        if (fileName.endsWith(".pdf")) {
            imageId = R.drawable.ic_pdf;
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            imageId = R.drawable.ic_excel;
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            imageId = R.drawable.ic_ppt;
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            imageId = R.drawable.ic_doc;
        } else if (fileName.endsWith(".txt")) {
            imageId = R.drawable.ic_txt;
        } else {
            imageId = R.drawable.ic_unknown;
        }
        holder.iv_image.setImageResource(imageId);
        return rowView;
    }
}
