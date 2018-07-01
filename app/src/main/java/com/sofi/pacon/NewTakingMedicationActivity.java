package com.sofi.pacon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.sofi.pacon.domain.dao.ReferencesDAO;
import com.sofi.pacon.domain.dao.TakingMedicationDayDAO;
import com.sofi.pacon.domain.listener.OnGetDataListener;
import com.sofi.pacon.domain.model.Medication;
import com.sofi.pacon.domain.model.TakingMedication;
import com.sofi.pacon.domain.model.TakingMedicationDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NewTakingMedicationActivity extends NewDataActivity {

    private static final String TAG = "NewTakingMedicationActivity";

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    private ReferencesDAO referencesDAO;

    private RelativeLayout checkboxesMedications;

    private TextView lblTitle_moment;

    private Map<String, Set<TakingMedication>> tookMedications = new HashMap<>();

    final TakingMedicationDayDAO takingMedicationDAO = new TakingMedicationDayDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        referencesDAO = new ReferencesDAO(this);

        setContentView(R.layout.activity_new_medication);

        checkboxesMedications = findViewById(R.id.checkBoxesMedications);

        lblTitle_moment = findViewById(R.id.lblTitle_moment);

        referencesDAO.getMedications(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                if (data != null && data.exists()) {
                    Iterable<DataSnapshot> medications = data.getChildren();

                    int previousLayoutId = 0;

                    for (DataSnapshot value : medications) {

                        RelativeLayout.LayoutParams checkBoxLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        Medication medication = value.getValue(Medication.class);

                        if (previousLayoutId != 0) {
                            checkBoxLayoutParams.addRule(RelativeLayout.BELOW, previousLayoutId);
                        }

                        CheckBox checkboxLabelMedication = new CheckBox(NewTakingMedicationActivity.this);
                        checkboxLabelMedication.setText(medication.getName());
                        checkboxLabelMedication.setId(View.generateViewId());
                        checkboxLabelMedication.setLayoutParams(checkBoxLayoutParams);

                        RelativeLayout layout = new RelativeLayout(NewTakingMedicationActivity.this);
                        layout.setId(View.generateViewId());
                        previousLayoutId = layout.getId();

                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.BELOW, checkboxLabelMedication.getId());
                        layout.setLayoutParams(layoutParams);

                        int previousNPId = 0;

                        for (String dose : medication.getDosage().getValues()) {
                            ScrollableNumberPicker numberPicker = new ScrollableNumberPicker(NewTakingMedicationActivity.this);
                            RelativeLayout.LayoutParams numberPickerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            if (previousNPId != 0) {
                                numberPickerParams.addRule(RelativeLayout.BELOW, previousNPId);
                            }
                            numberPicker.setLayoutParams(numberPickerParams);
                            numberPicker.setMinValue(0);
                            numberPicker.setMaxValue(5);
                            numberPicker.setId(View.generateViewId());
                            numberPicker.setListener(createScrollableNumberPickerListener(medication.getName(), dose, medication.getDosage().getUnit()));

                            TextView label = new TextView(NewTakingMedicationActivity.this);
                            RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            labelParams.addRule(RelativeLayout.RIGHT_OF, numberPicker.getId());
                            labelParams.addRule(RelativeLayout.BELOW, previousNPId);
                            label.setLayoutParams(labelParams);
                            label.setText(dose + " " + medication.getDosage().getUnit());

                            layout.addView(numberPicker);
                            layout.addView(label);

                            previousNPId = numberPicker.getId();
                        }

                        checkboxLabelMedication.setOnClickListener(createOnCheckBoxClickListener(layout, medication.getName()));
                        layout.setVisibility(View.GONE);

                        checkboxesMedications.addView(checkboxLabelMedication);
                        checkboxesMedications.addView(layout);
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load medication data");
            }
        });
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
        updateDate();
    }

    private void updateDate() {
        resetActivity();
        String myFormat = "dd/MM/yyyy à kk:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        takingMedicationDAO.getMedication(formatDate.format(editDate.getTime()), new OnGetDataListener() {

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load data");
            }

            @Override
            public void onSuccess(DataSnapshot data) {
            }
        });

        lblTitle_moment.setText("le : " + sdf.format(editDate.getTime()));

    }

    private void resetActivity() {
    }

    public void save(View v) {

        List<TakingMedication> takingMedications = tookMedications.values().stream().flatMap(set -> set.stream()).collect(Collectors.toList());

        TakingMedicationDay takingMedicationDay = new TakingMedicationDay(editDate.getTime(), takingMedications);
        takingMedicationDAO.save(formatDate.format(editDate.getTime()), takingMedicationDay);

        setResult(Activity.RESULT_OK);
        finish();
    }

    public void cancel(View v) {
        finish();
    }

    protected View.OnClickListener createOnCheckBoxClickListener(final RelativeLayout layout, final String medicationName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox ckb = (CheckBox) v;
                if (ckb.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                    tookMedications.put(medicationName, new HashSet<TakingMedication>());
                } else {
                    layout.setVisibility(View.GONE);
                    tookMedications.remove(medicationName);
                }
            }
        };
    }

    protected ScrollableNumberPickerListener createScrollableNumberPickerListener(final String medicationName, final String dosage, final String measure) {
        return new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                Set<TakingMedication> takingMedications = tookMedications.get(medicationName);

                boolean found = false;

                if (takingMedications != null) {
                    for (TakingMedication takingMedication : takingMedications) {
                        if (dosage != null && dosage.equals(takingMedication.getMedicationDose()) && measure != null && measure.equals(takingMedication.getMeasure())) {
                            takingMedication.setQuantity(value);
                            found = true;
                        }
                    }
                    if (!found) {
                        TakingMedication takingMedication = new TakingMedication(medicationName, dosage, measure, value);
                        takingMedications.add(takingMedication);
                    }
                    tookMedications.put(medicationName, takingMedications);
                }
            }


        };
    }

}
