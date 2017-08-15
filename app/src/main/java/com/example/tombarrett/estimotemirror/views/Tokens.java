package com.example.tombarrett.estimotemirror.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.presenter.TokenPresenter;
import com.example.tombarrett.estimotemirror.token.Token;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Tokens extends AppCompatActivity {

    private List<Token> tokens;
    private Token tempToken;
    private TokenPresenter tokenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokens);
        tokenPresenter=new TokenPresenter();
        tokens=tokenPresenter.tokens();
        setCustomListView();
    }

    public void setCustomListView(){
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (Token token : tokens) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", token.getName());
            hm.put("listview_discription", token.getDescription());
            hm.put("listview_image", Integer.toString(token.getImage()));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list, from, to);
        ListView androidListView = (ListView) findViewById(R.id.listv);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toastMessage((tokens.get(i).getDescription()+"\nExpiry Date: "+tokens.get(i).getDate()+"\nPoints: "+tokens.get(i).getPoints()),tokens.get(i));
            }
        });
    }

    public void toastMessage(String message, Token t){
        this.tempToken=t;
        Date date=tempToken.getExpDate();
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        if(tokenPresenter.nearExpired(date,tempToken))
          toast.getView().setBackgroundColor(Color.RED);
        toast.show();
    }
}
