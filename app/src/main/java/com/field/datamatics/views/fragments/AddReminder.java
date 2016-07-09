package com.field.datamatics.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.field.datamatics.R;
import com.field.datamatics.apimodels.ReminderModel;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.GPSTracker;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;


/**
 * Created by Jith on 10/22/2015.
 */
public class AddReminder extends BaseFragment {


    private EditText edtDescription;
    private AppCompatButton btnSave;
    private static EditText edtDate;
    private static EditText edtTime;

    private int remCount = 1;
    private int clientId = 0;
    private int routePlanNumber;
    private String message = "";
    private boolean isFromPending;

    private String startTime;
    private String startCoordinate;
    private MainActivity mContainer;
    private GPSTracker gps;

    public AddReminder() {
    }

    public static AddReminder getInstance(int client_id, int routePlanNumber) {
        AddReminder f = new AddReminder();
        f.clientId = client_id;
        f.routePlanNumber = routePlanNumber;
        return f;
    }

    public static AddReminder getInstance(String message) {
        AddReminder f = new AddReminder();
        f.message = message;
        return f;
    }
    public static AddReminder getInstance(String message,int routePlanNumber) {
        AddReminder f = new AddReminder();
        f.message = message;
        f.routePlanNumber = routePlanNumber;
        return f;
    }

    void save() {
        mContainer.shouldUpdateReminder = true;
        Utilities.hideKeyboard(getActivity());
        String desc = edtDescription.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            edtDescription.setError("Should not left empty");
            return;
        }
        if (choosenDate == null) {
            edtDate.setError("Should not left empty");
            return;
        }
        if (choosenTime == null) {
            edtTime.setError("Should not left empty");
            return;
        }
        choosenDate.set(Calendar.HOUR_OF_DAY, choosenTime.get(Calendar.HOUR_OF_DAY));
        choosenDate.set(Calendar.MINUTE, choosenTime.get(Calendar.MINUTE));


        ContentValues values = new ContentValues();
        values.put("calendar_id", 1);
        if (clientId == 0)
            values.put("title", "Pending Visit");
        else
            values.put("title", "Field Datamatics");
        values.put("description", desc);
        //l_event.put("eventLocation", "School");
        values.put("dtstart", choosenDate.getTimeInMillis());
        values.put("dtend", choosenDate.getTimeInMillis() + 60 * 60 * 1000);
        values.put("allDay", 0);
//        l_event.put("beginTime",choosenDate.getTimeInMillis());
//        l_event.put("endTime",choosenDate.getTimeInMillis()+60*60*1000);
        // values.put("rrule", "FREQ=YEARLY");
        values.put("hasAlarm", 1);
        // status: 0~ tentative; 1~ confirmed; 2~ canceled
        // l_event.put("eventStatus", 1);

        TimeZone tz = TimeZone.getDefault();
        values.put("eventTimezone", tz.getID());

        Uri EVENT_URI = Uri.parse(getCalendarUriBase(getActivity()) + "events");
        Uri event = activity.getContentResolver()
                .insert(EVENT_URI, values);

        // reminder insert

        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(getActivity()) + "reminders");
        values = new ContentValues();
        values.put("event_id", Long.parseLong(event.getLastPathSegment()));
        values.put("method", 1);
        values.put("minutes", 10);
        activity.getContentResolver().insert(REMINDERS_URI, values);


        Reminder reminder = new Reminder();
        reminder.date = Utilities.dateToString(choosenDate, "yyyy-MM-dd HH:mm");
        reminder.message = desc;
        reminder.client = new Client();
        reminder.client.Client_Number = clientId;
        reminder.startCoordinates = startCoordinate;
        mContainer = (MainActivity) getActivity();
        Location loc = null;
        if (mContainer.mLastLocation != null)
            loc = mContainer.mLastLocation;
        if (loc != null) {
            reminder.endCoordinates = loc.getLatitude() + "," + loc.getLongitude();
        }
        reminder.startTime = startTime;
        reminder.endTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        reminder.routePlanNumber = routePlanNumber;
        if(reminder.startCoordinates==null||reminder.startCoordinates.equals("")||reminder.startCoordinates.equals("0.0")){
            gps = new GPSTracker(getActivity());
            reminder.startCoordinates=gps.getLatitude()+","+gps.getLongitude();
            reminder.endCoordinates=gps.getLatitude()+","+gps.getLongitude();
        }
        reminder.save();

        if (clientId != 0) {
            addFragment(MrActions.getInstance(Bundle.EMPTY, false));
        } else {
            //addFragment(new TodayVisit());
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    private static Calendar choosenTime = null;
    private static Calendar choosenDate = null;
    private static Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume()
                .edit();
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_REMINDER);
        edit.putInt("client_id", clientId);
        edit.commit();
        activity = getActivity();
        mContainer = (MainActivity) getActivity();
        startTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        View view = inflater.inflate(R.layout.add_reminder, container, false);
        edtDate = (EditText) view.findViewById(R.id.edtDate);
        edtTime = (EditText) view.findViewById(R.id.edtTime);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        btnSave = (AppCompatButton) view.findViewById(R.id.btnSave);
        addFragmentTitle("AddReminder");
        if (clientId == 0) {
            edtDescription.setText(message);
            addFragmentTitle("AddReminderPending");
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        Location loc = null;
        if (mContainer.mLastLocation != null)
            loc = mContainer.mLastLocation;
        if (loc != null) {
            startCoordinate = loc.getLatitude() + "," + loc.getLongitude();
        }

        return view;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            choosenTime = Calendar.getInstance();
            choosenTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            choosenTime.set(Calendar.MINUTE, minute);
            edtTime.setText(Utilities.dateToString(choosenTime, "hh:mm a"));
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(activity, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            choosenDate = Calendar.getInstance();
            choosenDate.set(year, month, day);
            edtDate.setText(Utilities.dateToString(choosenDate, "MMM dd, yyyy"));
        }
    }

    private String getCalendarUriBase(Activity act) {

        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
        }
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
            }
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }
        }
        return calendarUriBase;
    }
}
