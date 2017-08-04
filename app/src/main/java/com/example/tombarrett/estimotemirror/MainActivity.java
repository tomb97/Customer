package com.example.tombarrett.estimotemirror;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.display.client.MirrorClient;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estimote.display.view.View;
import  com.estimote.sdk.mirror.context.MirrorContextManager;
import com.example.tombarrett.estimotemirror.estimote.NearableID;
import com.example.tombarrett.estimotemirror.estimote.Product;
import com.example.tombarrett.estimotemirror.estimote.ShowroomManager;


import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tombarrett.estimotemirror.R.layout.activity_main;


public class MainActivity extends AppCompatActivity {
    private MirrorContextManager ctxMgr;
    private static final String TAG = "MainActivity";
    private MirrorClient mirrorClient;
    private ShowroomManager showroomManager;
    private Map<NearableID, Product> products;
    private Context context=this;
    private Product tempProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        setTitle("Product Display");
        Toast.makeText(this, "Please turn on WiFi.",
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        initiateListeners();
    }

    public void initiateListeners(){
        EstimoteSDK.initialize(getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");

        Toast.makeText(this, "Please maintain user details by clicking on the User Details button.\nOnce this is done, pick up a product for more information.",
                Toast.LENGTH_LONG).show();

        setButtons();

        this.ctxMgr = MirrorContextManager.createMirrorContextManager(this);

        products = new HashMap<>();
        populateProducts();

        showroomManager = new ShowroomManager(this, products);
        showroomManager.setListener(new ShowroomManager.Listener() {
            private Product product;
            @Override
            public void onProductPickup(final Product product) {
                if(product!=tempProduct) {
                    final android.view.View currentFocus = getWindow().getCurrentFocus();
                    ImageView image = new ImageView(context);
                    image.setImageResource(product.getImage());
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Do you want to view this product?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            destroyMirror();
                            setContentView(activity_main);
                            pickup(product);
                            tempProduct = product;
                        }
                    });
                    setContentView(image);

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setContentView(activity_main);
                            setButtons();
                            if (tempProduct != null)
                                pickup(tempProduct);
                        }
                    });
                    alert.create().show();
                }
            }
            @Override
            public void onProductPutdown(Product product) {
                tempProduct=product;
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
                findViewById(R.id.radioButton).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton5).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton6).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.radioButton7).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.profile_image).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.button4).setVisibility(android.view.View.VISIBLE);
                findViewById(R.id.textView12).setVisibility(android.view.View.VISIBLE);
                ImageView img= (ImageView) findViewById(R.id.profile_image);
                img.setImageResource(product.getImage());
                setButtons();
            }

            public void pickup(Product product){
                this.product=product;
                try {
                    SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR," +
                            " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
                    Cursor resultSet = db.rawQuery("SELECT * FROM UserDetails WHERE ID='1';", null);
                    if (resultSet != null && resultSet.moveToFirst()) {
                        AWSSNSManager awssnsManager = new AWSSNSManager();
                        awssnsManager.publishMessageToShopAssistant((product.getEmailMessageSA(resultSet.getString(1), resultSet.getString(5))), "Customer is interested in a product!");
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
        });
    }

    public void populateProducts(){
        products.put(new NearableID("1b089cf2ccbf058b"), new Product("Running Shoes",
                "Experience smooth support on your next run.", "retail",R.drawable.shoe,"€99",true));
        products.put(new NearableID("22aaab0c27180003"), new Product("Bike",
                "Lovely Bike, much fast", "bike",R.drawable.bike,"€150",false));
        products.put(new NearableID("ee2b8cc919b453ab"), new Product("bag",
                "These running shoes are like, the best", "retail",R.drawable.shoe,"$49.99",false));
    }

    public void setButtons(){
        Button button= (Button) findViewById(R.id.button4);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                redeemCoupon();
            }
        });
        Button tokenbutton= (Button) findViewById(R.id.button);
        tokenbutton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                token();
            }
        });

        Button buttonDetails = (Button) findViewById(R.id.button2);
        buttonDetails.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(android.view.View view){
                Log.d("test","click");
                button2Clicked();
            }
        });
    }

    public void token(){
        Intent i = new Intent(getBaseContext(), Tokens.class);
        startActivity(i);
    }

    public void button2Clicked(){
        Intent i = new Intent(getBaseContext(), Details.class);
        startActivity(i);
    }

    public void destroyMirror(){
        ctxMgr.destroy();
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(this);
    }

    public void redeemCoupon(){
        destroyMirror();
        String name= ((TextView) findViewById(R.id.titleLabel)).getText().toString();
        String price= ((TextView) findViewById(R.id.priceLabel)).getText().toString();
        String message= "Product: "+name+" ,at price of " +price;
        Intent i = new Intent(getBaseContext(), Payment.class);
        i.putExtra("message",message);
        startActivity(i);
    }

    public void mirror(String template){
        destroyMirror();
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(this);
        MirrorManager mirrorManager=new MirrorManager(template,ctxMgr);
        mirrorManager.castToMirror();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // initiateListeners();
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
        ctxMgr.destroy();
    }

}
