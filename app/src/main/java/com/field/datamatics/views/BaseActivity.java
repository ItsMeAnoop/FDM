package com.field.datamatics.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.field.datamatics.R;
import com.field.datamatics.utils.AppControllerUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by anoop on 20/9/15.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        dialog = new ProgressDialog(BaseActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
//        Thread.setDefaultUncaughtExceptionHandler(new
//                                                          Thread.UncaughtExceptionHandler() {
//                                                              @Override
//                                                              public void uncaughtException(Thread paramThread,
//                                                                                            Throwable paramThrowable) {
//                                                                  try {
//                                                                      File file = new File(Environment
//                                                                              .getExternalStorageDirectory()
//                                                                              + File.separator
//                                                                              + "fdm_logs");
//                                                                      File f = new File(file, "" + System.currentTimeMillis()
//                                                                              + ".txt");
//                                                                      if (!file.exists())
//                                                                          file.mkdirs();
//                                                                      PrintWriter writer = new PrintWriter(f, "UTF-8");
//                                                                      paramThrowable.printStackTrace(writer);
//                                                                      writer.close();
//                                                                  } catch (IOException e) {
//                                                                  }
//
//                                                                  System.exit(2); // Prevents the service/app from freezing
//                                                              }
//                                                          });
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public String getCurrentFragmentName() {
        return AppControllerUtil.getInstance().getCurrent_fragment();
    }

    public void showMessage(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void showProgressDialog() {
        try {
            dialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        dialog.hide();
    }

    public void dissmissProgressDialog() {
        dialog.dismiss();
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

}
