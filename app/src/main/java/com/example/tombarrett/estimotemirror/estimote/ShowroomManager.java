package com.example.tombarrett.estimotemirror.estimote;

/**
 * Created by tombarrett on 28/07/2017.
 */

import android.content.Context;
import android.util.Log;


import com.estimote.coresdk.recognition.packets.Nearable;
import com.estimote.coresdk.service.BeaconManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowroomManager {

    private Listener listener;
    private static final String TAG = "MainActivity";
    private BeaconManager beaconManager;

    private Map<NearableID, Boolean> nearablesMotionStatus = new HashMap<>();

    public ShowroomManager(Context context, final Map<NearableID, Product> products) {

        beaconManager = new BeaconManager(context);
        Log.d(TAG, "yaw");
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> list) {
                Log.d(TAG, "any");
                for (Nearable nearable : list) {
                    Log.d(TAG, "Near");
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
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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

