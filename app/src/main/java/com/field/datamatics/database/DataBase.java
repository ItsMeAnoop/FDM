package com.field.datamatics.database;

import android.util.Log;

import com.field.datamatics.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * Created by Jith on 10/17/2015.
 */
public class DataBase {

    private static final String TAG = DataBase.class.getName();
    private static final int regionInitialId = 1;
    private static final int regionFinalId = 11;
    private static final int customerInitialId = 1;
    private static final int customerFinalId = 11;
    private static final int clientInitialId = 1;
    private static final int clientFinalId = 501;
    private static final int productInitialId = 1;
    private static final int productFinalId = 5001;
    private static final int surveyInitionId = 1;
    private static final int surveyFinalId = 51;


    public static void createDummyData() throws InterruptedException {
        createUser();
        createUserRegion();
        createCustomer();
        createClient();
        Thread.sleep(1000);
        createClientCustomer();
        Thread.sleep(1000);
        createWorkCalendar();
        Thread.sleep(1000);
        createRoutPlan();
        createProducts();
        createSurvey();

    }

    private static void createUser() {
        User user = new User();
        user.UserNumber = 100;
        user.Password = "123456";
        user.Profile = "profile";
        user.First_Name = "David";
        user.Last_Name = "Warner";
        user.AddressNo = 2321;
        user.Department = "Mech";
        user.Emailid1 = "asad@gmail.com";
        user.Emailid2 = "dfd@gmail.com";
        user.Phone1 = 4343;
        user.Phone2 = 23;
        user.Userstatus = true;
        user.Status = false;
        user.Geocordinat_home = "24.29437,54.642735";
        user.Geocordinate_office = "24.225321,54.814224";
        user.Remarks = "";
        user.save();
        Log.i(TAG, "Inserted user");
    }

    private static void createUserRegion() {
        String[] repo_manger = {"M1", "M2", "M3", "M4", "M5"};
        int k = 0;
        ArrayList<UserRegion> data = new ArrayList<>();
        for (int i = regionInitialId; i < regionFinalId; i++) {
            UserRegion userRegion = new UserRegion();
            userRegion.Userregion_Id = i;
            userRegion.user = new User();
            userRegion.user.User_Id = 1;
            userRegion.Region = "Region_" + i;
            userRegion.Reporting_Manager = repo_manger[k++];
            if (k == 4)
                k = 0;
            userRegion.Date_From = "2015-10-15";
            userRegion.Date_To = "2015-12-30";
            userRegion.Remarks = "Remarks_" + i;
            data.add(userRegion);
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted UserRegion");
    }

    private static void createCustomer() {
        ArrayList<String> routeLocations = new ArrayList<>();
        routeLocations.add("25.332059,55.466194");
        routeLocations.add("25.327539,55.430832");
        routeLocations.add("25.188611,55.257454");
        routeLocations.add("25.048081,55.117464");
        routeLocations.add("25.001886,55.469284");
        routeLocations.add("25.273086,55.339851");
        routeLocations.add("25.144932,55.895004");
        routeLocations.add("25.039219,55.587387");
        routeLocations.add("25.16855,55.228958");
        routeLocations.add("25.120067,55.37178");
        ArrayList<Customer> data = new ArrayList<>();
        int regionId = regionInitialId;
        int rlc = 0;
        for (int j = customerInitialId; j < customerFinalId; j++) {
            Customer customer = new Customer();
            customer.Customer_Id = j;
            customer.Customer_Name = "c_" + j;
            customer.Customer_Group = "c_g_" + j;
            customer.Address1 = "address_" + j;
            customer.Street = "c_street_" + j;
            customer.Location = "c_loc_" + j;
            customer.PO = 12;
            if (regionId == regionFinalId - 1) {
                regionId = regionInitialId;
            }
            customer.Region = "Region_" + regionId;
            if (rlc == routeLocations.size() - 1)
                rlc = 0;
            customer.Geo_Cordinates = routeLocations.get(rlc++);
            customer.Country = "India";
            customer.status = true;
            customer.Remarks = "sdfsdfsdf";
            data.add(customer);
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted Customer");
    }

    private static void createClient() {
        ArrayList<Client> data = new ArrayList<>();
        for (int n = clientInitialId; n < clientFinalId; n++) {
            Client client = new Client();
            client.Client_Number = n;
            client.Address_Number_JDE = n;
            client.Client_Prefix = "Dr";
            client.Client_First_Name = "Client_" + n;
            client.Client_Last_Name = "last_" + n;
            client.Client_Gender = "male";
            client.Client_Email = "cemail_" + n + "gmail.com";
            client.Client_Phone = 232;
            client.Client_Mobile = 354;
            client.Client_Fax = "FAX";
            client.Speciality = "SPECIAL";
            client.Marketclass = "M";
            client.STEclass = "STE";
            client.Type = "type";
            client.Nationality = "Indian";
            client.Status = true;
            client.Visit = false;
            client.Account_Manager = "Manager";
            client.Remarks = "Remark";
            data.add(client);

        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted Client.");
    }

    private static void createClientCustomer() {
        List<Client> clients = new Select().from(Client.class).queryList();
        List<Customer> customers = new Select().from(Customer.class).queryList();
        System.out.print("count client_customer " + clients.size() + "," + customers.size());
        ArrayList<Client_Customer> data = new ArrayList<>();
        Random random = new Random();
        int customerId = 0;
        for (Client client : clients) {
            int randomNum = random.nextInt(4) + 2;
            for (int i = 0; i < randomNum; i++) {
                if (customerId == customers.size() - 1)
                    customerId = 0;
                Client_Customer client_customer = new Client_Customer();
                client_customer.customer = customers.get(customerId++);
                client_customer.client = client;
                data.add(client_customer);
            }

        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted Client_Customer");

    }

    private static void createWorkCalendar() {
        /*String[] days = {"SUN", "MON", "TUE", "WED", "THU", "SAT"};
        ArrayList<Client_work_cal> data = new ArrayList<>();
        int p = 0;
        List<Client_Customer> clients = new Select().from(Client_Customer.class).queryList();
        int work_calendar_id = 0;

        for (Client_Customer client : clients) {
            if (p == 6)
                p = 0;
            Client_work_cal client_work_cal = new Client_work_cal();
            client_work_cal.Clientwork_Id = ++work_calendar_id;
            client_work_cal.client = client.client;
            client_work_cal.customer = client.customer;
            client_work_cal.Availabledays = days[p++];
            client_work_cal.Available_Time_From = "08:00:00";
            client_work_cal.Available_Time_To = "11:00:00";
            client_work_cal.Created_Date = "";
            data.add(client_work_cal);
            if (work_calendar_id == 1000) break;
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted Client_Work_Cal");*/
    }

    private static void createProducts() {
        ArrayList<Product> data = new ArrayList<>();
        for (int i = productInitialId; i < productFinalId; i++) {
            Product product = new Product();
            product.Product_Number = "" + i;
            product.Product_Description = "Product_" + i;
            product.Barcode = "Barcode";
            product.Category = "Category_" + i % 5;
            product.Category_desc = "sffdf";
            product.Subcategory = "edfdf";
            product.Subcat_desc = "fdfdf";
            product.UOM = "fdf";
            product.Pack_Size = "dfs";
            byte[] b = {0};
            product.Photo = new Blob(b);
            product.Narration = "sdfsfsdf";
            product.Supplier_Name = "gfgfg";
            product.Supplier_desc = "dfdfdf";
            product.Stock_Availability = 4;
            product.Usage = "srdesfdfgd";
            product.On_PO = 43;
            product.Department = "sdfsf";
            product.Department_desc = "ertregsdfsadfas";
            product.section = "dsaddsd";
            product.Section_desc = "rdsgghhgj";
            product.Family = "wewqdads";
            product.Family_desc = "sffgf";
            product.Subfamily = "efgfgfg";
            product.Subfamily_desc = "eetsd";
            product.Status = false;
            product.Remarks = "dsfsnksdnfkldsbf";
            data.add(product);
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted Products");
    }

    private static void createSurvey() {
        ArrayList<SurveyMaster> data = new ArrayList<>();
        for (int a = surveyInitionId; a < surveyFinalId; a++) {
            SurveyMaster surveyMaster = new SurveyMaster();
            surveyMaster.Survey_Id = a;
            surveyMaster.Question = "Queston_" + a;
            surveyMaster.validFrom = "2015-08-01";
            surveyMaster.validTo = "2016-01-01";
            if (a % 5 == 0) {
                surveyMaster.Type = 1;
            } else {
                surveyMaster.Type = 2;
                surveyMaster.Option1 = "Option one";
                surveyMaster.Option2 = "Option two";
                surveyMaster.Option3 = "Option three";
                surveyMaster.Option4 = "Option four";
            }
            surveyMaster.Remarks = "Remark_" + a;
            data.add(surveyMaster);
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted SurveyMaster");
    }

    private static void createRoutPlan() {
        /*Calendar calendar = Calendar.getInstance();
        int today;
        int routePlanNumber = 0;
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        List<Client_work_cal> client_work_cals = new Select().from(Client_work_cal.class).orderBy(true, Client_work_cal$Table.CLIENT_CLIENT_NUMBER).queryList();
        ArrayList<RoutePlan> data = new ArrayList<>();
        for (Client_work_cal cwc : client_work_cals) {
            today = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (today == 5)
                today++;
            if (cwc.Availabledays.equals(days[today])) {
                RoutePlan routePlan = new RoutePlan();
                routePlan.Route_Plan_Number = ++routePlanNumber;
                routePlan.routeno = routePlan.Route_Plan_Number;
                routePlan.Date = Utilities.dateInSqliteFormat(calendar);
                routePlan.client = cwc.client;
                routePlan.customer = cwc.customer;
                routePlan.user = new User(1);
                routePlan.client_work_cal = cwc;
                routePlan.Creation_Date = routePlan.Date;
                routePlan.preparedBy = routePlan.user;
                routePlan.Prepareduser = "Someone";
                routePlan.AuthorizedBy = routePlanNumber;
                if(today%2==0)
                    routePlan.status=0;
                else
                    routePlan.status=2;
                data.add(routePlan);
                if (routePlanNumber % 15 == 0) {
                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(data)));
        Log.i(TAG, "Inserted RoutPlan");*/
    }

}
