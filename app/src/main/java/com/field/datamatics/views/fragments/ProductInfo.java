package com.field.datamatics.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Product;

/**
 * Created by Jithz on 4/7/2016.
 * Product info screen logic
 */
public class ProductInfo extends BaseFragment {


    private Product product;
    private static ProductInfo mInstance;

    private TextView txtProductNum;
    private TextView txtDesc;
    private TextView txtCategory;
    private TextView txtCategoryDesc;
    private TextView txtSubCategory;
    private TextView txtSubCategoryDesc;
    private TextView txtNarration;
    private TextView txtMrNarration;
    private TextView txtUsage;
    private View viewMrNarration;

    private String mrNarration;

    public ProductInfo() {
    }

    public static Fragment getInstance(Product product, String mrNarration) {
        mInstance = new ProductInfo();
        mInstance.product = product;
        mInstance.mrNarration = mrNarration;
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_info_fragment, container, false);

        initViews(view);
        setData();

        return view;
    }

    private void initViews(View view) {
        txtProductNum = (TextView) view.findViewById(R.id.txtProductNumber);
        txtDesc = (TextView) view.findViewById(R.id.txtProductDesc);
        txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        txtCategoryDesc = (TextView) view.findViewById(R.id.txtCategoryDesc);
        txtSubCategory = (TextView) view.findViewById(R.id.txtSubCategory);
        txtSubCategoryDesc = (TextView) view.findViewById(R.id.txtSubCategoryDesc);
        txtNarration = (TextView) view.findViewById(R.id.txtNarration);
        txtMrNarration = (TextView) view.findViewById(R.id.txtMrNarration);
        txtUsage = (TextView) view.findViewById(R.id.txtUsage);
        viewMrNarration = view.findViewById(R.id.view_mr_narration);
    }

    private void setData() {
        txtProductNum.setText(product.Product_Number);
        txtDesc.setText(product.Product_Description);
        txtCategory.setText(product.Category);
        txtCategoryDesc.setText(product.Category_desc);
        txtSubCategory.setText(product.Subcategory);
        txtSubCategoryDesc.setText(product.Subcat_desc);
        txtNarration.setText(product.Narration);
        txtUsage.setText(product.Usage);
        if (mrNarration != null) {
            viewMrNarration.setVisibility(View.VISIBLE);
            txtMrNarration.setText(mrNarration);
        }
    }
}
