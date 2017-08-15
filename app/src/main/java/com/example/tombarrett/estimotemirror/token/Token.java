package com.example.tombarrett.estimotemirror.token;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tombarrett on 08/08/2017.
 * Token class contains information about the tokens used in the token display activity.
 */

public class Token {

    private String name;
    private Date expDate;
    private String date;
    private int image;
    private String description;
    private SimpleDateFormat sdf;
    private int points;

    Token(){

    }

    public Token(String name, String date, int image, String description, int points) throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.name=name;
        this.date=date;
        this.image=image;
        this.description=description;
        expDate=sdf.parse(date);
        this.points=points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpDate() {
        return expDate;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getPoints(){ return points;}
}
