package com.field.datamatics.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.field.datamatics.R;
import com.field.datamatics.views.MainActivity;

/**
 * Created by USER on 12/28/2015.
 */
public class VisitDoneGraph extends BaseFragment {
    private LinearLayout container_weeks,container_days;
    private SpeedometerGauge speedometer;
    private TextView tv_time,tv_c1,tv_c2,tv_c3,tv_c4;
    private Button btn_day1,btn_day2,btn_day3,btn_day4,btn_day5,btn_day6,btn_day7,btn_week1,btn_week2,btn_week3,btn_week4;
    private static boolean isMonthly=false;
    int[] vals_monthy = new int[4];
    int[] vals_daily;
    public static VisitDoneGraph getInstance(boolean type){
        isMonthly=type;
        return new VisitDoneGraph();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.time_spend_fragment,container,false);
        tv_time= (TextView) v.findViewById(R.id.tv_time);
        tv_c1= (TextView) v.findViewById(R.id.tv_c1);
        tv_c2= (TextView) v.findViewById(R.id.tv_c2);
        tv_c3= (TextView) v.findViewById(R.id.tv_c3);
        tv_c4= (TextView) v.findViewById(R.id.tv_c4);

        btn_day1= (Button) v.findViewById(R.id.btn_day1);
        btn_day2= (Button) v.findViewById(R.id.btn_day2);
        btn_day3= (Button) v.findViewById(R.id.btn_day3);
        btn_day4= (Button) v.findViewById(R.id.btn_day4);
        btn_day5= (Button) v.findViewById(R.id.btn_day5);
        btn_day6= (Button) v.findViewById(R.id.btn_day6);
        btn_day7= (Button) v.findViewById(R.id.btn_day7);

        btn_week1= (Button) v.findViewById(R.id.btn_week1);
        btn_week2= (Button) v.findViewById(R.id.btn_week2);
        btn_week3= (Button) v.findViewById(R.id.btn_week3);
        btn_week4= (Button) v.findViewById(R.id.btn_week4);

        speedometer = (SpeedometerGauge) v.findViewById(R.id.speedometer);

        container_weeks= (LinearLayout) v.findViewById(R.id.container_weeks);
        container_days= (LinearLayout) v.findViewById(R.id.container_days);
        vals_daily= MainActivity.visit_daily;
        vals_monthy= MainActivity.visit_montly;
        container_weeks.setVisibility(View.GONE);
        container_days.setVisibility(View.GONE);
        if(isMonthly){

            for(int i=0;i<vals_monthy.length;i++){
                Log.d("VISIT_DONE_M", vals_monthy[i] + "");
            }
            setUpGraph(vals_monthy, 0);

        }
        else{

            for(int i=0;i<vals_daily.length;i++){
                Log.d("VISIT_DONE_D",vals_daily[i]+"");
            }
            setUpGraph(vals_daily,0);

        }



        return v;
    }
    private void registerEvents(){
        btn_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,0);

            }
        });
        btn_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,1);

            }
        });
        btn_day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,2);

            }
        });
        btn_day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,3);

            }
        });
        btn_day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,4);

            }
        });
        btn_day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,5);

            }
        });
        btn_day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_daily,6);

            }
        });
        btn_week4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_monthy,3);

            }
        });
        btn_week3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_monthy,2);

            }
        });
        btn_week2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_monthy,1);

            }
        });
        btn_week1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph(vals_monthy, 0);

            }
        });

    }


    private void setUpGraph(int[] vals,int position){
        int value=0;
        if(isMonthly){
            value=210;
        }
        else{
            value=7;
        }
        value= (int) ((int)value+(Math.round((value*.2))));
        speedometer.setMaxSpeed(value);
        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {

                return String.valueOf((int) Math.round(progress));
            }
        });
        //speedometer.setMaxSpeed(50);
        //speedometer.setMajorTickStep(5);
        //speedometer.setMinorTicks(4);
       // double range=value/4;
        if(isMonthly){
            speedometer.addColoredRange(0, 90, Color.RED);
            speedometer.addColoredRange(90, 210, Color.BLUE);
            speedometer.addColoredRange(210, value, Color.GREEN);
            tv_c1.setText("0 - 90");
            tv_c2.setText("91 - 210");
            tv_c3.setText(">=210");
            tv_c4.setText("");
        }
        else{
            speedometer.addColoredRange(0, 3, Color.RED);
            speedometer.addColoredRange(3, 7, Color.BLUE);
            speedometer.addColoredRange(7, value, Color.GREEN);
            tv_c1.setText("0 - 3");
            tv_c2.setText("3 - 7");
            tv_c3.setText(">=7");
            tv_c4.setText("");
        }
        tv_c1.setTextColor(Color.RED);
        tv_c2.setTextColor(Color.BLUE);
        tv_c3.setTextColor(Color.GREEN);
        try {
            double result=0;
            for(int i=0;i<vals.length;i++){
                result=result+vals[i];
            }
            speedometer.setSpeed(Math.round(result), 1000, 300);
            tv_time.setText(Math.round(result) + "");
        } catch (Exception e) {
            e.printStackTrace();
            speedometer.setSpeed(0, 1000, 300);
            tv_time.setText("0.0");
        }
        registerEvents();

    }
}
