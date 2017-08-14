package com.example.tombarrett.estimotemirror.shop;

/**
 * Created by tombarrett on 14/08/2017.
 */

public class NonWearableProduct extends Product {

    public NonWearableProduct(ProductBuilder builder) {
        super(builder);
    }

    @Override
    public String getEmailMessageSA(String name, String size){
        String emailMessageSA= ("Customer " + name + " is interested in " + getName());
        emailMessageSA+=". This product is in stock!";
        return emailMessageSA;
    }
}
