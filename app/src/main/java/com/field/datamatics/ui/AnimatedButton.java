package com.field.datamatics.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.field.datamatics.R;


@SuppressLint("ClickableViewAccessibility")
public class AnimatedButton extends ImageButton {

    Context mContext;
    ImageButton view = this;
    private int animation;

    public AnimatedButton(Context context, AttributeSet attribute) {
        super(context, attribute);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attribute,
                R.styleable.AnimatedButton, 0, 0);
        try {
            animation = ta.getResourceId(R.styleable.AnimatedButton_animation,
                    R.anim.blink);
        } finally {
            ta.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (view.isEnabled())
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            Animation anim = AnimationUtils.loadAnimation(mContext,
                                    animation);
                            view.startAnimation(anim);

                        }
                    });
                    break;
                case KeyEvent.ACTION_UP:

                    break;
                default:
                    break;
            }

        return super.onTouchEvent(event);
    }
}
