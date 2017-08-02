package com.example.tombarrett.estimotemirror;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.coresdk.cloud.api.CloudCallback;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.exception.EstimoteCloudException;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
//import com.estimote.display.client.DisplayCallback;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.estimote.display.view.TableView;
import com.estimote.display.view.style.TableViewStyle;
import com.estimote.display.view.data.TableViewData;
import com.estimote.indoorsdk.algorithm.IndoorLocationManager;
import com.estimote.indoorsdk.algorithm.OnPositionUpdateListener;
import com.estimote.indoorsdk.cloud.IndoorCloudManager;
import com.estimote.indoorsdk.cloud.IndoorCloudManagerFactory;
import com.estimote.indoorsdk.cloud.Location;
import com.estimote.indoorsdk.cloud.LocationPosition;
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
        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                redeemCoupon();
            }
        });

        this.ctxMgr = MirrorContextManager.createMirrorContextManager(this);

        Map<NearableID, Product> products = new HashMap<>();
        products.put(new NearableID("1b089cf2ccbf058b"), new Product("Running Shoes",
                "These running shoes are like, the best", "retail",R.drawable.shoe,"$49.99"));
        products.put(new NearableID("22aaab0c27180003"), new Product("Bike",
                "Lovely Bike, much fast", "bike",R.drawable.bike,"$99.99"));

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
                        awssnsManager.publishMessageToShopAssistant((product.getEmailMessageSA(resultSet.getString(1), resultSet.getString(5))), "Attention!");
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
                ((TextView) findViewById(R.id.priceLabel)).setText(product.getPrice());
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
                findViewById(R.id.button).setVisibility(android.view.View.VISIBLE);
                ImageView img= (ImageView) findViewById(R.id.imageView4);
                img.setImageResource(product.getImage());
            }
        });
    }

    public void button2Clicked(){
        Intent i = new Intent(getBaseContext(), Details.class);
        startActivity(i);
    }

    public void redeemCoupon(){
        String name= ((TextView) findViewById(R.id.titleLabel)).getText().toString();
        String price= ((TextView) findViewById(R.id.priceLabel)).getText().toString();
        String message= "Product: "+name+" ,at price of " +price;
        Intent i = new Intent(getBaseContext(), Payment.class);
        i.putExtra("message",message);
        startActivity(i);
    }

    public void mirror(String template){
        MirrorManager mirrorManager=new MirrorManager(template,ctxMgr);
        mirrorManager.castToMirror();
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
