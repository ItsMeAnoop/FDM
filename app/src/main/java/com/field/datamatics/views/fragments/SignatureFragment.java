package com.field.datamatics.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.Services.appservices.SignatureUploadService;
import com.field.datamatics.apimodels.ActivityModel;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.apimodels.DocumentModel;
import com.field.datamatics.apimodels.ProductQuatityModel;
import com.field.datamatics.apimodels.ReminderModel;
import com.field.datamatics.apimodels.SendPendingRemarks;
import com.field.datamatics.apimodels.SurveyModel;
import com.field.datamatics.apimodels.VisitJson;
import com.field.datamatics.apimodels.VisitModel;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Activities;
import com.field.datamatics.database.Activities$Table;
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Documents;
import com.field.datamatics.database.Documents$Table;
import com.field.datamatics.database.PendingRemarks;
import com.field.datamatics.database.PendingRemarks$Table;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.Reminder$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.database.SurveyDetails;
import com.field.datamatics.database.SurveyDetails$Table;
import com.field.datamatics.database.TimeSpendInMarket;
import com.field.datamatics.database.VisitedDetails;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.synctables.SyncVisitDetails;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.GPSTracker;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jith on 10/18/2015.
 */
public class SignatureFragment extends BaseFragment implements View.OnTouchListener {

    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    private EditText edtFeedback;
    public int count = 1;
    public String current = null;
    private Bitmap mBitmap = null;
    View mView;

    private String uniqueId;

    private Calendar visitedDate;

    private MainActivity mContainer;
    private String notes;
    private String signature_path;
    private boolean isSign = false;
    private GPSTracker gps;
    private String corndnt_in, cordnt_out;
    private RoutePlan routePlan;

    public SignatureFragment() {
    }

    public static SignatureFragment getInstance(Bundle data, Calendar visitedDate, String notes) {
        SignatureFragment f = new SignatureFragment();
        f.setArguments(data);
        f.visitedDate = visitedDate;
        f.notes = notes;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume()
                .edit();
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_SIGNATURE);
        edit.commit();
        View v = inflater.inflate(R.layout.fragment_signature, container, false);
        gps = new GPSTracker(getActivity());
        mContainer = (MainActivity) getActivity();
        final ImageView image = (ImageView) v.findViewById(R.id.image_sign);
        final CardView cv = (CardView) v.findViewById(R.id.cv);
        addFragmentTitle("SaveSignature");
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
        current = uniqueId + ".png";


        mContent = (LinearLayout) v.findViewById(R.id.linearLayout);
        mSignature = new signature(getActivity(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mClear = (Button) v.findViewById(R.id.clear);
        mGetSign = (Button) v.findViewById(R.id.getsign);
        edtFeedback = (EditText) v.findViewById(R.id.edt_feedback);
        // mGetSign.setEnabled(false);
        mCancel = (Button) v.findViewById(R.id.cancel);
        mView = mContent;

        edtFeedback.setOnTouchListener(this);


        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                isSign = false;
                //   mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Saved");
                if (isSign == false) {
                    showMessage("Please provide your signature", mCancel);
                    return;
                }

                mView.setDrawingCacheEnabled(true);
                mSignature.save(mView);
                Utilities.hideKeyboard(getActivity());
                saveVisitedDetails();
//                cv.setVisibility(View.GONE);
//                image.setImageBitmap(Utilities.getBitmap(mBitmap));
//                image.setVisibility(View.VISIBLE);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isSign = false;
                Log.v("log_tag", "Panel Canceled");
                Bundle b = new Bundle();
                b.putString("status", "cancel");
                Intent intent = new Intent();
                intent.putExtras(b);
                Utilities.hideKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            try {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(mBitmap);
                v.draw(canvas);

                //signature image
                signature_path = Environment.getExternalStorageDirectory() + File.separator + "sign.png";
                File file = new File(signature_path);
                FileOutputStream mFileOutStream = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void clear() {
            path.reset();
            invalidate();
            mBitmap = null;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            getParent().requestDisallowInterceptTouchEvent(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    isSign = true;
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    private void saveVisitedDetails() {
        mContainer.shouldUpdatePerformanceGraph = true;
        mContainer.shouldUpdateScore = true;
        long timeSpent = System.currentTimeMillis() - AppControllerUtil.getCheckinTime();
        SharedPreferences pref = AppControllerUtil.getPrefs();
        SharedPreferences.Editor editor = pref.edit();
        Calendar calendar = Calendar.getInstance();
        String month = calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR);
        String prevMonth = pref.getString(Constants.PREF_LAST_VISITED_MONTH, month);
        //Time Spent Calculation
        long prevTime;
        if (month.equals(prevMonth)) {
            prevTime = pref.getLong(Constants.PREF_TIME_SPENT, 0);
        } else {
            prevTime = 0;
        }
        editor.putLong(Constants.PREF_TIME_SPENT, prevTime + timeSpent);
        editor.putString(Constants.PREF_LAST_VISITED_MONTH, month);
        editor.commit();
        /**
         * Save time spent to database table
         */
        TimeSpendInMarket timeSpendInMarket = new TimeSpendInMarket();
        timeSpendInMarket.date_ = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
        timeSpendInMarket.time_spend = Math.round((timeSpent / 1000));
        timeSpendInMarket.save();
        mContainer.shouldUpdateTimeSpent = true;

        Bundle data = getArguments();
        String sessionEnd = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");

        final boolean isPending = data.getBoolean("is_pending", false);
        final int appointmentNumber = data.getInt("appointment_id");
        routePlan = new Select().from(RoutePlan.class)
                .where(Condition.column(RoutePlan$Table.ROUTE_PLAN_NUMBER)
                        .eq(data.getInt("route_plan_number"))).querySingle();
        if(routePlan==null){
            routePlan=new RoutePlan();
            routePlan.Route_Plan_Number=data.getInt("route_plan_number");
        }
        final VisitedDetails visitedDetails = new VisitedDetails();
        visitedDetails.routePlan = routePlan;
        visitedDetails.appointment = new Appointment();
        if (appointmentNumber > 0)
            visitedDetails.appointment.Appointment_Id = appointmentNumber;
        //visitedDetails.Visited_Date = Utilities.dateInSqliteFormat(visitedDate);
        visitedDetails.Visited_Date = data.getString("checkin");
        visitedDetails.checkintime = data.getString("checkin");
        visitedDetails.checkouttime = data.getString("checkout");
        visitedDetails.Sessionend = sessionEnd;
        visitedDetails.status = appointmentNumber <= 0 ? 3 : 1;
        if (mBitmap != null) {
            visitedDetails.signature = new Blob(Utilities.bitmapToByteArray(mBitmap));
        }
        visitedDetails.Geo_Cordinates_in = data.getString("checkin_coordinates");
        visitedDetails.Geo_Cordinates_out = data.getString("checkout_coordinates");
        String coordinates = "";
        Location loc = mContainer.mLastLocation;
        if (loc != null)
            coordinates = loc.getLatitude() + "," + loc.getLongitude();
        visitedDetails.Geo_Cordinates_sessout = coordinates;
        visitedDetails.Feedback = edtFeedback.getText().toString();
        visitedDetails.notes = notes;
        visitedDetails.save();

        routePlan.Visittype = 1;
        routePlan.update();

        /*DATA FOR SENDING TO SERVER*/
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            corndnt_in = latitude + "," + longitude;
            cordnt_out = latitude + "," + longitude;
        } else {
            corndnt_in = visitedDetails.Geo_Cordinates_in;
            cordnt_out = visitedDetails.Geo_Cordinates_out;
        }

        final ArrayList<HashMap<String, SurveyModel>> surveydetails = new ArrayList<>();
        final ArrayList<HashMap<String, ProductQuatityModel>> productqties = new ArrayList<>();
        final ArrayList<HashMap<String, ReminderModel>> reminders = new ArrayList<>();
        final ArrayList<HashMap<String, ActivityModel>> activities = new ArrayList<>();
        final ArrayList<HashMap<String, DocumentModel>> document = new ArrayList<>();
        final ArrayList<HashMap<String, SendPendingRemarks>> pendingRemarks = new ArrayList<>();


        new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Saving Visit Details... Pleas wait!");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<SurveyDetails> surveyResult = new Select()
                        .from(SurveyDetails.class)
                        .where(Condition.column(SurveyDetails$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlan.Route_Plan_Number))
                        .queryList();
                if (surveyResult != null) {
                    int m = 1;
                    for (SurveyDetails sd : surveyResult) {
                        SurveyModel model =
                                new SurveyModel(String.valueOf(sd.surveyMaster.Survey_Id),
                                        String.valueOf(sd.routePlan.Route_Plan_Number),
                                        sd.visiteddate, sd.Survey_Date, sd.Selected_Option, sd.Remarks, "", "", sd.startTime, sd.endTime,
                                        corndnt_in, cordnt_out);
                        HashMap<String, SurveyModel> sample_data = new HashMap<String, SurveyModel>();
                        sample_data.put("surveydetails" + (m++), model);
                        surveydetails.add(sample_data);
                    }
                }
        /* get samples issued for current visit */
                List<ProductSample> allSamples = new Select()
                        .from(ProductSample.class)
                        .where(Condition.column(ProductSample$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlan.Route_Plan_Number))
                        .queryList();
                if (allSamples != null) {
                    int i = 1;
                    for (ProductSample sample : allSamples) {
                        HashMap<String, ProductQuatityModel> productQuantity = new HashMap<>();
                        ProductQuatityModel model = new ProductQuatityModel(String.valueOf(routePlan.Route_Plan_Number),
                                sample.visited_date, sample.date,
                                String.valueOf(sample.product.Product_Number),
                                Integer.toString(sample.quantity), "",
                                sample.startTime, sample.endTime,
                                corndnt_in, cordnt_out);
                        productQuantity.put("sampleissued" + (i++), model);
                        productqties.add(productQuantity);
                    }
                }

    /* get all reminders for current visit */
                List<Reminder> allReminders = new Select()
                        .from(Reminder.class)
                        .where(Condition.column(Reminder$Table.ROUTEPLANNUMBER).eq(routePlan.Route_Plan_Number))
                        .queryList();
                if (allReminders != null) {
                    int remCount = 1;
                    for (Reminder reminder : allReminders) {
                        HashMap<String, ReminderModel> rem = new HashMap<>();
                        ReminderModel model = new ReminderModel("", reminder.date, reminder.message, "0", "",
                                reminder.startTime, reminder.endTime,
                                reminder.startCoordinates, reminder.endCoordinates);
                        rem.put("reminder" + (remCount++), model);
                        reminders.add(rem);
                    }
                }

    /* get all activities for current visit*/
                List<Activities> allActiviites = new Select()
                        .from(Activities.class)
                        .where(Condition.column(Activities$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlan.Route_Plan_Number))
                        .queryList();
                if (allActiviites != null) {
                    int i = 1;
                    for (Activities activity : allActiviites) {
                        HashMap<String, ActivityModel> mapActivities = new HashMap<>();
                        ActivityModel model = new ActivityModel(activity.activitydate,
                                String.valueOf(routePlan.Route_Plan_Number),
                                activity.visitdate,
                                activity.activityname,
                                activity.Activity + "",
                                activity.starttime,
                                activity.endtime,
                                "",
                                corndnt_in,
                                cordnt_out);
                        mapActivities.put("activity" + (i++), model);
                        activities.add(mapActivities);
                    }
                }

    /* get all the documents for current visit*/
                List<Documents> docs = new Select()
                        .from(Documents.class)
                        .where(Condition.column(Documents$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(routePlan.Route_Plan_Number))
                        .queryList();
                if (docs != null) {
                    int i = 1;
                    for (Documents doc : docs) {
                        DocumentModel model = new DocumentModel(doc.visiteddate, String.valueOf(doc.routePlan.Route_Plan_Number),
                                doc.date, doc.Doc_Path, String.valueOf(doc.Doc_Type), doc.Remarks, doc.startTime, doc.endTime,
                                corndnt_in, cordnt_out);
                        HashMap<String, DocumentModel> map = new HashMap<>();
                        map.put("doc_" + (i++), model);
                        document.add(map);
                    }
                }
                //Get All remarks
                ArrayList<PendingRemarks>remarkses= (ArrayList<PendingRemarks>)
                        new Select().from(PendingRemarks.class)
                                .where(Condition.column(PendingRemarks$Table.ROUTEPLANNO).eq(routePlan.Route_Plan_Number))
                                .queryList();
                if(remarkses!=null&&remarkses.size()>0){
                    for(int i=0;i<remarkses.size();i++){
                        HashMap<String, SendPendingRemarks> map = new HashMap<>();
                        SendPendingRemarks sendPendingRemarks=new SendPendingRemarks(remarkses.get(i).remark_id,
                                remarkses.get(i).routeplanno,remarkses.get(i).datetime,remarkses.get(i).remarks,remarkses.get(i).client_ID);
                        map.put("pendingrmks"+(i+1),sendPendingRemarks);
                        pendingRemarks.add(map);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (pd.isShowing()) pd.dismiss();

                String apnmntNumber = appointmentNumber > 0 ? String.valueOf(appointmentNumber) : "0";
                final VisitModel visitModel = new VisitModel(PreferenceUtil.getIntsance().getCLIENT_ID(),
                        PreferenceUtil.getIntsance().getCUSTOMER_ID(),PreferenceUtil.getIntsance().getUSER_ID(),
                        apnmntNumber, String.valueOf(routePlan.Route_Plan_Number),
                        visitedDetails.Visited_Date,
                        visitedDetails.checkintime, visitedDetails.checkouttime,
                        visitedDetails.Sessionend, visitedDetails.Feedback, corndnt_in, cordnt_out,
                        visitedDetails.Geo_Cordinates_sessout,
                        String.valueOf(visitedDetails.status),
                /*Utilities.encodeTobase64(mBitmap)*/"",
                        visitedDetails.Remarks, visitedDetails.notes,
                        activities, document, reminders, surveydetails, productqties,pendingRemarks);

                Log.d("VISIT", new Gson().toJson(visitModel));

                submitVisitDetails(visitModel, isPending);

            }
        }.execute();


    }

    private void goBack(boolean isPending) {
        Delete.table(PendingRemarks.class,Condition.column(PendingRemarks$Table.ROUTEPLANNO).eq(routePlan.Route_Plan_Number));

        try {
            if(routePlan.Route_Plan_Number==-1){
                Delete.table(SurveyDetails.class,Condition.column(SurveyDetails$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(-1));
                Delete.table(ProductSample.class,Condition.column(ProductSample$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(-1));
                Delete.table(Reminder.class,Condition.column(Reminder$Table.ROUTEPLANNUMBER).eq(-1));
                Delete.table(Activities.class,Condition.column(Activities$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(-1));
                Delete.table(Documents.class,Condition.column(Documents$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER).eq(-1));
                addFragment(TodaysVisitTabbed.getAdditionalVisitTabbed());
            }
            else{
                if (isPending)
                    addFragment(TodaysVisitTabbed.getPendingVisitTabbed());
                else addFragment(TodaysVisitTabbed.getInstance());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****Change Start***/

    /**
     * Function which make an api call to submit visit details.
     */
    private void submitVisitDetails(final VisitModel visitModel, final boolean isPending) {
        // Toast.makeText(getActivity(),"IN Fn",Toast.LENGTH_LONG).show();
        ArrayList<VisitModel> arrVisitModels = new ArrayList<VisitModel>();
        arrVisitModels.add(visitModel);
        final VisitJson visitJson = new VisitJson(ApiConstants.STATUS, arrVisitModels);
        final Gson gson = new Gson();
        final CommonSubmitJson commonSubmitJson = new CommonSubmitJson(visitJson);
        Log.d("VISIT", gson.toJson(commonSubmitJson));
        showProgressDialog();
        PreferenceUtil.getIntsance().setSurvey(false);
        if (PreferenceUtil.getIntsance().isSyncManual()) {
            dissmissProgressDialog();
            // Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            //keep to local DB
            SyncVisitDetails syncVisitDetails = new SyncVisitDetails();
            syncVisitDetails.visit_details = gson.toJson(commonSubmitJson);
            syncVisitDetails.save();
            dissmissProgressDialog();
            goBack(isPending);

        } else {
            ApiService.getInstance().makeApiCall(ApiConstants.AppvisitedDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    dissmissProgressDialog();
                    SignatureUploadService.routplanno = AppControllerUtil.getInstance().getRoutePlanNumber() + "";
                    getActivity().startService(new Intent(getActivity(), SignatureUploadService.class));
                    goBack(isPending);
                }

                @Override
                public void onError(Object objects) {
                    dissmissProgressDialog();
                    // Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    //keep to local DB
                    SyncVisitDetails syncVisitDetails = new SyncVisitDetails();
                    syncVisitDetails.visit_details = gson.toJson(commonSubmitJson);
                    syncVisitDetails.save();
                    dissmissProgressDialog();
                    goBack(isPending);
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        }


    }

    /****Change End*******/


}
