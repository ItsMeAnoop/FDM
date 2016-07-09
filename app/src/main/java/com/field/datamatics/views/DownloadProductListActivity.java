package com.field.datamatics.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.Services.appservices.SecondSyncService;
import com.field.datamatics.Services.appservices.UpdateSyncService;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.database.Product;
import com.field.datamatics.interfaces.OperationCallBacks;
import com.field.datamatics.interfaces.SyncingCallBack;
import com.field.datamatics.utils.CSVReader;
import com.field.datamatics.utils.PreferenceUtil;
import com.raizlabs.android.dbflow.sql.language.Delete;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by USER on 12/13/2015.
 */
public class DownloadProductListActivity extends BaseActivity {
    private TextView tv_percentage;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url = "";
    private boolean isLoadAll=true;
    private String isSyncAllData=null;
    private int percentage=0;
    public static boolean apiCalled=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_file_activity);
        tv_percentage= (TextView) findViewById(R.id.tv_percentage);
        if(!apiCalled){
            getBundleData();
        }
        else
        {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiCalled=false;
    }

    private void getBundleData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("syncType")!=null){
                //Sync data from server
                if(bundle.getString("syncType").equals("PULL")){
                    if(bundle.getString("isSyncAllData")!=null&&bundle.getString("isSyncAllData").equals("no")){
                        isLoadAll=false;
                    }
                    loadProduct();
                }
                else{
                    //Sync data to server
                    apiCalled=true;
                    sycnDataToServer();
                }

            }

        }

    }


    private void loadProduct(){
            if(PreferenceUtil.getIntsance().isTesting()){
                file_url = "http://sarauae.dyndns.org:8099/"+ ApiConstants.url_test+"/PR_DETA_ILS.csv";
            }
            else{
                file_url = "http://sarauae.dyndns.org:8099/"+ ApiConstants.url+"/PR_DETA_ILS.csv";
            }
            apiCalled=true;
            new DownloadFileFromURL().execute(file_url);
    }


    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/PR_DETA_ILS.csv");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            Log.e("Success: ", "S");
            Toast.makeText(getApplicationContext(), "File downloaded", Toast.LENGTH_SHORT).show();

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/PR_DETA_ILS.csv";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
            createFile("PR_DETA_ILS.csv");
        }

    }

    private void createFile(String data){
        try {
            Delete.table(Product.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        showProgressDialog();
        CSVReader.getInstance().addProductsFromCSV(DownloadProductListActivity.this,data,data, new OperationCallBacks() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        percentage=40;
                        tv_percentage.setText(percentage+"%");
                       if(isLoadAll){
                           sycnDataFromServer();
                       }
                        else{
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           Toast.makeText(getApplicationContext(), "Successfully Synced", Toast.LENGTH_LONG).show();
                           finish();
                       }

                    }
                }, 10);
                dissmissProgressDialog();

            }

            @Override
            public void onError() {
                dissmissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "file creacreation failed", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });
    }
    private void sycnDataFromServer(){
        startService(SecondSyncService.getIntent(new SyncingCallBack() {
            @Override
            public void onPerecentage(int percentage,boolean isSuccess) {
                if(!isSuccess){
                    Toast.makeText(getApplicationContext(), "Syncing failed, Try again", Toast.LENGTH_LONG).show();
                    finish();
                }
                tv_percentage.setText(percentage + "%");
                if (isLoadAll) {
                    if (percentage == 100) {
                        Toast.makeText(getApplicationContext(), "Successfully Synced", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },200);
                    }
                }
            }
        }, DownloadProductListActivity.this));
    }

    private void sycnDataToServer(){
        startService(UpdateSyncService.getIntent(new SyncingCallBack() {
            @Override
            public void onPerecentage(int percentage,boolean isSuccess) {
                if(!isSuccess){
                    Toast.makeText(getApplicationContext(), "Syncing failed, Try again", Toast.LENGTH_LONG).show();
                    finish();
                }
                tv_percentage.setText(percentage + "%");
                if(percentage==100) {
                    Toast.makeText(getApplicationContext(), "Successfully Synced", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },200);
                }

            }
        }, DownloadProductListActivity.this));
    }
}
