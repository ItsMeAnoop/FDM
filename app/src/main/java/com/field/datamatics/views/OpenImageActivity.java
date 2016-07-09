package com.field.datamatics.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.field.datamatics.R;

import java.io.File;

/**
 * Created by Jith on 10/21/2015.
 */
public class OpenImageActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.image_view);
        ImageView image = (ImageView) findViewById(R.id.image);
        String path = getIntent().getStringExtra("path");
        Glide.with(this).load(new File(path))
                .crossFade()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(image);
    }
}
