package com.sofi.pacon.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sofi.pacon.R;

public class MedicationLine extends RelativeLayout {

    private TextView textViewLabel;
    private TextView textViewDosage;
    private TextView textViewTime;
    private TextView textViewQty;

    private ImageView trashIcon;

    public MedicationLine(Context context) {
        super(context);
        init();
    }

    public MedicationLine(Context context, String label, String dosage, String time, int quantity, OnClickListener onClickListener) {
        super(context);
        init(label, dosage, time, quantity, onClickListener);
    }

    private void init() {
        inflate(getContext(), R.layout.medication_line, this);
        this.textViewLabel = findViewById(R.id.name);
        this.textViewDosage = findViewById(R.id.description);
        this.textViewTime = findViewById(R.id.time);
        this.textViewQty = findViewById(R.id.quantity);
        this.trashIcon = findViewById(R.id.trashIcon);
    }

    private void init(String label, String dosage, String time, int quantity, OnClickListener callback) {

        init();
        // Setting the attributes to view.
        this.textViewLabel.setText(label);
        this.textViewDosage.setText(dosage);
        this.textViewTime.setText(time);
        this.textViewQty.setText(String.valueOf(quantity));

        trashIcon.setClickable(true);
        trashIcon.setOnClickListener(callback);
    }

}
