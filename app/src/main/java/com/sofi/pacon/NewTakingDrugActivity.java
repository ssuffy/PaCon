package com.sofi.pacon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.sofi.pacon.domain.dao.TakingDrugDAO;
import com.sofi.pacon.domain.dao.ReferencesDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class NewTakingDrugActivity extends NewDataActivity {

    private static final String TAG = "NewDayActivity";

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    private ReferencesDAO referencesDAO;

    private RelativeLayout checkboxesDrugs;

    private Button save;

    private Set<String> drugs = new HashSet<>();

    final TakingDrugDAO takingDrugDAO = new TakingDrugDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        referencesDAO = new ReferencesDAO(this);

        setContentView(R.layout.activity_new_drug);

        checkboxesDrugs = findViewById(R.id.checkBoxesDrugs);
        checkboxesDrugs = referencesDAO.getDrugs(checkboxesDrugs, createOnCheckBoxClickListener(drugs));

    }

    public void openDateTimePicker(final View v) {
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editDate.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        editDate.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen time is:  " + editDate.getTime());
                    }
                }, editDate.get(Calendar.HOUR_OF_DAY), editDate.get(Calendar.MINUTE), true).show();
            }
        }, editDate.get(Calendar.YEAR), editDate.get(Calendar.MONTH),
                editDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void save(View v) {
        finish();
    }

    public void cancel(View v) {
        finish();
    }

}
