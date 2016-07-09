package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Product;

import java.util.ArrayList;

/**
 * Created by Jith on 18/10/2015.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private ArrayList<Product> data;
    private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvProductNumber;

        public ViewHolder(View v) {
            super(v);
            tvProductName = (TextView) v.findViewById(R.id.tv_product_name);
            tvCategory = (TextView) v.findViewById(R.id.tv_product_catgry);
            tvDescription = (TextView) v.findViewById(R.id.tv_product_desc);
            tvProductNumber = (TextView) v.findViewById(R.id.tv_product_number);
        }

    }

    public ProductListAdapter(Activity activity, ArrayList<Product> data) {
        this.data = data;
        this.activity = activity;
    }

    public void setData(ArrayList<Product> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public Product getItem(int position) {
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_product_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = data.get(position);
        holder.tvProductName.setText(product.Product_Description);
        holder.tvDescription.setText(product.Category);
        holder.tvCategory.setText(product.Category_desc);
        holder.tvProductNumber.setText(product.Product_Number);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

