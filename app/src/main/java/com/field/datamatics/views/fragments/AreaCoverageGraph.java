package com.field.datamatics.views.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.views.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 12/28/2015.
 * Area coverage graph logic
 */
public class AreaCoverageGraph extends BaseFragment {

    private PieChart mChart;
    private TextView tv_percentage;
    // we're going to display pie chart for smartphones martket shares
    private float[] yData;
    private static boolean isMonthly;

    public static AreaCoverageGraph getInstance(boolean type) {
        isMonthly = type;
        return new AreaCoverageGraph();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_graph, container, false);
        tv_percentage = (TextView) view.findViewById(R.id.tv_percentage);
        mChart = (PieChart) view.findViewById(R.id.pieChart1);
        mChart.setDescription("");
        // radius of the center hole in percent of maximum radius
//        mChart.setHoleRadius(7f);
//        mChart.setTransparentCircleRadius(10f);
//
        mChart.setDrawHoleEnabled(false);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        PieData data = generatePieData();
        mChart.setData(data);


        return view;

    }

    /**
     * generates less data (1 DataSet, 4 values)
     *
     * @return
     */
    protected PieData generatePieData() {
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        String day_percentage = "";
        if (isMonthly) {
            yData = MainActivity.area_monthly;
            try {
                if (yData[0] > 0) {
                    xVals.add("W 1");
                    day_percentage = day_percentage + "  W1=" + yData[0] + "%";
                }
                if (yData[1] > 0) {
                    xVals.add("W 2");
                    day_percentage = day_percentage + "  W2=" + yData[1] + "%";
                }
                if (yData[2] > 0) {
                    xVals.add("W 3");
                    day_percentage = day_percentage + "  W3=" + yData[2] + "%";
                }
                if (yData[3] > 0) {
                    xVals.add("W 4");
                    day_percentage = day_percentage + "  W4=" + yData[3] + "%";
                }
                if ((yData[0] + yData[1] + yData[2] + yData[3]) < 100) {
                    xVals.add("Not Covered");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            yData = MainActivity.area_daily;
            try {
                if (yData[0] > 0) {
                    xVals.add("D 7");
                    day_percentage = day_percentage + " D7(" + getDate(0) + ")=" + yData[0] + "%";
                }
                if (yData[1] > 0) {
                    xVals.add("D 6");
                    day_percentage = day_percentage + " D6(" + getDate(1) + ")=" + yData[1] + "%";
                }
                if (yData[2] > 0) {
                    xVals.add("D 5");
                    day_percentage = day_percentage + " D5(" + getDate(2) + ")=" + yData[2] + "%";
                }
                if (yData[3] > 0) {
                    xVals.add("D 4");
                    day_percentage = day_percentage + " D4(" + getDate(3) + ")=" + yData[3] + "%";
                }
                if (yData[4] > 0) {
                    xVals.add("D 3");
                    day_percentage = day_percentage + " D3(" + getDate(4) + ")=" + yData[4] + "%";
                }
                if (yData[5] > 0) {
                    xVals.add("D 2");
                    day_percentage = day_percentage + " D2(" + getDate(5) + ")=" + yData[5] + "%";
                }
                if (yData[6] > 0) {
                    xVals.add("D 1");
                    day_percentage = day_percentage + " D1(" + getDate(6) + ")=" + yData[6] + "%";
                }
                if ((yData[0] + yData[1] + yData[2] + yData[3] + yData[4] + yData[5] + yData[6]) < 100) {
                    xVals.add("Not Covered");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        tv_percentage.setText(day_percentage);
        try {
            for (int i = 0; i < yData.length; i++) {
                if (yData[i] > 0)
                    entries1.add(new Entry(yData[i], i));
            }
            if (isMonthly) {
                if ((yData[0] + yData[1] + yData[2] + yData[3]) < 100) {
                    entries1.add(new Entry((100 - ((yData[0] + yData[1] + yData[2] + yData[3]))), entries1.size()));
                }
            } else {
                if ((yData[0] + yData[1] + yData[2] + yData[3] + yData[4] + yData[5] + yData[6]) < 100) {
                    entries1.add(new Entry((100 - ((yData[0] + yData[1] + yData[2] + yData[3] + yData[4] + yData[5] + yData[6]))), entries1.size()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        PieDataSet ds1 = new PieDataSet(entries1, "Area Coverage");
        int[] VORDIPLOM_COLORS = {
                Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), Color.rgb(255, 108, 110),
                Color.rgb(240, 234, 255), Color.rgb(155, 140, 157)
        };
        ds1.setColors(VORDIPLOM_COLORS);
      //  ds1.setSliceSpace(2f);


        PieData d = new PieData(xVals);
        d.setDataSet(ds1);
        d.setValueTypeface(Typeface.SANS_SERIF);
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(8f);
        d.setValueFormatter(new PercentFormatter());

        return d;
    }

    private String getDate(int day) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat s = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis() - (day * 24 * 60 * 60 * 1000));
        return s.format(date);
    }

}
