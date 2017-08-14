package com.example.tombarrett.estimotemirror.shop;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by tombarrett on 28/07/2017.
 * Product information is stored here.
 * These products are used in the shop.
 */

public abstract class Product {

    private String name;
    private String summary;
    private String template;
    private int image;
    private String price;

    public Product(ProductBuilder builder){
        this.name = builder.name;
        this.summary = builder.summary;
        this.template=builder.template;
        this.image=builder.image;
        this.price=builder.price;
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
        String emailMessageSA= "";
        return emailMessageSA;
    }
}
