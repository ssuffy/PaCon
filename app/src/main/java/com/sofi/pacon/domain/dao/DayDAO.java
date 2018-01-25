package com.sofi.pacon.domain.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofi.pacon.domain.model.Day;

/**
 * Created by sofi on 25/01/18.
 */

public class DayDAO {

    private DatabaseReference fireDatabase;

    public DayDAO() {
        fireDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewDay(String dayId, Day newDay) {
        fireDatabase.child("testDays").child(dayId).setValue(newDay);
    }
}
