package com.example.tombarrett.estimotemirror;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

/**
 * Created by tombarrett on 28/07/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: put your App ID and App Token here
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "toms-location-odr", "c76685df7fdccaec45b617c18cf50bdc");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
//        EstimoteSDK.enableDebugLogging(true);
    }
}
