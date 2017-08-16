package com.example.tombarrett.estimotemirror.presenter;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.sdk.mirror.context.MirrorContextManager;
import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.awsSNS.IAWSSNSManager;
import com.example.tombarrett.estimotemirror.database.DatabaseHelper;
import com.example.tombarrett.estimotemirror.database.IDatabaseHelper;
import com.example.tombarrett.estimotemirror.estimote.NearableID;
import com.example.tombarrett.estimotemirror.mirror.IMirrorManager;
import com.example.tombarrett.estimotemirror.mirror.MirrorManager;
import com.example.tombarrett.estimotemirror.shop.Product;
import com.example.tombarrett.estimotemirror.shop.ProductBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class MainActivityPresenter {

    private IDatabaseHelper dbHelper;
    private Context context;
    private Cursor resultSet;
    private MirrorContextManager ctxMgr;

    public MainActivityPresenter(Context context){
        this.context=context;
        dbHelper=new DatabaseHelper(context);
    }

    public void initialiseEstimote(){
        EstimoteSDK.initialize(context.getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");
    }

    public void connectToDB(){
        dbHelper.connectToDB();
    }

    public Cursor getShoe(){
        return dbHelper.getShoe();
    }

    public Cursor getDetails(){
        return dbHelper.getDetails();
    }

    public void disconnectToDB(){
        dbHelper.disconnectToDB();
    }

    public void sendEmailtoSA(Product product, Cursor resultSet){
        IAWSSNSManager awssnsManager = new AWSSNSManager();
        awssnsManager.publishMessageToShopAssistant((product.getEmailMessageSA(resultSet.getString(1), resultSet.getString(5))), "Customer is interested in a product!");
    }

    public void initialiseMirror(){
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(context);
    }

    public void pickup(Product product){
        connectToDB();
        resultSet=getDetails();
        if (resultSet != null && resultSet.moveToFirst()) {
            sendEmailtoSA(product,resultSet);
        }
        else{
            Log.d("SNS","crash");
        }
        disconnectToDB();
    }

    public Map<NearableID, Product> products(){
        Map<NearableID, Product> products= new HashMap<NearableID, Product>();
        products.put(new NearableID("2d088f71be8bc22f"), new ProductBuilder("Bike",
                "Lovely Bike, much fast","€150")
                .image(R.drawable.bike)
                .template("bike")
                .build());
        products.put(new NearableID("1e35554b0afec7ab"), new ProductBuilder("Running Shoes",
                "These running shoes are like, the best","€99")
                .image(R.drawable.shoe2)
                .template("retail")
                .wearable()
                .build());
        return products;
    }

    public void Mirror(String template){
        destroyMirror();
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(context);
        IMirrorManager mirrorManager=new MirrorManager(template,ctxMgr);
        mirrorManager.castToMirror();
    }

    public void destroyMirror(){
        ctxMgr.destroy();
        this.ctxMgr = MirrorContextManager.createMirrorContextManager(context);
    }

    public void destroy(){
        ctxMgr.destroy();
    }
}
