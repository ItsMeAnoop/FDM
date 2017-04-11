package com.field.datamatics.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.utils.AppControllerUtil;

import java.util.ArrayList;

/**
 * Created by Jith on 11/8/2015.
 * Sample issued session adapter
 */
public class SamplesIssuedInSessionAdapter extends RecyclerView.Adapter<SamplesIssuedInSessionAdapter.ViewHolder> {

    ArrayList<ProductSample> data;
    private ItemClickListener mClickListener;
    private MoreClickListener mMoreClickListener;

    public SamplesIssuedInSessionAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_samples_issued_in_session, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void setClickListener(ItemClickListener listener) {
        mClickListener = listener;
    }

    public void setmMoreClickListener(MoreClickListener listener) {
        mMoreClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductSample ps = data.get(position);
        holder.txtProductNum.setText("" + ps.product.Product_Number);
        holder.txtQuantity.setText("" + ps.quantity);
        holder.txtDescription.setText(ps.product.Product_Description);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        TextView txtProductNum, txtQuantity, txtDescription, btnMore;

        public ViewHolder(View v) {
            super(v);
            txtProductNum = (TextView) v.findViewById(R.id.txt_product_number);
            txtQuantity = (TextView) v.findViewById(R.id.txt_quantity);
            txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            btnMore = (TextView) v.findViewById(R.id.btnMore);
            btnMore.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == btnMore) {
                if (mMoreClickListener != null)
                    mMoreClickListener.onItemClick(v, getAdapterPosition());
            } else {
                if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }

    public void setData(ArrayList<ProductSample> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ProductSample getItems(int position) {
        return data.get(position);
    }

    public ProductSample getData(int position) {
        return data.get(position);
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface MoreClickListener {
        void onItemClick(View v, int position);
    }
}
