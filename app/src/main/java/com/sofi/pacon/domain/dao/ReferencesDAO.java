package com.sofi.pacon.domain.dao;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by sofi on 05/01/18.
 */

public class ReferencesDAO {

    private DatabaseReference fireDatabase;
    private Context context;

    public ReferencesDAO(RelativeLayout.LayoutParams layoutParams, Context context) {
        fireDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public List<String> getLocations(RelativeLayout relativeLayout) {
        return getListReferences("Location", relativeLayout);
    }

    public List<String> getActivities(RelativeLayout relativeLayout) {
        return getListReferences("Activity", relativeLayout);
    }

    public List<String> getFeelings(RelativeLayout relativeLayout) {
        return getListReferences("Felt", relativeLayout);
    }

    public List<String> getContributeFactors(RelativeLayout relativeLayout) {
        return getListReferences("ContributeFactor", relativeLayout);
    }

    public List<String> getRelieveEffects(RelativeLayout relativeLayout) {
        return getListReferences("Effects", relativeLayout);
    }

    public List<String> getIneffectiveEffect(RelativeLayout relativeLayout) {
        return getListReferences("Effects", relativeLayout);
    }

    public List<String> getListReferences(String type, final RelativeLayout checkBoxesLayout) {
        DatabaseReference databaseReference = fireDatabase.child(type);

        final List<String> listResult = new ArrayList<>();

        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> result = dataSnapshot.getChildren();

                    int cpt = 0, up = 0, left = 0;

                    for (DataSnapshot value : result) {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        String reference = value.getValue().toString();
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setText(reference);
                        int id = View.generateViewId();
                        checkBox.setId(id);
                        if (cpt > 0) {
                            layoutParams.addRule(RelativeLayout.RIGHT_OF, left);
                            layoutParams.addRule(RelativeLayout.BELOW, up);
                        }
                        checkBox.setLayoutParams(layoutParams);
                        checkBoxesLayout.addView(checkBox);
                        left = (cpt % 2 == 0) ? id : 0;
                        up = (cpt % 2 == 0) ? up : id;
                        cpt++;
                    }
                } else {
                    Log.e(TAG, "Erreur lors de la lecture de la reference");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });

        return listResult;
    }
}
