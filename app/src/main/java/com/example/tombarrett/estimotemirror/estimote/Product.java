package com.example.tombarrett.estimotemirror.estimote;

/**
 * Created by tombarrett on 28/07/2017.
 */

import java.util.List;

public class Product {

    private String name;
    private String summary;
    private List<String> sizesAvailable;
    private List<String> typesAvailable;
    private String price;

    public Product(String name, String summary, List<String> sizesAvailable, List<String> typesAvailable, String price) {
        this.name = name;
        this.summary = summary;
        this.sizesAvailable=sizesAvailable;
        this.typesAvailable=typesAvailable;
        this.price=price;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getPrice(){ return price; }

    public List<String> getSizesAvailable(){ return sizesAvailable; }

    public List<String> getTypesAvailable(){ return typesAvailable; }


}
