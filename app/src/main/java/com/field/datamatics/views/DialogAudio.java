package com.field.datamatics.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.views.fragments.MrActions;

/**
 * Created by Anoop on 12-06-2016.
 */
public class DialogAudio extends BaseActivity{
    private Button btnStart,btnStop;
    private MediaRecorder mediaRecorder;
    private TextView tv_recording;
    private Handler hn=new Handler();
    private Runnable rn;
    private String[]label={"Recording","Recording.","Recording..","Recording...","Recording....","Recording.....","Recording......"};
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_audio_take);
        btnStart= (Button) findViewById(R.id.btnStart);
        btnStop= (Button) findViewById(R.id.btnStop);
        tv_recording= (TextView) findViewById(R.id.tv_recording);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                try {
                    Bundle bundle=getIntent().getExtras();
                    mediaRecorder=new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    String path=bundle.getString("PATH");
                    mediaRecorder.setOutputFile(path);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    hn.post(rn);
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }


            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                Intent intent=new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        rn=new Runnable() {
            @Override
            public void run() {
                tv_recording.setText(label[i]);
                i++;
                if(i==7) {
                    i = 0;
                }
                hn.postDelayed(rn,400);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            hn.removeCallbacks(rn);
            if(mediaRecorder!=null)
                mediaRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
