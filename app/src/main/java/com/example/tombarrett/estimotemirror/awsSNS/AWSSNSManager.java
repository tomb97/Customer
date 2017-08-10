package com.example.tombarrett.estimotemirror.awsSNS;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.PublishRequest;

/**
 * Created by tombarrett on 01/08/2017.
 * Class used to send messages via AWS SNS.
 *
 * Set up on AWS as follows:
 * 1. Log into the AWS console
 * 2. Select the SNS service
 * 3. Create a topic
 * 4. Subscribe to that top.
 * 
 * Email is easiest and free of charge.
 * Set up BasicAWSCredientails as snsClient creation below:
 * 1. Log in to the AWS console.
 * 2. Click on Account name and select 'My Security Credentials'
 * 3. Select Access Keys
 * 4. Click 'Create New Access Key'
 * 5. CSV file will be downloaded and paste contents into line below....shorter one is first!
 * The 3 methods below are seperated to allow for different future ARN's
 * and also any custom config needed for the different message types.
 * The AWS connection is running on a separate thread.
 */

public class AWSSNSManager {

    private Runnable publish(final String message, final String subject, final String arn) {

        Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials("", ""));
                    snsClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
                    Log.d("sns", "client");
                    PublishRequest publishRequest = new PublishRequest();
                    publishRequest.setMessage(message);
                    publishRequest.setSubject(subject);
                    publishRequest.withTargetArn(arn);
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

    public AWSSNSManager(){}

    public void publishMessageToShopAssistant(String message, String subject){
        String TargetARN="arn:aws:sns:eu-west-1:151762801558:Estimote";
        Runnable myRunnable = publish(message,subject,TargetARN);
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    public void publishMessageToCustomer(String message, String subject){
        String TargetARN="arn:aws:sns:eu-west-1:151762801558:Receipt";
        Runnable myRunnable = publish(message,subject,TargetARN);
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    public void publishMessageForNearExpiredToken(String message, String subject){
        String TargetARN="arn:aws:sns:eu-west-1:151762801558:Receipt";
        Runnable myRunnable = publish(message,subject,TargetARN);
        Thread thread = new Thread(myRunnable);
        thread.start();
    }



}
