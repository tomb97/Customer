package com.example.tombarrett.estimotemirror.mirror;

import android.util.Log;

import com.estimote.display.client.MirrorClient;
import com.estimote.sdk.mirror.context.DisplayCallback;
import com.estimote.sdk.mirror.context.MirrorContextManager;
import com.estimote.sdk.mirror.context.Zone;
import com.estimote.sdk.mirror.core.common.exception.MirrorException;
import com.estimote.sdk.mirror.core.connection.Dictionary;

import org.json.JSONObject;

/**
 * Created by tombarrett on 02/08/2017.
 * This class handles display requests to the estimote mirror.
 * A Dictionary is used to store the template to be used and any varibales to be inputted
 * such as distance indicator.
 * The MirrorContextManager measures your current distance from the mirror
 * in real time and the display can update accordingly until you clearDisplayRequests.
 * This class does not specify a specific mirror but you can select them by mirror ID.
 */

public class MirrorManager implements IMirrorManager{

    private String template;
    private MirrorContextManager ctxMgr;

    public MirrorManager(String template, MirrorContextManager ctxMgr){
        this.template=template;
        this.ctxMgr=ctxMgr;
    }

    public void castToMirror(){
        this.ctxMgr.clearDisplayRequests();

        Dictionary dictionary2 = new Dictionary();
        dictionary2.setTemplate(template);
        dictionary2.put("zone","near");

        Dictionary dictionary = new Dictionary();
        dictionary.setTemplate(template);
        dictionary.put("zone","mid");

        this.ctxMgr.display(dictionary, Zone.FAR, new DisplayCallback() {

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

        this.ctxMgr.display(dictionary2, Zone.NEAR, new DisplayCallback() {

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
}
