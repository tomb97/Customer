package com.example.tombarrett.estimotemirror;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
//import com.estimote.display.client.DisplayCallback;
import com.estimote.display.client.MirrorClient;
import com.estimote.display.view.View;
import com.estimote.display.view.data.PosterViewData;
import com.estimote.display.view.style.PosterViewStyle;
import com.estimote.display.view.data.PosterViewData;
import com.estimote.display.view.style.Position;
import com.estimote.display.view.PosterView;
import com.estimote.display.view.style.Horizontal;
import com.estimote.display.view.style.Vertical;
import com.estimote.display.proximity.MirrorZone;
import com.estimote.display.view.operation.ViewOperation;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.estimote.display.view.TableView;
import com.estimote.display.view.style.TableViewStyle;
import com.estimote.display.view.data.TableViewData;
import  com.estimote.sdk.mirror.context.MirrorContextManager;
import  com.estimote.sdk.mirror.core.connection.Dictionary;
import  com.estimote.sdk.mirror.core.common.exception.MirrorException;
import  com.estimote.sdk.mirror.context.Zone;
import com.estimote.sdk.mirror.context.DisplayCallback;
import  com.estimote.display.client.DisplayConditionCreator;
import com.example.tombarrett.estimotemirror.estimote.NearableID;
import com.example.tombarrett.estimotemirror.estimote.Product;
import com.example.tombarrett.estimotemirror.estimote.ShowroomManager;


import android.widget.Button;
import android.content.Intent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private MirrorContextManager ctxMgr;
    private static final String TAG = "MainActivity";
    private MirrorClient mirrorClient;
    private ShowroomManager showroomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstimoteSDK.initialize(getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");

        Button button2= (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                button2Clicked();
            }
        });
//        Toast.makeText(getApplicationContext(), "done!", Toast.LENGTH_LONG).show();
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(this);

        Map<NearableID, Product> products = new HashMap<>();
        products.put(new NearableID("1b089cf2ccbf058b"), new Product("Running Shoes",
                "$49.99", "retail"));
        products.put(new NearableID("22aaab0c27180003"), new Product("Bike",
                "$99.99", "bike"));

        showroomManager = new ShowroomManager(this, products);
        showroomManager.setListener(new ShowroomManager.Listener() {
            private Product product;
            @Override
            public void onProductPickup(Product product) {
                this.product=product;
                try {
                    SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR," +
                            " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
                    Cursor resultSet = db.rawQuery("SELECT * FROM UserDetails WHERE ID='1';", null);
                    if (resultSet != null && resultSet.moveToFirst()) {
                        AWSSNSManager awssnsManager = new AWSSNSManager();
                        awssnsManager.publishMessage(("Customer " + resultSet.getString(1) + " is interested in " + product.getName() + " in size " + resultSet.getString(5)), "Customer is interested in a product!");
                    }
                    else{
                        Log.d("SNS","crash");
                    }
                    updateViews();
                }
                catch (Exception ex){
                    Log.d("SNS","dbcrash");
                }
                mirror(product.getTemplate());
            }
            @Override
            public void onProductPutdown(Product product) {
                //clear text?
            }

            public void updateViews(){
                ((TextView) findViewById(R.id.titleLabel)).setText(product.getName());
                ((TextView) findViewById(R.id.descriptionLabel)).setText(product.getSummary());
                ((TextView) findViewById(R.id.priceLabel)).setText(product.getSummary());
                findViewById(R.id.descriptionLabel).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.titleLabel).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.priceLabel).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.sizeLabel).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.colorLabel).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton10).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton11).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton12).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton13).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton14).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton15).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.imageView4).setVisibility(android.view.View.VISIBLE);
            }
        });

    }

    public void button2Clicked(){
        Intent i = new Intent(getBaseContext(), Details.class);
        startActivity(i);
    }

    public void mirror(String template){
        this.ctxMgr.clearDisplayRequests();

        Dictionary dictionary2 = new Dictionary();
        dictionary2.setTemplate(template);
        dictionary2.put("zone","near");

        Dictionary dictionary = new Dictionary();
        dictionary.setTemplate(template);
        dictionary.put("zone","far");
        Log.d("Mirror","plz");

        this.ctxMgr.display(dictionary, Zone.FAR, new DisplayCallback() {

            @Override
            public void onDataDisplayed() {
                Log.d("Mirror","Display");
            }

            @Override
            public void onFinish() {
                Log.d("Mirror","finish");
            }

            @Override
            public void onFailure(MirrorException exception) {
                Log.d("Mirror","fail");
            }

            @Override
            public void onData(JSONObject data){
                Log.d("Mirror","data");
            }
        });

        this.ctxMgr.display(dictionary2, Zone.NEAR, new DisplayCallback() {

            @Override
            public void onDataDisplayed() {
                Log.d("Mirror","Display");
            }

            @Override
            public void onFinish() {
                Log.d("Mirror","finish");
            }

            @Override
            public void onFailure(MirrorException exception) {
                Log.d("Mirror","fail");
            }

            @Override
            public void onData(JSONObject data){
                Log.d("Mirror","data");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ShowroomManager updates");
            showroomManager.startUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ShowroomManager updates");
        showroomManager.stopUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showroomManager.destroy();
    }

}
