package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addTextbook extends AppCompatActivity {
    Button addTextSubmit;
    SQLiteDatabase sellDatabase;
    String selltablename;
    EditText titleEdit, authorEdit, subjectEdit, ISBNEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_textbook);
        addTextSubmit = (Button) findViewById(R.id.addTextSubmit);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        authorEdit = (EditText) findViewById(R.id.authorEdit);
        subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        ISBNEdit = (EditText) findViewById(R.id.ISBNEdit);

        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        final String username = myBundle.getString("user");
        String pass = myBundle.getString("pass");
        selltablename = username.concat("selltable");


        try{
            sellDatabase = openOrCreateDatabase("sellDatabase", MODE_PRIVATE, null);
            createTable(selltablename, sellDatabase);
        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }


        addTextSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String title = titleEdit.getText().toString();
                String author = authorEdit.getText().toString();
                String subject = subjectEdit.getText().toString();
                String ISBN = ISBNEdit.getText().toString();
                if (!title.equals("")) {
                    sellDatabase.execSQL("INSERT INTO " + selltablename + "(Title, Author, Subject, ISBN, OWNER)" +
                            "VALUES('" + title + "', '" + author + "', '" + subject + "', '" + ISBN + "', '"+ username+"')");
                    sellDatabase.execSQL("INSERT INTO " + "buyTable" + "(Title, Author, Subject, ISBN, OWNER)" +
                            "VALUES('" + title + "', '" + author + "', '" + subject + "', '" + ISBN + "', '"+ username+"')");

                    Toast.makeText(addTextbook.this, title + " added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(addTextbook.this, "Must at least input a title.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }  // ------------- END ON CREATE------------------


    public void createTable(String tablename, SQLiteDatabase db)
    {
        Log.d(getLocalClassName(), "in create table");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                tablename +
                " (Title VARCHAR, " +
                "  Author VARCHAR," +
                "   Subject VARCHAR, ISBN VARCHAR, OWNER VARCHAR);");
    }
}
