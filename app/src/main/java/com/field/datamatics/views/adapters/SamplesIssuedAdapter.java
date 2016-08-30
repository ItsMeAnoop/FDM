package com.field.datamatics.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by Jith on 11/8/2015.
 */
public class SamplesIssuedAdapter extends RecyclerView.Adapter<SamplesIssuedAdapter.ViewHolder> {

    ArrayList<ProductSample> data;

    public SamplesIssuedAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_samples_issued, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductSample ps = data.get(position);

        holder.txtProductNum.setText("" + ps.product.Product_Number);
        holder.txtProductDesc.setText(ps.product.Product_Description);
        if(ps.routePlan!=null) {
            holder.txtClientName.setText(ps.routePlan.client.getName());
            holder.txtCustomerName.setText(ps.routePlan.customer.Customer_Name);
        }
        else{
            holder.txtClientName.setText(ps.clientName);
            holder.txtCustomerName.setText(ps.customerName);
        }
        holder.txtQuantity.setText("" + ps.quantity);
        String category = ps.category;
        if (position == 0) {
            holder.cvHeader.setVisibility(View.VISIBLE);
            holder.header.setText(category);
        } else {
            String categoryPrev = data.get(position - 1).category;
            if (!category.equals(categoryPrev)) {
                holder.cvHeader.setVisibility(View.VISIBLE);
                holder.header.setText(category);
            } else {
                holder.cvHeader.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductNum, txtProductDesc, txtClientName, txtCustomerName, txtQuantity, header;
        CardView cvHeader;

        public ViewHolder(View v) {
            super(v);
            txtProductNum = (TextView) v.findViewById(R.id.txt_product_number);
            txtProductDesc = (TextView) v.findViewById(R.id.txt_product_desc);
            txtClientName = (TextView) v.findViewById(R.id.txt_client_name);
            txtCustomerName = (TextView) v.findViewById(R.id.txt_customer_name);
            txtQuantity = (TextView) v.findViewById(R.id.txt_quantity);
            header = (TextView) v.findViewById(R.id.header);
            cvHeader = (CardView) v.findViewById(R.id.cv_header);
        }
    }

    public void setData(ArrayList<ProductSample> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
