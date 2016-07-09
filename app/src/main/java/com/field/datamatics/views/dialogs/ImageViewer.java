package com.field.datamatics.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.field.datamatics.R;
import com.field.datamatics.utils.ImageCompressor;

import java.io.File;

/**
 * Created by Anoop on 29-05-2016.
 */
public class ImageViewer {
    private static ImageViewer instance=new ImageViewer();
    private Context context;
    private Dialog dialog;
    private ImageView imageView;
    private Button next,prev;
    private File[] imageFiles;
    private int position=0;
    public static  ImageViewer getInstance(){
        return instance;
    }
    public void showImageViewer(Context context, final File[] imageFiles){
        this.context=context;
        this.imageFiles=imageFiles;
        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_image_view);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        imageView= (ImageView) dialog.findViewById(R.id.iv_pic);
        prev= (Button) dialog.findViewById(R.id.btn_prev);
        next= (Button) dialog.findViewById(R.id.btn_next);
        showImage(imageFiles[0].getPath());
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                    return;
                --position;
                showImage(imageFiles[position].getPath());

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==(imageFiles.length-1))
                    return;
                ++position;
                showImage(imageFiles[position].getPath());

            }
        });
        dialog.show();

    }
    private void showImage(String path){
        try {
            Bitmap bm= ImageCompressor.getInstance().decodeSampledBitmapFromResource(path,600,600);
            imageView.setImageBitmap(bm);
        } catch (Exception e) {
            Log.e("Error","Error");
            e.printStackTrace();
        }
    }


}
