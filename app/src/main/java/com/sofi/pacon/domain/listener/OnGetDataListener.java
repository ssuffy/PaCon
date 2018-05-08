package com.sofi.pacon.domain.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetDataListener {
        void onSuccess(DataSnapshot data);
        void onFailed(DatabaseError databaseError);
}
