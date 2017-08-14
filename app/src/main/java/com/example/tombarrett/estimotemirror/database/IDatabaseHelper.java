package com.example.tombarrett.estimotemirror.database;

import android.database.Cursor;

import com.example.tombarrett.estimotemirror.userDetails.UserDetails;

/**
 * Created by tombarrett on 14/08/2017.
 */

public interface IDatabaseHelper {
    void connectToDB();
    void disconnectToDB();
    void updateDetails(UserDetails details);
    Cursor getDetails();
    Cursor getShoe();
    void insertToDB();
}
