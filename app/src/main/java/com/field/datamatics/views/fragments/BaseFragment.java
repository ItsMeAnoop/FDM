package com.field.datamatics.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.BaseActivity;
import com.field.datamatics.views.MainActivity;

/**
 * Created by anoop on 20/9/15.
 */
public class BaseFragment extends Fragment {

    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment,String name,boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }

    public void showProgressDialog() {
        dialog.show();
    }

    public void hideProgressDialog() {
        dialog.hide();
    }

    public void dissmissProgressDialog() {
        dialog.dismiss();
    }

    public void addFragmentTitle(String title) {
        AppControllerUtil.getInstance().setCurrent_fragment(title);
        log("fragment title is " + title);
    }

    public void showMessage(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void showDialog(boolean b) {
    }


    @Override
    public void onResume() {
        super.onResume();
        Utilities.checkPlayServices(getContext());
    }

    public void setTitle(String title) {

        try {
            MainActivity activity = (MainActivity) getActivity();
            activity.setTitle(title);
        } catch (ClassCastException e) {
            Log.i("FDM", "Not a member of MainActivity");
        }
    }

    public void setNavigationDrawer(int id) {

        try {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationChecked(id);
        } catch (ClassCastException e) {
            Log.i("FDM", "Not a member of MainActivity");
        }
    }

    protected void log(String desc) {
        Log.i("FDM", desc);
    }
}
