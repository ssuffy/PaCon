package com.sofi.pacon;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.sofi.pacon.domain.dao.DayDAO;
import com.sofi.pacon.domain.dao.ReferencesDAO;
import com.sofi.pacon.domain.listener.OnGetDataListener;
import com.sofi.pacon.domain.model.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class NewDayActivity extends NewDataActivity {

    private static final String TAG = "NewDayActivity";

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    private ReferencesDAO referencesDAO;

    private TextView lblscoreEVA, lblTitle_day;

    private EditText nbHours;

    private RadioGroup rg_typePain, rg_painIntensity;

    private RelativeLayout checkboxesActivity, checkBoxesLocation, checkboxesEnvironment, checkBoxesFeelings,
            checkBoxesContributeFactor, checkBoxesRelieveEffect;

    private SeekBar evaBar;

    private Button save;

    private Set<String> activities = new HashSet<>(), locations = new HashSet<>(), environments = new HashSet<>(),
            feelings = new HashSet<>(), contributeFactors = new HashSet<>(),
            relieveFactors = new HashSet<>();

    final DayDAO dayDAO = new DayDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        referencesDAO = new ReferencesDAO(this);

        setContentView(R.layout.activity_new_day);

        lblscoreEVA = findViewById(R.id.lblScoreEVA);
        evaBar = findViewById(R.id.evaBar);

        evaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeLblEVA(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        lblTitle_day = findViewById(R.id.lblTitle_day);

        checkBoxesLocation = findViewById(R.id.checkBoxesLocation);
        checkBoxesLocation = referencesDAO.getLocations(checkBoxesLocation, createOnCheckBoxClickListener(locations));

        checkboxesEnvironment = findViewById(R.id.checkBoxesEnvironment);
        checkboxesEnvironment = referencesDAO.getEnvironments(checkboxesEnvironment, createOnCheckBoxClickListener(environments));

        checkboxesActivity = findViewById(R.id.checkBoxesActivity);
        checkboxesActivity = referencesDAO.getActivities(checkboxesActivity, createOnCheckBoxClickListener(activities));

        checkBoxesFeelings = findViewById(R.id.checkBoxesFeelings);
        checkBoxesFeelings = referencesDAO.getFeelings(checkBoxesFeelings, createOnCheckBoxClickListener(feelings));

        checkBoxesContributeFactor = findViewById(R.id.checkBoxesContributeFactor);
        checkBoxesContributeFactor = referencesDAO.getContributeFactors(checkBoxesContributeFactor, createOnCheckBoxClickListener(contributeFactors));

        checkBoxesRelieveEffect = findViewById(R.id.checkBoxesRelieveEffect);
        checkBoxesRelieveEffect = referencesDAO.getRelieveEffects(checkBoxesRelieveEffect, createOnCheckBoxClickListener(relieveFactors));

        nbHours = findViewById(R.id.editTextDuree);

        rg_typePain = findViewById(R.id.RadioGroupPainType);
        rg_painIntensity = findViewById(R.id.RadioGroupPainIntensity);
        rg_painIntensity.check(R.id.radioFaible);

        save = findViewById(R.id.btn_saveNewDay);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Day newDay = new Day();
                List<String> lstActivities = new ArrayList<>(), lstLocations = new ArrayList<>(), lstEnvironments = new ArrayList<>(),
                        lstFeelings = new ArrayList<>(), lstContributeFactors = new ArrayList<>(),
                        lstRelieveFactors = new ArrayList<>();

                newDay.setDate(editDate.getTime());
                newDay.setScore(Integer.parseInt(lblscoreEVA.getText().toString()));
                newDay.setIntensity(((RadioButton) rg_painIntensity.findViewById(rg_painIntensity.getCheckedRadioButtonId())).getText().toString());
                lstLocations.addAll(locations);
                newDay.setPainLocation(lstLocations);
                lstActivities.addAll(activities);
                newDay.setActivity(lstActivities);
                lstContributeFactors.addAll(contributeFactors);
                newDay.setContributeFactor(lstContributeFactors);
                newDay.setDuree(nbHours.getText() != null && !"".equals(nbHours.getText().toString().trim()) ?
                        new Integer(nbHours.getText().toString()) : 24);
                lstEnvironments.addAll(environments);
                newDay.setEnvironment(lstEnvironments);
                lstFeelings.addAll(feelings);
                newDay.setFeltPain(lstFeelings);
                //nouvelleJournee.setInterruption();
                newDay.setMoments(getMoments());
                // nouvelleJournee.setPainDiffusion();
                lstRelieveFactors.addAll(relieveFactors);
                newDay.setRelieveEffect(lstRelieveFactors);
                if (rg_typePain.findViewById(rg_typePain.getCheckedRadioButtonId()) != null) {
                    newDay.setType(((RadioButton) rg_typePain.findViewById(rg_typePain.getCheckedRadioButtonId())).getText().toString());
                }

                dayDAO.writeNewDay(formatDate.format(editDate.getTime()), newDay);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    private void changeLblEVA(int progress) {
        lblscoreEVA.setText(String.valueOf(progress));

        if (progress < 2) {
            lblscoreEVA.setBackgroundResource(R.drawable.circle_green_one);
            rg_painIntensity.check(R.id.radioFaible);
        }
        if (progress >= 2 && progress < 4) {
            lblscoreEVA.setBackgroundResource(R.drawable.circle_green_two);
            rg_painIntensity.check(R.id.radioFaible);
        }
        if (progress >= 4 && progress < 6) {
            lblscoreEVA.setBackgroundResource(R.drawable.circle_yellow_one);
            rg_painIntensity.check(R.id.radioMoyen);
        }
        if (progress >= 6 && progress < 8) {
            lblscoreEVA.setBackgroundResource(R.drawable.circle_yellow_two);
            rg_painIntensity.check(R.id.radioMoyenFort);

        }
        if (progress >= 8) {
            lblscoreEVA.setBackgroundResource(R.drawable.circle_red);
            rg_painIntensity.check(R.id.radioFort);
        }
    }

    private List<String> getMoments() {
        List<String> result = new ArrayList<>();
        CheckBox cb_night = findViewById(R.id.checkboxNight),
                cb_am = findViewById(R.id.checkboxMorning),
                cb_pm = findViewById(R.id.checkboxAm),
                cb_evening = findViewById(R.id.checkboxEvening);

        if (cb_night.isChecked()) {
            result.add("Nuit");
        }
        if (cb_am.isChecked()) {
            result.add("Matin");
        }
        if (cb_pm.isChecked()) {
            result.add("Après-midi");
        }
        if (cb_evening.isChecked()) {
            result.add("Soirée");
        }

        return result;
    }

    private void setMoments(List<String> moments) {
        ((CheckBox) findViewById(R.id.checkboxNight)).setChecked(moments.contains("Nuit"));
        ((CheckBox) findViewById(R.id.checkboxMorning)).setChecked(moments.contains("Matin"));
        ((CheckBox) findViewById(R.id.checkboxAm)).setChecked(moments.contains("Après-midi"));
        ((CheckBox) findViewById(R.id.checkboxEvening)).setChecked(moments.contains("Soirée"));
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            editDate.set(Calendar.YEAR, year);
            editDate.set(Calendar.MONTH, monthOfYear);
            editDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }

    };

    public void openDatePicker(View v) {
        new DatePickerDialog(this, date, editDate
                .get(Calendar.YEAR), editDate.get(Calendar.MONTH),
                editDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void checkCheckBoxes(RelativeLayout checkBoxesView, Set<String> values, List<String> listValues) {

        if (listValues != null && listValues.size() > 0) {

            Map<String, Integer> keyOfMaps = new HashMap<>();

            for (int i = 0; i < checkBoxesView.getChildCount(); i++) {
                View child = checkBoxesView.getChildAt(i);
                if (child instanceof CheckBox) {
                    keyOfMaps.put(((CheckBox) child).getText().toString(), child.getId());
                }
            }

            for (String value : listValues) {
                if (keyOfMaps.containsKey(value)) {
                    ((CheckBox) findViewById(keyOfMaps.get(value))).setChecked(true);
                    values.add(value);
                } else {
                    System.out.println(value + " doesn't exist!");
                }
            }
        }

    }

    private void updateDate() {

        resetActivity();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dayDAO.getDay(formatDate.format(editDate.getTime()), new OnGetDataListener() {

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load data");
            }

            @Override
            public void onSuccess(DataSnapshot data) {

                if (data != null && data.exists()) {

                    Day existDay = data.getValue(Day.class);
                    lblscoreEVA.setText(String.valueOf(existDay.getScore()));
                    evaBar.setProgress(existDay.getScore());

                    ((RadioButton) findViewById(R.id.radioFaible)).setChecked(getString(R.string.faible).equals(existDay.getIntensity()));
                    ((RadioButton) findViewById(R.id.radioMoyen)).setChecked(getString(R.string.moyen).equals(existDay.getIntensity()));
                    ((RadioButton) findViewById(R.id.radioMoyenFort)).setChecked(getString(R.string.mFort).equals(existDay.getIntensity()));
                    ((RadioButton) findViewById(R.id.radioFort)).setChecked(getString(R.string.fort).equals(existDay.getIntensity()));

                    checkCheckBoxes(checkBoxesLocation, locations, existDay.getPainLocation());
                    checkCheckBoxes(checkboxesActivity, activities, existDay.getActivity());
                    checkCheckBoxes(checkBoxesContributeFactor, contributeFactors, existDay.getContributeFactor());

                    checkCheckBoxes(checkBoxesFeelings, feelings, existDay.getFeltPain());
                    checkCheckBoxes(checkboxesEnvironment, environments, existDay.getEnvironment());
                    checkCheckBoxes(checkBoxesRelieveEffect, relieveFactors, existDay.getRelieveEffect());

                    if (existDay.getDuree() != 0) {
                        nbHours.setText(String.valueOf(existDay.getDuree()));
                    }

                    if (existDay.getType() != null) {
                        ((RadioButton) findViewById(R.id.radioIntermittent)).setChecked(getString(R.string.intermittent).equals(existDay.getType()));
                        ((RadioButton) findViewById(R.id.radioConstant)).setChecked(getString(R.string.constant).equals(existDay.getType()));
                        ((RadioButton) findViewById(R.id.radioPercee)).setChecked(getString(R.string.perce).equals(existDay.getType()));
                    }
                    setMoments(existDay.getMoments());
                }
            }
        });

        lblTitle_day.setText("le : " + sdf.format(editDate.getTime()) + "?");

    }

    private void resetActivity() {
        lblscoreEVA.setText("0");
        evaBar.setProgress(0);

        ((RadioButton) findViewById(R.id.radioFaible)).setChecked(false);
        ((RadioButton) findViewById(R.id.radioMoyen)).setChecked(false);
        ((RadioButton) findViewById(R.id.radioMoyenFort)).setChecked(false);
        ((RadioButton) findViewById(R.id.radioFort)).setChecked(false);

        resetCheckBoxes(checkBoxesLocation);
        resetCheckBoxes(checkboxesActivity);
        resetCheckBoxes(checkBoxesContributeFactor);

        resetCheckBoxes(checkBoxesFeelings);
        resetCheckBoxes(checkboxesEnvironment);
        resetCheckBoxes(checkBoxesRelieveEffect);

        nbHours.setText("");

        ((RadioButton) findViewById(R.id.radioIntermittent)).setChecked(false);
        ((RadioButton) findViewById(R.id.radioConstant)).setChecked(false);
        ((RadioButton) findViewById(R.id.radioPercee)).setChecked(false);

        setMoments(Collections.<String>emptyList());
    }

    private void resetCheckBoxes(RelativeLayout checkBoxesView) {
        for (int i = 0; i < checkBoxesView.getChildCount(); i++) {
            View child = checkBoxesView.getChildAt(i);
            if (child instanceof CheckBox) {
                ((CheckBox) child).setChecked(false);
            }
        }

    }

    public void cancel(View v) {
        finish();
    }
}
