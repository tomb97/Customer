package com.example.tombarrett.estimotemirror.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokens);
        tokens();
        setCustomListView();
    }

    public void tokens(){
        try {
            tokens = new ArrayList<Token>();
            Token nike = new Token("Nike", "31/08/2017", R.drawable.nike, "Token for nike shoes", 10);
            Token chanel = new Token("Chanel", "31/08/2017", R.drawable.channel, "Token for Chanel perfume", 25);
            Token trek = new Token("Trek", "31/08/2017", R.drawable.trek, "Token for Trek Bike", 35);
            Token coke = new Token("Coke", "31/08/2017", R.drawable.coke, "Token for a can of coke", 2);
            Token starbucks = new Token("Starbucks", "31/08/2017", R.drawable.starbuck, "Token for any Tall Coffee", 5);
            Token rayban = new Token("Raybans", "31/08/2017", R.drawable.rayban, "Token for our new range of RayBans", 50);
            Token canon = new Token("Canon", "31/08/2017", R.drawable.canon, "Token for Canon lense", 30);
            Token samsung = new Token("Samsung", "13/08/2017", R.drawable.samsung, "Token for the Galaxy S8", 70);
            tokens.add(nike);
            tokens.add(chanel);
            tokens.add(trek);
            tokens.add(coke);
            tokens.add(starbucks);
            tokens.add(rayban);
            tokens.add(canon);
            tokens.add(samsung);
        } catch(ParseException parse){
            tokens = new ArrayList<Token>();
        }
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
                toastMessage((tokens.get(i).getDescription()+"\nExpiry Date: "+tokens.get(i).getDate()),tokens.get(i).getExpDate());
            }
        });
    }

    public void toastMessage(String message, Date date){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        if(nearExpired(date))
          toast.getView().setBackgroundColor(Color.RED);
        toast.show();
    }

    public boolean nearExpired(Date expDate){
        boolean nearExp=true;

        Date currentDate = new Date();
        long week = 7l * 24 * 60 * 60 * 1000;
        Date currentAndWeek=new Date(currentDate.getTime()+week);
        if(expDate.before(currentAndWeek)){
            nearExp=true;
            tokenNearEXPSNS();
        }
        else {
            nearExp = false;
        }

        return nearExp;
    }

    public void tokenNearEXPSNS(){
//        AWSSNSManager awssnsManager = new AWSSNSManager();
//        awssnsManager.publishMessageToCustomer(("Token: "+),"Token near expiry date!");
    }


}
