package com.field.datamatics.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.utils.AppControllerUtil;

/**
 * Created by anoop on 27/9/15.
 */
public class RoutePlan extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_route_plan,container,false);
        AppControllerUtil.getInstance().setCurrent_fragment("RoutePlan");
        return view;
    }
}
