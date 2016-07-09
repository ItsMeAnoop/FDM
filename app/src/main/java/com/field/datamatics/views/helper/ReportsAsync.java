package com.field.datamatics.views.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.CustomVisitedDetails;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.database.User;
import com.field.datamatics.database.VisitedDetails;
import com.field.datamatics.database.VisitedDetails$Table;
import com.field.datamatics.utils.Utilities;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * Created by Jith on 10/26/2015.
 */
public class ReportsAsync {

    private Context mContext;
    private ProgressDialog pd;
    private String path;

    private static final int FAIL = -1;
    private static final int SUCCESS = 1;
    private static final int NO_DATA = 0;
    private File directory;

    public ReportsAsync(Context context) {
        mContext = context;
        pd = new ProgressDialog(mContext);
        pd.setMessage("Generating report... Please wait");
        pd.setCancelable(false);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && Environment.isExternalStorageRemovable())
            directory = new File(mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + "Reports");
        else directory = new File(Constants.EXCEL_PATH);

        if (!directory.exists())
            directory.mkdirs();
    }

    public void generateVisitedDetails() {
        new GenerateVisitedDetails().execute();
    }

    public void generatePendingList() {
        new GeneratePendingVisit().execute();
    }

    public void generateTodaysVisit() {
        new GenerateTodaysVisit().execute();
    }

    public void geneateSchedule() {

    }


    // generate visited details
    private class GenerateVisitedDetails extends AsyncTask<Void, Void, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!pd.isShowing())
                pd.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            int success = FAIL;
            path = "";
            ColumnAlias c1 = ColumnAlias.column("Client." + Client$Table.CLIENT_FIRST_NAME).as("clientFirstName");
            ColumnAlias c2 = ColumnAlias.column("Client." + Client$Table.CLIENT_LAST_NAME).as("clientLastName");
            ColumnAlias c3 = ColumnAlias.column("Customer." + Customer$Table.CUSTOMER_NAME).as("customerName");
            ColumnAlias c4 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.VISITED_DATE).as("visitedDate");
            ColumnAlias c5 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.CHECKINTIME).as("checkinTime");
            ColumnAlias c6 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.CHECKOUTTIME).as("checkOutTime");
            ColumnAlias c7 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.SESSIONEND).as("sessionEndTime");
            ColumnAlias c8 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.FEEDBACK).as("feedback");
            ColumnAlias c9 = ColumnAlias.column("VisitedDetails." + VisitedDetails$Table.NOTES).as("notes");

            List<CustomVisitedDetails> data = new Select(c1, c2, c3, c4, c5, c6, c7, c8, c9)
                    .from(VisitedDetails.class)
                    .join(com.field.datamatics.database.RoutePlan.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.ROUTE_PLAN_NUMBER))
                            .is(ColumnAlias.columnWithTable("VisitedDetails", VisitedDetails$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER)))
                    .join(com.field.datamatics.database.Client.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CLIENT_CLIENT_NUMBER))
                            .is(ColumnAlias.columnWithTable("Client", Client$Table.CLIENT_NUMBER)))
                    .join(com.field.datamatics.database.Customer.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CUSTOMER_CUSTOMER_ID))
                            .is(ColumnAlias.columnWithTable("Customer", Customer$Table.CUSTOMER_ID)))
                    .where("strftime('%m', VisitedDetails.Visited_Date) = ? and strftime('%Y', VisitedDetails.Visited_Date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                    .queryCustomList(CustomVisitedDetails.class);
            User user = new Select().from(User.class).querySingle();

            if (data == null || data.size() == 0)
                return NO_DATA;


            File file = new File(directory, "Vis_" + Utilities.dateToString(Calendar.getInstance(), "yyyyMMdd_HH:mm:ss") + ".xls");

            path = file.getPath();
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                WritableSheet sheet = workbook.createSheet("Visited Details", 0);

                try {
                    sheet.addCell(new Label(0, 0, "Client Name"));
                    sheet.addCell(new Label(1, 0, "Customer Name"));
                    sheet.addCell(new Label(2, 0, "MR Name"));
                    sheet.addCell(new Label(3, 0, "Visited Date"));
                    sheet.addCell(new Label(4, 0, "Check in Time"));
                    sheet.addCell(new Label(5, 0, "Check out Time"));
                    sheet.addCell(new Label(6, 0, "Session end Time"));
                    sheet.addCell(new Label(7, 0, "Feedback"));
                    sheet.addCell(new Label(8, 0, "Notes"));
                    int i = 0;
                    for (CustomVisitedDetails vd : data) {
                        i++;
                        sheet.addCell(new Label(0, i, vd.getName()));
                        sheet.addCell(new Label(1, i, vd.customerName));
                        sheet.addCell(new Label(2, i, user.getName()));
                        sheet.addCell(new Label(3, i, TextUtils.isEmpty(vd.visitedDate) ? "" : Utilities.dateToString(vd.visitedDate, "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy")));
                        sheet.addCell(new Label(4, i, TextUtils.isEmpty(vd.checkinTime) ? "" : Utilities.dateToString(vd.checkinTime, "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm:ss a")));
                        sheet.addCell(new Label(5, i, TextUtils.isEmpty(vd.checkOutTime) ? "" : Utilities.dateToString(vd.checkOutTime, "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm:ss a")));
                        sheet.addCell(new Label(6, i, TextUtils.isEmpty(vd.sessionEndTime) ? "" : Utilities.dateToString(vd.sessionEndTime, "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm:ss a")));
                        sheet.addCell(new Label(7, i, vd.feedback));
                        sheet.addCell(new Label(8, i, vd.notes));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                workbook.write();
                success = SUCCESS;
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if (pd.isShowing()) pd.dismiss();
            if (s == SUCCESS) {
                showAlertDialog();
            } else if (s == NO_DATA) {
                Utilities.showAlertDialog(mContext, "No data is found to create Report!");
            } else {
                Utilities.showAlertDialog(mContext, "Failed to create Report!");
            }
        }
    }

    //generate pending visit

    private class GeneratePendingVisit extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!pd.isShowing())
                pd.show();
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if (pd.isShowing()) pd.dismiss();
            if (s == SUCCESS) {
                showAlertDialog();
            } else if (s == NO_DATA) {
                Utilities.showAlertDialog(mContext, "No data is found to create Report!");
            } else {
                Utilities.showAlertDialog(mContext, "Failed to create Report!");
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            int success = FAIL;
            path = "";
            List<RoutePlan> data = new Select().from(RoutePlan.class)
                    .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH))).and(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                    .queryList();
            if (data == null || data.size() == 0)
                return NO_DATA;


            File file = new File(directory, "Pending_" + Utilities.dateToString(Calendar.getInstance(), "yyyyMMdd_HH:mm:ss") + ".xls");

            path = file.getPath();
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                WritableSheet sheet = workbook.createSheet("Visited Details", 0);

                try {
                    sheet.addCell(new Label(0, 0, "Routeplan no"));
                    sheet.addCell(new Label(1, 0, "Routeplan date"));
                    sheet.addCell(new Label(2, 0, "Customer no"));
                    sheet.addCell(new Label(3, 0, "Customer name"));
                    sheet.addCell(new Label(4, 0, "Client no"));
                    sheet.addCell(new Label(5, 0, "Client name"));
                    int i = 0;
                    for (RoutePlan rp : data) {
                        i++;
                        sheet.addCell(new Label(0, i, String.valueOf(rp.Route_Plan_Number)));
                        sheet.addCell(new Label(1, i, Utilities.dateToString(rp.Date, "yyyy-MM-dd", "MMM dd, yyyy")));
                        sheet.addCell(new Label(2, i, String.valueOf(rp.customer.Customer_Id)));
                        sheet.addCell(new Label(3, i, rp.customer.Customer_Name));
                        sheet.addCell(new Label(4, i, String.valueOf(rp.client.Client_Number)));
                        sheet.addCell(new Label(5, i, rp.client.getName()));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                workbook.write();
                success = SUCCESS;
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }
    }

    //generate today's visit
    private class GenerateTodaysVisit extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!pd.isShowing())
                pd.show();
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if (pd.isShowing()) pd.dismiss();
            if (s == SUCCESS) {
                showAlertDialog();
            } else if (s == NO_DATA) {
                Utilities.showAlertDialog(mContext, "No data is found to create Report!");
            } else {
                Utilities.showAlertDialog(mContext, "Failed to create Report!");
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            String today = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
            int success = FAIL;
            path = "";
            List<RoutePlan> data = new Select().from(RoutePlan.class)
                    .where(Condition.column(RoutePlan$Table.DATE).eq(today)).and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0))
                    .queryList();
            if (data == null || data.size() == 0)
                return NO_DATA;


            File file = new File(directory, "Today_" + Utilities.dateToString(Calendar.getInstance(), "yyyyMMdd_HH:mm:ss") + ".xls");

            path = file.getPath();
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                WritableSheet sheet = workbook.createSheet("Visited Details", 0);

                try {
                    sheet.addCell(new Label(0, 0, "Routeplan no"));
                    sheet.addCell(new Label(1, 0, "Routeplan date"));
                    sheet.addCell(new Label(2, 0, "Customer no"));
                    sheet.addCell(new Label(3, 0, "Customer name"));
                    sheet.addCell(new Label(4, 0, "Client no"));
                    sheet.addCell(new Label(5, 0, "Client name"));
                    int i = 0;
                    for (RoutePlan rp : data) {
                        i++;
                        sheet.addCell(new Label(0, i, String.valueOf(rp.Route_Plan_Number)));
                        sheet.addCell(new Label(1, i, Utilities.dateToString(rp.Date, "yyyy-MM-dd", "MMM dd, yyyy")));
                        sheet.addCell(new Label(2, i, String.valueOf(rp.customer.Customer_Id)));
                        sheet.addCell(new Label(3, i, rp.customer.Customer_Name));
                        sheet.addCell(new Label(4, i, String.valueOf(rp.client.Client_Number)));
                        sheet.addCell(new Label(5, i, rp.client.getName()));
                    }

                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                workbook.write();
                success = SUCCESS;
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }

    }

    private void showAlertDialog() {
        File file = new File(path);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Field Datamatics");
        builder.setMessage("Report generated successfully and the file has been stored as " + file.getPath() + "\nDo you want to open it now?");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (openFile())
                    dialog.cancel();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private boolean openFile() {
        File file = new File(path);
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(Utilities.fileExt(file.getName()).substring(1));
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Field Datamatics");
            builder.setMessage("You don't have an app that can open this file(.xls). Try searching Google Play for one that can.");
            builder.setIcon(R.drawable.ic_launcher);
            builder.setCancelable(false);
            builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q=office"));
                    mContext.startActivity(goToMarket);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
            return false;
        }
        return true;
    }
}
