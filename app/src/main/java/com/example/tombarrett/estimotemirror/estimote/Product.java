package com.example.tombarrett.estimotemirror.estimote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by tombarrett on 28/07/2017.
 */

public class Product {

    private String name;
    private String summary;
    private String template;
    private int image;
    private String emailMessageSA;
    private String emailMessageCustomer;
    private String price;

    public Product(String name, String summary, String template, int image, String price) {
        this.name = name;
        this.summary = summary;
        this.template=template;
        this.image=image;
        this.price=price;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getTemplate() { return template; }

    public int getImage(){ return image; }

    public String getPrice() { return price; }

    public String getEmailMessageSA(String name, String size){
        emailMessageSA= ("Customer " + name + " is interested in " + getName() + " in size " + size);
        return emailMessageSA;
    }

    public String getEmailMessageCustomer(String name){
        emailMessageCustomer= (name+", This is your digital receipt for product: "+getName()+"\nBilling Amount: "+getSummary());
        return emailMessageCustomer;
    }
}
