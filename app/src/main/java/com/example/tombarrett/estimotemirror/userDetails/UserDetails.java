package com.example.tombarrett.estimotemirror.userDetails;

import android.widget.EditText;

/**
 * Created by tombarrett on 31/07/2017.
 * Class used for recording usr details to be used by the DatabaseHelper class.
 */

public class UserDetails {

    private String name="unset";
    private String email;
    private String address;
    private String pantsSize;
    private String shoeSize;
    private String topSize;

    public UserDetails(String name, String email, String address, String pantsSize, String shoeSize, String topSize){
        this.name=name;
        this.email=email;
        this.address=address;
        this.pantsSize=pantsSize;
        this.shoeSize=shoeSize;
        this.topSize=topSize;
    }

    public UserDetails(){

    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPantsSize() {
        return pantsSize;
    }

    public void setPantsSize(String pantsSize) {
        this.pantsSize = pantsSize;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getTopSize() {
        return topSize;
    }

    public void setTopSize(String topSize) {
        this.topSize = topSize;
    }
}
