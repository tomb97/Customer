package com.example.tombarrett.estimotemirror;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tokens extends AppCompatActivity {

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] listviewImage = new int[]{
            R.drawable.nike, R.drawable.channel, R.drawable.trek, R.drawable.coke,
            R.drawable.starbuck, R.drawable.rayban, R.drawable.canon, R.drawable.samsung,
    };

    String[] listviewShortDescription = new String[]{
            "Token for nike shoes: 10 points!", "Token for Chanel perfume: 25 points", "Token for Trek Bike: 35 points", "Token for a can of coke: 2 points",
            "Token for any Tall Coffee: 5 points", "Token for our new range of RayBans: 50 points", "Token for Canon lense: 30 points", "Token for the Galaxy S8: 70 points",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
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
                String message= listviewShortDescription[i];
                toastMessage(message);
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }


}
