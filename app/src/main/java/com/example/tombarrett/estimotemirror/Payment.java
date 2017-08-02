package com.example.tombarrett.estimotemirror;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent myIntent = getIntent(); // gets the previously created intent
        String message = myIntent.getStringExtra("message");
        try {
            SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR," +
                    " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
            Cursor resultSet = db.rawQuery("SELECT * FROM UserDetails WHERE ID='1';", null);
            if(resultSet==null)
                Log.d("SNS","null");
            if (resultSet != null && resultSet.moveToFirst()) {
                AWSSNSManager awssnsManager = new AWSSNSManager();
                awssnsManager.publishMessageToCustomer(("Digital Receipt\n"+message),"Receipt");
                Toast.makeText(this,
                        "Your purchase has been successful, please show the barcode to the shop assistant to redeem your points", Toast.LENGTH_LONG).show();
            }
            else{
                Log.d("SNS","crash");
            }
        }
        catch (Exception ex){
            Log.d("SNS","dbcrash");
        }
    }

}
