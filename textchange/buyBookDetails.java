package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class buyBookDetails extends AppCompatActivity {
    TextView nameholder, emailholder, majorholder, gradyearholder;
    SQLiteDatabase signInDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_book_details);

        try{
            signInDatabase = openOrCreateDatabase("signInDatabase", MODE_PRIVATE, null);
            createTable("signInTable", signInDatabase);
        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }


        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        String user = myBundle.getString("owner");

        String email = getStringFromSQLtable(user, "Email");
        String major = getStringFromSQLtable(user, "Major");
        String gradyear = getIntFromSQLtable(user, "gradyear").toString();



        nameholder = (TextView) findViewById(R.id.buyusernameholder);
        emailholder = (TextView) findViewById(R.id.buyemailholder);
        majorholder = (TextView) findViewById(R.id.buymajorholder);
        gradyearholder = (TextView) findViewById(R.id.buygradyearholder);

        nameholder.setText(user);
        emailholder.setText(email);
        majorholder.setText(major);
        gradyearholder.setText(gradyear);
    }


    public void createTable(String tablename, SQLiteDatabase db)
    {
        Log.d(getLocalClassName(), "in create table");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                tablename +
                " (Username VARCHAR, " +
                "  Email VARCHAR," +
                "   Password VARCHAR, SellTableName VARCHAR, BuyTableName VARCHAR, Major VARCHAR, Gradyear DECIMAL(4,0));");
    }

    public String getStringFromSQLtable(String user, String colName)
    {
        cursor = signInDatabase.rawQuery("Select " + colName + " from signInTable where Username = '" + user+"'", null);
        cursor.moveToFirst();
        String result = cursor.getString(0);
        cursor.close();
        return result;
    }

    public Integer getIntFromSQLtable(String user,String colName)
    {
        cursor = signInDatabase.rawQuery("Select " + colName + " from signInTable where Username = '" + user+"'", null);
        cursor.moveToFirst();
        Integer result = cursor.getInt(0);
        cursor.close();
        return result;
    }


}
