package com.example.tombarrett.estimotemirror.estimote;

import android.content.Context;
import android.util.Log;

import com.estimote.coresdk.recognition.packets.Nearable;
import com.estimote.coresdk.service.BeaconManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tombarrett on 28/07/2017.
 * The ShowroomManager class handles the discovery of nearables/stickers.
 * When it finds stickers it waits for them to move and then invokes the onProductPickup method.
 */

public class ShowroomManager {

    private Listener listener;

    private BeaconManager beaconManager;

    private Map<NearableID, Boolean> nearablesMotionStatus = new HashMap<>();

    public ShowroomManager(Context context, final Map<NearableID, Product> products) {
        beaconManager = new BeaconManager(context);
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> list) {
                Log.d("test","found");
                for (Nearable nearable : list) {
                    Log.d("near",nearable.identifier.toString());
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

