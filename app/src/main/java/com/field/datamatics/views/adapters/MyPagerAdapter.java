package com.field.datamatics.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.field.datamatics.views.fragments.BrochureList;
import com.field.datamatics.views.fragments.ImageList;
import com.field.datamatics.views.fragments.VideoList;

import java.util.Calendar;

/**
 * Created by anoop on 27/9/15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String productNumber;
    String category;
    int routePlanNumber;
    Calendar visitedDate;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs, String productNumber, String category, int routePlanNumber, Calendar visitedDate) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.productNumber = productNumber;
        this.routePlanNumber = routePlanNumber;
        this.category = category;
        this.visitedDate = visitedDate;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ImageList tab1 = ImageList.getInstance(productNumber, category, routePlanNumber, visitedDate, true);
                return tab1;
            case 1:
                VideoList tab2 = VideoList.getInstance(productNumber, category, routePlanNumber, visitedDate, true);
                return tab2;
            case 2:
                BrochureList tab3 = BrochureList.getInstance(productNumber, category, routePlanNumber, visitedDate, true);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}