package com.field.datamatics.views.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.apimodels.ProductQuatityModel;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Product;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.GPSTracker;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.MyPagerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jith on 21/10/2015.
 * Product details screen logic
 */
public class ProductDetails extends BaseFragment implements OnClickListener {


    private TextView tvProductNumber;
    private TextView tvDescription;
    private TextView tvNarration;
    private TextView tvSamplesGiven;
    private TextView lblNarration;

    private Product productDetails;
    private static ProductDetails mInstance;
    private int routePlanNumber;
    private Calendar visitedDate;

    private int pdctQuantityCount = 1;

    private MainActivity mContainer;
    private ProductSample sample = null;
    private GPSTracker gps;
    private String corndnt_in, cordnt_out;
    private String mrNarration;
    View view;

    public ProductDetails() {
    }

    public static ProductDetails getInstance(Product product, int routePlanNumber, Calendar visitedDate) {
        mInstance = new ProductDetails();
        mInstance.productDetails = product;
        mInstance.routePlanNumber = routePlanNumber;
        mInstance.visitedDate = visitedDate;
        return mInstance;
    }

    public static ProductDetails getInstance(Product product, ProductSample sample, int routePlanNumber, Calendar visitedDate) {
        mInstance = new ProductDetails();
        mInstance.productDetails = product;
        mInstance.routePlanNumber = routePlanNumber;
        mInstance.visitedDate = visitedDate;
        mInstance.sample = sample;
        return mInstance;
    }

    public static ProductDetails getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gps = new GPSTracker(getActivity());
        mContainer = (MainActivity) getActivity();
        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume()
                .edit();
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        if (MrActions.fragment_use.equals("SAMPLE"))
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_PRODUCT_SAMPLE_DETAIL);
        else if (MrActions.fragment_use.equals("PRODUCT"))
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_PRODUCT_DETAIL);

        edit.commit();
        addFragmentTitle("ProductDetails");
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_full_details_of_product, container, false);
        tvProductNumber = (TextView) view.findViewById(R.id.tv_product_number);
        tvDescription = (TextView) view.findViewById(R.id.tv_product_desc);
        tvNarration = (TextView) view.findViewById(R.id.tv_product_narration);
        tvSamplesGiven = (TextView) view.findViewById(R.id.tv_product_quantity);
        lblNarration = (TextView) view.findViewById(R.id.lbl_narration);
        tvNarration.setText(productDetails.Narration);
        if (!MrActions.fragment_use.equals("SAMPLE")) {
            tvSamplesGiven.setVisibility(View.GONE);
            TextView tv_sample = (TextView) view.findViewById(R.id.tv_sample);
            TextView tv_dot = (TextView) view.findViewById(R.id.tv_dot);
            tv_sample.setVisibility(View.GONE);
            tv_dot.setVisibility(View.GONE);

        } else {
            // lblNarration.setText("MR Narration");
            new AsyncTask<Void, Void, Void>() {
                ProgressDialog pd;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Please wait a moment!");
                    pd.setCancelable(false);
                    pd.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (pd.isShowing()) pd.dismiss();
                    if (sample != null) {
                        tvSamplesGiven.setText(Integer.toString(sample.quantity));
                        mrNarration = sample.narration;
                        // tvNarration.setText(sample.narration == null ? "" : sample.narration);
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    sample = new Select().from(ProductSample.class)
                            .where(Condition.column(ProductSample$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlanNumber))
                            .and(Condition.column(ProductSample$Table.PRODUCT_PRODUCT_NUMBER).eq(productDetails.Product_Number))
                            .querySingle();
                    return null;
                }
            }.execute();
        }
        showData();

        view.findViewById(R.id.btnMore).setOnClickListener(this);
        File file = new File(Constants.PRODUCT_DOCS);
        if (!file.exists())
            file.mkdirs();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Documents"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), productDetails.Product_Number, productDetails.Category, routePlanNumber, visitedDate);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (MrActions.fragment_use.equals("SAMPLE"))
            inflater.inflate(R.menu.menu_product, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_introduce_sample) {
            final String startCoordinates;
            final String startTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            Location loc = mContainer.mLastLocation;
            if (loc != null) {
                startCoordinates = loc.getLatitude() + "," + loc.getLongitude();
            } else {
                startCoordinates = "";
            }
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_product_sample);
            dialog.setTitle("Product Sample");
            AppCompatButton btnDone = (AppCompatButton) dialog.findViewById(R.id.button_done);
            Spinner sp = (Spinner) dialog.findViewById(R.id.sp);
            final EditText edtNarration = (EditText) dialog.findViewById(R.id.edt_narration);
            final EditText edtQuantity = (EditText) dialog.findViewById(R.id.edt_quantity);
            if (sample != null) {
                String quantity = Integer.toString(sample.quantity);
                edtQuantity.setText(quantity);
                edtQuantity.setSelection(quantity.length());
                if (sample.narration == null) sp.setSelection(0);
                else {
                    edtNarration.setText(sample.narration);
                    edtNarration.setSelection(sample.narration.length());
                    sp.setSelection(1);
                }
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

            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quantityText = edtQuantity.getText().toString();
                    if (TextUtils.isEmpty(quantityText)) {
                        edtQuantity.setError("Enter a value");
                        return;
                    }
                    boolean isUpdate = true;
                    if (sample == null) {
                        isUpdate = false;
                        sample = new ProductSample();
                    }
                    sample.product = productDetails;
                    sample.category = productDetails.Category;
                    sample.routePlan = new RoutePlan();
                    sample.routePlan.Route_Plan_Number = routePlanNumber;
                    sample.date = Utilities.dateInSqliteFormat(Calendar.getInstance());
                    sample.visited_date = sample.date;
                    int quantity = Integer.parseInt(quantityText);
                    sample.quantity = quantity;
                    sample.narration = isDoctor[0] ? null : edtNarration.getText().toString();
                    Location loc = mContainer.mLastLocation;
                    String endCoordinates = "";
                    if (loc != null) {
                        endCoordinates = loc.getLatitude() + "," + loc.getLongitude();
                    }
                    sample.startCoordinates = startCoordinates;
                    sample.endCoordinates = endCoordinates;
                    String endTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
                    sample.startTime = startTime;
                    sample.endTime = endTime;
                    if (isUpdate) sample.update();
                    else
                        sample.save();
                    dialog.dismiss();
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                    String json = gson.toJson(sample);
                    AppControllerUtil.getPrefsResume().edit().putString(Constants.VAL_SAMPLES, json).commit();
                    tvSamplesGiven.setText(Integer.toString(quantity));
                    tvNarration.setText(sample.narration);

                    //prepare data for sending to server
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        corndnt_in = latitude + "," + longitude;
                        cordnt_out = latitude + "," + longitude;
                    } else {
                        corndnt_in = startCoordinates;
                        cordnt_out = endCoordinates;
                    }
                }
            };
            btnDone.setOnClickListener(onClickListener);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showData() {
        tvProductNumber.setText(productDetails.Product_Number + "");
        tvDescription.setText(productDetails.Product_Description);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMore) {
            addFragment(ProductInfo.getInstance(productDetails, mrNarration));
            addFragmentTitle("ProductInfo");
        }
    }
}
