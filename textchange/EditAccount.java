package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditAccount extends AppCompatActivity {
    Cursor cursor;
    SQLiteDatabase signInDatabase;
    EditText majorEdit, GradYearEdit;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        majorEdit = (EditText) findViewById(R.id.acctmajorEdit);
        GradYearEdit = (EditText) findViewById(R.id.acctgradyearEdit);
        update = (Button) findViewById(R.id.saveacctButton);

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
        final String user = myBundle.getString("user");
        String pass = myBundle.getString("pass");
        Log.d("edit acct user is: ", user);

        String major = getStringFromSQLtable(user, pass, "Major");

        majorEdit.setText(major);

        Integer grad = getIntFromSQLtable(user, pass, "Gradyear");
        GradYearEdit.setText(grad.toString());

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String newmajor = majorEdit.getText().toString();
                boolean digitsOnly = TextUtils.isDigitsOnly(GradYearEdit.getText());
                if (digitsOnly) {
                    Integer newgradyear = Integer.parseInt(GradYearEdit.getText().toString());
                    signInDatabase.execSQL("Update signInTable set Major = '" + newmajor + "', Gradyear = '" + newgradyear + "'" +
                            "where Username = '" + user + "';");
                    Toast.makeText(EditAccount.this, "Account Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(EditAccount.this, "Grad Year Must be a Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }// end on create


    public String getStringFromSQLtable(String usern, String pass, String colName)
    {
        Log.d("edit acct user is: ", usern);
        Log.d("edit acct pass is: ", pass);
        Log.d("edit acct colName is: ", colName);
        cursor = signInDatabase.rawQuery("Select " + colName + " from signInTable where Username = '" + usern+"' and " +
                "Password = '" + pass + "'", null);
        cursor.moveToFirst();
        String result = cursor.getString(0);
        return result;
    }

    public Integer getIntFromSQLtable(String user, String pass, String colName)
    {
        cursor = signInDatabase.rawQuery("Select " + colName + " from signInTable where Username = '" + user+"' and " +
                "Password = '" + pass + "'", null);
        cursor.moveToFirst();
        Integer result = cursor.getInt(0);
        return result;
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
}
