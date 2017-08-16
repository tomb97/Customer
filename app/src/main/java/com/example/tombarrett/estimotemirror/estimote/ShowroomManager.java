package com.example.tombarrett.estimotemirror.estimote;

import android.content.Context;
import android.util.Log;

import com.estimote.coresdk.recognition.packets.Nearable;
import com.estimote.coresdk.service.BeaconManager;
import com.example.tombarrett.estimotemirror.shop.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tombarrett on 28/07/2017.
 * The ShowroomManager class handles the discovery of nearables/stickers.
 * When it finds stickers it waits for them to move and then invokes the onProductPickup method.
 */

public class ShowroomManager {

    private Listener listener;
    private List<String> stickers;
    private List<String> tempStickers;
    private BeaconManager beaconManager;
    private Map<NearableID, Boolean> nearablesMotionStatus = new HashMap<>();

    public ShowroomManager(Context context, final Map<NearableID, Product> products) {
        stickers=new ArrayList<String>();
        beaconManager = new BeaconManager(context);
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> list) {
                Log.d("test","found");
                for (final Nearable nearable : list) {
                    Log.d("near",nearable.identifier.toString()+nearable.type+nearable.batteryLevel);
                    if(!(stickers.contains(nearable.identifier)))
                        stickers.add(nearable.identifier);
                    NearableID nearableID = new NearableID(nearable.identifier);
                    if (!products.keySet().contains(nearableID)) { continue; }

                    boolean previousStatus = nearablesMotionStatus.containsKey(nearableID) && nearablesMotionStatus.get(nearableID);
                    if (previousStatus != nearable.isMoving) {
                        Product product = products.get(nearableID);
                        if (nearable.isMoving) {
                            listener.onProductPickup(product);
                        } else {
                            listener.onProductPutdown(product);
                        }
                        nearablesMotionStatus.put(nearableID, nearable.isMoving);
                    }
                }
                for(String x: tempStickers){
                    if(!(stickers.contains(x)))
                        Log.d("stick","lost"+x);
                    else
                        Log.d("stick","found");
                }
            }
        });
        tempStickers=stickers;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void timer(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                     stickers=new ArrayList<String>();
                                      Log.d("stick","temp");
                                  }
                              }, 0, 5000);
    }

    public interface Listener {
        void onProductPickup(Product product);
        void onProductPutdown(Product product);
    }

    public void startUpdates() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startNearableDiscovery();
            }
        });
    }

    public void stopUpdates() {
        beaconManager.stopNearableDiscovery();
    }

    public void destroy() {
        beaconManager.disconnect();
    }
}

