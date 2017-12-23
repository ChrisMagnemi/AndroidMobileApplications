package com.example.chrismagnemi.textchange;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.security.KeyStore;

public class CreateAccount extends AppCompatActivity {
    EditText userEdit, emailEdit, p1Edit, p2Edit;
    Button submitAcct;
    SQLiteDatabase signInDatabase = null;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        userEdit = (EditText) findViewById(R.id.createusernameEdit);
        emailEdit = (EditText) findViewById(R.id.createemailEdit);
        p1Edit = (EditText) findViewById(R.id.createpasswordEdit);
        p2Edit = (EditText) findViewById(R.id.createconfirmEdit);
        submitAcct = (Button) findViewById(R.id.createNewAccount);

        try{
            signInDatabase = openOrCreateDatabase("signInDatabase", MODE_PRIVATE, null);
            createTable("signInTable", signInDatabase);
        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }


        submitAcct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = userEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String p1 = p1Edit.getText().toString();
                String p2 = p2Edit.getText().toString();

                Log.d("username, ",  username);

                int verify = verifyPassword(username, email, p1, p2);
                if (verify == 4){
                    signInDatabase.execSQL(addNewUserSQL(username, email, p1));
                    Toast.makeText(CreateAccount.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }}
        );
    }


    private int verifyPassword(String username, String email, String p1, String p2){
        // currently 4 point verification
        int verify = 0;
        // confirm unique username.....1 point
        if (!username.equals("")) {
            cursor = signInDatabase.rawQuery("select count(*) from signInTable where Username = '" + username + "'", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            if (count == 0) {
                verify += 1;
            } else {
                Toast.makeText(this, "Another user has chosen this username", Toast.LENGTH_SHORT).show();
            }
        }
        // confirm email is an email using @ symbol .... 1 point
        if (email.contains("@")){
            verify += 1;
        } else {
            if (verify == 1)
                Toast.makeText(this, "Must enter a proper email address", Toast.LENGTH_SHORT).show();
        }
        // confirm non null password....1 point
        if (p1.length() < 5) {
                verify += 1;
        } else {
            if (verify == 2)
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
        }
        // confirm passwords match..... 1 point
        if (p1.equals(p2)) {
            verify += 1;
        } else {
            if (verify == 3)
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
        }

        return verify;
    }

    private String addNewUserSQL(String username, String email, String p1){
        String sellTableName = username.concat("selltable");
        String buyTableName = username.concat("buyTable");

        return "INSERT INTO signInTable (Username, " +
        "Email," +
                " Password, SellTableName, BuyTableName, Major, Gradyear)" +
                " VALUES (" + "'"+ username+"'" + ", '" + email + "', '" +
                  p1 + "', '" + sellTableName + "', '" + buyTableName + "', 'Undecided', 0000);";
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
} // end of class
