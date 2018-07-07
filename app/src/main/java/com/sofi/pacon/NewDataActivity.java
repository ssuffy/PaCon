package com.sofi.pacon;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    protected LinearLayout createTitleCell(String text) {
        return createCell(text, R.color.colorPrimaryDark, true, Gravity.CENTER_HORIZONTAL);
    }

    protected LinearLayout createCell(String text) {
        return createCell(text, R.color.colorPrimaryDark, false, Gravity.LEFT);
    }

    private LinearLayout createCell(String text, int bgColor, boolean isBold, int hGravity) {
        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(6, 0, 6, 0);

        LinearLayout cell = new LinearLayout(this);
        cell.setBackgroundColor(getResources().getColor(bgColor, getTheme()));
        cell.setLayoutParams(llp);
        cell.setPadding(20, 10, 20, 10);
        cell.setMinimumHeight(110);
        cell.setVerticalGravity(Gravity.CENTER_VERTICAL);
        cell.setHorizontalGravity(hGravity);

        TextView label = new TextView(this);
        if(isBold) {
            label.setTypeface(null, Typeface.BOLD);
        }
        label.setText(text);
        cell.addView(label);

        return cell;
    }
}
