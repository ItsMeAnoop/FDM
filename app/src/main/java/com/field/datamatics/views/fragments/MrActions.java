package com.field.datamatics.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.*;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.DialogAudio;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anoop on 27/9/15.
 * MR Actios view logic
 */
public class MrActions extends BaseFragment implements TakeSurvey.OnSurveyFinishedListener {
    private LinearLayout grid_1;
    private LinearLayout grid_2;
    private LinearLayout grid_3;
    private LinearLayout grid_4;
    private LinearLayout grid_5;
    private LinearLayout grid_6;
    private LinearLayout grid_7;
    private LinearLayout grid_8;
    private LinearLayout grid_9;

    private final int REQ_VIDEO_CAPTURE = 100;
    private final int REQ_IMAGE_CAPTURE = 101;
    public static final int REQUEST_RECORD_SOUND = 7;

    private File mediaFile;

    private View view;

    private Bitmap bitmapSignature;

    private static MrActions mInstance;
    private boolean isNew;
    private Calendar visitedDate;
    private String notes = "";
    private String path = "";
    private Animation animFadein;
    public static String fragment_use;
    public static int routePlanNumber;
    private String fileName;

    private boolean shouldGoToSurvey = false;
    private boolean shouldShowNotes = false;
    private boolean shouldGoToSignature = false;

    private String startTime;


    private boolean shouldCreateNewSurvey = true;

    public MrActions() {
    }

    public static MrActions getInstance(Bundle data, boolean isNew) {
        if (isNew || mInstance == null) {
            mInstance = new MrActions();
            mInstance.isNew = true;
            mInstance.setArguments(data);
            mInstance.visitedDate = Calendar.getInstance();
        } else {
            mInstance.isNew = false;
        }
        return mInstance;
    }

    public static MrActions goToSurvey(Bundle data) {
        mInstance = new MrActions();
        mInstance.isNew = true;
        mInstance.setArguments(data);
        mInstance.visitedDate = Calendar.getInstance();
        mInstance.shouldGoToSurvey = true;
        return mInstance;
    }

    public static MrActions goToSignature(Bundle data) {
        mInstance = new MrActions();
        mInstance.isNew = true;
        mInstance.setArguments(data);
        mInstance.visitedDate = Calendar.getInstance();
        mInstance.shouldGoToSignature = true;

        return mInstance;
    }

    public static MrActions showNotesDialog(Bundle data) {
        mInstance = new MrActions();
        mInstance.isNew = true;
        mInstance.setArguments(data);
        mInstance.visitedDate = Calendar.getInstance();
        mInstance.shouldShowNotes = true;

        return mInstance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mr_actions, container, false);
        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_MR_ACTIONS);
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.commit();
        if (getArguments().getBoolean("is_pending", false)) {
            addFragmentTitle("MrActions_Pending");
            setTitle("Pending Visits");
        } else {
            addFragmentTitle("MrActions");
            setTitle("Today's Visit");
        }
        try {
            setTitle(getArguments().getString("fragmentName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("MrActions");

        AppControllerUtil.getInstance().setIsSessionStart(true);
        setUP();
        registerEvents();
        if (shouldGoToSurvey) {
            shouldGoToSurvey = false;
            addFragment(TakeSurvey.getInstance(getArguments(), mInstance, isNew));
        }
        if (shouldShowNotes) {
            shouldShowNotes = false;
            showNotesDialog();
        }
        if (shouldGoToSignature) {
            shouldGoToSignature = false;
            addFragment(SignatureFragment.getInstance(getArguments(), visitedDate, notes));
        }
        return view;
    }

    /**
     * Set up grid view
     */
    private void setUP() {
        animFadein = AnimationUtils.loadAnimation(getActivity(),
                R.anim.blink);
        grid_1 = (LinearLayout) view.findViewById(R.id.grid_1);
        grid_2 = (LinearLayout) view.findViewById(R.id.grid_2);
        grid_3 = (LinearLayout) view.findViewById(R.id.grid_3);
        grid_4 = (LinearLayout) view.findViewById(R.id.grid_4);
        grid_5 = (LinearLayout) view.findViewById(R.id.grid_5);
        grid_6 = (LinearLayout) view.findViewById(R.id.grid_6);
        grid_7 = (LinearLayout) view.findViewById(R.id.grid_7);
        grid_8 = (LinearLayout) view.findViewById(R.id.grid_8);
        grid_9 = (LinearLayout) view.findViewById(R.id.grid_9);
        grid_1.startAnimation(animFadein);
        grid_2.startAnimation(animFadein);
        grid_3.startAnimation(animFadein);
        grid_4.startAnimation(animFadein);
        grid_5.startAnimation(animFadein);
        grid_6.startAnimation(animFadein);
        grid_7.startAnimation(animFadein);
        grid_8.startAnimation(animFadein);
        grid_9.startAnimation(animFadein);
    }

    /**
     * Register listeners
     */
    private void registerEvents() {
        grid_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(ProductList.getInstance(getArguments().getInt("route_plan_number"), getArguments().getInt("client_id"), visitedDate));
                fragment_use = "PRODUCT";
            }
        });
        grid_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(AddReminder.getInstance(getArguments().getInt("client_id"), getArguments().getInt("route_plan_number")));
            }
        });
        grid_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(TakeSurvey.getInstance(getArguments(), mInstance, shouldCreateNewSurvey));
                shouldCreateNewSurvey = false;
            }
        });
        grid_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaPathUri(1));
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.1);
                        startTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
                        startActivityForResult(takeVideoIntent, REQ_VIDEO_CAPTURE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        grid_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaPathUri(2));
                        startTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
                        startActivityForResult(takePhotoIntent, REQ_IMAGE_CAPTURE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        grid_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotesDialog();

            }
        });
        grid_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(SignatureFragment.getInstance(getArguments(), visitedDate, notes));
            }
        });
        grid_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_use = "SAMPLE";
                addFragment(ProductList.getInstance(getArguments().getInt("route_plan_number"), getArguments().getInt("client_id"), visitedDate));
            }
        });
        //audio
        grid_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getMediaPathUri(3);
                    Intent intent  = new Intent(getActivity(), DialogAudio.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("PATH",path);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_RECORD_SOUND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Note dialog
     */
    private void showNotesDialog() {
        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_NOTES);
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.commit();

        final Dialog notesDialog = new Dialog(getContext());
        notesDialog.setContentView(R.layout.dialog_notes);
        notesDialog.setTitle("Notes");
        AppCompatButton btnDone = (AppCompatButton) notesDialog.findViewById(R.id.btn_done);
        final EditText edtNotes = (EditText) notesDialog.findViewById(R.id.edt_notes);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes = edtNotes.getText().toString();
                notesDialog.dismiss();
            }
        });
        notesDialog.show();
    }

    /**
     * Return media path
     * @param type
     * @return
     * @throws IOException
     */
    public Uri getMediaPathUri(int type) throws IOException {
        String directory, prefix, extension;

        if (type == 1) {
            directory = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_VIDEO, Constants.VIDEO_DIRECTORY_F);
            prefix = "VID_";
            extension = ".mp4";
        }
        else if(type==2) {
            directory = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_IMAGE, Constants.PHOTO_DIRECTORY_F);
            prefix = "IMG_";
            extension = ".jpg";
        }
        else{
            //directory = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_IMAGE, Constants.AUDIO_DIRECTORY_F);
            directory=AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_AUDIO, Constants.AUDIO_DIRECTORY_F);
            prefix = "AUDIO_";
            extension = ".3gp";
        }
        mediaFile = new File(directory);
        if (!mediaFile.exists()) {
            if (!mediaFile.mkdirs()) {
                Log.i("FDM", "Failed to create Directory : " + directory);
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMMdd_HHmmss").format(Calendar.getInstance().getTime());
        fileName = prefix + timeStamp + extension;
        File videoFile = new File(mediaFile.getPath() + File.separator + prefix + timeStamp + extension);
        path = mediaFile.getPath() + File.separator + prefix + timeStamp + extension;
        File f=new File(path);
        f.createNewFile();
        return Uri.fromFile(videoFile);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Documents doc = new Documents();
            if (requestCode == REQ_VIDEO_CAPTURE) {
                doc.Doc_Type = Constants.DOC_TYPE_VIDEO;
                showMessage("Video has been saved in " + mediaFile.getAbsolutePath(), getActivity().findViewById(android.R.id.content));
                //upload(path, "3");

            } else if (requestCode == REQ_IMAGE_CAPTURE) {
                doc.Doc_Type = Constants.DOC_TYPE_IMAGE;
                showMessage("Image has been saved in " + mediaFile.getAbsolutePath(), getActivity().findViewById(android.R.id.content));
                //upload(path, "2");
            }
            else if (requestCode == REQUEST_RECORD_SOUND){
                doc.Doc_Type = Constants.DOC_TYPE_AUDIO;
                showMessage("Audio has been saved in " + path, getActivity().findViewById(android.R.id.content));

                //String sourcePath = getRealPathFromURI(intent.getData());
                /*File root = Environment.getExternalStorageDirectory();
                String destPath = root.getPath() + File.separator + "a.3gp";
                File sourceF = new File(savedUri.getPath());
                try {
                    sourceF.renameTo(new File(destPath));
                } catch (Exception e) {
                    //Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }*/
            }
            Utilities.initMediaScanner(getActivity(), mediaFile.getAbsolutePath());
            doc.visiteddate = Utilities.dateInSqliteFormat(visitedDate);
            doc.date = Utilities.dateInSqliteFormat(Calendar.getInstance());
            doc.routePlan = new RoutePlan();
            doc.routePlan.Route_Plan_Number = getArguments().getInt("route_plan_number");
            doc.Doc_Path = fileName;
            doc.startTime = startTime;
            doc.endTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            doc.save();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        storeInSdCard(requestCode, new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void storeInSdCard(int type, File from) throws IOException {
      /*  String path;
        if (type == REQ_IMAGE_CAPTURE) {
            path = Constants.SD_PATH_IMAGE;
        } else {
            path = Constants.SD_PATH_VIDEO;
        }
        File to = new File(path + from.getName());
        Utilities.copy(from, to);
        Utilities.initMediaScanner(getActivity(), to.getAbsolutePath());*/
    }

    @Override
    public void OnSurveyFinished(final ArrayList<SurveyDetails> surveyResult) {

    }

    private void upload(String file_path, String type) {
        //UploadMediaService.getInstance().uploadMedia(file_path);
        String imagePath = file_path;
        AsyncHttpClient client = new AsyncHttpClient();
        File myFile = new File(imagePath);
        RequestParams params = new RequestParams();
        try {
            params.put("file", new FileInputStream(myFile));
            params.put("routplanno", AppControllerUtil.getInstance().getRoutePlanNumber());
            params.put("visiteddate", Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
            params.put("date", Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
            params.put("doctype", type);

            client.post(ApiConstants.BASE_URL + ApiConstants.url + ApiConstants.AppMulitiPardData, params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    Toast.makeText(getActivity(), "failure...!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();

                }

            });
        } catch (FileNotFoundException e) {
            Log.d("MyApp", "File not found!!!" + imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
