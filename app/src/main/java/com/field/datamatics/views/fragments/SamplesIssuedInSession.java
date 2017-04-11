package com.field.datamatics.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.*;
import com.field.datamatics.json.ProductDetailJson;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.ui.RecyclerItemClickListener;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.SamplesIssuedAdapter;
import com.field.datamatics.views.adapters.SamplesIssuedInSessionAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jith on 11/8/2015.
 * Sample issued session screen logic
 */
public class SamplesIssuedInSession extends BaseFragment {


    private RecyclerView recyclerView;
    private static SamplesIssuedInSessionAdapter mAdapter;
    private ProgressBar progressBar;
    private static TextView emptyView;
    private static List<ProductSample> data;
    private int routePlanNumber;

    public SamplesIssuedInSession() {
    }

    public static SamplesIssuedInSession getInstance(int routePlanNumber) {
        SamplesIssuedInSession f = new SamplesIssuedInSession();
        f.routePlanNumber = routePlanNumber;
        MrActions.routePlanNumber = routePlanNumber;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.samples_issued_in_session, container, false);
        setTitle("Samples issued");
        addFragmentTitle(SamplesIssuedInSession.class.getName());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        emptyView = (TextView) view.findViewById(R.id.empty);
        emptyView.setText("No samples issued!");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        SamplesIssuedInSessionAdapter.ItemClickListener mItemClickListener = new SamplesIssuedInSessionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                final ProductSample sample = mAdapter.getData(position);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_product_sample);
                dialog.setTitle("Product Sample");
                AppCompatButton btnDone = (AppCompatButton) dialog.findViewById(R.id.button_done);
                Spinner sp = (Spinner) dialog.findViewById(R.id.sp);
                final EditText edtNarration = (EditText) dialog.findViewById(R.id.edt_narration);
                final EditText edtQuantity = (EditText) dialog.findViewById(R.id.edt_quantity);

                String quantity = Integer.toString(sample.quantity);
                edtQuantity.setText(quantity);
                edtQuantity.setSelection(quantity.length());
                if (sample.narration == null) sp.setSelection(0);
                else {
                    edtNarration.setText(sample.narration);
                    edtNarration.setSelection(sample.narration.length());
                    sp.setSelection(1);
                }


                final boolean[] isDoctor = new boolean[1];
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            edtNarration.setText("");
                            edtNarration.setVisibility(View.GONE);
                            isDoctor[0] = true;
                        } else {
                            edtNarration.setVisibility(View.VISIBLE);
                            isDoctor[0] = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantityText = edtQuantity.getText().toString();
                        if (TextUtils.isEmpty(quantityText)) {
                            edtQuantity.setError("Enter a value");
                            return;
                        }
                        int quantity = Integer.parseInt(quantityText);
                        sample.quantity = quantity;
                        sample.narration = isDoctor[0] ? null : edtNarration.getText().toString();
                        sample.update();
                        dialog.dismiss();
                        new loadData().execute();
                    }
                };
                btnDone.setOnClickListener(onClickListener);
                dialog.show();
            }
        };
        SamplesIssuedInSessionAdapter.MoreClickListener mMoreClickListener = new SamplesIssuedInSessionAdapter.MoreClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                addFragment(ProductInfo.getInstance(mAdapter.getData(position).product, null));
                addFragmentTitle("ProductInfoIssued");
            }
        };
        mAdapter = new SamplesIssuedInSessionAdapter();
        mAdapter.setClickListener(mItemClickListener);
        mAdapter.setmMoreClickListener(mMoreClickListener);
        recyclerView.setAdapter(mAdapter);
        new loadData().execute();

        return view;
    }


    private class loadData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            data = new Select()
                    .from(ProductSample.class)
                    .where(Condition.column(ProductSample$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlanNumber))
                    .queryList();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (data == null || data.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                mAdapter.setData((ArrayList<ProductSample>) data);
            }
        }
    }
}
