package com.example.tombarrett.estimotemirror;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
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
    private MirrorClient mirrorClient;
    private MirrorContextManager ctxMgr;
    private static final String TAG = "MainActivity";

    private ShowroomManager showroomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstimoteSDK.initialize(getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");


        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                button1Clicked();
            }
        });

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
        // TODO: replace with identifiers of your own nearables
        products.put(new NearableID("1b089cf2ccbf058b"), new Product("Running Shoes",
                "$49.99"));
        products.put(new NearableID("abcdef0000000002"), new Product("Nyan Bicycle 3x14",
                "Rush down the local streets with this amazing bike, leaving a trail of rainbow behind you, to the awe of everyone around."));
        showroomManager = new ShowroomManager(this, products);
        showroomManager.setListener(new ShowroomManager.Listener() {
            @Override
            public void onProductPickup(Product product) {
                ((EditText) findViewById(R.id.titleLabel)).setText(product.getName());
                ((EditText) findViewById(R.id.descriptionLabel)).setText(product.getSummary());
                findViewById(R.id.descriptionLabel).setVisibility(android.view.View.VISIBLE);
                button2Clicked();
            }
            @Override
            public void onProductPutdown(Product product) {
                ((EditText) findViewById(R.id.titleLabel)).setText("Pick up an object to learn more about it");
                findViewById(R.id.descriptionLabel).setVisibility(android.view.View.INVISIBLE);
            }
        });

    }


    public void button1Clicked(){
        Intent i = new Intent(getBaseContext(), Details.class);
        startActivity(i);
    }
    //anywhere: works
    //immediate: works right next to
    //near: works right next to
    //far: works far and also near and right next to
    public void button2Clicked(){
        Dictionary dictionary2 = new Dictionary();
        dictionary2.setTemplate("retail");
        Log.d("Mirror","plz");
        this.ctxMgr.display(dictionary2, Zone.WHEREVER_YOU_ARE, new DisplayCallback() {

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
