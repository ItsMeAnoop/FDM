package com.field.datamatics.Services.appservices;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.AppoinmentResponse;
import com.field.datamatics.apimodels.AppoinmentResponseBody;
import com.field.datamatics.apimodels.ClientResponse;
import com.field.datamatics.apimodels.ClientResponseBody;
import com.field.datamatics.apimodels.ClientResponseBodyOne;
import com.field.datamatics.apimodels.ClientproductResponse;
import com.field.datamatics.apimodels.ClientproductResponseBody;
import com.field.datamatics.apimodels.CustomerResponse;
import com.field.datamatics.apimodels.CustomerResponseBody;
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
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.Client_Customer;
import com.field.datamatics.database.Client_Product;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.Product;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.database.SurveyMaster;
import com.field.datamatics.database.User;
import com.field.datamatics.database.UserRegion;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.interfaces.SyncingCallBack;
import com.field.datamatics.views.MainActivity;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anoop on 4/11/15.
 */
public class SecondSyncService extends Service {
    private Gson gson;
    private ArrayList<Client> clients = new ArrayList<Client>();
    private ArrayList<Client_Customer> client_customers = new ArrayList<>();
    private User myUser;
    private String mrid;
    private static SyncingCallBack callBack;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
    }
    public static Intent getIntent(SyncingCallBack call,Activity activity){
        callBack =call;
        return new Intent(activity,SecondSyncService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readUser();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void readUser() {
        myUser = new Select().from(User.class).querySingle();
        mrid = myUser.UserNumber + "";
        getUserRegion();
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
            getCustomers();
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
            getClients();
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
            getClientproduct();
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
            getRoutePlan();
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
            //getReminder();
            getSurveyQns();
            return true;
        }
    };


    private void getUserRegion() {
        //UserRegion API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid);
        ApiService.getInstance().makeApiCall(ApiConstants.AppUserRegionDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                callBack.onPerecentage(50,true);
                final RegionResponse regionResponse = gson.fromJson(objects.toString(), RegionResponse.class);
                if (regionResponse.getStatus().equals(ApiConstants.STATUS)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            //save data to table
                            //regionResponse.getBody()[0]
                            ArrayList<UserRegion> data = new ArrayList<>();
                            int size = regionResponse.getBody().length;
                            if (size > 0)
                                for (int i = 0; i < size; i++) {
                                    try {
                                        UserRegion region = new UserRegion();
                                        RegionResponseBody body = regionResponse.getBody()[i];
                                        try {
                                            region.Userregion_Id = Integer.parseInt(body.getRegionid());
                                        } catch (Exception e) {

                                        }
                                        try {
                                            region.user = myUser;
                                        } catch (Exception e) {

                                        }
                                        try {
                                            region.Region = body.getRegion();
                                        } catch (Exception e) {

                                        }
                                        try {

                                            region.Reporting_Manager = body.getReporting_manager();
                                        } catch (Exception e) {

                                        }
                                        try {
                                            region.Remarks = body.getRemarks();
                                        } catch (Exception e) {

                                        }
                                        region.Date_From = body.getDate_from();
                                        region.Date_To = body.getDate_to();
                                        data.add(region);
                                    } catch (Exception e) {

                                    }
                                }
                            if (data != null && data.size() > 0) {
                                Delete.table(UserRegion.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data).result(onRegionSavedListener)));
                            } else {
                                getCustomers();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            //call next function
                            //getCustomers();
                        }
                    }.execute();

                } else {
                    //call next function
                    getCustomers();

                }


            }

            @Override
            public void onError(Object objects) {
                callBack.onPerecentage(50,false);
                stopSelf();

            }

            @Override
            public void onErrorMessage(String message) {

            }
        });
    }

    private void getCustomers() {
        //Customer API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid);
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewCustomerDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                callBack.onPerecentage(60,true);
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

                                } catch (Exception e) {

                                }
                                try {

                                    customer.Customer_Name = body.getCustomername();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Customer_Group = body.getCustomergroup();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Address1 = body.getAddress1();

                                } catch (Exception e) {

                                }
                                try {
                                    customer.Street = body.getStreet();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Location = body.getLocation();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.PO = Integer.parseInt(body.getCustomerid());

                                } catch (Exception e) {

                                }
                                try {
                                    customer.Region = body.getRegion();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Geo_Cordinates = body.getGeocordinates();


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Country = body.getCountry();


                                } catch (Exception e) {

                                }
                                try {
                                    if (body.getCustomerid().equals("1"))
                                        customer.status = true;
                                    else
                                        customer.status = false;


                                } catch (Exception e) {

                                }
                                try {
                                    customer.Remarks = body.getRemarks_customer();

                                } catch (Exception e) {

                                }

                                data.add(customer);
                            }

                            if (data != null && data.size() > 0) {
                                Delete.table(Customer.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data).result(onCustomerSaveListener)));
                            } else {
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
                callBack.onPerecentage(60,false);
                stopSelf();
            }

            @Override
            public void onErrorMessage(String message) {

            }
        });

    }

    private void getClients() {
        //Clientproduct relationship API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid);
        ApiService.getInstance().makeApiCall(ApiConstants.AppClientDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                callBack.onPerecentage(70,true);
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


                                    } catch (Exception e) {

                                    }
                                    try {
                                        //client.Address_Number_JDE=Long.parseLong(body.getNu);
                                        client.Client_Prefix = body.getClientprefix();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_First_Name = body.getClientfirstname();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Last_Name = body.getClientlastname();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Gender = body.getClientgender();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Email = body.getClientemail();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Phone = Long.parseLong(body.getClientphone());


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Mobile = Long.parseLong(body.getClientphone());


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Client_Fax = body.getClientfax();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Speciality = body.getSpecialty();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Marketclass = body.getMarketclass();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.STEclass = body.getSteclass();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Type = body.getType();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Nationality = body.getNationality();


                                    } catch (Exception e) {

                                    }
                                    try {
                                        if (body.getStatus().equals("1"))
                                            client.Status = true;
                                        else
                                            client.Status = false;


                                    } catch (Exception e) {

                                    }
                                    try {
                                        if (body.getVisit().equals("1"))
                                            client.Visit = true;
                                        else
                                            client.Visit = false;

                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Account_Manager = body.getAccountmanager();

                                    } catch (Exception e) {

                                    }
                                    try {
                                        client.Remarks = body.getRemarks();

                                    } catch (Exception e) {

                                    }
                                    clients.add(client);

                                } catch (Exception e) {

                                }


                            }
                            for (int k = 0; k < clientResponse.getBody1().length; k++) {
                                Client_Customer client_customer = new Client_Customer();
                                ClientResponseBodyOne bodyOne = clientResponse.getBody1()[k];
                                Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID)
                                        .eq(Integer.parseInt(bodyOne.getCustomerid()))).querySingle();
                                Client client1 = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER)
                                        .eq(Integer.parseInt(bodyOne.getClient_number()))).querySingle();
                                client_customer.client = client1;
                                client_customer.customer = customer;
                                client_customers.add(client_customer);
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
                                    Delete.table(Client_Customer.class);
                                    if (client_customers != null && client_customers.size() > 0) {
                                        TransactionManager.getInstance()
                                                .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(client_customers).result(onClientCustomerSavedListener)));
                                    }
                                    return true;
                                }
                            };

                            if (clients != null && clients.size() > 0) {
                                Delete.table(Client.class);
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
                    //failed to load clients
                    getClientproduct();
                }

            }

            @Override
            public void onError(Object objects) {
                callBack.onPerecentage(70,false);
                stopSelf();
            }

            @Override
            public void onErrorMessage(String message) {


            }
        });

    }

    private void getClientproduct() {
        //Clientproduct relationship API
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid+"");
        ApiService.getInstance().makeApiCall(ApiConstants.AppClietProductDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                callBack.onPerecentage(80,true);
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


                                } catch (Exception e) {

                                }
                                try {
                                    client_product.client = new Client();
                                    client_product.client.Client_Number = Integer.parseInt(body.getClientno());

                                } catch (Exception e) {

                                }
                                try {
                                    client_product.product = new Product();
                                    client_product.product.Product_Number = body.getProductid();

                                } catch (Exception e) {

                                }
                                try {
                                    client_product.customer = new Customer();
                                    client_product.customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {
                                }
                                try {
                                    client_product.ExpiryDate = body.getExpirydate();
                                } catch (Exception e) {
                                }
                                try {
                                    client_product.remarks = body.getRemarks();
                                } catch (Exception e) {
                                }
                                try {
                                    client_product.status = body.getStatus();
                                } catch (Exception e) {
                                }

                                data.add(client_product);
                            }
                            if (data != null && data.size() > 0) {
                                Delete.table(Client_Product.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onClientProductSavedListener)));
                            } else {
                                //getClientworkcalander();
                                getRoutePlan();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();


                } else {
                    //getClientworkcalander();
                    getRoutePlan();
                }

            }

            @Override
            public void onError(Object objects) {
                callBack.onPerecentage(80,false);
                stopSelf();
            }

            @Override
            public void onErrorMessage(String message) {


            }
        });

    }


    private void getRoutePlan() {
        //routeplan ApI

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = new StringBuilder()
                .append(year).append("-").append(month + 1).append("-")
                .append(day).append(" ").toString();


        RoutePlan routePlan = new Select().from(RoutePlan.class).where(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0)).orderBy(false, RoutePlan$Table.ROUTE_PLAN_NUMBER).querySingle();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("encription_key", ApiConstants.ENCRYPTION_KEY);
        params.put("mrid", mrid);
        if (routePlan != null && !routePlan.Auth_Date.equals(""))
            params.put("date", routePlan.Auth_Date + "");
        else
            params.put("date", date + "");
        params.put("part", "5");
        //part<part=4 for first zinking; part=5 for second zinking >
        ApiService.getInstance().makeApiCall(ApiConstants.AppViewRoutePlaneDetails, params, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                callBack.onPerecentage(90,true);
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
                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.status = Integer.parseInt(body.getStatus());
                                } catch (Exception e) {
                                    routePlan.status=0;
                                }
                                try {
                                    routePlan.routeno = Integer.parseInt(body.getRouteno());
                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Date = body.getDate();
                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Creation_Date = body.getCreateddate();

                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Prepareduser = body.getPrepareduser();


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.authuser = body.getAuthuser();


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Auth_Date = body.getAuthdate();


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Visittype = Integer.parseInt(body.getVisitype());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Remarks = body.getRemarks();


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Customer_Id = Integer.parseInt(body.getCustomerid());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.User_Id = Integer.parseInt(body.getUserid());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Clientwork_Id = Integer.parseInt(body.getWorkcalanderid());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.PreparedBy = Integer.parseInt(body.getPreparedby());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.Client_Number = Integer.parseInt(body.getClientno());


                                } catch (Exception e) {

                                }
                                try {
                                    routePlan.AuthorizedBy = Integer.parseInt(body.getAuthorizedby());


                                } catch (Exception e) {
                                }
                                try {
                                    routePlan.client = new Client();
                                    routePlan.client.Client_Number = Integer.parseInt(body.getClientno());
                                } catch (Exception e) {
                                }
                                try {
                                    routePlan.customer = new Customer();
                                    routePlan.customer.Customer_Id = Integer.parseInt(body.getCustomerid());
                                } catch (Exception e) {
                                }
                                try {
                                    routePlan.client_work_cal = new Client_work_cal();
                                    routePlan.client_work_cal.Clientwork_Id = Integer.parseInt(body.getWorkcalanderid());
                                    routePlan.preparedBy = myUser;

                                } catch (Exception e) {
                                }
                                try {
                                    routePlan.preparedBy = myUser;

                                } catch (Exception e) {

                                }
                                data.add(routePlan);
                            }
                            if (data != null && data.size() > 0) {
                                //Delete.table(RoutePlan.class);
                                TransactionManager.getInstance()
                                        .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)
                                                .result(onRoutePlanSavedListener)));
                            } else {
                                //getReminder();
                                getSurveyQns();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                } else {
                    //getReminder();
                    getSurveyQns();
                }
            }

            @Override
            public void onError(Object objects) {
                callBack.onPerecentage(90,false);
                stopSelf();
            }

            @Override
            public void onErrorMessage(String message) {

            }
        });
    }



    private void getSurveyQns(){
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
                            Delete.table(SurveyMaster.class);
                            for(int r=0;r<surveyDetailsJson.getBody().size();r++){
                                SurveyMaster surveyMaster=new SurveyMaster();
                                try {
                                    surveyMaster.Survey_Id=Integer.parseInt(surveyDetailsJson.getBody().get(r).getSurveyid());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Question=surveyDetailsJson.getBody().get(r).getQuestion();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Option1=surveyDetailsJson.getBody().get(r).getOption1();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Option2=surveyDetailsJson.getBody().get(r).getOption2();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Option3=surveyDetailsJson.getBody().get(r).getOption3();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Option4=surveyDetailsJson.getBody().get(r).getOption4();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Type=Integer.parseInt(surveyDetailsJson.getBody().get(r).getStatus());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.Remarks=surveyDetailsJson.getBody().get(r).getRemarks();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.surveynumber=surveyDetailsJson.getBody().get(r).getSurveynumber();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.validFrom=surveyDetailsJson.getBody().get(r).getDatefrom();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    surveyMaster.validTo=surveyDetailsJson.getBody().get(r).getDateto();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                surveyMaster.save();

                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            callBack.onPerecentage(100,true);
                            stopSelf();

                        }
                    }.execute();

                } else {
                    callBack.onPerecentage(100,true);
                    stopSelf();
                }


            }

            @Override
            public void onError(Object objects) {
                callBack.onPerecentage(100,false);
                stopSelf();

            }

            @Override
            public void onErrorMessage(String message) {

            }
        });

    }
}