package com.example.tombarrett.estimotemirror.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tombarrett.estimotemirror.userDetails.UserDetails;
import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by tombarrett on 08/08/2017.
 * Very basic database helper class due to time constraints
 * Methods names shouldn't have to be changed if a more robust implementation is added
 * and therefore little changes outside this class would be needed.
 */

public class DatabaseHelper implements IDatabaseHelper{
    private SQLiteDatabase db;
    private Context context;

    public DatabaseHelper(Context context){
        this.context=context;
    }

    public void connectToDB(){
        try{
            db = context.openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS UserDetails(ID VARCHAR PRIMARY KEY,NAME VARCHAR," +
                    " EMAIL VARCHAR, ADDRESS VARCHAR, PANT VARCHAR, SHOE VARCHAR, TOP VARCHAR);");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void disconnectToDB(){
        try{
            db.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void updateDetails(UserDetails details){
        try{
            db.execSQL("UPDATE UserDetails SET NAME='"+details.getName()+"',EMAIL='"+details.getEmail()+"',ADDRESS='"+details.getAddress()+"',PANT='"
                    +details.getPantsSize()+"',SHOE='"+details.getShoeSize()+"',TOP='"+details.getTopSize()+"' WHERE ID='1';");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Cursor getDetails(){
        Cursor resultSet = null;
        try{
            resultSet= db.rawQuery("SELECT * FROM UserDetails WHERE ID='1';",null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return resultSet;
    }

    public Cursor getShoe(){
        Cursor resultSet = null;
        try{
            resultSet = db.rawQuery("SELECT SHOE FROM UserDetails WHERE ID='1';", null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return resultSet;
    }

    public void insertToDB(){
        try{
            db.execSQL("INSERT INTO UserDetails VALUES('1',' ',' ',' ','30','6','XS');");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
