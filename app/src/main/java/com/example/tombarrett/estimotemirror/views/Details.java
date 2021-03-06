package com.example.tombarrett.estimotemirror.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.presenter.UserDetailsPresenter;
import com.example.tombarrett.estimotemirror.userDetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    private UserDetails details;
    private Cursor resultSet;
    private Spinner sItems;
    private Spinner sItems2;
    private Spinner sItems3;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private ArrayAdapter<String> adapter3;
    private UserDetailsPresenter userDetailsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        userDetailsPresenter=new UserDetailsPresenter(this);
        userDetailsPresenter.connectToDB();
        Cursor resultSet=userDetailsPresenter.getDetails();

        populateSpinners();

        if(resultSet!=null && resultSet.moveToFirst()){
            ((EditText) findViewById(R.id.editText)).setText((resultSet.getString(1)), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText2)).setText(resultSet.getString(2), TextView.BufferType.EDITABLE);
            ((EditText) findViewById(R.id.editText3)).setText(resultSet.getString(3), TextView.BufferType.EDITABLE);
            sItems.setSelection(adapter.getPosition(resultSet.getString(4)));
            sItems2.setSelection(adapter2.getPosition(resultSet.getString(5)));
            sItems3.setSelection(adapter3.getPosition(resultSet.getString(6)));
            userDetailsPresenter.disconnectToDB();
            details=new UserDetails();
            details.setName(((EditText) findViewById(R.id.editText)).getText().toString());
            details.setEmail(((EditText) findViewById(R.id.editText2)).getText().toString());
            details.setAddress(((EditText) findViewById(R.id.editText3)).getText().toString());
            details.setPantsSize(sItems.getSelectedItem().toString());
            details.setShoeSize(sItems2.getSelectedItem().toString());
            details.setTopSize(sItems3.getSelectedItem().toString());
        }
        else{
            Log.d("DB","Empty");
            userDetailsPresenter.insertToDB();
            userDetailsPresenter.disconnectToDB();
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
        details.setPantsSize(sItems.getSelectedItem().toString());
        details.setShoeSize(sItems2.getSelectedItem().toString());
        details.setTopSize(sItems3.getSelectedItem().toString());
        userDetailsPresenter.connectToDB();
        userDetailsPresenter.updateDetails(details);
        userDetailsPresenter.disconnectToDB();
    }

    public void populateSpinners(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("30");
        spinnerArray.add("31");
        spinnerArray.add("32");
        spinnerArray.add("33");

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray2.add("6");
        spinnerArray2.add("7");
        spinnerArray2.add("8");

        adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray2);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems2 = (Spinner) findViewById(R.id.spinner2);
        sItems2.setAdapter(adapter2);

        List<String> spinnerArray3 =  new ArrayList<String>();
        spinnerArray3.add("XS");
        spinnerArray3.add("S");
        spinnerArray3.add("M");
        spinnerArray3.add("L");

        adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray3);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems3 = (Spinner) findViewById(R.id.spinner3);
        sItems3.setAdapter(adapter3);
    }
}
