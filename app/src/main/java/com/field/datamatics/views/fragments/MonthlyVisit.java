package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.MonthlyVisitAdapter;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anoop on 20/9/15.
 * Monthly visit logic
 */
public class MonthlyVisit extends BaseFragment {
    private LinearLayout container_view;
    private GridView gridview;
    private TextView tv_pending_list;
    private ArrayList<String> data=new ArrayList<String>();
    private ArrayList<String> visit=new ArrayList<String>();
    private MonthlyVisitAdapter monthlyVisitAdapter;
    private Spinner spin_months,spin_years;
    int days_count=30;
    ArrayList<String>months=new ArrayList<String>();
    ArrayAdapter month_adapter;
    ArrayList<String>years=new ArrayList<String>();
    ArrayAdapter years_adapter;
    String month_="01";
    String year_="2016";
    public static int month_position=0;
    public static int year_position=0;
    int start=1;
    int day=1;
    //public static ArrayList<RoutePlan>route_plan=new ArrayList<RoutePlan>();
    public static String SELECTED_DATE="";
    private int load =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_monthly_visit,container,false);
        setTitle("Monthly visits");
        addFragmentTitle("MonthlyVisit");
        container_view= (LinearLayout) view.findViewById(R.id.container_view);
        gridview= (GridView) view.findViewById(R.id.grid_monthly_visit);
        tv_pending_list= (TextView) view.findViewById(R.id.tv_pending_list);
        tv_pending_list.setVisibility(View.GONE);
        spin_months= (Spinner) view.findViewById(R.id.spin_months);
        spin_years= (Spinner) view.findViewById(R.id.spin_years);
        tv_pending_list.setText("Submit route plan for approval");
        months.add("Januari");
        months.add("Februari");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("Augest");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        final Calendar c = Calendar.getInstance();
        for(int i=c.get(Calendar.YEAR);i<(c.get(Calendar.YEAR)+2);i++){
            years.add(i+"");
            Log.d("YEARS",i+"");
        }
        ArrayAdapter<String> adapter_=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,years);
        spin_years.setAdapter(adapter_);

        month_position=c.get(Calendar.MONTH);
        spin_years.setSelection(year_position);
        spin_months.setSelection(month_position);

        year_ = c.get(Calendar.YEAR)+"";
        if(month_position<9)
            month_="0"+(month_position+1)+"";
        else
            month_=(month_position+1)+"";

        setGridView();
        registerEvents();
        return  view;
    }
    @Override
    public void onResume() {
        super.onResume();
        monthlyVisitAdapter.notifyDataSetChanged();
    }

    /**
     * Grid view set up
     */
    private void setGridView(){
        container_view.setVisibility(View.GONE);
        try {
            String input_date="01/"+month_+"/"+year_;
            //String input_date="01/11/2015";
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            Date date1=format.parse(input_date);
            Calendar cal=Calendar.getInstance();
            cal.setTime(date1);
            //Toast.makeText(getActivity(),cal.get(Calendar.DAY_OF_WEEK)+"",Toast.LENGTH_SHORT).show();
            start=cal.get(Calendar.DAY_OF_WEEK);
        }
        catch (Exception e){

        }


        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int gridwidth=(int)width/7;
        data.clear();
        visit.clear();
        switch (start){
            case 1:
                break;
            case 2:
                data.add("");
                visit.add("");
                break;
            case 3:
                data.add("");
                data.add("");

                visit.add("");
                visit.add("");
                break;
            case 4:
                data.add("");
                data.add("");
                data.add("");

                visit.add("");
                visit.add("");
                visit.add("");
                break;
            case 5:
                data.add("");
                data.add("");
                data.add("");
                data.add("");

                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                break;
            case 6:
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");

                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                break;
            case 7:
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");

                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                visit.add("");
                break;
            default:
                break;
        }
        if(month_.equals("02")){
            if((Integer.parseInt(year_))%4==0)
                days_count=29;
            else
                days_count=28;
        }
        else if((Integer.parseInt(month_))<=7){
            if((Integer.parseInt(month_))%2==0)
                days_count=30;
            else
                days_count=31;
        }
        else{
            if((Integer.parseInt(month_))%2==0)
                days_count=31;
            else
                days_count=30;
        }

        for(int i=1;i<=days_count;i++)
            data.add(i+"");
        monthlyVisitAdapter=new MonthlyVisitAdapter(getActivity(),data,visit,gridwidth);
        gridview.setAdapter(monthlyVisitAdapter);
        monthlyVisitAdapter.notifyDataSetChanged();
        createMonthlyCalander();

    }

    /**
     * Register events
     */
    private void registerEvents() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day=position-(start-2);
                if(day>9)
                    readClientList(day + "");
                else
                    readClientList("0"+day + "");

            }
        });
        tv_pending_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(PendingTask.getInstance("PendingTask_monthly_visit"));
            }
        });
        spin_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(load<3) {
                    load++;
                    return;
                }
                month_position = position;
                if(position >=9)
                    month_ = (position + 1) + "";
                else
                    month_ = "0"+(position + 1) + "";
                setGridView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (load < 3) {
                    load++;
                    return;
                }
                year_position = position;
                year_ = years.get(position);
                setGridView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * read clients from db
     * @param day_
     */
    private void readClientList(final String day_){
        new AsyncTask<Void,RoutePlan,RoutePlan>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog(true);
            }

            @Override
            protected RoutePlan doInBackground(Void... params) {
                RoutePlan routePlan= new Select().from(RoutePlan.class).where(Condition.column(RoutePlan$Table.DATE).eq(year_ + "-" + month_ + "-" + day_)).querySingle();
                SELECTED_DATE=year_ + "-" + month_ + "-" + day_;
                return routePlan;
            }

            @Override
            protected void onPostExecute(RoutePlan routePlan) {
                super.onPostExecute(routePlan);
                showDialog(false);
                if(routePlan!=null) {
                    addFragment(new ClientList());
                }
                else{
                    DialogUtil.getInstance().getDialog(getActivity(), "Monthly Visit", "No route plan is schedule" +
                            " for " + year_ + "-" + month_ + "-" + day, new DialogCallBacks() {
                        @Override
                        public void onOk() {

                        }
                    });
                }

            }
        }.execute();
    }

    /**
     * Monthly visit calender logic
     */
    private void createMonthlyCalander(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                for(int i=1;i<=days_count;i++){
                    String day_=i+"";
                    ArrayList<RoutePlan>planed=new ArrayList<RoutePlan>();
                    ArrayList<RoutePlan>unplaned=new ArrayList<RoutePlan>();
                    ArrayList<RoutePlan>pending=new ArrayList<RoutePlan>();
                    ArrayList<RoutePlan>completed=new ArrayList<RoutePlan>();
                    if(i<10)
                        day_="0"+i;
                    //Log.d("DAY",year_ + "-" + month_ + "-" + day_);
                    try {
                        planed.addAll((ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                                .where(Condition.column(RoutePlan$Table.DATE).eq(year_ + "-" + month_ + "-" + day_))
                                .and(Condition.column(RoutePlan$Table.STATUS).lessThan(2))
                                .queryList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        unplaned.addAll((ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                                .where(Condition.column(RoutePlan$Table.DATE).eq(year_ + "-" + month_ + "-" + day_))
                                .and(Condition.column(RoutePlan$Table.STATUS).eq(2))
                                .queryList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        pending.addAll((ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                                .where(Condition.column(RoutePlan$Table.DATE).eq(year_ + "-" + month_ + "-" + day_))
                                .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                                .queryList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        completed.addAll((ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                                .where(Condition.column(RoutePlan$Table.DATE).eq(year_ + "-" + month_ + "-" + day_))
                                .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(1))
                                .queryList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    visit.add(planed.size()+"/"+unplaned.size()+"/"+pending.size()+"/"+completed.size());
                    planed=null;
                    unplaned=null;
                    pending=null;
                    completed=null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dissmissProgressDialog();
                monthlyVisitAdapter.notifyDataSetChanged();
                container_view.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(),load+"",Toast.LENGTH_SHORT).show();
                Log.d("LOAD",load+"");
                load++;
            }
        }.execute();


    }

}
