package com.field.datamatics.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.MyPagerAdapter;

import java.util.Calendar;

/**
 * Created by Jith on 11/8/2015.
 * Today visit tab logic
 */
public class TodaysVisitTabbed extends BaseFragment {

    private static TodaysVisitTabbed mInstance;

    private boolean isPendingSelected;
    private int tabPosition=0;

    public TodaysVisitTabbed() {
    }

    public static TodaysVisitTabbed getInstance() {
        boolean isPendingSelected = false;
        if (mInstance != null) {
            isPendingSelected = mInstance.isPendingSelected;
        }
        mInstance = new TodaysVisitTabbed();
        mInstance.isPendingSelected = isPendingSelected;
        return mInstance;
    }

    public static TodaysVisitTabbed getNewInstance() {
        mInstance = new TodaysVisitTabbed();
        mInstance.isPendingSelected = false;
        return mInstance;
    }
    public static TodaysVisitTabbed getPendingVisitTabbed() {
        mInstance = new TodaysVisitTabbed();
        mInstance.isPendingSelected = true;
        mInstance.tabPosition=1;
        return mInstance;
    }
    public static TodaysVisitTabbed getAdditionalVisitTabbed() {
        mInstance = new TodaysVisitTabbed();
        mInstance.isPendingSelected = false;
        mInstance.tabPosition=2;
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        AppControllerUtil.getInstance().setCurrent_fragment("TodayVisit");

    }

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
        edit.putBoolean(Constants.PREF_SHOULD_RESUME, true).commit();
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_TODAYS_VISIT);
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.commit();
        v = inflater.inflate(R.layout.fragment_image_video_tab, container, false);
        setTitle("Today's Visit");
        addFragmentTitle("TodayVisit");
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Additional"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
                (getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.tab_selected));
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                isPendingSelected = tab.getPosition() == 1 ? true : false;
                if(tab.getPosition()==0){
                    setTitle("Today's Visit");
                }
                else if(tab.getPosition()==1){
                    setTitle("Pending Visit");
                }
                else if(tab.getPosition()==2){
                    setTitle("Additional Visit");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (isPendingSelected) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        }
        else{
            TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
            tab.select();
        }
        return v;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TodayVisit();
                case 1:
                    return new PendingTask();
                case 2:
                    return new AdditionalVisit();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
