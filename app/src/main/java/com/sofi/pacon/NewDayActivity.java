package com.sofi.pacon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sofi.pacon.domain.dao.ReferencesDAO;

import java.util.List;

public class NewDayActivity extends AppCompatActivity {

    private ReferencesDAO referencesDAO;

    private TextView lblscoreEVA;

    private RelativeLayout checkboxesActivity, checkBoxesLocation, checkBoxesFeelings,
            checkBoxesContributeFactor, checkBoxesRelieveEffect, checkBoxesIneffectiveEffect;

    private SeekBar evaBar;

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

        checkBoxesLocation = findViewById(R.id.checkBoxesLocation);
        referencesDAO.getLocations(checkBoxesLocation);

        checkboxesActivity = findViewById(R.id.checkBoxesActivity);
        referencesDAO.getActivities(checkboxesActivity);

        checkBoxesFeelings = findViewById(R.id.checkBoxesFeelings);
        referencesDAO.getFeelings(checkBoxesFeelings);

        checkBoxesContributeFactor = findViewById(R.id.checkBoxesContributeFactor);
        referencesDAO.getContributeFactors(checkBoxesContributeFactor);

        checkBoxesRelieveEffect = findViewById(R.id.checkBoxesRelieveEffect);
        referencesDAO.getRelieveEffects(checkBoxesRelieveEffect);

        checkBoxesIneffectiveEffect = findViewById(R.id.checkBoxesIneffectiveFactor);
        referencesDAO.getIneffectiveEffect(checkBoxesIneffectiveEffect);
    }

    private void changeLblEVA(int progress) {
        lblscoreEVA.setText(String.valueOf(progress));

        if (progress < 2) {
            lblscoreEVA.setBackgroundColor(Color.GREEN);
        }
        if (progress >= 2 && progress < 4) {
            lblscoreEVA.setBackgroundColor(Color.rgb(214, 243, 65));
        }
        if (progress >= 4 && progress < 6) {
            lblscoreEVA.setBackgroundColor(Color.YELLOW);
        }
        if (progress >= 6 && progress < 8) {
            lblscoreEVA.setBackgroundColor(Color.rgb(255, 195, 0));
        }
        if (progress >= 8) {
            lblscoreEVA.setBackgroundColor(Color.RED);
        }
    }

}
