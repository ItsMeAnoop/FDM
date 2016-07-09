package com.field.datamatics.views.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.Appointment$Table;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.views.adapters.AppointmentAdapter;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by anoop on 11/10/15.
 */
public class TakeAnAppointment extends BaseFragment {
    private RecyclerView rv_list;
    private Button btn_choose;
    private TextView tv_take_new;
    private TextView tv_date;
    private ArrayList<com.field.datamatics.database.Appointment> data = new ArrayList<com.field.datamatics.database.Appointment>();
    private AppointmentAdapter adapter;
    public static String date = "2015-2-1";
    public static int year = 0;
    public static int month;
    public static int day;
    static final int DATE_DIALOG_ID = 999;
    private int picker_use;
    public static boolean isShowMsg = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_appointment, container, false);
        setTitle("Appointments");
        AppControllerUtil.getInstance().setCurrent_fragment("TakeAnAppointment");
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        tv_take_new = (TextView) view.findViewById(R.id.tv_take_new);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        btn_choose = (Button) view.findViewById(R.id.btn_choose);
        tv_date.setText(date);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview

        date = new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("-").append(month + 1).append("-")
                .append(day).toString();

        tv_date.setText(date);

        registerEvents();
        listSetUp();
        //readData();
        return view;
    }

    private void listSetUp() {
        ArrayList<com.field.datamatics.database.Appointment> appointments = (ArrayList<com.field.datamatics.database.Appointment>) new Select().from(com.field.datamatics.database.Appointment.class).where(Condition.column(Appointment$Table.APPOINTMENT_DATE).eq(date)).queryList();
        if (appointments != null) {
            data.clear();
            data.addAll(appointments);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv_list.setLayoutManager(llm);
            adapter = new AppointmentAdapter(getActivity(), data);
            rv_list.setAdapter(adapter);
        }
        if (data.size() == 0 && isShowMsg) {
            DialogUtil.getInstance().getDialog(getActivity(), "Appointment", "No Appointments available!", new DialogCallBacks() {
                @Override
                public void onOk() {

                }
            });
        }

        isShowMsg = true;


    }

    private void registerEvents() {
        tv_take_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker_use = 2;
                newAppointment();
            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker_use = 1;
                showDatePicker();
            }
        });


    }

    private void readData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //showDialog(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                ArrayList<com.field.datamatics.database.Appointment> appointments =
                        (ArrayList<com.field.datamatics.database.Appointment>)
                                new Select().from(com.field.datamatics.database.Appointment.class).
                                        where(Condition.column(Appointment$Table.APPOINTMENT_DATE).eq(date)).queryList();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //showDialog(false);
                /*if(appointments!=null&&appointments.size()!=0)
                    listSetUp(appointments);*/
            }
        }.execute();

    }

    private void newAppointment() {
        showDatePicker();

    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

// Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                R.style.AppTheme, datePickerListener,
                year,
                month,
                day);
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }

    // Listener
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            date = year1 + "-" + month1 + "-" + day1;
            tv_date.setText(date);
            if (picker_use == 1)
                listSetUp();
            else
                addFragment(new TakeNewAppointment());

        }
    };
}
