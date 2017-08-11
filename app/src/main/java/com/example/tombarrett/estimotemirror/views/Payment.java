package com.example.tombarrett.estimotemirror.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.presenter.PaymentPresenter;

public class Payment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pay();
    }

    public void pay(){
        Intent myIntent = getIntent();
        String message = myIntent.getStringExtra("message");
        PaymentPresenter paymentPresenter= new PaymentPresenter(message,this);
        String toastMessage=paymentPresenter.createDigitalReceipt();
        Toast.makeText(this, toastMessage,
                Toast.LENGTH_LONG).show();
    }

}
