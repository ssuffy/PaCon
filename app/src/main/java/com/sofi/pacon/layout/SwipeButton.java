package com.sofi.pacon.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sofi.pacon.R;

public class SwipeButton extends RelativeLayout {

    private String label;
    private Drawable drawableIcon;

    private TextView textView1;
    private ImageView icon;
    private RelativeLayout layout;

    public SwipeButton(Context context) {
        super(context);
        init();
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.swipe_button, this);
        this.textView1 = findViewById(R.id.header);
        this.icon = findViewById(R.id.icon);
        this.layout = findViewById(R.id.layout_bg);
    }

    private void init(Context context, AttributeSet attrs) {

        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SwipeButton, 0, 0);
        try {
            label = a.getString(R.styleable.SwipeButton_label);
            drawableIcon = a.getDrawable(R.styleable.SwipeButton_icon);
        } finally {
            a.recycle();
        }
        this.textView1.setText(label);
        this.icon.setImageDrawable(drawableIcon);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int h = layout.getHeight();
        int p = h / 4;
        this.icon.setPadding(p, p, p, p);
    }
}
