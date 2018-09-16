package com.sofi.pacon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.sofi.pacon.layout.MedicationLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewTakingMedicationActivity extends NewDataActivity {

    private static final String TAG = "NewTakingMedicationActivity";

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    private ReferencesDAO referencesDAO;

    private RelativeLayout checkboxesMedications;

    private TextView lblTitle_moment;

    private Map<String, List<TakingMedication>> tookMedications = new HashMap<>();
    private Map<String, Integer> mapCheckboxIds = new HashMap<>();
    private Map<String, Integer> mapRelLayIds = new HashMap<>();
    private Map<String, Integer> mapScrollNumberPickerIds = new HashMap<>();

    private TableLayout tableLayout;

    private boolean notManualUpd = false;

    final TakingMedicationDayDAO takingMedicationDAO = new TakingMedicationDayDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        referencesDAO = new ReferencesDAO(this);

        setContentView(R.layout.activity_new_medication);

        checkboxesMedications = findViewById(R.id.checkBoxesMedications);
        tableLayout = findViewById(R.id.rel_table_layout);

        lblTitle_moment = findViewById(R.id.lblTitle_moment);

        referencesDAO.getMedications(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                notManualUpd = true;
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
                        mapCheckboxIds.put(medication.getName(), checkboxLabelMedication.getId());

                        RelativeLayout layout = new RelativeLayout(NewTakingMedicationActivity.this);
                        layout.setId(View.generateViewId());
                        previousLayoutId = layout.getId();
                        mapRelLayIds.put(medication.getName(), layout.getId());

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
                            mapScrollNumberPickerIds.put(medication.getName() + "-" + dose, numberPicker.getId());

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
                updateDate();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load medication data");
            }
        });
    }


    DatePickerDialog.OnDateSetListener date = (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
        editDate.set(Calendar.YEAR, year);
        editDate.set(Calendar.MONTH, monthOfYear);
        editDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDate();
    };

    public void openDatePicker(View v) {
        new DatePickerDialog(v.getContext(), date, editDate
                .get(Calendar.YEAR), editDate.get(Calendar.MONTH),
                editDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void openTimePicker(Activity activity, String medicationName, List<TakingMedication> takingMedications, TakingMedication takingMedication) {
        Calendar editTime = Calendar.getInstance();
        editTime.set(editDate.YEAR, editDate.MONTH, editDate.DATE);

        TimePickerDialog.OnTimeSetListener time = (TimePicker view, int hourOfDay, int minute) -> {
            editTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            editTime.set(Calendar.MINUTE, minute);
            takingMedication.setTimeWithDate(editTime.getTime());
            updateTookMedications(medicationName, takingMedications, takingMedication);
        };
        new TimePickerDialog(activity, time, editTime.get(Calendar.HOUR_OF_DAY), editTime.get(Calendar.MINUTE), true).show();
    }

    private void updateDate() {
        String myFormat = "dd/MM/yyyy";
        resetActivity();
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        takingMedicationDAO.getMedication(formatDate.format(editDate.getTime()), new OnGetDataListener() {

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load data");
            }

            @Override
            public void onSuccess(DataSnapshot data) {
                Log.d(TAG, "Failed to load data");

                if (data != null && data.exists()) {
                    TakingMedicationDay existDay = data.getValue(TakingMedicationDay.class);
                    completeTableLayout(existDay);
                }
                notManualUpd = false;
            }
        });

        lblTitle_moment.setText("le : " + sdf.format(editDate.getTime()));
    }

    private void resetActivity() {
        for (int i = 0; i < checkboxesMedications.getChildCount(); i++) {
            View child = checkboxesMedications.getChildAt(i);

            if (child instanceof CheckBox) {
                ((CheckBox) child).setChecked(false);
            }

            if (child instanceof RelativeLayout) {
                for (int j = 0; j < ((RelativeLayout) child).getChildCount(); j++) {
                    View childLayout = ((RelativeLayout) child).getChildAt(i);

                    if (child instanceof ScrollableNumberPicker) {
                        ((NumberPicker) childLayout).setValue(0);
                    }
                }
                child.setVisibility(View.GONE);
            }
        }
        tableLayout.invalidate();
        tableLayout.removeAllViews();
        tableLayout.refreshDrawableState();
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
        return (View v) -> {
            CheckBox ckb = (CheckBox) v;
            if (ckb.isChecked()) {
                layout.setVisibility(View.VISIBLE);
                if(!tookMedications.containsKey(medicationName)) {
                    tookMedications.put(medicationName, new ArrayList<>());
                }
            } else {
                layout.setVisibility(View.GONE);
                tookMedications.remove(medicationName);
            }
        };
    }

    protected ScrollableNumberPickerListener createScrollableNumberPickerListener(final String medicationName, final String dosage, final String measure) {
        return (int value) -> {
            if (!notManualUpd) {
                updateTookMedication(medicationName, measure, dosage, value);
            }
        };
    }

    protected void completeTableLayout(TakingMedicationDay existTakingMedicationDay) {

        if (existTakingMedicationDay != null && existTakingMedicationDay.getTakingMedications() != null && existTakingMedicationDay.getTakingMedications().size() > 0) {

            List<TakingMedication> existTakingMedications = existTakingMedicationDay.getTakingMedications();

            tableLayout.setPadding(0, 0, 0, 20);

            for (TakingMedication existTakingMedication : existTakingMedications) {

                if (existTakingMedication.getQuantity() > 0) {

                    TableRow tableRow = new TableRow(this);
                    tableRow.setPadding(0, 0, 0, 0);

                    String label = existTakingMedication.getName();
                    String dosage = existTakingMedication.getMedicationDose() + " " + existTakingMedication.getMeasure();
                    String time = existTakingMedication.getTime();
                    int quantity = existTakingMedication.getQuantity();

                    MedicationLine medicationLine = new MedicationLine(this, label, dosage, time, quantity, (View v) -> {
                        existTakingMedication.setQuantity(quantity - 1);
                        List<TakingMedication> tookMedicationsList = tookMedications.get(existTakingMedication.getName());
                        tookMedicationsList.stream()
                                .filter(medication -> medication.getTime().equals(time))
                                .findFirst()
                                .map(m -> tookMedicationsList.remove(m));
                        tookMedications.put(existTakingMedication.getName(), tookMedicationsList);
                        tableLayout.removeViewInLayout(tableRow);
                        save(v);
                    });

                    tableRow.addView(medicationLine);

                    List<TakingMedication> takingMedications = tookMedications.get(existTakingMedication.getName());
                    if (takingMedications == null) {
                        takingMedications = new ArrayList<>();
                    }
                    updateTookMedications(existTakingMedication.getName(), takingMedications, existTakingMedication);

                    tableLayout.addView(tableRow);
                }
            }
        }
    }

    private void updateTookMedication(String medicationName, String measure, String dosage, int value) {
        List<TakingMedication> takingMedications = tookMedications.get(medicationName);

        if (takingMedications == null) {
            takingMedications = new ArrayList<>();
        }
        Date time = null;
        TakingMedication takingMedication = new TakingMedication(time, medicationName, dosage, measure, value);
        openTimePicker(this, medicationName, takingMedications, takingMedication);
    }

    private void updateTookMedications(String medicationName, List<TakingMedication> takingMedications, TakingMedication takingMedication) {
        takingMedications.add(takingMedication);
        tookMedications.put(medicationName, takingMedications);
    }

}
