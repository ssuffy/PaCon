package com.sofi.pacon.domain.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofi.pacon.domain.listener.OnGetDataListener;
import com.sofi.pacon.domain.model.TakingMedication;
import com.sofi.pacon.domain.model.TakingMedicationDay;

public class TakingMedicationDayDAO {

    private DatabaseReference fireDatabase;

    public TakingMedicationDayDAO() {

        fireDatabase = FirebaseDatabase.getInstance().getReference("testTakingMedications");
    }

    public void save(String takingMedicationId, TakingMedicationDay newTakingMedicationDay) {
        fireDatabase.child(takingMedicationId).setValue(newTakingMedicationDay);
    }

    public void getMedication(String takingMedicationId, final OnGetDataListener listener) {
        fireDatabase.child(takingMedicationId).addListenerForSingleValueEvent(new ValueEventListener() {

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
