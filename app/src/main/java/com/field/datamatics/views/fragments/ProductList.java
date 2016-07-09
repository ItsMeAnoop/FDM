package com.field.datamatics.views.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.apimodels.ActivityModel;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.*;
import com.field.datamatics.json.ProductDetailJson;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.ui.RecyclerItemClickListener;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.ProductListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jith on 18/10/2015.
 */
public class ProductList extends BaseFragment {
    private RecyclerView recyclerView;
    private ArrayList<Product> data;
    private ProductListAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton searchButton;
    private ImageButton clearButton;
    private EditText edtSearch;
    private TextView empty;

    private static ProductList mInstance;
    private int routePlanNumber;

    private boolean isActivity = false;

    Calendar visitedDate;
    Calendar activityDate;
    private String activityName = "";

    private MainActivity mContainer;
    private String startCoordinate = "";
    private String endCoordinate = "";

    private int activityCount = 1;
    private static int clien_id;

    private View mView = null;

    public ProductList() {
    }

    public static ProductList getInstance(int routePlanNumber, int client_id_, Calendar visitedDate) {
        if (mInstance == null) {
            mInstance = new ProductList();
            mInstance.routePlanNumber = routePlanNumber;
            mInstance.visitedDate = visitedDate;
            clien_id = client_id_;
        }
        return mInstance;
    }

    public static ProductList getInstance(int routePlanNumber, int client_id_, Calendar visitedDate, ProductDetailJson prodDetails) {
        if (mInstance == null) {
            mInstance = new ProductList();
            mInstance.routePlanNumber = routePlanNumber;
            mInstance.visitedDate = visitedDate;
            clien_id = client_id_;

            mInstance.isActivity = true;
            mInstance.activityDate = prodDetails.activityDate;
            mInstance.activityName = prodDetails.activityName;
            mInstance.startCoordinate = prodDetails.startCoordinates;
        }
        return mInstance;
    }

    public static void clearInstance() {
        mInstance = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContainer = (MainActivity) getActivity();
        addFragmentTitle("CustomProductList");

        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
        if (MrActions.fragment_use.equals("SAMPLE"))
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_PRODUCT_SAMPLE);
        else
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_PRODUCT_LIST);
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.commit();
        if (mView != null) return mView;
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        initializeViews(view);
        if (data == null || data.size() == 0)
            new loadData().execute();
        mView = view;
        return view;
    }

    private void initializeViews(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        searchButton = (ImageButton) view.findViewById(R.id.search_button);
        edtSearch = (EditText) view.findViewById(R.id.edt_search);
        clearButton = (ImageButton) view.findViewById(R.id.clear_button);
        empty = (TextView) view.findViewById(R.id.empty);

        recyclerView.setLayoutManager(layoutManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        if (data == null)
            data = new ArrayList<>();
        adapter = new ProductListAdapter(getActivity(), data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isActivity = true;
                activityDate = Calendar.getInstance();
                Product product = adapter.getItem(position);
                activityName = "Product No:" + product.Product_Number;
                Location loc = mContainer.mLastLocation;
                if (loc != null) {
                    startCoordinate = loc.getLatitude() + "," + loc.getLongitude();
                }
                Utilities.hideKeyboard(getActivity());
                ProductDetailJson dataToRetain = new ProductDetailJson(activityDate, activityName, startCoordinate);
                AppControllerUtil.getPrefsResume()
                        .edit()
                        .putString(Constants.VAL_PRDUCT_DETAIL, new Gson().toJson(dataToRetain))
                        .commit();

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String json = gson.toJson(product);
                AppControllerUtil.getPrefsResume().edit().putString(Constants.VAL_PRDUCTS, json).commit();
                addFragment(ProductDetails.getInstance(product, routePlanNumber, visitedDate));
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(edtSearch.getText().toString());
            }
        };

        View.OnClickListener clearClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                adapter.setData(data);
            }
        };
        recyclerView.addOnItemTouchListener(itemClickListener);
        searchButton.setOnClickListener(clickListener);
        clearButton.setOnClickListener(clearClickListener);
    }

    private void filter(final String text) {
        final String searchText = text.toLowerCase();
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {

            ProgressDialog pd;
            ArrayList<Product> searchResult;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                searchResult = new ArrayList<>();
                pd = new ProgressDialog(mContainer);
                pd.setMessage("Searching...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (pd.isShowing()) pd.dismiss();
                adapter.setData(searchResult);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                if (TextUtils.isEmpty(searchText)) {
                    searchResult = new ArrayList<>(data);
                    return true;
                }
                for (Product p : data) {
                    if (p.Product_Description.toLowerCase().startsWith(searchText)
                            || p.Category.toLowerCase().startsWith(searchText)
                            || p.Subcategory.toLowerCase().startsWith(searchText)
                            || p.Family.toLowerCase().startsWith(searchText)
                            || p.Department.toLowerCase().startsWith(searchText)
                            || p.Product_Number.toLowerCase().startsWith(searchText)
                            || p.Supplier_Name.toLowerCase().startsWith(searchText)
                            || p.section.toLowerCase().startsWith(searchText)) {
                        searchResult.add(p);
                    }
                }
                return true;
            }
        };
        asyncTask.execute();
    }

    private class loadData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (!MrActions.fragment_use.equals("SAMPLE")) {
                data = (ArrayList<Product>) new Select().from(Product.class).where().limit(500).queryList();
                publishProgress();
                if (data.size() > 499)
                    data.addAll(new Select().from(Product.class).where().limit(-1).offset(500).queryList());
            } else {
                ArrayList<Client_Product> client_products = (ArrayList<Client_Product>) new Select().from(Client_Product.class).
                        where(Condition.column(Client_Product$Table.CLIENT_CLIENT_NUMBER).eq(clien_id)).queryList();
                //Log.d("client_products",client_products.size()+"");
                data = new ArrayList<Product>();
                for (int i = 0; i < client_products.size(); i++) {
                    try {
                        Product product = new Select().from(Product.class).
                                where(Condition.column(Product$Table.PRODUCT_NUMBER).eq(client_products.get(i).product.Product_Number)).querySingle();
                        data.add(product);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.GONE);
            adapter.setData(data);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            progressBar.setVisibility(View.GONE);
            if (data == null || data.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            }
            adapter.setData(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MrActions.fragment_use.equals("SAMPLE"))
            return;
        if (isActivity) {
            isActivity = false;
            Location loc = mContainer.mLastLocation;
            if (loc != null) {
                endCoordinate = loc.getLatitude() + "," + loc.getLongitude();
            }
            Activities activity = new Activities();
            activity.visitdate = Utilities.dateInSqliteFormat(visitedDate);
            activity.activitydate = Utilities.dateInSqliteFormat(activityDate);
            activity.routePlan = new com.field.datamatics.database.RoutePlan();
            activity.routePlan.Route_Plan_Number = routePlanNumber;
            activity.Activity = Constants.ACTIVITY_PRODUCT;
            activity.activityname = activityName;
            activity.starttime = Utilities.dateToString(activityDate, "yyyy-MM-dd HH:mm:ss");
            activity.endtime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            activity.startCoOrdinates = startCoordinate;
            activity.endCoOrdinates = endCoordinate;
            activity.save();
            activityName = "";

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (MrActions.fragment_use.equals("SAMPLE"))
            inflater.inflate(R.menu.menu_sample, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_samples_given) {
            addFragment(SamplesIssuedInSession.getInstance(routePlanNumber));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}