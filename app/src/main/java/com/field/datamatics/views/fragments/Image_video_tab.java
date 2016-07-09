package com.field.datamatics.views.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.views.adapters.MyPagerAdapter;

import java.util.Calendar;

/**
 * Created by anoop on 27/9/15.
 */
public class Image_video_tab extends BaseFragment {


    private static Image_video_tab mInstance;
    private int productNumber;
    private int routePlanNumber;

    private Calendar visitedDate;

    public Image_video_tab() {
    }

    public static Image_video_tab getInstance(int productNumber, int routePlanNumber, Calendar visitedDate, boolean newinstance) {
        if (mInstance == null || newinstance) {
            mInstance = new Image_video_tab();
            mInstance.productNumber = productNumber;
            mInstance.routePlanNumber = routePlanNumber;
            mInstance.visitedDate = visitedDate;
        }
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_video_tab, container, false);
        addFragmentTitle("Image_video_tab");
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Documents"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), productNumber + "", "", routePlanNumber, visitedDate);
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

        return v;
    }

}
