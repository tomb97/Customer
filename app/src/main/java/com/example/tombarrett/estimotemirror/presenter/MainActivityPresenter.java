package com.example.tombarrett.estimotemirror.presenter;

import android.content.Context;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class MainActivityPresenter {

    private DatabaseHelper dbHelper;
    private Context context;

    public MainActivityPresenter(Context context){
        this.context=context;
        dbHelper=new DatabaseHelper(context);
    }

    public void initialiseEstimote(){
        EstimoteSDK.initialize(context.getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");
    }
}
