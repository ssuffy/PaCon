package com.sofi.pacon.domain.dao;


import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofi.pacon.domain.listener.OnGetDataListener;
import com.sofi.pacon.domain.model.Day;

import static android.content.ContentValues.TAG;

/**
 * Created by sofi on 25/01/18.
 */

public class DayDAO {

    private DatabaseReference fireDatabase;

    public DayDAO() {
        fireDatabase = FirebaseDatabase.getInstance().getReference("testDays");
    }

    public void writeNewDay(String dayId, Day newDay) {
        fireDatabase.child(dayId).setValue(newDay);
    }

    public void getDay(String dayId, final OnGetDataListener listener) {
        fireDatabase.child(dayId).addListenerForSingleValueEvent(new ValueEventListener() {

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
