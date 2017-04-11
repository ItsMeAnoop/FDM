package com.field.datamatics.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.Services.appservices.MyAlaramService;
import com.field.datamatics.apimodels.AppoinmentResponse;
import com.field.datamatics.apimodels.AppoinmentResponseBody;
import com.field.datamatics.apimodels.ClientResponse;
import com.field.datamatics.apimodels.ClientResponseBody;
import com.field.datamatics.apimodels.ClientResponseBodyOne;
import com.field.datamatics.apimodels.ClientproductResponse;
import com.field.datamatics.apimodels.ClientproductResponseBody;
import com.field.datamatics.apimodels.CustomerResponse;
import com.field.datamatics.apimodels.CustomerResponseBody;
import com.field.datamatics.apimodels.LoginResponse;
import com.field.datamatics.apimodels.LoginResponseBody;
import com.field.datamatics.apimodels.RegionResponse;
import com.field.datamatics.apimodels.RegionResponseBody;
import com.field.datamatics.apimodels.ReminderResponse;
import com.field.datamatics.apimodels.ReminderResponseBody;
import com.field.datamatics.apimodels.RoutePlanResponse;
import com.field.datamatics.apimodels.RoutePlanResponseBody;
import com.field.datamatics.apimodels.SurveyDetailsJson;
import com.field.datamatics.apimodels.WorkCalanderResponse;
import com.field.datamatics.apimodels.WorkCalanderResponseBody;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Activities;
import com.field.datamatics.database.AdditionalVisits;
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client_Customer;
import com.field.datamatics.database.Client_Customer$Table;
import com.field.datamatics.database.Client_Product;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Documents;
import com.field.datamatics.database.GlobalMessages;
import com.field.datamatics.database.Logindetails;
import com.field.datamatics.database.Message;
import com.field.datamatics.database.Product;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlanTemp;
import com.field.datamatics.database.SurveyDetails;
import com.field.datamatics.database.SurveyMaster;
import com.field.datamatics.database.TimeSpendInMarket;
import com.field.datamatics.database.User;
import com.field.datamatics.database.User$Table;
import com.field.datamatics.database.UserRegion;
import com.field.datamatics.database.VisitedDetails;
import com.field.datamatics.gcm.RegistrationIntentService;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.interfaces.IrequestPassword;
import com.field.datamatics.synctables.SyncAppLog;
import com.field.datamatics.synctables.SyncAppointment;
import com.field.datamatics.synctables.SyncGeo;
import com.field.datamatics.synctables.SyncMessage;
import com.field.datamatics.synctables.SyncRoutePlan;
import com.field.datamatics.synctables.SyncSurveyDetails;
import com.field.datamatics.synctables.SyncVisitDetails;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.utils.NetworkStatusUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by anoop on 28/9/15.
 * Login page logic
 */
public class Login extends BaseActivity {
    private TextView tv_sign_in;
    private EditText et_user_name;
    private EditText et_password;
    private TextView btnRequestPwd;
    private int mrid = -1;

    private Gson gson = new Gson();
    public int year = 0;
    public int month;
    public int day;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<Client> clients = new ArrayList<Client>();
    private ArrayList<Client_Customer> client_customers = new ArrayList<>();
    private String count = "";
    public static boolean isLoginComplete;
    private int cutomer_index = 0;
    private int client_index = 0;

    //DB
    User myuser;
    private int oldUserNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isLoginComplete = false;
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        btnRequestPwd = (TextView) findViewById(R.id.btnRequestPwd);
        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_user_name.getText().toString().trim().equals("") && !et_password.getText().toString().trim().equals("")) {
                    checkLogin(et_user_name.getText().toString().trim(), et_password.getText().toString().trim());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter both username and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    unregisterReceiver(mRegistrationBroadcastReceiver);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if(mrid==-1)
                    return;
                boolean sentToken = AppControllerUtil.getPrefs().getBoolean(Constants.PREF_GCM_IS_SENT, false);
                getUserRegion();

            }
        };
        btnRequestPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.dialog_request_password);
                dialog.setTitle("Request password");
                final EditText edtUserName = (EditText) dialog.findViewById(R.id.edtUserName);
                final EditText edtEmpCode = (EditText) dialog.findViewById(R.id.edtEmpCode);
                AppCompatButton btnDone = (AppCompatButton) dialog.findViewById(R.id.btn_done);
                AppCompatButton btnCancel = (AppCompatButton) dialog.findViewById(R.id.btn_cancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userName = edtUserName.getText().toString();
                        String empCode = edtEmpCode.getText().toString();
                        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(empCode)) {
                            DialogUtil.getInstance().getDialog(Login.this, "Request Password",
                                    "Fill both fields.", new DialogCallBacks() {
                                        @Override
                                        public void onOk() {

                                        }
                                    });
                            return;
                        }
                        dialog.dismiss();
                        String myUrl = "";
                        if (PreferenceUtil.getIntsance().isTesting()) {
                            myUrl = ApiConstants.url_test;
                        } else {
                            myUrl = ApiConstants.url;
                        }
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(ApiConstants.BASE_URL)
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        IrequestPassword methods = restAdapter.create(IrequestPassword.class);
                        final ProgressDialog pDialog = new ProgressDialog(Login.this);
                        pDialog.setMessage("Requesting password. Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        methods.sendPassword(ApiConstants.ENCRYPTION_KEY, myUrl, userName, empCode, new Callback<JsonObject>() {
                            @Override
                            public void success(JsonObject jsonObject, Response response) {
                                pDialog.cancel();
                                try {
                                    JSONObject obj = new JSONObject(jsonObject.toString());
                                    if (obj.getString("status").equals("success")) {
                                        DialogUtil.getInstance().getDialog(Login.this, "Request Password",
                                                "Your password has been sent to your registered Email.", new DialogCallBacks() {
                                                    @Override
                                                    public void onOk() {

                                                    }
                                                });
                                    } else {
                                        DialogUtil.getInstance().getDialog(Login.this, "Request Password",
                                                "Something went wrong. Looks like you entered wrong credentials.", new DialogCallBacks() {
                                                    @Override
                                                    public void onOk() {

                                                    }
                                                });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                pDialog.cancel();
                                DialogUtil.getInstance().getDialog(Login.this, "Request Password",
                                        "Something went wrong. Please try again.", new DialogCallBacks() {
                                            @Override
                                            public void onOk() {

                                            }
                                        });
                            }
                        });
                    }
                });

                dialog.show();
            }

        });

        LocalBroadcastManager.getInstance(Login.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("registrationComplete"));
    }

    TransactionListener<List<UserRegion>> onRegionSavedListener = new TransactionListener<List<UserRegion>>() {


        @Override
        public void onResultReceived(List<UserRegion> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<UserRegion>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<UserRegion>> transaction, List<UserRegion> result) {
            return true;
        }
    };

    TransactionListener<List<Customer>> onCustomerSaveListener = new TransactionListener<List<Customer>>() {


        @Override
        public void onResultReceived(List<Customer> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Customer>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Customer>> transaction, List<Customer> result) {
            return true;
        }
    };
    TransactionListener<List<Client_Customer>> onClientCustomerSavedListener = new TransactionListener<List<Client_Customer>>() {


        @Override
        public void onResultReceived(List<Client_Customer> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Client_Customer>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Client_Customer>> transaction, List<Client_Customer> result) {
            return true;
        }
    };
    TransactionListener<List<Client_Product>> onClientProductSavedListener = new TransactionListener<List<Client_Product>>() {


        @Override
        public void onResultReceived(List<Client_Product> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Client_Product>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Client_Product>> transaction, List<Client_Product> result) {
            //getClientworkcalander();
            // getRoutePlan();
            return true;
        }
    };
    TransactionListener<List<RoutePlan>> onRoutePlanSavedListener = new TransactionListener<List<RoutePlan>>() {


        @Override
        public void onResultReceived(List<RoutePlan> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<RoutePlan>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<RoutePlan>> transaction, List<RoutePlan> result) {
            // getReminder();
            return true;
        }
    };
    TransactionListener<List<Reminder>> onReminderSavedListener = new TransactionListener<List<Reminder>>() {


        @Override
        public void onResultReceived(List<Reminder> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Reminder>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Reminder>> transaction, List<Reminder> result) {
            //getAppoinments();
            return true;
        }
    };
    TransactionListener<List<Appointment>> onAppointmentSavedListener = new TransactionListener<List<Appointment>>() {


        @Override
        public void onResultReceived(List<Appointment> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Appointment>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Appointment>> transaction, List<Appointment> result) {
            // getSurveyQns();
            return true;
        }
    };
    TransactionListener<List<Product>> onProductSavedListener = new TransactionListener<List<Product>>() {


        @Override
        public void onResultReceived(List<Product> result) {

        }

        @Override
        public boolean onReady(BaseTransaction<List<Product>> transaction) {
            return false;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Product>> transaction, List<Product> result) {
            dissmissProgressDialog();
            //getSurveyQns();
            return true;
        }
    };


    /**
     * Check login with api call
     * @param user_name
     * @param password
     */

    private void checkLogin(String user_name, final String password) {
        if (!NetworkStatusUtil.getInstance().isNetworkAvailable()) {
            showMessage("No network available", tv_sign_in);
            return;
        }
        showProgressDialog();
        cutomer_index = 0;
        client_index = 0;
        //login-success
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", user_name);
        params.put("password", password);
        params.put("encription_key", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
        ApiService.getInstance().makeApiCall(ApiConstants.AppLoginDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                //String response = (String) objects;
                final LoginResponse loginResponse = getGson().fromJson(objects.toString(), LoginResponse.class);
                Log.i("status", loginResponse.getStatus());
                if (loginResponse.getStatus().equals(ApiConstants.STATUS)) {
                    final LoginResponseBody body = loginResponse.getBody()[0];
                    mrid = Integer.parseInt(body.getUserid());
                    PreferenceUtil.getIntsance().setUSER_ID(body.getUserid());
                    //Toast.makeText(Login.this, mrid+"", Toast.LENGTH_SHORT).show();
                    Log.i("mrid", "" + mrid);
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            //loginResponse.getBody()[0].getAddressno();
                            int addressNo = 0;
                            try {
                                addressNo = Integer.parseInt(body.getAddressno());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            long phone1 = 0;
                            long phone2 = 0;
                            try {
                                phone1 = Long.parseLong(body.getPhone1());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            try {
                                phone2 = Long.parseLong(body.getPhone2());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            count = "Login-1";
                            User currentUser = new Select().from(User.class).querySingle();
                            if (currentUser != null)
                                oldUserNum = currentUser.UserNumber;
                            Delete.table(User.class, Condition.column(User$Table.USER_ID).greaterThan(-1));
                            //  Delete.table(User.class);
                            User user = new User(mrid,
                                    password, body.getProfile(),
                                    body.getFirstname(), body.getLastname(),
                                    addressNo, body.getDepartment(), body.getEmailid1(),
                                    body.getEmailid2(), phone1, phone2,
                                    true, true,
                                    body.getGeocordinatehome(), body.getGeocordinateoffice(), body.getRemarks()
                            );
                            myuser = user;
                            //user.save();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent;
                            if (oldUserNum != 0 && oldUserNum == mrid) {
                                dissmissProgressDialog();
                                myuser.save();
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                             if (Utilities.checkPlayServices(Login.this)) {
                                 try {
                                     Delete.table(AdditionalVisits.class);
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                                try {
                                    Delete.table(Client_work_cal.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Delete.table(Activities.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(RoutePlan.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(VisitedDetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Appointment.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Client.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Client_Customer.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Client_Product.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Client_work_cal.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Customer.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Documents.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(GlobalMessages.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Logindetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Message.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Product.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(ProductSample.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Reminder.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SurveyDetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SurveyMaster.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(UserRegion.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(TimeSpendInMarket.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(Logindetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(RoutePlanTemp.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncAppLog.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncAppointment.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncGeo.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncMessage.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncRoutePlan.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncSurveyDetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Delete.table(SyncVisitDetails.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                intent = new Intent(Login.this, RegistrationIntentService.class);
                                startService(intent);
                            }

                        }
                    }.execute();

                } else {
                    showMessage("Check your credentials", tv_sign_in);
                    dissmissProgressDialog();
                }

            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();

            }

            @Override
            public void onErrorMessage(String message) {

            }
        });

    }

    /**
     * user region api call
     */
    private void getUserRegion() {
        //UserRegion API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        ApiService.getInstance().makeApiCall(ApiConstants.AppUserRegionDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final RegionResponse regionResponse = gson.fromJson(objects.toString(), RegionResponse.class);
                if (regionResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            //regionResponse.getBody()[0]
                            ArrayList<UserRegion> data = new ArrayList<>();
                            int size = regionResponse.getBody().length;
                            if (size > 0) {
                                for (int i = 0; i < size; i++) {
                                    try {
                                        UserRegion region = new UserRegion();
                                        RegionResponseBody body = regionResponse.getBody()[i];
                                        try {
                                            region.Userregion_Id = Integer.parseInt(body.getRegionid());
                                        } catch (Exception e) {}
                                        try {
                                            region.user = myuser;
                                        } catch (Exception e) {}
                                        try {
                                            region.Region = body.getRegion();
                                        } catch (Exception e) { }
                                        try {
                                            region.Reporting_Manager = body.getReporting_manager();
                                        } catch (Exception e) { }
                                        try {
                                            region.Remarks = body.getRemarks();
                                        } catch (Exception e) {}
                                        region.Date_From = body.getDate_from();
                                        region.Date_To = body.getDate_to();
                                        data.add(region);
                                    } catch (Exception e) {}
                                }
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "AppUserRegionDetails-" + data.size();
                                Delete.table(UserRegion.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data).result(onRegionSavedListener)));
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                }
                //call next function
                getCustomers();
            }
            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }
            @Override
            public void onErrorMessage(String message) {

            }
        });
    }

    /**
     * customer api call
     */
    private void getCustomers() {
        //Customer API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        params.put("index",cutomer_index+"");
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewCustomerDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final CustomerResponse customerResponse = gson.fromJson(objects.toString(), CustomerResponse.class);
                if (customerResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            int size = customerResponse.getBody().length;
                            ArrayList<Customer> data = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                Customer customer = new Customer();
                                CustomerResponseBody body = customerResponse.getBody()[i];
                                try {
                                    customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) { }
                                try {
                                    customer.Customer_Name = body.getCustomername();
                                } catch (Exception e) { }
                                try {
                                    customer.Customer_Group = body.getCustomergroup();
                                } catch (Exception e) { }
                                try {
                                    customer.Address1 = body.getAddress1();
                                } catch (Exception e) { }
                                try {
                                    customer.Street = body.getStreet();
                                } catch (Exception e) { }
                                try {
                                    customer.Location = body.getLocation();
                                } catch (Exception e) { }
                                try {
                                    customer.PO = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {}
                                try {
                                    customer.Region = body.getRegion();
                                } catch (Exception e) { }
                                try {
                                    customer.Geo_Cordinates = body.getGeocordinates();
                                } catch (Exception e) {}
                                try {
                                    customer.Country = body.getCountry();
                                } catch (Exception e) {}
                                try {
                                    if (body.getCustomerid().equals("1"))
                                        customer.status = true;
                                    else
                                        customer.status = false;
                                } catch (Exception e) {}
                                try {
                                    customer.Remarks = body.getRemarks_customer();
                                } catch (Exception e) {}
                                data.add(customer);
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "Customers-" + data.size();
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data).result(onCustomerSaveListener)));
                                cutomer_index = data.get(data.size() - 1).Customer_Id;
                                getCustomers();
                            }
                            else{
                                getClients();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();

                } else {
                    getClients();
                }
            }
            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }
            @Override
            public void onErrorMessage(String message) {

            }
        });

    }

    /**
     * cllient list api call
     */
    private void getClients() {
        //Clientproduct relationship API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        params.put("index",client_index+"");
        ApiService.getInstance().makeApiCall(ApiConstants.AppClientDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final ClientResponse clientResponse = gson.fromJson(objects.toString(), ClientResponse.class);
                if (clientResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            for (int i = 0; i < clientResponse.getBody().length; i++) {
                                try {
                                    ClientResponseBody body = clientResponse.getBody()[i];
                                    Client client = new Client();
                                    try {
                                        client.Client_Number = Integer.parseInt(body.getClient_number());
                                    } catch (Exception e) { }
                                    try {
                                        client.Client_Prefix = body.getClientprefix();
                                    } catch (Exception e) { }
                                    try {
                                        client.Client_First_Name = body.getClientfirstname();
                                    } catch (Exception e) { }
                                    try {
                                        client.Client_Last_Name = body.getClientlastname();
                                    } catch (Exception e) {}
                                    try {
                                        client.Client_Gender = body.getClientgender();
                                    } catch (Exception e) { }
                                    try {
                                        client.Client_Email = body.getClientemail();
                                    } catch (Exception e) {  }
                                    try {
                                        client.Client_Phone = Long.parseLong(body.getClientphone());
                                    } catch (Exception e) {}
                                    try {
                                        client.Client_Mobile = Long.parseLong(body.getClientphone());
                                    } catch (Exception e) {}
                                    try {
                                        client.Client_Fax = body.getClientfax();
                                    } catch (Exception e) {}
                                    try {
                                        client.Speciality = body.getSpecialty();
                                    } catch (Exception e) {}
                                    try {
                                        client.Marketclass = body.getMarketclass();
                                    } catch (Exception e) {}
                                    try {
                                        client.STEclass = body.getSteclass();
                                    } catch (Exception e) {}
                                    try {
                                        client.Type = body.getType();
                                    } catch (Exception e) {}
                                    try {
                                        client.Nationality = body.getNationality();
                                    } catch (Exception e) {}
                                    try {
                                        if (body.getStatus().equals("1"))
                                            client.Status = true;
                                        else
                                            client.Status = false;
                                    } catch (Exception e) {}
                                    try {
                                        if (body.getVisit().equals("1"))
                                            client.Visit = true;
                                        else
                                            client.Visit = false;
                                    } catch (Exception e) {}
                                    try {
                                        client.Account_Manager = body.getAccountmanager();
                                    } catch (Exception e) {}
                                    try {
                                        client.Remarks = body.getRemarks();
                                    } catch (Exception e) {}
                                    clients.add(client);
                                } catch (Exception e) {}
                            }
                            TransactionListener<List<Client>> onClientsSavedListener = new TransactionListener<List<Client>>() {
                                @Override
                                public void onResultReceived(List<Client> result) {

                                }

                                @Override
                                public boolean onReady(BaseTransaction<List<Client>> transaction) {
                                    return false;
                                }

                                @Override
                                public boolean hasResult(BaseTransaction<List<Client>> transaction, List<Client> result) {
                                    client_customers.clear();
                                    for (int k = 0; k < clientResponse.getBody1().length; k++) {
                                        Client_Customer client_customer = new Client_Customer();
                                        ClientResponseBodyOne bodyOne = clientResponse.getBody1()[k];
                                        client_customer.client = new Client();
                                        client_customer.client.Client_Number = Integer.parseInt(bodyOne.getClient_number());
                                        client_customer.customer = new Customer();
                                        client_customer.customer.Customer_Id = Integer.parseInt(bodyOne.getCustomerid());
                                        client_customers.add(client_customer);
                                    }
                                    if (client_customers != null && client_customers.size() > 0) {
                                        TransactionManager.getInstance()
                                                .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(client_customers).result(onClientCustomerSavedListener)));
                                        client_index = Integer.parseInt(clientResponse.getBody1()[clientResponse.getBody1().length - 1].getRowNo());
                                        getClients();
                                    }
                                    return true;
                                }
                            };

                            if (clients != null && clients.size() > 0) {
                                count = count + "\n" + "Clients-" + clients.size();
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(clients).result(onClientsSavedListener)));
                            } else {
                                getClientproduct();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();


                } else {
                    getClientproduct();
                }
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }

            @Override
            public void onErrorMessage(String message) {


            }
        });

    }

    /**
     * client - product api call
     */
    private void getClientproduct() {
        //Clientproduct relationship API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        ApiService.getInstance().makeApiCall(ApiConstants.AppClietProductDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final ClientproductResponse clientproductResponse = gson.fromJson(objects.toString(), ClientproductResponse.class);
                if (clientproductResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            int size = clientproductResponse.getBody().length;
                            ArrayList<Client_Product> data = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                Client_Product client_product = new Client_Product();
                                ClientproductResponseBody body = clientproductResponse.getBody()[i];
                                try {
                                    client_product.Client_Productid_Id = Integer.parseInt(body.getClproductid());
                                } catch (Exception e) {}
                                try {
                                    client_product.client = new Client();
                                    client_product.client.Client_Number = Integer.parseInt(body.getClientno());
                                } catch (Exception e) {}
                                try {
                                    client_product.product = new Product();
                                    client_product.product.Product_Number = body.getProductid();
                                } catch (Exception e) {}
                                try {
                                    client_product.customer = new Customer();
                                    client_product.customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {}
                                try {
                                    client_product.ExpiryDate = body.getExpirydate();
                                } catch (Exception e) {}
                                try {
                                    client_product.remarks = body.getRemarks();
                                } catch (Exception e) {}
                                try {
                                    client_product.status = body.getStatus();
                                } catch (Exception e) {}
                                data.add(client_product);
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "Client Product-" + data.size();
                                Delete.table(Client_Product.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onClientProductSavedListener)));
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                }
                getRoutePlan();
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }

            @Override
            public void onErrorMessage(String message) {
            }
        });

    }

    /**
     * route plan api
     */
    private void getRoutePlan() {
        //routeplan ApI

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        String date = new StringBuilder()
                .append(year).append("-").append(month + 1).append("-")
                .append(day).append(" ").toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        params.put("date", date);
        params.put("part", "4");
        //part<part=4 for first zinking; part=5 for second zinking >
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewRoutePlaneDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final RoutePlanResponse routePlanResponse = gson.fromJson(objects.toString(), RoutePlanResponse.class);
                if (routePlanResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            int size = routePlanResponse.getBody().length;
                            ArrayList<RoutePlan> data = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                RoutePlan routePlan = new RoutePlan();
                                RoutePlanResponseBody body = routePlanResponse.getBody()[i];
                                try {
                                    routePlan.Route_Plan_Number = Integer.parseInt(body.getRoutplanno());
                                } catch (Exception e) { }
                                try {
                                    routePlan.routeno = Integer.parseInt(body.getRouteno());
                                } catch (Exception e) {}
                                try {
                                    routePlan.Date = body.getDate();
                                } catch (Exception e) {}
                                try {
                                    routePlan.status = Integer.parseInt(body.getStatus());
                                } catch (Exception e) {
                                    routePlan.status = 0;
                                }
                                try {
                                    routePlan.Creation_Date = body.getCreateddate();
                                } catch (Exception e) {}
                                try {
                                    routePlan.Prepareduser = body.getPrepareduser();
                                } catch (Exception e) {}
                                try {
                                    routePlan.authuser = body.getAuthuser();
                                } catch (Exception e) {}
                                try {
                                    routePlan.Auth_Date = body.getAuthdate();
                                } catch (Exception e) {}
                                try {
                                    routePlan.Visittype = Integer.parseInt(body.getVisitype());
                                } catch (Exception e) {}
                                try {
                                    routePlan.Remarks = body.getRemarks();
                                } catch (Exception e) {}
                                try {
                                    routePlan.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {}
                                try {
                                    routePlan.User_Id = Integer.parseInt(body.getUserid());
                                } catch (Exception e) {}
                                try {
                                    routePlan.Clientwork_Id = Integer.parseInt(body.getWorkcalanderid());
                                } catch (Exception e) {}
                                try {
                                    routePlan.PreparedBy = Integer.parseInt(body.getPreparedby());
                                } catch (Exception e) {}
                                try {
                                    routePlan.Client_Number = Integer.parseInt(body.getClientno());
                                } catch (Exception e) {}
                                try {
                                    routePlan.AuthorizedBy = Integer.parseInt(body.getAuthorizedby());
                                } catch (Exception e) {}
                                try {
                                    routePlan.client = new Client();
                                    routePlan.client.Client_Number = Integer.parseInt(body.getClientno());
                                } catch (Exception e) {}
                                try {
                                    routePlan.customer = new Customer();
                                    routePlan.customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {}
                                try {
                                    routePlan.client_work_cal = new Client_work_cal();
                                    routePlan.client_work_cal.Clientwork_Id = Integer.parseInt(body.getWorkcalanderid());
                                    routePlan.preparedBy = myuser;
                                } catch (Exception e) {}
                                try {
                                    routePlan.preparedBy = myuser;
                                } catch (Exception e) {}
                                data.add(routePlan);
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "Route Plan-" + data.size();
                                Delete.table(RoutePlan.class);
                                RoutePlan r=new RoutePlan();
                                r.Route_Plan_Number=-1;
                                r.save();
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onRoutePlanSavedListener)));
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                }
                getReminder();
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }

            @Override
            public void onErrorMessage(String message) {

            }
        });
    }

    /**
     * remainder api
     */
    private void getReminder() {
        //Reminder API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewReminderDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final ReminderResponse reminderResponse = gson.fromJson(objects.toString(), ReminderResponse.class);
                if (reminderResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            int size = reminderResponse.getBody().length;
                            ArrayList<Reminder> data = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                Reminder reminder = new Reminder();
                                ReminderResponseBody body = reminderResponse.getBody()[i];
                                try {
                                    reminder.Pkey_id = Integer.parseInt(body.getPkeyid());
                                } catch (Exception e) {    }
                                try {
                                    reminder.date = body.getDate();
                                } catch (Exception e) {}
                                try {
                                    reminder.status = Integer.parseInt(body.getStatus());
                                } catch (Exception e) {}
                                try {
                                    reminder.message = body.getMessage();
                                } catch (Exception e) {}
                                try {
                                    reminder.Remarks = body.getRemarks();
                                } catch (Exception e) {}
                                data.add(reminder);
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "Reminder-" + data.size();
                                Delete.table(Reminder.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onReminderSavedListener)));
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                }
                getAppoinments();
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }

            @Override
            public void onErrorMessage(String message) {
            }
        });

    }

    /**
     * appointment api
     */
    private void getAppoinments() {
        //Appoinment API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid + "");
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewAppointmentDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final AppoinmentResponse appoinmentResponse = gson.fromJson(objects.toString(), AppoinmentResponse.class);
                if (appoinmentResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            int size = appoinmentResponse.getBody().length;
                            ArrayList<Appointment> data = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                AppoinmentResponseBody body = appoinmentResponse.getBody()[i];
                                try {
                                    Appointment appointment = new Appointment();
                                    try {
                                        appointment.Appointment_Date = body.getAppdate();
                                    } catch (Exception e) {}
                                    try {
                                        appointment.client = new Client();
                                        appointment.client.Client_Number = Integer.parseInt(body.getClientno());
                                    } catch (Exception e) {}
                                    try {
                                        appointment.customer = new Customer();
                                        appointment.customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                    } catch (Exception e) {}
                                    try {
                                        appointment.routePlan = new RoutePlan();
                                        appointment.routePlan.Route_Plan_Number = Integer.parseInt(body.getRoutplanno());
                                    } catch (Exception e) {}
                                    try {
                                        appointment.Remarks = body.getRemarks();
                                    } catch (Exception e) {}
                                    data.add(appointment);
                                } catch (Exception e) {}
                            }
                            if (data != null && data.size() > 0) {
                                count = count + "\n" + "Appointment-" + data.size();
                                Delete.table(Appointment.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onAppointmentSavedListener)));
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();

                }
                getSurveyQns();
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();
            }

            @Override
            public void onErrorMessage(String message) {

            }
        });
    }


    /**
     * Survey Qns api
     */
    private void getSurveyQns() {
        //UserRegion API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        ApiService.getInstance().makeApiCall(ApiConstants.AppSurveyDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                final SurveyDetailsJson surveyDetailsJson = gson.fromJson(objects.toString(), SurveyDetailsJson.class);
                if (surveyDetailsJson.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                count = count + "\n" + "AppUserRegionDetails-" + surveyDetailsJson.getBody().size();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int r = 0; r < surveyDetailsJson.getBody().size(); r++) {
                                SurveyMaster surveyMaster = new SurveyMaster();
                                try {
                                    surveyMaster.Survey_Id = Integer.parseInt(surveyDetailsJson.getBody().get(r).getSurveyid());
                                } catch (NumberFormatException e) { }
                                try {
                                    surveyMaster.Question = surveyDetailsJson.getBody().get(r).getQuestion();
                                } catch (Exception e) {}
                                try {
                                    surveyMaster.Option1 = surveyDetailsJson.getBody().get(r).getOption1();
                                } catch (Exception e) { }
                                try {
                                    surveyMaster.Option2 = surveyDetailsJson.getBody().get(r).getOption2();
                                } catch (Exception e) {}
                                try {
                                    surveyMaster.Option3 = surveyDetailsJson.getBody().get(r).getOption3();
                                } catch (Exception e) { }
                                try {
                                    surveyMaster.Option4 = surveyDetailsJson.getBody().get(r).getOption4();
                                } catch (Exception e) { }
                                try {
                                    surveyMaster.Type = Integer.parseInt(surveyDetailsJson.getBody().get(r).getStatus());
                                } catch (NumberFormatException e) { }
                                try {
                                    surveyMaster.Remarks = surveyDetailsJson.getBody().get(r).getRemarks();
                                } catch (Exception e) { }
                                try {
                                    surveyMaster.surveynumber = surveyDetailsJson.getBody().get(r).getSurveynumber();
                                } catch (Exception e) { }
                                try {
                                    surveyMaster.validFrom = surveyDetailsJson.getBody().get(r).getDatefrom();
                                } catch (Exception e) {}
                                try {
                                    surveyMaster.validTo = surveyDetailsJson.getBody().get(r).getDateto();
                                } catch (Exception e) {}
                                surveyMaster.save();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Log.d("COUNT", count);
                            moveToNextActivity();
                        }
                    }.execute();

                } else {
                    moveToNextActivity();
                }
            }

            @Override
            public void onError(Object objects) {
                showMessage("Login and Syncing failed, Try again", tv_sign_in);
                dissmissProgressDialog();

            }

            @Override
            public void onErrorMessage(String message) {

            }
        });

    }

    /**
     * finish ogin and move to product download screen.
     */
    private void moveToNextActivity() {
        if (isLoginComplete)
            finish();
        isLoginComplete = true;
        myuser.save();
        dissmissProgressDialog();
        Toast.makeText(getApplicationContext(), "Successfully Synced", Toast.LENGTH_LONG).show();
        PreferenceUtil.getIntsance().setLastSyncDate(Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd hh:mm a"));
        AppControllerUtil.getPrefs().edit().putString("user", et_user_name.getText().toString()).apply();
        PreferenceUtil.getIntsance().setSyncManul(false);
        SetAlarm();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void SetAlarm()
    {
        try {
            //Create a new PendingIntent and add it to the AlarmManager
            Intent intent = new Intent(this, MyAlaramService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this,
                    0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager =(AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);

        } catch (Exception e) {}
        catch (Throwable e){

        }
    }
}
