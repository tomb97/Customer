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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Details extends AppCompatActivity {

    private UserDetails details;
    private Cursor resultSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SQLiteDatabase db=openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR," +
                " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
        Cursor resultSet= db.rawQuery("SELECT * FROM UserDetails WHERE ID='1';",null);

        if(resultSet!=null && resultSet.moveToFirst()){
            ((EditText) findViewById(R.id.editText)).setText((resultSet.getString(1)), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText2)).setText(resultSet.getString(2), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText3)).setText(resultSet.getString(3), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText4)).setText(resultSet.getString(4), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText7)).setText(resultSet.getString(5), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText8)).setText(resultSet.getString(6), TextView.BufferType.EDITABLE);
            db.close();
            details=new UserDetails();
            details.setName(((EditText) findViewById(R.id.editText)).getText().toString());
            details.setEmail(((EditText) findViewById(R.id.editText2)).getText().toString());
            details.setAddress(((EditText) findViewById(R.id.editText3)).getText().toString());
            details.setPantsSize(((EditText) findViewById(R.id.editText4)).getText().toString());
            details.setShoeSize(((EditText) findViewById(R.id.editText7)).getText().toString());
            details.setTopSize(((EditText) findViewById(R.id.editText8)).getText().toString());
        }
        else{
            Log.d("DB","Empty");
            db.execSQL("INSERT INTO UserDetails VALUES('1',' ',' ',' ',' ',' ',' ');");
            db.close();
        }
        Button buttonSave= (Button) findViewById(R.id.button3);
        buttonSave.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                updateDetails();
            }
        });
    }

    public void updateDetails(){
        details=new UserDetails();
        details.setName(((EditText) findViewById(R.id.editText)).getText().toString());
        details.setEmail(((EditText) findViewById(R.id.editText2)).getText().toString());
        details.setAddress(((EditText) findViewById(R.id.editText3)).getText().toString());
        details.setPantsSize(((EditText) findViewById(R.id.editText4)).getText().toString());
        details.setShoeSize(((EditText) findViewById(R.id.editText7)).getText().toString());
        details.setTopSize(((EditText) findViewById(R.id.editText8)).getText().toString());

        SQLiteDatabase db=openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR,DAY VARCHAR," +
                " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
        db.execSQL("UPDATE UserDetails SET NAME='"+details.getName()+"',EMAIL='"+details.getEmail()+"',ADDRESS='"+details.getAddress()+"',PANT='"
                +details.getPantsSize()+"',SHOE='"+details.getShoeSize()+"',TOP='"+details.getTopSize()+"' WHERE ID='1';");
        db.close();

    }
}
