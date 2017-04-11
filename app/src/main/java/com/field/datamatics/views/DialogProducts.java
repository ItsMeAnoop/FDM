package com.field.datamatics.views;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.CustomProductList;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.interfaces.ItemSelectedListener;
import com.field.datamatics.interfaces.ProductSelectedFinishedListener;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.utils.Utilities;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jithz on 12/20/2015.
 * Product dialog
 */
public class DialogProducts implements ItemSelectedListener {

    private Context mContext;
    private RecyclerView recyclerView;
    private AppCompatButton btnDone;
    private RelativeLayout progress;
    private TextView empty;
    private TextView txtNumSelected;
    private TextView clearSelection;
    private MyAdapter mAdapter;
    private List<CustomProductList> data;
    private final int LIMIT_SELECTION = 10;

    public static final int TYPE_PRODUCT_NUM = 1;
    public static final int TYPE_CATEGORY = 2;

    private int type;

    private ProductSelectedFinishedListener productSelectedFinishedListener;
    private ArrayList<CustomProductList> selectedProducts;

    public DialogProducts(Context context, ProductSelectedFinishedListener mListener, int type, ArrayList<CustomProductList> selectedProducts) {
        mContext = context;
        productSelectedFinishedListener = mListener;
        this.selectedProducts = selectedProducts;
        this.type = type;
    }

    public void showDialogProductNumber() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_product_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        clearSelection = (TextView) dialog.findViewById(R.id.clearSelection);
        txtNumSelected = (TextView) dialog.findViewById(R.id.txtNumSelected);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.listProducts);
        btnDone = (AppCompatButton) dialog.findViewById(R.id.btnDone);
        progress = (RelativeLayout) dialog.findViewById(R.id.progress);
        empty = (TextView) dialog.findViewById(R.id.empty);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (data == null || data.size() == 0)
                    return;
                productSelectedFinishedListener.onProductSelectedFinished(mAdapter.getSelectedItems(), type);
            }
        });
        clearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clearSelection();
            }
        });

        new loadData().execute();
        dialog.show();
    }

    @Override
    public void onItemSelected(int value) {
        txtNumSelected.setText(Integer.toString(value));
    }

    private class loadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Calendar calendar = Calendar.getInstance();
            ColumnAlias c1;
            if (type == TYPE_PRODUCT_NUM)
                c1 = ColumnAlias.column("ProductSample." + ProductSample$Table.PRODUCT_PRODUCT_NUMBER).as("product");
            else
                c1 = ColumnAlias.column("ProductSample." + ProductSample$Table.CATEGORY).as("product");
            data = new Select(c1).distinct()
                    .from(ProductSample.class)
                    .where("strftime('%m', visited_date) = ? and strftime('%Y', visited_date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                    .queryCustomList(CustomProductList.class);
            if (selectedProducts != null) {
                for (CustomProductList cp1 : data) {
                    for (CustomProductList cp2 : selectedProducts) {
                        if (cp2.product.equals(cp1.product)) {
                            cp1.isSelected = true;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.GONE);
            Log.i("FDM_dialog", "size : " + data.size());
            if (data == null || data.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                mAdapter = new MyAdapter(data, DialogProducts.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    recyclerView.addItemDecoration(new DividerDecoration(mContext));
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(mAdapter);
                clearSelection.setEnabled(true);
            }

        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<CustomProductList> items;
        ItemSelectedListener itemSelectedListener;
        private int numSelected = 0;

        public MyAdapter(List<CustomProductList> data, ItemSelectedListener itemSelectedListener) {
            items = new ArrayList<>(data);
            this.itemSelectedListener = itemSelectedListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_products, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.txtProduct.setText(items.get(position).product);
            holder.cb.setChecked(items.get(position).isSelected);
            holder.cb.setTag(items.get(position));
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    if (cb.isChecked()) {
                        numSelected++;
                        if (numSelected > LIMIT_SELECTION) {
                            numSelected--;
                            Toast.makeText(mContext, "You cannot select more than " + LIMIT_SELECTION + " products", Toast.LENGTH_SHORT).show();
                            cb.setChecked(false);
                            return;
                        }
                    } else {
                        numSelected--;
                    }
                    CustomProductList pl = (CustomProductList) cb.getTag();
                    pl.isSelected = cb.isChecked();
                    itemSelectedListener.onItemSelected(numSelected);
                }
            });
        }

        public void clearSelection() {
            numSelected = 0;
            for (CustomProductList p : items) {
                p.isSelected = false;
            }
            notifyDataSetChanged();
            itemSelectedListener.onItemSelected(0);
        }

        public ArrayList<CustomProductList> getSelectedItems() {
            ArrayList<CustomProductList> selectedItems = new ArrayList<>();
            for (CustomProductList p : items) {
                if (p.isSelected) selectedItems.add(p);
            }
            return selectedItems;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtProduct;
            CheckBox cb;

            public ViewHolder(View v) {
                super(v);
                txtProduct = (TextView) v.findViewById(R.id.txtProductNum);
                cb = (CheckBox) v.findViewById(R.id.cb);
            }
        }
    }
}
