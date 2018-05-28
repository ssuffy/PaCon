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

import static android.content.ContentValues.TAG;

/**
 * Created by sofi on 05/01/18.
 */

public class ReferencesDAO {

    private DatabaseReference fireDatabase;
    private Context context;

    public ReferencesDAO(Context context) {
        fireDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public RelativeLayout getLocations(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Location", relativeLayout, listenerOnClick);
    }

    public RelativeLayout getEnvironments(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Environment", relativeLayout, listenerOnClick);
    }

    public RelativeLayout getActivities(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Activity", relativeLayout, listenerOnClick);
    }

    public RelativeLayout getFeelings(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Felt", relativeLayout, listenerOnClick);
    }

    public RelativeLayout getContributeFactors(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("ContributeFactor", relativeLayout, listenerOnClick);
    }

    public RelativeLayout getRelieveEffects(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Effects", relativeLayout, listenerOnClick);
    }
    
    public RelativeLayout getListReferences(String type, final RelativeLayout checkBoxesLayout, final View.OnClickListener listenerOnClick) {
        DatabaseReference databaseReference = fireDatabase.child(type);

        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> result = dataSnapshot.getChildren();

                    int cpt = 0, up = 0;

                    for (DataSnapshot value : result) {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(10, 10,50, 0);
                        String reference = value.getValue().toString();
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setText(reference);
                        int id = View.generateViewId();
                        checkBox.setId(id);
                        if (cpt > 0) {
                            layoutParams.addRule(RelativeLayout.BELOW, up);
                            if(cpt % 2 != 0) {
                                layoutParams.setMargins(500, 10,10, 0);
                            }
                        }
                        checkBox.setLayoutParams(layoutParams);
                        checkBox.setOnClickListener(listenerOnClick);
                        checkBoxesLayout.addView(checkBox);
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
        return checkBoxesLayout;
    }

    public RelativeLayout getDrugs(RelativeLayout relativeLayout, View.OnClickListener listenerOnClick) {
        return getListReferences("Drug", relativeLayout, listenerOnClick);
    }
}
