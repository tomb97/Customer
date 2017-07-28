package com.example.tombarrett.estimotemirror;

import com.example.tombarrett.estimotemirror.estimote.NearableID;
import com.example.tombarrett.estimotemirror.estimote.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tombarrett on 28/07/2017.
 */

public class Shop {
    private List<String> sizesAvailable;
    private Map<NearableID, Product> products;
    private List<String> typesAvailable;

    Shop(){
        sizesAvailable= new ArrayList<String>();
        sizesAvailable.add("4");
        sizesAvailable.add("5");
        sizesAvailable.add("6");

        typesAvailable= new ArrayList<String>();
        sizesAvailable.add("red");
        sizesAvailable.add("white");
        sizesAvailable.add("blue");

        products= new HashMap<NearableID, Product>();
    }

    public void addProduct(NearableID nearableID, String name, String desc, String price){
        products.put(nearableID, new Product(name,desc,sizesAvailable,typesAvailable,price));
    }

    public Map<NearableID, Product> getProducts(){
        return products;
    }


}