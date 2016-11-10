package com.field.datamatics.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.User;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.Login;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.helper.DirectoryChooserDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.raizlabs.android.dbflow.sql.language.Delete;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;

/**
 * Created by anoop on 11/10/15.
 */
public class Settings extends BaseFragment {


    private static final int REQUEST_DIRECTORY = 100;
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private AppCompatButton btnStorageLocationImage;
    private AppCompatButton btnProductDocLoc;
    private AppCompatButton btnStorageLocationVideo;
    private TextView txtProductDocLoc;
    private TextView txtStorageLocImages;
    private TextView txtStorageLocVideos;
    private TextView txtStorageLocationAudio;
    private AppCompatButton btnAudioLocation;

    private final String prefix_storage_loc_audio = "Storage location of the  captured audio : ";
    private final String prefix_product_doc_loc = "Location of the product documents : ";
    private final String prefix_storage_loc_images = "Storage location of the  captured images : ";
    private final String prefix_storage_loc_video = "Storage location of the  captured videos : ";

    private Activity context;
    private boolean isRequestedOfficeLocation;
    private RadioButton rbProduction, rbTesting, rbAuto, rbManual;

    private final int REQ_STORAGE_LOC_IMAGES = 100;
    private final int REQ_STORAGE_LOC_VIDEOS = 101;
    private final int REQ_STORAGE_LOC_PRODUCTS = 102;
    private final int REQ_STORAGE_LOC_AUDIO = 103;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setTitle("Settings");
        addFragmentTitle("Settings");
        context = getActivity();
        initializeViews(view);
        if (PreferenceUtil.getIntsance().isTesting()) {
            rbProduction.setChecked(false);
            rbTesting.setChecked(true);
        } else {
            rbProduction.setChecked(true);
            rbTesting.setChecked(false);
        }
        if (PreferenceUtil.getIntsance().isSyncManual()) {
            rbManual.setChecked(true);
            rbAuto.setChecked(false);
        } else {
            rbAuto.setChecked(true);
            rbManual.setChecked(false);
        }
        rbTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance().getDialog(getActivity(),
                        "URL Change", "The app will point to testing URL by clicking ok(Current session will be end.)", new DialogCallBacks() {
                            @Override
                            public void onOk() {
                                PreferenceUtil.getIntsance().setIsTesting(true);
                                logout();
                            }
                        });

            }
        });
        rbProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance().getDialog(getActivity(), "URL Change", "app will point to production URL by clicking ok(Current session will be end.)", new DialogCallBacks() {
                    @Override
                    public void onOk() {
                        PreferenceUtil.getIntsance().setIsTesting(false);
                        logout();
                    }
                });

            }
        });

        rbAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getIntsance().setSyncManul(false);
                DialogUtil.getInstance().getDialog(getActivity(), "Syncing",
                        "The syncing mode is changed to the Auto", new DialogCallBacks() {
                            @Override
                            public void onOk() {

                            }
                        });

            }
        });
        rbManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getIntsance().setSyncManul(true);
                DialogUtil.getInstance().getDialog(getActivity(), "Syncing",
                        "The syncing mode is changed to the Manual", new DialogCallBacks() {
                            @Override
                            public void onOk() {

                            }
                        });

            }
        });
        return view;
    }

    private void logout() {
        PreferenceUtil.getIntsance().setIsLogin(false);
        AppControllerUtil.setLoginStatus(false);
        boolean urlMode=PreferenceUtil.getIntsance().isTesting();
        PreferenceUtil.getIntsance().clearPreference();
        PreferenceUtil.getIntsance().setIsTesting(urlMode);
        Delete.table(User.class);

        //Move to login screen.
        Intent next = new Intent(getActivity(), Login.class);
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(next);
        getActivity().finish();
    }

    private void initializeViews(View view) {

        btnStorageLocationImage = (AppCompatButton) view.findViewById(R.id.btn_browse);
        btnStorageLocationVideo = (AppCompatButton) view.findViewById(R.id.btnVideoLocation);
        btnProductDocLoc = (AppCompatButton) view.findViewById(R.id.btn_product_doc_location);
        txtProductDocLoc = (TextView) view.findViewById(R.id.txtProductDocLocation);
        txtStorageLocVideos = (TextView) view.findViewById(R.id.txtStorageLocationVideo);
        txtStorageLocImages = (TextView) view.findViewById(R.id.txtStorageLocationImages);
        rbProduction = (RadioButton) view.findViewById(R.id.rbProduction);
        rbTesting = (RadioButton) view.findViewById(R.id.rbTesting);
        rbAuto = (RadioButton) view.findViewById(R.id.rbAuto);
        rbManual = (RadioButton) view.findViewById(R.id.rbManual);
        btnAudioLocation= (AppCompatButton) view.findViewById(R.id.btnAudioLocation);
        txtStorageLocationAudio= (TextView) view.findViewById(R.id.txtStorageLocationAudio);

        final Intent chooserIntent = new Intent(getActivity(), DirectoryChooserActivity.class);

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("Folder")
                .allowReadOnlyDirectory(true)
                .allowNewDirectoryNameModification(true)
                .build();
        chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        btnAudioLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(chooserIntent, REQ_STORAGE_LOC_AUDIO);

            }
        });
        btnStorageLocationImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(chooserIntent, REQ_STORAGE_LOC_IMAGES);

            }
        });

        btnStorageLocationVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(chooserIntent, REQ_STORAGE_LOC_VIDEOS);

            }
        });

        btnProductDocLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(chooserIntent, REQ_STORAGE_LOC_PRODUCTS);
            }
        });

        String loc = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_AUDIO, null);
        if (loc == null) {
            loc = Constants.AUDIO_DIRECTORY_F;
            File f = new File(loc);
            if (!f.exists()) f.mkdirs();
        }
        txtStorageLocationAudio.setText(prefix_storage_loc_audio + loc);
        loc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, null);
        if (loc == null) {
            loc = Constants.PRODUCT_DOCS;
            File f = new File(loc);
            if (!f.exists()) f.mkdirs();
        }
        txtProductDocLoc.setText(prefix_product_doc_loc + loc);

        loc = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_IMAGE, null);
        if (loc == null) {
            loc = Constants.PRODUCT_IMAGE;
            File f = new File(loc);
            if (!f.exists()) f.mkdirs();
        }
        txtStorageLocImages.setText(prefix_storage_loc_images + loc);

        loc = AppControllerUtil.getPrefs().getString(Constants.PREF_STORAGE_LOCATION_VIDEO, null);
        if (loc == null) {
            loc = Constants.PRODUCT_VIDEO;
            File f = new File(loc);
            if (!f.exists()) f.mkdirs();
        }
        txtStorageLocVideos.setText(prefix_storage_loc_video + loc);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_STORAGE_LOC_IMAGES) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String chosenDir = data
                        .getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                AppControllerUtil.getPrefs().edit()
                        .putString(Constants.PREF_STORAGE_LOCATION_IMAGE, chosenDir)
                        .apply();
                txtStorageLocImages.setText(prefix_storage_loc_images + chosenDir);
                Utilities.showAlertDialog(getActivity(),
                        "Successfully updated storage location for images!");
            }
        } else if (requestCode == REQ_STORAGE_LOC_VIDEOS) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String chosenDir = data
                        .getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                AppControllerUtil.getPrefs().edit()
                        .putString(Constants.PREF_STORAGE_LOCATION_VIDEO, chosenDir)
                        .apply();
                txtStorageLocVideos.setText(prefix_storage_loc_video + chosenDir);
                Utilities.showAlertDialog(getActivity(),
                        "Successfully updated storage location for videos!");
            }
        } else if (requestCode == REQ_STORAGE_LOC_PRODUCTS) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String chosenDir = data
                        .getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                AppControllerUtil.getPrefs().edit()
                        .putString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, chosenDir)
                        .apply();
                txtProductDocLoc.setText(prefix_product_doc_loc + chosenDir);
                Utilities.showAlertDialog(getActivity(),
                        "Successfully updated product document directory!");
            }
        }
        else if (requestCode == REQ_STORAGE_LOC_AUDIO) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String chosenDir = data
                        .getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                AppControllerUtil.getPrefs().edit()
                        .putString(Constants.PREF_STORAGE_LOCATION_AUDIO, chosenDir)
                        .apply();
                txtStorageLocationAudio.setText(prefix_storage_loc_audio + chosenDir);
                Utilities.showAlertDialog(getActivity(),
                        "Successfully updated storage location for audio!");
            }
        }
    }
}
