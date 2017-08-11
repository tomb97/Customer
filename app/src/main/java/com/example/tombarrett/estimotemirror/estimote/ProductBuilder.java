package com.example.tombarrett.estimotemirror.estimote;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class ProductBuilder {

    public String name;
    public String summary;
    public String template;
    public int image;
    public String price;
    public boolean wearable;

    public ProductBuilder(String name, String summary, String price){
        this.name=name;
        this.summary=summary;
        this.price=price;
    }

    public ProductBuilder template(String template){
        this.template=template;
        return this;
    }

    public ProductBuilder image(int image){
        this.image=image;
        return this;
    }

    public ProductBuilder wearable(boolean wearable){
        this.wearable=wearable;
        return this;
    }

    public Product build(){
        return new Product(this);
    }
}
