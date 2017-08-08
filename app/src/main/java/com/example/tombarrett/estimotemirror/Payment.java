package com.example.tombarrett.estimotemirror;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;

public class Payment extends AppCompatActivity {
    private DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pay();
    }

    public void pay(){
        Intent myIntent = getIntent(); // gets the previously created intent
        String message = myIntent.getStringExtra("message");
        dbhelper= new DatabaseHelper(this);
        dbhelper.connectToDB();
        Cursor resultSet=dbhelper.getDetails();
        if(resultSet==null)
            Log.d("SNS","null");
        if (resultSet != null && resultSet.moveToFirst()) {
            AWSSNSManager awssnsManager = new AWSSNSManager();
            awssnsManager.publishMessageToCustomer(("Digital Receipt\n"+message+"\nName: "+resultSet.getString(1)+ "\nAddress: "+resultSet.getString(3)+"."),"Receipt");
            Toast.makeText(this,
                    "Your purchase has been successful, please show the barcode to the shop assistant to redeem your points", Toast.LENGTH_LONG).show();
        }
        else{
            Log.d("SNS","crash");
        }
        dbhelper.disconnectToDB();
    }

}
