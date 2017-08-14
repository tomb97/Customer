package com.example.tombarrett.estimotemirror.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.awsSNS.IAWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.database.IDatabaseHelper;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class PaymentPresenter {

    private String snsMessage;
    private IDatabaseHelper dbhelper;
    private Cursor resultSet;
    private Context context;

    public PaymentPresenter(String snsMessage, Context context){
        this.snsMessage=snsMessage;
        this.context=context;
    }

    public String createDigitalReceipt(){
        String toastMessage="";
        getFromDatabase();
        if(sendSNSMessage())
            toastMessage="Your purchase has been successful, please show the barcode to the shop assistant to redeem your points";
        else
            toastMessage="Your purchase has not been successful, please contact your provider to rectify this issue";
        return toastMessage;
    }

    public boolean sendSNSMessage(){
        boolean sent=true;
        if(resultSet==null)
            Log.d("SNS","null");
        if (resultSet != null && resultSet.moveToFirst()) {
            IAWSSNSManager awssnsManager = new AWSSNSManager();
            awssnsManager.publishMessageToCustomer(("Digital Receipt\n"+snsMessage+"\nName: "+resultSet.getString(1)+ "\nAddress: "+resultSet.getString(3)+"."),"Receipt");
        }
        else{
            Log.d("SNS","crash");
            sent= false;
        }
        disconnectFromDB();
        return sent;
    }

    public void getFromDatabase(){
        dbhelper= new DatabaseHelper(context);
        dbhelper.connectToDB();
        resultSet=dbhelper.getDetails();
    }

    public void disconnectFromDB(){
        dbhelper.disconnectToDB();
    }

}
