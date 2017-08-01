package com.example.tombarrett.estimotemirror;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

/**
 * Created by tombarrett on 01/08/2017.
 */

public class AWSSNSManager {

    private Runnable publish(final String message, final String subject) {

        Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials("AKIAJT4WSAC5YXUP2IWA", "kKVuvJQd1Mh1C81tz0xDGqJC1TB9SHxy3LTiroM7"));
                    snsClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
                    Log.d("sns", "client");
                    PublishRequest publishRequest = new PublishRequest();
                    publishRequest.setMessage(message);
                    publishRequest.setSubject(subject);
                    publishRequest.withTargetArn("arn:aws:sns:eu-west-1:151762801558:Estimote");
                    Log.d("sns", "request");
                    snsClient.publish(publishRequest);
                    Log.d("sns", "requested");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return aRunnable;
    }

    AWSSNSManager(){}

    public void publishMessage(String message, String subject){
        Runnable myRunnable = publish(message,subject);
        Thread thread = new Thread(myRunnable);
        thread.start();

    }



}
