package com.sofi.pacon;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.sofi.pacon.domain.dao.DayDAO;
import com.sofi.pacon.domain.dao.ReferencesDAO;
import com.sofi.pacon.domain.model.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NewDayActivity extends AppCompatActivity {

    private SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHH");

    private ReferencesDAO referencesDAO;

    private TextView lblscoreEVA, lblTitle_day;

    private EditText nbHours;

    private RadioGroup rg_typePain, rg_painIntensity;

    private RelativeLayout checkboxesActivity, checkBoxesLocation, checkboxesEnvironment, checkBoxesFeelings,
            checkBoxesContributeFactor, checkBoxesRelieveEffect;

    private SeekBar evaBar;

    private Button save;

    private Set<String> activities = new HashSet<>(), locations = new HashSet<>(), environments = new HashSet<>(),
            feelings = new HashSet<>(), contriuteFactors = new HashSet<>(),
            relieveFactors = new HashSet<>();

    Calendar editDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        referencesDAO = new ReferencesDAO(this);
        final DayDAO dayDAO = new DayDAO();

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
        checkBoxesContributeFactor = referencesDAO.getContributeFactors(checkBoxesContributeFactor, createOnCheckBoxClickListener(contriuteFactors));

        checkBoxesRelieveEffect = findViewById(R.id.checkBoxesRelieveEffect);
        checkBoxesRelieveEffect = referencesDAO.getRelieveEffects(checkBoxesRelieveEffect, createOnCheckBoxClickListener(relieveFactors));

        nbHours = findViewById(R.id.editTextDuree);

        rg_typePain = findViewById(R.id.RadioGroupPainType);
        rg_painIntensity = findViewById(R.id.RadioGroupPainIntensity);

        save = findViewById(R.id.btn_saveNewDay);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Day nouvelleJournee = new Day();
                List<String> lstActivities = new ArrayList<>(), lstLocations = new ArrayList<>(), lstEnvironments = new ArrayList<>(),
                        lstFeelings = new ArrayList<>(), lstContributeFactors = new ArrayList<>(),
                        lstRelieveFactors = new ArrayList<>();


                Date date = new Date();
                nouvelleJournee.setDate(date);
                nouvelleJournee.setScore(Integer.parseInt(lblscoreEVA.getText().toString()));
                nouvelleJournee.setIntensity(((RadioButton) rg_painIntensity.findViewById(rg_painIntensity.getCheckedRadioButtonId())).getText().toString());
                lstLocations.addAll(locations);
                nouvelleJournee.setPainLocation(lstLocations);
                lstActivities.addAll(activities);
                nouvelleJournee.setActivity(lstActivities);
                lstContributeFactors.addAll(contriuteFactors);
                nouvelleJournee.setContributeFactor(lstContributeFactors);
                nouvelleJournee.setDuree(nbHours.getText() != null && !"".equals(nbHours.getText().toString().trim()) ?
                        new Integer(nbHours.getText().toString()) : 24);
                lstEnvironments.addAll(environments);
                nouvelleJournee.setEnvironment(lstEnvironments);
                lstFeelings.addAll(feelings);
                nouvelleJournee.setFeltPain(lstFeelings);
                //nouvelleJournee.setInterruption();
                nouvelleJournee.setMoments(getMoments());
                // nouvelleJournee.setPainDiffusion();
                nouvelleJournee.setRelieveEffect(lstRelieveFactors);
                if ((RadioButton) rg_typePain.findViewById(rg_typePain.getCheckedRadioButtonId()) != null)
                    nouvelleJournee.setType(((RadioButton) rg_typePain.findViewById(rg_typePain.getCheckedRadioButtonId())).getText().toString());
                dayDAO.writeNewDay(formatDate.format(date), nouvelleJournee);

            }
        });

    }

    private View.OnClickListener createOnCheckBoxClickListener(final Set<String> values) {
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
        CheckBox cb_allDay = findViewById(R.id.checkboxAllDay),
                cb_night = findViewById(R.id.checkboxNight),
                cb_am = findViewById(R.id.checkboxMorning),
                cb_pm = findViewById(R.id.checkboxAm),
                cb_evening = findViewById(R.id.checkboxEvening);

        if (cb_allDay.isChecked()) {
            result.add("Tout le jour");
        }
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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            editDate.set(Calendar.YEAR, year);
            editDate.set(Calendar.MONTH, monthOfYear);
            editDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void openDatePicker(View v) {
        new DatePickerDialog(this, date, editDate
                .get(Calendar.YEAR), editDate.get(Calendar.MONTH),
                editDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        lblTitle_day.setText("le : " + sdf.format(editDate.getTime()) + "?");
    }
}
