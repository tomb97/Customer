package com.example.tombarrett.estimotemirror.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.awsSNS.IAWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.database.IDatabaseHelper;
import com.example.tombarrett.estimotemirror.estimote.Product;
import com.example.tombarrett.estimotemirror.userDetails.UserDetails;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class MainActivityPresenter {

    private IDatabaseHelper dbHelper;
    private Context context;
    private Cursor resultSet;

    public MainActivityPresenter(Context context){
        this.context=context;
        dbHelper=new DatabaseHelper(context);
    }

    public void initialiseEstimote(){
        EstimoteSDK.initialize(context.getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");
    }

    public void connectToDB(){
        dbHelper.connectToDB();
    }

    public Cursor getShoe(){
        return dbHelper.getShoe();
    }

    public Cursor getDetails(){
        return dbHelper.getDetails();
    }

    public void disconnectToDB(){
        dbHelper.disconnectToDB();
    }

    public void sendEmailtoSA(Product product, Cursor resultSet){
        IAWSSNSManager awssnsManager = new AWSSNSManager();
        awssnsManager.publishMessageToShopAssistant((product.getEmailMessageSA(resultSet.getString(1), resultSet.getString(5))), "Customer is interested in a product!");
    }

    public void pickup(Product product){
        connectToDB();
        resultSet=getDetails();
        if (resultSet != null && resultSet.moveToFirst()) {
            sendEmailtoSA(product,resultSet);
        }
        else{
            Log.d("SNS","crash");
        }
        disconnectToDB();
    }

}
