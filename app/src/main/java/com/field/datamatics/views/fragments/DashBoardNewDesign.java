package com.field.datamatics.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.AppDatabase;
import com.field.datamatics.database.CustomProductList;
import com.field.datamatics.database.DataBase;
import com.field.datamatics.database.Product;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.eventbus.AreaCoverageEvent;
import com.field.datamatics.eventbus.ChartSamplesCatEvent;
import com.field.datamatics.eventbus.ChartSamplesEvent;
import com.field.datamatics.eventbus.MediaSizeEvent;
import com.field.datamatics.eventbus.PerformanceChartEvent;
import com.field.datamatics.eventbus.ReminderEvent;
import com.field.datamatics.eventbus.ScoreEvent;
import com.field.datamatics.eventbus.TimeSpentEvent;
import com.field.datamatics.eventbus.VisitByClientEvent;
import com.field.datamatics.eventbus.VisitDoneEvent;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.interfaces.ProductSelectedFinishedListener;
import com.field.datamatics.models.DashboardModel;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.utils.NetworkStatusUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.DialogProducts;
import com.field.datamatics.views.DownloadProductListActivity;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.DashBoardAdapter;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by anoop on 7/10/15.
 */
public class DashBoardNewDesign extends BaseFragment implements View.OnClickListener, ProductSelectedFinishedListener {
    private GridView grid;
    private DashBoardAdapter adapter;
    private ArrayList<DashboardModel> data = new ArrayList<>();
    private Fragment fragment = null;
    private long start = 0;

    private LineChart lineChart;
    private FancyButton btnScheduleVsActual;
    private FancyButton btnPending;
    private FancyButton btnSamples;
    private FancyButton btnScorecard;
    private TextView btnMoreReminder;
    private TextView txtMediaSize;
    private LinearLayout layoutReminder;
    private TextView emptyView;
    private DonutProgress pgMonthlyScore;
    private DonutProgress pgTodayScore;

    private static DashBoardNewDesign mInstance;
    private View mView = null;

    private MainActivity mContainer;
    private volatile boolean hasScoreLoaded, hasChartLoaded, hasReminderLoaded,
            hasMediaSizeLoaded, hasTimeSpentLoaded,
            hasSampleChartLoaded, hasSampleChartCatLoaded, hasVisitByClientLoaded;

    private RelativeLayout pgPerformance;
    private RadioButton rbPerfDaily, rbPerfMonth;
    //private RadioGroup rgPerformance;

    //time spent graph
    private BarChart timeSpentChart;
    private RelativeLayout progressTimeSpent;
    private TextView txt_last_sync_date;

    //visits by client
    private BarChart visitsByClientChart;
    private RelativeLayout progressVisitsByClient;

    //for samples' graph
    private HorizontalBarChart samplesChart;
    private RelativeLayout pgSample;
    private RadioButton rbSampleDaily, rbSampleMonth;
    private RadioGroup rgSample;
    private FancyButton btnChooseProduct;
    private BarData chartDataSampleMonth;
    private BarData chartDataSampleDaily;

    //for samples' graph by category
    private HorizontalBarChart samplesChartCat;
    private RelativeLayout pgSampleCat;
    private RadioButton rbSampleCatDaily, rbSampleCatMonth;
    private RadioGroup rgSampleCat;
    private FancyButton btnChooseCat;
    private BarData chartDataSampleCatMonth;
    private BarData chartDataSampleCatDaily;

    private RadioButton rbMonthly, rbDaily;
    private volatile boolean isDaily = false;

    private final int CHART_ANIM_DURATION = 800;

    private ArrayList<CustomProductList> selectedProducts;
    private ArrayList<CustomProductList> selectedBrands;

    private LabelValueFormatter formatter;

    public DashBoardNewDesign() {
    }

    public static Fragment getInstance() {
        if (mInstance == null) {
            mInstance = new DashBoardNewDesign();
        }
        return mInstance;
    }

    public static Fragment getNewInstance() {
        mInstance = new DashBoardNewDesign();
        mInstance.mView = null;
        return mInstance;
    }

    public static void clearInstance() {
        mInstance = null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = (MainActivity) getActivity();
        mContainer.setNavigationChecked(R.id.nav_dashboard);
        View view = inflater.inflate(R.layout.dashboard_new, container, false);
        setHasOptionsMenu(true);
        setTitle("Dashboard");
        addFragmentTitle("DashBoard");
        setNavigationDrawer(R.id.nav_dashboard);

        if (mView != null) return mView;
        AppControllerUtil.getInstance().setIsCheckIn(false);
        AppControllerUtil.getInstance().setIsSessionStart(false);

        formatter = new LabelValueFormatter();
        initializeViews(view);
        configureLineChart();
        configureSampleChart();
        configureSampleCatChart();
        configureTimeSpentChart();
        configureVisitByClientChart();
        mView = view;
        return view;
    }

    private void initializeViews(View view) {
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        btnScheduleVsActual = (FancyButton) view.findViewById(R.id.btnSheduleVsActual);
        btnPending = (FancyButton) view.findViewById(R.id.btnPending);
        btnSamples = (FancyButton) view.findViewById(R.id.btnSamples);
        btnScorecard = (FancyButton) view.findViewById(R.id.btnScoreCard);
        btnMoreReminder = (TextView) view.findViewById(R.id.txtBtnMore);
        txtMediaSize = (TextView) view.findViewById(R.id.txtMediaSize);
        layoutReminder = (LinearLayout) view.findViewById(R.id.layoutReminder);
        pgMonthlyScore = (DonutProgress) view.findViewById(R.id.pgMothlyScore);
        pgTodayScore = (DonutProgress) view.findViewById(R.id.pgTodaysVisit);
        emptyView = (TextView) view.findViewById(R.id.emptyReminder);
        rbPerfDaily = (RadioButton) view.findViewById(R.id.rbDailyPerf);
        rbPerfMonth = (RadioButton) view.findViewById(R.id.rbMonthPerf);
        pgPerformance = (RelativeLayout) view.findViewById(R.id.progressPerformance);
        //rgPerformance = (RadioGroup) view.findViewById(R.id.rgPerformance);

        samplesChart = (HorizontalBarChart) view.findViewById(R.id.sampleChart);
        rbSampleDaily = (RadioButton) view.findViewById(R.id.rbDailySample);
        rbSampleMonth = (RadioButton) view.findViewById(R.id.rbMonthSample);
        pgSample = (RelativeLayout) view.findViewById(R.id.progressSamples);
        rgSample = (RadioGroup) view.findViewById(R.id.rgSample);
        btnChooseProduct = (FancyButton) view.findViewById(R.id.btnChooseProducts);

        samplesChartCat = (HorizontalBarChart) view.findViewById(R.id.sampleChartCat);
        rbSampleCatDaily = (RadioButton) view.findViewById(R.id.rbDailySampleCat);
        rbSampleCatMonth = (RadioButton) view.findViewById(R.id.rbMonthSampleCat);
        pgSampleCat = (RelativeLayout) view.findViewById(R.id.progressSamplesCat);
        rgSampleCat = (RadioGroup) view.findViewById(R.id.rgSampleCat);
        btnChooseCat = (FancyButton) view.findViewById(R.id.btnChooseCategory);

        rbDaily = (RadioButton) view.findViewById(R.id.rbDaily);
        rbMonthly = (RadioButton) view.findViewById(R.id.rbMonthly);

        timeSpentChart = (BarChart) view.findViewById(R.id.chartTimeSpent);
        progressTimeSpent = (RelativeLayout) view.findViewById(R.id.progressTimeSpent);

        visitsByClientChart = (BarChart) view.findViewById(R.id.chartVisitByClient);
        progressVisitsByClient = (RelativeLayout) view.findViewById(R.id.progressVisitByClient);
        txt_last_sync_date = (TextView) view.findViewById(R.id.txt_last_sync_date);

        btnScheduleVsActual.setOnClickListener(this);
        btnPending.setOnClickListener(this);
        btnSamples.setOnClickListener(this);
        btnScorecard.setOnClickListener(this);
        btnMoreReminder.setOnClickListener(this);
        rbPerfDaily.setOnClickListener(this);
        rbPerfMonth.setOnClickListener(this);
        rbDaily.setOnClickListener(this);
        rbMonthly.setOnClickListener(this);

        btnChooseProduct.setOnClickListener(this);
        rbSampleDaily.setOnClickListener(this);
        rbSampleMonth.setOnClickListener(this);

        btnChooseCat.setOnClickListener(this);
        rbSampleCatDaily.setOnClickListener(this);
        rbSampleCatMonth.setOnClickListener(this);
        if (PreferenceUtil.getIntsance().getLastSyncDate() != null)
            txt_last_sync_date.setText(PreferenceUtil.getIntsance().getLastSyncDate());
    }

    @Override
    public void onClick(View v) {
        Fragment f = null;
        if (v == btnScheduleVsActual) showDialog();
        else if (v == btnPending) f = PendingTask.getInstance("PendingVisit");
        else if (v == btnSamples) f = SamplesIssuedFragment.getInstance();
        else if (v == btnScorecard) f = new ScoreCard();
        else if (v == btnMoreReminder) f = new DashBoardRemainderList();
        else if (v == btnChooseProduct) {
            new DialogProducts(mContainer, DashBoardNewDesign.this, DialogProducts.TYPE_PRODUCT_NUM, selectedProducts).showDialogProductNumber();
        } else if (v == btnChooseCat) {
            new DialogProducts(mContainer, DashBoardNewDesign.this, DialogProducts.TYPE_CATEGORY, selectedBrands).showDialogProductNumber();
        } else if (v == rbPerfDaily) {
            lineChart.setData(mContainer.chartDataDailyPerformance);
            lineChart.animateX(CHART_ANIM_DURATION);
        } else if (v == rbDaily) {
            isDaily = true;
            switchGraphs();
        } else if (v == rbMonthly) {
            isDaily = false;
            switchGraphs();
        }
        if (f != null)
            addFragment(f);
    }

    private void configureLineChart() {

        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setNoDataText(" ");
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(getActivity(), Math.round(e.getVal()) + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setTypeface(mTf);
        //leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = lineChart.getAxisRight();
        // rightAxis.setTypeface(mTf);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        Legend lh = lineChart.getLegend();

        lh.setWordWrapEnabled(true);
        lh.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        lh.setFormSize(8f);
        lh.setFormToTextSpace(4f);
        lh.setXEntrySpace(6f);

        leftAxis.setValueFormatter(formatter);

    }

    private void configureVisitByClientChart() {
        visitsByClientChart.setDescription("");
        XAxis xAxis = visitsByClientChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = visitsByClientChart.getAxisLeft();
        yAxis.setAxisMinValue(0f);
        //visitsByClientChart.setNoDataText("No data available yet!");
        visitsByClientChart.getAxisRight().setEnabled(false);
        visitsByClientChart.setDrawValueAboveBar(false);
        Legend lh = visitsByClientChart.getLegend();

        lh.setWordWrapEnabled(true);
        lh.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        lh.setFormSize(8f);
        lh.setFormToTextSpace(4f);
        lh.setXEntrySpace(6f);

        yAxis.setValueFormatter(formatter);
    }

    private void configureSampleChart() {
        samplesChart.setDescription("");
        XAxis xAxis = samplesChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        samplesChart.getAxisLeft().setEnabled(false);
        samplesChart.setDrawValueAboveBar(false);
        Legend lh = samplesChart.getLegend();

        YAxis yAxis = samplesChart.getAxisLeft();
        lh.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        // lh.setFormSize(8f);
        //  lh.setFormToTextSpace(4f);
        lh.setXEntrySpace(6f);
        lh.setYEntrySpace(0f);
        lh.setYOffset(0f);
        lh.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        lh.setWordWrapEnabled(true);

        yAxis.setValueFormatter(formatter);
    }

    private void configureSampleCatChart() {
        samplesChartCat.setDescription("");
        XAxis xAxis = samplesChartCat.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        samplesChartCat.getAxisLeft().setEnabled(false);
        samplesChartCat.setDrawValueAboveBar(false);
        Legend lh = samplesChartCat.getLegend();

        YAxis yAxis = samplesChartCat.getAxisLeft();
        lh.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        // lh.setFormSize(8f);
        //  lh.setFormToTextSpace(4f);
        lh.setXEntrySpace(6f);
        lh.setYEntrySpace(0f);
        lh.setYOffset(0f);
        lh.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        lh.setWordWrapEnabled(true);

        yAxis.setValueFormatter(formatter);
    }


    private void configureTimeSpentChart() {
        timeSpentChart.setDescription("");
        XAxis xAxis = timeSpentChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        timeSpentChart.getAxisRight().setEnabled(false);
        timeSpentChart.setDrawValueAboveBar(false);
        timeSpentChart.getLegend().setEnabled(false);
    }

    private void switchGraphs() {
        /**.......Changes 03/01/16 Start.................*/
        if (isDaily) {
            timeSpendFragment(false);
            visitDoneFragment(false);
            areaCoverageFragment(false);
        } else {
            timeSpendFragment(true);
            visitDoneFragment(true);
            areaCoverageFragment(true);
        }
        /**.......Changes 03/01/16 End.................*/
        if (hasChartLoaded) {
            if (isDaily) {
                lineChart.setData(mContainer.chartDataDailyPerformance);
            } else {
                lineChart.setData(mContainer.chartDataMonthPerformance);
            }
            lineChart.animateX(CHART_ANIM_DURATION);
        }
        if (hasSampleChartLoaded) {
            if (isDaily) {
                samplesChart.setData(chartDataSampleDaily);
            } else {
                samplesChart.setData(chartDataSampleMonth);
            }
            samplesChart.animateY(CHART_ANIM_DURATION);
        }
        if (hasSampleChartCatLoaded) {
            if (isDaily) {
                samplesChartCat.setData(chartDataSampleCatDaily);
            } else {
                samplesChartCat.setData(chartDataSampleCatMonth);
            }
            samplesChartCat.animateY(CHART_ANIM_DURATION);
        }
        if (hasTimeSpentLoaded) {
            if (isDaily) timeSpentChart.setData(mContainer.dataTimeSpentDaily);
            else
                timeSpentChart.setData(mContainer.dataTimeSpentMonthly);
            timeSpentChart.animateX(CHART_ANIM_DURATION);
        }
        if (hasVisitByClientLoaded) {
            if (isDaily) {
                visitsByClientChart.setData(mContainer.dataVisitsByClientDaily);
            } else {
                visitsByClientChart.setData(mContainer.dataVisitsByClientMonthly);
            }
            visitsByClientChart.animateY(CHART_ANIM_DURATION);
        }
    }

    @Override
    public void onProductSelectedFinished(final ArrayList<CustomProductList> selectedProduct, int type) {
        if (selectedProduct == null || selectedProduct.size() == 0) {
            //Toast.makeText(mContainer, "No Products has been selected!", Toast.LENGTH_SHORT).show();
            if (type == DialogProducts.TYPE_PRODUCT_NUM) {
                samplesChart.setVisibility(View.GONE);
                selectedProducts = null;
            } else {
                selectedBrands = null;
                samplesChartCat.setVisibility(View.GONE);
            }
            return;
        }
        if (type == DialogProducts.TYPE_PRODUCT_NUM) {
            new LoadSamplesByProduct(selectedProduct).execute();
        } else {
            new LoadSamplesByBrand(selectedProduct).execute();
        }
    }

    private int[] getColors(int stacksize) {
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = Constants.CHART_COLORS[i];
        }

        return colors;
    }

    private class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {

            if (value > 0)
                return mFormat.format(value);
            else
                return "";
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mr_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_synct) {
            if (NetworkStatusUtil.getInstance().isNetworkAvailable())
                syncFromServer();
            else
                Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();

        } else if (item.getItemId() == R.id.action_sync_to_server) {
            if (NetworkStatusUtil.getInstance().isNetworkAvailable())
                syncToServer();
            else
                Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDummyData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait....");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                getActivity().deleteDatabase(AppDatabase.NAME);
                try {
                    DataBase.createDummyData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                addFragment(new DashBoardNewDesign());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Select().from(Product.class).queryList();
                    }
                }).start();
            }
        }.execute();
    }

    private static String dateFrom;
    private static String dateTo;
    private static int type;
    private static EditText edtDateFrom;
    private static EditText edtDateTo;
    private int filterType;

    private static Date fromDate;
    private static Date toDate;

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_schedule_vs_actual);
        dialog.setTitle("Schedule vs Actual");

        edtDateFrom = (EditText) dialog.findViewById(R.id.edt_date_from);
        edtDateTo = (EditText) dialog.findViewById(R.id.edt_date_to);
        Spinner spFilterType = (Spinner) dialog.findViewById(R.id.sp_filter_type);
        AppCompatButton btnOk = (AppCompatButton) dialog.findViewById(R.id.btn_done);

        spFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        fromDate = toDate = calendar.getTime();
        dateFrom = Utilities.dateToString(calendar, "yyyy-MM-dd");
        String today = Utilities.dateToString(calendar, "MMM dd, yyyy");
        dateTo = Utilities.dateToString(calendar, "yyyy-MM-dd");
        edtDateFrom.setText(today);
        edtDateTo.setText(today);
        edtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
                type = 1;
            }
        });
        edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
                type = 2;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDate.after(toDate)) {
                    edtDateTo.setError("Should not be less than 'from date'");
                    Toast.makeText(getContext(), "Should not be less than 'from date'", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                addFragment(Schedule_vs_ActualFragment.getInstance(true, dateFrom, dateTo, filterType));
            }
        });
        dialog.show();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            if (type == 1) {
                dateFrom = Utilities.dateToString(calendar, "yyyy-MM-dd");
                edtDateFrom.setText(Utilities.dateToString(calendar, "MMM dd, yyyy"));
                fromDate = calendar.getTime();
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dateTo = Utilities.dateToString(calendar, "yyyy-MM-dd");
                edtDateTo.setText(Utilities.dateToString(calendar, "MMM dd, yyyy"));
                toDate = calendar.getTime();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mContainer.isFirstTime) {
//            mContainer.isFirstTime = false;
//            return;
//        }
        if (!hasReminderLoaded) mContainer.shouldUpdateReminder = true;
        if (!hasChartLoaded) mContainer.shouldUpdatePerformanceGraph = true;
        if (!hasScoreLoaded) mContainer.shouldUpdateScore = true;
        if (!hasMediaSizeLoaded) mContainer.shouldUpdateMediaSize = true;
        if (!hasTimeSpentLoaded) mContainer.shouldUpdateTimeSpent = true;
        mContainer.refresh();

        if (PreferenceUtil.getIntsance().getLastSyncDate() != null)
            txt_last_sync_date.setText(PreferenceUtil.getIntsance().getLastSyncDate());
    }


    public void onEventMainThread(ReminderEvent event) {

        hasReminderLoaded = true;
        LayoutInflater inflater = LayoutInflater.from(mContainer);
        layoutReminder.removeAllViews();
        //set Reminder
        if (mContainer.reminders == null || mContainer.reminders.size() == 0) {
            View v = inflater.inflate(R.layout.empty_view, null);
            layoutReminder.addView(v);
        } else {
            for (Reminder r : mContainer.reminders) {
                View v = inflater.inflate(R.layout.item_reminder_dashboard, null);
                TextView desc = (TextView) v.findViewById(R.id.txtDescription);
                TextView when = (TextView) v.findViewById(R.id.txtWhen);
                desc.setText(r.message);
                when.setText(Utilities.dateToString(r.date, "yyyy-MM-dd HH:mm", "MMM dd,yyyy hh:mm a"));
                layoutReminder.addView(v);
            }
        }
        //  setProgressDialog();
    }

    public void onEventMainThread(ScoreEvent event) {
        //set score
        hasScoreLoaded = true;
        pgMonthlyScore.setProgress(mContainer.monthlyScore);
        pgTodayScore.setProgress(mContainer.todaysScore);
        //    setProgressDialog();
    }

    public void onEventMainThread(MediaSizeEvent event) {
        //set score
        hasMediaSizeLoaded = true;
        txtMediaSize.setText(mContainer.mediaSize);
        //  setProgressDialog();
    }

    /**
     * .......Changes 03/01/16 Start.................
     */
    public void onEventMainThread(TimeSpentEvent event) {
        //set score
        hasTimeSpentLoaded = true;
        progressTimeSpent.setVisibility(View.GONE);
        if (isDaily) timeSpentChart.setData(mContainer.dataTimeSpentDaily);
        else
            timeSpentChart.setData(mContainer.dataTimeSpentMonthly);
        timeSpentChart.animateY(CHART_ANIM_DURATION);
        if (isDaily)
            timeSpendFragment(false);
        else
            timeSpendFragment(true);
        // setProgressDialog();
    }

    /**
     * .......Changes 03/01/16 End.................
     */

    public void onEventMainThread(PerformanceChartEvent event) {

        pgPerformance.setVisibility(View.GONE);
        hasChartLoaded = true;
        setProgressDialog();
        if (isDaily) lineChart.setData(mContainer.chartDataDailyPerformance);
        else
            lineChart.setData(mContainer.chartDataMonthPerformance);
        lineChart.highlightValues(null);
        lineChart.animateX(CHART_ANIM_DURATION);
    }

    public void onEventMainThread(VisitByClientEvent event) {
        progressVisitsByClient.setVisibility(View.GONE);
        hasVisitByClientLoaded = true;
        setProgressDialog();
        visitsByClientChart.getLegend().setCustom(getColors(event.prefixes.length), event.prefixes);
        // if (mContainer.dataVisitsByClientMonthly.getDataSetCount() > 0) {
        if (isDaily) {
            visitsByClientChart.setData(mContainer.dataVisitsByClientDaily);
        } else {
            visitsByClientChart.setData(mContainer.dataVisitsByClientMonthly);
        }
        visitsByClientChart.highlightValues(null);
        visitsByClientChart.animateY(CHART_ANIM_DURATION);
        //   }
    }

    private class LabelValueFormatter implements YAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
//            try {
//                String numberD = String.valueOf(value);
//                numberD = numberD.substring(numberD.indexOf(".") + 1);
//                int val = Integer.parseInt(numberD);
//                if (val > 0) return "";
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
            return Math.round(value) + "";
        }
    }

    public void onEventMainThread(ChartSamplesEvent event) {
        pgSample.setVisibility(View.GONE);
        samplesChart.setVisibility(View.VISIBLE);
        // rgSample.setVisibility(View.VISIBLE);
        btnChooseProduct.setEnabled(true);
        if (event.labels.length > 0)
            samplesChart.getLegend().setCustom(getColors(event.labels.length), event.labels);
        if (isDaily) samplesChart.setData(chartDataSampleDaily);
        else
            samplesChart.setData(chartDataSampleMonth);
        samplesChart.highlightValues(null);
        samplesChart.animateY(CHART_ANIM_DURATION);
        hasSampleChartLoaded = true;
        setProgressDialog();
    }

    public void onEventMainThread(ChartSamplesCatEvent event) {
        pgSampleCat.setVisibility(View.GONE);
        samplesChartCat.setVisibility(View.VISIBLE);
        // rgSampleCat.setVisibility(View.VISIBLE);
        btnChooseCat.setEnabled(true);
        if (event.labels.length > 0)
            samplesChartCat.getLegend().setCustom(getColors(event.labels.length), event.labels);
        if (isDaily) samplesChartCat.setData(chartDataSampleCatDaily);
        else
            samplesChartCat.setData(chartDataSampleCatMonth);
        samplesChartCat.highlightValues(null);
        samplesChartCat.animateY(CHART_ANIM_DURATION);
        hasSampleChartCatLoaded = true;
        setProgressDialog();
    }

    /**
     * .......Changes 03/01/16 Start.................
     */
    public void onEventMainThread(VisitDoneEvent event) {
        if (isDaily)
            visitDoneFragment(false);
        else
            visitDoneFragment(true);
        // setProgressDialog();
    }

    public void onEventMainThread(AreaCoverageEvent event) {
        if (isDaily)
            areaCoverageFragment(false);
        else
            areaCoverageFragment(true);
        //  setProgressDialog();
    }

    private void timeSpendFragment(boolean type) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_time, TimeSpendGraph.getInstance(type));
        fragmentTransaction.commit();
    }

    private void visitDoneFragment(boolean type) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_visit_done, VisitDoneGraph.getInstance(type));
        fragmentTransaction.commit();
    }

    private void areaCoverageFragment(boolean type) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_area, com.field.datamatics.views.fragments.AreaCoverageGraph.getInstance(type));
        fragmentTransaction.commit();
    }

    /**
     * .......Changes 03/01/16 End.................
     */

    private void syncToServer() {
        DialogUtil.getInstance().getDialog(getActivity(),
                "Sync", "Do you want to send data to server? If yes press Ok.", new DialogCallBacks() {
                    @Override
                    public void onOk() {
                        PreferenceUtil.getIntsance().setLastSyncDate(Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd hh:mm a"));
                        Bundle bundle = new Bundle();
                        bundle.putString("syncType", "PUSH");
                        Intent intent = new Intent(getActivity(), DownloadProductListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


    }

    private void syncFromServer() {
        DialogUtil.getInstance().getDialog(getActivity(),
                "Sync", "Do you want to fetch data from server? If yes press Ok.", new DialogCallBacks() {
                    @Override
                    public void onOk() {
                        PreferenceUtil.getIntsance().setLastSyncDate(Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd hh:mm a"));
                        //loadDummyData();
                        Bundle bundle = new Bundle();
                        bundle.putString("isSyncAllData", "all");
                        bundle.putString("syncType", "PULL");
                        Intent intent = new Intent(getActivity(), DownloadProductListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


    }

    private void setProgressDialog() {
        if (mContainer.progressDialog.isShowing()) {
            mContainer.progressDialog.dismiss();
            Log.i("FDM", "dialog dismissed");
        }

    }

    private class LoadSamplesByProduct extends AsyncTask<Void, Void, String> {

        ArrayList<CustomProductList> selectedProduct;
        ChartSamplesEvent event;

        public LoadSamplesByProduct(ArrayList<CustomProductList> selectedProduct) {
            this.selectedProduct = selectedProduct;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mContainer.progressDialog.isShowing()) mContainer.progressDialog.show();
            selectedProducts = selectedProduct;
            hasSampleChartLoaded = false;
            pgSample.setVisibility(View.VISIBLE);
            samplesChart.setVisibility(View.GONE);
            btnChooseProduct.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mContainer.progressDialog.isShowing()) mContainer.progressDialog.dismiss();
            onEventMainThread(event);
        }

        @Override
        protected String doInBackground(Void... params) {
            {
                final String[] projection = new String[selectedProduct.size()];
                int i = 0;
                for (CustomProductList p : selectedProduct) {
                    projection[i++] = p.product;
                }
                Log.i("FDM", "Loading sample chart");
                Calendar calendar = Calendar.getInstance();
                List<ProductSample> items = new Select().from(ProductSample.class)
                        .where("strftime('%m', visited_date) = ? and strftime('%Y', visited_date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .and(Condition.column(ProductSample$Table.PRODUCT_PRODUCT_NUMBER).in("", projection))
                        .orderBy(ProductSample$Table.PRODUCT_PRODUCT_NUMBER)
                        .queryList();
                //x values
                ArrayList<String> xVals1 = new ArrayList<String>();
                xVals1.add("week-1");
                xVals1.add("week-2");
                xVals1.add("week-3");
                xVals1.add("week-4");

                //y values
                int size = selectedProduct.size();
                ArrayList<float[]> vals = new ArrayList<>();
                for (i = 0; i < 4; i++) {
                    vals.add(new float[size]);
                }
                List<String> productNames = Arrays.asList(projection);
                for (ProductSample p : items) {
                    int d = Integer.parseInt(Utilities.dateToString(p.visited_date, "yyyy-MM-dd", "dd"));
                    if (d < 8) {
                        vals.get(0)[productNames.indexOf(p.product.Product_Number)] += p.quantity;
                    } else if (d < 15) {
                        vals.get(1)[productNames.indexOf(p.product.Product_Number)] += p.quantity;
                    } else if (d < 22) {
                        vals.get(2)[productNames.indexOf(p.product.Product_Number)] += p.quantity;
                    } else if (d < 32) {
                        vals.get(3)[productNames.indexOf(p.product.Product_Number)] += p.quantity;
                    }
                }
                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                for (i = 0; i < vals.size(); i++) {
                    yVals1.add(new BarEntry(vals.get(i), i));
                }

                BarDataSet set1 = new BarDataSet(yVals1, "");
                set1.setColors(getColors(size));
                set1.setStackLabels(projection);
                ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
                dataSets1.add(set1);

                chartDataSampleMonth = new BarData(xVals1, dataSets1);
                chartDataSampleMonth.setValueFormatter(new MyValueFormatter());

                //load daily chart
                Calendar today = Calendar.getInstance();
                Calendar lastWeek = Calendar.getInstance();
                if (lastWeek.get(Calendar.DAY_OF_MONTH) < 8) {
                    lastWeek.add(Calendar.DAY_OF_MONTH, 1 - lastWeek.get(Calendar.DAY_OF_MONTH));
                } else
                    lastWeek.add(Calendar.DAY_OF_MONTH, -6);
                items = new Select().from(ProductSample.class)
                        .where(Condition.column(ProductSample$Table.VISITED_DATE).between(Utilities.dateToString(lastWeek, "yyyy-MM-dd"))
                                .and(Utilities.dateToString(today, "yyyy-MM-dd")))
                        .and(Condition.column(ProductSample$Table.PRODUCT_PRODUCT_NUMBER).in("", projection))
                        .orderBy(ProductSample$Table.PRODUCT_PRODUCT_NUMBER)
                        .queryList();
                log("size :" + items.size());
                xVals1 = new ArrayList<>();
                int thisDay = Integer.parseInt(Utilities.dateToString(today, "dd"));
                int lastWeekDay = Integer.parseInt(Utilities.dateToString(lastWeek, "dd"));
                int numXvals = thisDay - lastWeekDay + 1;
                for (i = lastWeekDay; i <= thisDay; i++) {
                    xVals1.add(Integer.toString(i));
                    log(Integer.toString(i));
                }
                vals = new ArrayList<>();
                for (i = 0; i < numXvals; i++) {
                    vals.add(new float[size]);
                }

                for (ProductSample p : items) {
                    int d = Integer.parseInt(Utilities.dateToString(p.visited_date, "yyyy-MM-dd", "dd"));
                    int dif = d - lastWeekDay;
                    vals.get(dif)[productNames.indexOf(p.product.Product_Number)] += p.quantity;
                }
                yVals1 = new ArrayList<>();
                yVals1 = new ArrayList<>();
                for (i = 0; i < vals.size(); i++) {
                    yVals1.add(new BarEntry(vals.get(i), i));
                }

                set1 = new BarDataSet(yVals1, "");
                if (size > 0)
                    set1.setColors(getColors(size));
                dataSets1 = new ArrayList<>();
                dataSets1.add(set1);

                chartDataSampleDaily = new BarData(xVals1, dataSets1);
                chartDataSampleDaily.setValueFormatter(new MyValueFormatter());

                event = new ChartSamplesEvent(projection);

                return null;
            }
        }
    }

    private class LoadSamplesByBrand extends AsyncTask<Void, Void, String> {

        ArrayList<CustomProductList> selectedProduct;
        ChartSamplesCatEvent event;

        public LoadSamplesByBrand(ArrayList<CustomProductList> selectedProduct) {
            this.selectedProduct = selectedProduct;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mContainer.progressDialog.isShowing()) mContainer.progressDialog.show();
            selectedBrands = selectedProduct;
            hasSampleChartCatLoaded = false;
            pgSampleCat.setVisibility(View.VISIBLE);
            samplesChartCat.setVisibility(View.GONE);
            btnChooseCat.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mContainer.progressDialog.isShowing()) mContainer.progressDialog.dismiss();
            onEventMainThread(event);
        }

        @Override
        protected String doInBackground(Void... params) {
            final String[] projection = new String[selectedProduct.size()];
            int i = 0;
            for (CustomProductList p : selectedProduct) {
                projection[i++] = p.product;
            }
            Log.i("FDM", "loading sample chart by category");
            Calendar calendar = Calendar.getInstance();
            List<ProductSample> items = new Select().from(ProductSample.class)
                    .where("strftime('%m', visited_date) = ? and strftime('%Y', visited_date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                    .and(Condition.column(ProductSample$Table.CATEGORY).in("", projection))
                    .orderBy(ProductSample$Table.CATEGORY)
                    .queryList();
            Log.i("FDM", "size : " + items.size());
            //x values
            ArrayList<String> xVals1 = new ArrayList<String>();
            xVals1.add("week-1");
            xVals1.add("week-2");
            xVals1.add("week-3");
            xVals1.add("week-4");

            //y values
            int size = selectedProduct.size();
            float f1[] = new float[size];
            float f2[] = new float[size];
            float f3[] = new float[size];
            float f4[] = new float[size];
            List<String> categoryNames = Arrays.asList(projection);
            for (ProductSample p : items) {
                int d = Integer.parseInt(Utilities.dateToString(p.visited_date, "yyyy-MM-dd", "dd"));
                if (d < 8) {
                    f1[categoryNames.indexOf(p.category)] += p.quantity;
                } else if (d < 15) {
                    f2[categoryNames.indexOf(p.category)] += p.quantity;
                } else if (d < 22) {
                    f3[categoryNames.indexOf(p.category)] += p.quantity;
                } else if (d < 32) {
                    f4[categoryNames.indexOf(p.category)] += p.quantity;
                }
            }
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            yVals1.add(new BarEntry(f1, 0));
            yVals1.add(new BarEntry(f2, 1));
            yVals1.add(new BarEntry(f3, 2));
            yVals1.add(new BarEntry(f4, 3));

            BarDataSet set1 = new BarDataSet(yVals1, "");
            if (size > 0)
                set1.setColors(getColors(size));
            set1.setStackLabels(projection);
            ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
            dataSets1.add(set1);

            chartDataSampleCatMonth = new BarData(xVals1, dataSets1);
            chartDataSampleCatMonth.setValueFormatter(new MyValueFormatter());

            //load daily chart
            Calendar today = Calendar.getInstance();
            Calendar lastWeek = Calendar.getInstance();
            if (lastWeek.get(Calendar.DAY_OF_MONTH) < 8) {
                lastWeek.add(Calendar.DAY_OF_MONTH, 1 - lastWeek.get(Calendar.DAY_OF_MONTH));
            } else
                lastWeek.add(Calendar.DAY_OF_MONTH, -6);
            items = new Select().from(ProductSample.class)
                    .where(Condition.column(ProductSample$Table.VISITED_DATE).between(Utilities.dateToString(lastWeek, "yyyy-MM-dd"))
                            .and(Utilities.dateToString(today, "yyyy-MM-dd")))
                    .and(Condition.column(ProductSample$Table.CATEGORY).in("", projection))
                    .orderBy(ProductSample$Table.CATEGORY)
                    .queryList();
            xVals1 = new ArrayList<>();
            int thisDay = Integer.parseInt(Utilities.dateToString(today, "dd"));
            int lastWeekDay = Integer.parseInt(Utilities.dateToString(lastWeek, "dd"));
            int numXvals = thisDay - lastWeekDay + 1;
            for (i = lastWeekDay; i <= thisDay; i++) {
                xVals1.add(Integer.toString(i));
            }
            ArrayList<float[]> vals = new ArrayList<>();
            for (i = 0; i < numXvals; i++) {
                vals.add(new float[size]);
            }

            for (ProductSample p : items) {
                int d = Integer.parseInt(Utilities.dateToString(p.visited_date, "yyyy-MM-dd", "dd"));
                int dif = d - lastWeekDay;
                vals.get(dif)[categoryNames.indexOf(p.category)] += p.quantity;
            }
            yVals1 = new ArrayList<>();
            for (i = 0; i < numXvals; i++) {
                yVals1.add(new BarEntry(vals.get(i), i));
            }

            set1 = new BarDataSet(yVals1, "");
            if (size > 0)
                set1.setColors(getColors(size));
            dataSets1 = new ArrayList<>();
            dataSets1.add(set1);

            chartDataSampleCatDaily = new BarData(xVals1, dataSets1);
            chartDataSampleCatDaily.setValueFormatter(new MyValueFormatter());

            event = new ChartSamplesCatEvent(projection);

            return null;
        }
    }
}
