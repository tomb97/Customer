package com.example.tombarrett.estimotemirror.presenter;

import com.example.tombarrett.estimotemirror.R;
import com.example.tombarrett.estimotemirror.awsSNS.AWSSNSManager;
import com.example.tombarrett.estimotemirror.awsSNS.IAWSSNSManager;
import com.example.tombarrett.estimotemirror.token.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tombarrett on 11/08/2017.
 */

public class TokenPresenter {

    public TokenPresenter(){

    }

    public void sentExpiryReminder(Token tempToken){
        IAWSSNSManager awssnsManager = new AWSSNSManager();
        awssnsManager.publishMessageForNearExpiredToken(("Token: "+tempToken.getName()+" is near expiry!\nExpiry Date: "+tempToken.getDate()),"Token near expiry date!");
    }

    public List<Token> tokens(){
        List<Token> tokens;
        try {
            tokens = new ArrayList<Token>();
            Token nike = new Token("Nike", "31/08/2017", R.drawable.nike, "Token for nike shoes", 10);
            Token chanel = new Token("Chanel", "31/08/2017", R.drawable.channel, "Token for Chanel perfume", 25);
            Token trek = new Token("Trek", "31/08/2017", R.drawable.trek, "Token for Trek Bike", 35);
            Token coke = new Token("Coke", "31/08/2017", R.drawable.coke, "Token for a can of coke", 2);
            Token starbucks = new Token("Starbucks", "31/08/2017", R.drawable.starbuck, "Token for any Tall Coffee", 5);
            Token rayban = new Token("Raybans", "31/08/2017", R.drawable.rayban, "Token for our new range of RayBans", 50);
            Token canon = new Token("Canon", "31/08/2017", R.drawable.canon, "Token for Canon lense", 30);
            Token samsung = new Token("Samsung", "13/08/2017", R.drawable.samsung, "Token for the Galaxy S8", 70);
            tokens.add(nike);
            tokens.add(chanel);
            tokens.add(trek);
            tokens.add(coke);
            tokens.add(starbucks);
            tokens.add(rayban);
            tokens.add(canon);
            tokens.add(samsung);
        } catch(ParseException parse){
            tokens = new ArrayList<Token>();
        }
        return tokens;
    }

    public boolean nearExpired(Date expDate, Token tempToken){
        boolean nearExp=true;

        Date currentDate = new Date();
        long week = 7l * 24 * 60 * 60 * 1000;
        Date currentAndWeek=new Date(currentDate.getTime()+week);
        if(expDate.before(currentAndWeek)){
            nearExp=true;
            sentExpiryReminder(tempToken);
        }
        else {
            nearExp = false;
        }
        return nearExp;
    }
}
