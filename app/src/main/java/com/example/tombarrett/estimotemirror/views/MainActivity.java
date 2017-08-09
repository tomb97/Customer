package com.example.tombarrett.estimotemirror.views;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.estimote.sdk.mirror.context.MirrorContextManager;
import com.example.tombarrett.estimotemirror.mirror.MirrorManager;
import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.estimote.NearableID;
import com.example.tombarrett.estimotemirror.estimote.Product;
import com.example.tombarrett.estimotemirror.estimote.ShowroomManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import static com.example.tombarrett.estimotemirror.R.layout.activity_main;


public class MainActivity extends AppCompatActivity {
    private MirrorContextManager ctxMgr;
    private static final String TAG = "MainActivity";
    private ShowroomManager showroomManager;
    private Map<NearableID, Product> products;
    private Context context=this;
    private Product tempProduct;
    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private Product product;
    private String colour;
    private String size;
    private DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        setTitle("Product Display");
        Toast.makeText(this, "Please turn on WiFi.",
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        initiateListeners();
        dbhelper=new DatabaseHelper(this);
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
                    alertDialog(product);
                }
            }
            @Override
            public void onProductPutdown(Product product) {
                // tempProduct=product;
            }
        });
    }

    public void selectRadioButton() {
        if(product.getWearable()) {
            radioButton = (RadioButton) findViewById(R.id.radioButton6);
            radioButton2 = (RadioButton) findViewById(R.id.radioButton7);
            radioButton3 = (RadioButton) findViewById(R.id.radioButton10);

            dbhelper.connectToDB();
            Cursor resultSet= dbhelper.getShoe();
            if (resultSet != null && resultSet.moveToFirst()) {
                String size=resultSet.getString(0);
                if (radioButton.getText().toString().equals(size))
                    radioButton.setChecked(true);
                else if (radioButton2.getText().toString().equals(size))
                    radioButton2.setChecked(true);
                else if (radioButton3.getText().toString().equals(size))
                    radioButton3.setChecked(true);
            }
            dbhelper.disconnectToDB();

            radioButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    radio1Clicked();
                }
            });
            radioButton2.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    radio2Clicked();
                }
            });
            radioButton3.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    radio3Clicked();
                }
            });
        }
        radioButton4 = (RadioButton) findViewById(R.id.radioButton);
        radioButton5 = (RadioButton) findViewById(R.id.radioButton11);
        radioButton6 = (RadioButton) findViewById(R.id.radioButton5);

        radioButton4.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                radio4Clicked();
            }
        });
        radioButton5.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                radio5Clicked();
            }
        });
        radioButton6.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                radio6Clicked();
            }
        });
    }

    public void radio1Clicked(){
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
    }

    public void radio2Clicked(){
        radioButton.setChecked(false);
        radioButton3.setChecked(false);
    }

    public void radio3Clicked(){
        radioButton.setChecked(false);
        radioButton2.setChecked(false);
    }

    public void radio4Clicked(){
        radioButton5.setChecked(false);
        radioButton6.setChecked(false);
    }

    public void radio5Clicked(){
        radioButton4.setChecked(false);
        radioButton6.setChecked(false);
    }

    public void radio6Clicked(){
        radioButton4.setChecked(false);
        radioButton5.setChecked(false);
    }

    public boolean checkRadioButtons(){
        boolean selected;

        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton6);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton7);
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioButton10);
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton radioButton6 = (RadioButton) findViewById(R.id.radioButton11);
        if((radioButton.isChecked() || radioButton2.isChecked() || radioButton3.isChecked()) && (radioButton4.isChecked() || radioButton5.isChecked() || radioButton6.isChecked()) && product.getWearable())
            selected=true;
        else if(radioButton4.isChecked() || radioButton5.isChecked() || radioButton6.isChecked())
            selected=true;
        else
            selected=false;
        if(radioButton.isChecked())
            size=radioButton.getText().toString();
        if(radioButton2.isChecked())
            size=radioButton2.getText().toString();
        if(radioButton3.isChecked())
            size=radioButton3.getText().toString();
        if(radioButton4.isChecked())
            colour=radioButton4.getText().toString();
        if(radioButton5.isChecked())
            colour=radioButton5.getText().toString();
        if(radioButton6.isChecked())
            colour=radioButton6.getText().toString();
        return selected;
    }


    public void populateProducts(){
        products.put(new NearableID("22aaab0c27180003"), new Product("Bike",
                "Lovely Bike, much fast", "bike",R.drawable.bike,"€150",false));
        products.put(new NearableID("1e35554b0afec7ab"), new Product("Running Shoes",
                "These running shoes are like, the best", "retail",R.drawable.shoe,"€99",true));
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

    public void updateViews(Product product){
        ((TextView) findViewById(R.id.titleLabel)).setText(product.getName());
        ((TextView) findViewById(R.id.descriptionLabel)).setText(product.getSummary());
        ((TextView) findViewById(R.id.priceLabel)).setText(product.getPrice());
        findViewById(R.id.descriptionLabel).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.titleLabel).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.priceLabel).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.colorLabel).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.radioButton11).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.radioButton).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.radioButton5).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.profile_image).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.button4).setVisibility(android.view.View.VISIBLE);
        findViewById(R.id.textView12).setVisibility(android.view.View.VISIBLE);
        ImageView img= (ImageView) findViewById(R.id.profile_image);
        img.setImageResource(product.getImage());

        if(product.getWearable()) {
            findViewById(R.id.radioButton6).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.radioButton7).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.radioButton10).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.sizeLabel).setVisibility(android.view.View.VISIBLE);
        }

        selectRadioButton();
        setButtons();
    }

    public void alertDialog(final Product product){
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

    public void pickup(Product product){
        this.product=product;
        dbhelper.connectToDB();
        Cursor resultSet=dbhelper.getDetails();
        if (resultSet != null && resultSet.moveToFirst()) {
            AWSSNSManager awssnsManager = new AWSSNSManager();
            awssnsManager.publishMessageToShopAssistant((product.getEmailMessageSA(resultSet.getString(1), resultSet.getString(5))), "Customer is interested in a product!");
        }
        else{
            Log.d("SNS","crash");
        }
        dbhelper.disconnectToDB();
        updateViews(product);
        mirror(product.getTemplate());
    }

    public void redeemCoupon(){
        if(checkRadioButtons()) {
            destroyMirror();
            String name = ((TextView) findViewById(R.id.titleLabel)).getText().toString();
            String price = ((TextView) findViewById(R.id.priceLabel)).getText().toString();
            String message = "Product: " + name + " \nPrice: " + price+"\nSize: "+size+"\nColour: "+colour;
            Intent i = new Intent(getBaseContext(), Payment.class);
            i.putExtra("message", message);
            startActivity(i);
        }
        else
            Toast.makeText(this, "Please select a colour and size",
                    Toast.LENGTH_LONG).show();
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