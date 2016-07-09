package com.field.datamatics.views.fragments;

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
import com.field.datamatics.views.adapters.MyPagerAdapter;

/**
 * Created by Jith on 11/8/2015.
 */
public class Schedule_vs_ActualFragment extends BaseFragment {

    private static Schedule_vs_ActualFragment mInstance;
    private String dateFrom;
    private String dateTo;
    private int filterType;

    public Schedule_vs_ActualFragment() {
    }

    public static Schedule_vs_ActualFragment getInstance(boolean isNew, String dateFrom, String dateTo,int filterType) {
        if (isNew || mInstance == null) {
            mInstance = new Schedule_vs_ActualFragment();
            mInstance.dateFrom = dateFrom;
            mInstance.dateTo = dateTo;
            mInstance.filterType = filterType;
        }
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
    }

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setTitle("Schdule vs Actual");
        addFragmentTitle(Schedule_vs_ActualFragment.class.getSimpleName());
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_image_video_tab, container, false);
        } else {
            return v;
        }

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Visited"));
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
                    return ScheduleFragment.getInstance(dateFrom,dateTo,filterType);
                case 1:
                    return VisitedFragment.getInstance(dateFrom,dateTo,filterType);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
