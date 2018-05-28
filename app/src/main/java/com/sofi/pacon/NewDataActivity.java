package com.sofi.pacon;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import java.util.Calendar;
import java.util.Set;

public class NewDataActivity extends AppCompatActivity {

    Calendar editDate = Calendar.getInstance();

    protected View.OnClickListener createOnCheckBoxClickListener(final Set<String> values) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox ckb = (CheckBox) v;
                if (ckb.isChecked()) {
                    values.add(ckb.getText().toString());
                } else {
                    values.remove(ckb.getText().toString());
                }
            }
        };
    }
}
