package com.example.tombarrett.estimotemirror.presenter;

import android.content.Context;
import android.database.Cursor;

import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.userDetails.UserDetails;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class UserDetailsPresenter {

    private DatabaseHelper dbhelper;
    private Context context;

    public UserDetailsPresenter(Context context){
        this.context=context;
        dbhelper=new DatabaseHelper(context);
    }

    public void connectToDB(){
        dbhelper.connectToDB();
    }

    public Cursor getDetails(){
        return dbhelper.getDetails();
    }

    public void disconnectToDB(){
        dbhelper.disconnectToDB();
    }

    public void insertToDB(){
        dbhelper.insertToDB();
    }

    public void updateDetails(UserDetails details){
        dbhelper.updateDetails(details);
    }
}
