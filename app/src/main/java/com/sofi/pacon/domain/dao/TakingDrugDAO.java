package com.sofi.pacon.domain.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofi.pacon.domain.listener.OnGetDataListener;
import com.sofi.pacon.domain.model.Day;
import com.sofi.pacon.domain.model.TakingDrug;

public class TakingDrugDAO {

    private DatabaseReference fireDatabase;

    public TakingDrugDAO() {

        fireDatabase = FirebaseDatabase.getInstance().getReference("testTakingDrugs");
    }

    public void writeNewDrug(String takingDrugId, TakingDrug newTakingDrug) {
        fireDatabase.child(takingDrugId).setValue(newTakingDrug);
    }

    public void getDrug(String takingDrugId, final OnGetDataListener listener) {
        fireDatabase.child(takingDrugId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
}
