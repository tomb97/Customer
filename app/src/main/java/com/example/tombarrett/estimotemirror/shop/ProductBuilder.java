package com.example.tombarrett.estimotemirror.shop;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class ProductBuilder {

    public String name;
    public String summary;
    public String template;
    public int image;
    public String price;
    public boolean wearable=false;

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

    public ProductBuilder wearable(){
        this.wearable=true;
        return this;
    }

    public Product build(){
        if(wearable) {
            return new WearableProduct(this);
        }
        else
            return new NonWearableProduct(this);
    }
}
