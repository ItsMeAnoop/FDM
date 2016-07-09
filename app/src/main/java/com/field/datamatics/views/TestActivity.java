package com.field.datamatics.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.field.datamatics.R;
import com.field.datamatics.database.VisitedDetails;
import com.field.datamatics.database.VisitedDetails$Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        layout = (LinearLayout) findViewById(R.id.layout);

        List<VisitedDetails> data = new Select().from(VisitedDetails.class).queryList();
        for (VisitedDetails v : data) {
            ImageView image = new ImageView(TestActivity.this);
            byte[] byteArray = v.signature.getBlob();
            Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArray));
            image.setImageBitmap(bm);
            layout.addView(image);
        }
    }
}
