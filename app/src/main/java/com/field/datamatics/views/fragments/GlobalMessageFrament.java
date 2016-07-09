package com.field.datamatics.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.GlobalMessages;
import com.field.datamatics.database.GlobalMessages$Table;
import com.field.datamatics.database.User;
import com.field.datamatics.interfaces.IgcmApi;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.GlobalMessageAdapter;
import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jith on 29/10/2015.
 */
public class GlobalMessageFrament extends BaseFragment {
    private RecyclerView recyclerView;
    private ArrayList<GlobalMessages> data = new ArrayList<>();
    private GlobalMessageAdapter adapter;

    private ProgressBar progressBar;

    private ImageButton btnSend;
    private EditText edtMessage;
    private boolean loadDataFirstTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global_message, container, false);
        setTitle("Global message");
        AppControllerUtil.getInstance().setCurrent_fragment("GlobalMessageFrament");

        initializeViews(view);
        loadDataFirstTime = true;
        new loadData().execute();
        return view;
    }

    private void initializeViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        btnSend = (ImageButton) view.findViewById(R.id.btn_send);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        edtMessage = (EditText) view.findViewById(R.id.edt_message);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        recyclerView.setLayoutManager(llm);
        adapter = new GlobalMessageAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (false) return;

                String message = edtMessage.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    return;
                }

                edtMessage.setText("");
                String token = AppControllerUtil.getPrefs().getString(Constants.PREF_GCM_TOKEN, "");
                User user = AppControllerUtil.getUser();
                if (user == null) {
                    user = new User();
                    user.First_Name = "Sender";
                    user.Last_Name = "";
                    user.UserNumber = 3;
                }

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ApiConstants.BASE_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                IgcmApi methods = restAdapter.create(IgcmApi.class);
                final GlobalMessages m = new GlobalMessages();
                m.message = message;
                m.isOutgoing = true;
                m.date = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
                m.save();
                data.add(0, m);
                adapter.setData(data);
                recyclerView.scrollToPosition(0);
                String myUrl = "";
                if (PreferenceUtil.getIntsance().isTesting()) {
                    myUrl = ApiConstants.url_test;
                } else {
                    myUrl = ApiConstants.url;
                }
                methods.sendMessage(ApiConstants.ENCRYPTION_KEY, myUrl, String.valueOf(user.UserNumber), user.getName(), message, m.date, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject s, Response response) {
                        Log.i("gcm_result", "success : " + s);
                        try {
                            JSONObject obj = new JSONObject(s.toString());
                            if (obj.getString("status").equals("success")) {
                                new loadData().execute();
                                m.status = 1;
                                m.update();
                                new loadData().execute();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("gcm_result", "failed " + error.getMessage());
                    }
                });

            }
        });

    }

    private class loadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loadDataFirstTime) {
                progressBar.setVisibility(View.VISIBLE);
                loadDataFirstTime = false;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(ProgressBar.GONE);
            adapter.setData(data);
            recyclerView.scrollToPosition(0);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            data = (ArrayList<GlobalMessages>) new Select().from(GlobalMessages.class)
                    .orderBy(false, GlobalMessages$Table.DATE)
                    .queryList();
            return null;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Global message", "resumed");
        getActivity().registerReceiver(messageReceiver, new IntentFilter(Constants.MESSAGE_RECEIVER));
        new loadData().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Global message", "on pause");
        getActivity().unregisterReceiver(messageReceiver);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new loadData().execute();
            Log.i("Global message", "on recieve broadcast");
        }
    };
}

