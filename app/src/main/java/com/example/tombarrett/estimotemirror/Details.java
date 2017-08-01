package com.example.tombarrett.estimotemirror;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {

    private UserDetails details;
    private Cursor resultSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Thread thread=new Thread(pullFromDB);
        thread.start();

        if(resultSet!=null) {
            ((EditText) findViewById(R.id.editText)).setText(resultSet.getString(0), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText2)).setText(resultSet.getString(1), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText3)).setText(resultSet.getString(2), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText4)).setText(resultSet.getString(3), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText7)).setText(resultSet.getString(4), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText8)).setText(resultSet.getString(5), TextView.BufferType.EDITABLE);
        }

        Button buttonSave= (Button) findViewById(R.id.button3);
        buttonSave.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                updateDetails();
            }
        });
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    Runnable UpdateDB=new Runnable() {
        @Override
        public void run() {
            Log.d("detail","update");
            SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY, NAME VARCHAR, EMAIL VARCHAR," +
                    " ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
            db.execSQL("INSERT INTO UserDetails VALUES('"+details.getName()+"','"+details.getEmail()+"','"+details.getAddress()+"','"+details.getPantsSize()+
            "','"+details.getShoeSize()+"','"+details.getTopSize()+"');");
            db.close();
            handler.sendEmptyMessage(0);
        }
    };

    Handler handler3=new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    Runnable pullFromDB=new Runnable() {
        @Override
        public void run() {
            SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
//            db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(NAME VARCHAR PRIMARY KEY,EMAIL VARCHAR," +
//                    " ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
            resultSet = db.rawQuery("Select * from UserDetails;", null);
            db.close();
            handler3.sendEmptyMessage(0);
        }
    };

    public void updateDetails(){
        details=new UserDetails();
        details.setName(((EditText) findViewById(R.id.editText)).getText().toString());
        details.setEmail(((EditText) findViewById(R.id.editText2)).getText().toString());
        details.setAddress(((EditText) findViewById(R.id.editText3)).getText().toString());
        details.setPantsSize(((EditText) findViewById(R.id.editText4)).getText().toString());
        details.setShoeSize(((EditText) findViewById(R.id.editText7)).getText().toString());
        details.setTopSize(((EditText) findViewById(R.id.editText8)).getText().toString());

        Thread t=new Thread(UpdateDB);
        t.start();
    }
}
