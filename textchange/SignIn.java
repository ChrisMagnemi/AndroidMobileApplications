package com.example.chrismagnemi.textchange;

import android.content.Intent;
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

public class SignIn extends AppCompatActivity {
    Button createAccount, signin;
    SQLiteDatabase signInDatabase;
    EditText username, password;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signin = (Button) findViewById(R.id.signinButton);
        createAccount = (Button) findViewById(R.id.createAcctButton);
        username = (EditText) findViewById(R.id.usernameEdit);
        password = (EditText) findViewById(R.id.passwordEdit);



        try{
            signInDatabase = openOrCreateDatabase("signInDatabase", MODE_PRIVATE, null);
            createTable("signInTable", signInDatabase);
        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }






        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent newAccount = new Intent(SignIn.this, CreateAccount.class);
                startActivityForResult(newAccount, 101);
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String user = username.getText().toString();
                String pass = password.getText().toString();

                String test = isExistingUser(user, pass);
                Log.d("in signin", test);

                if (isExistingUser(user, pass).equals("true") && !user.equals("") && !pass.equals("")){
                    Intent homepage = new Intent(SignIn.this, Homepage.class);

                    Bundle mydata = new Bundle();
                    mydata.putSerializable("user", user);
                    mydata.putSerializable("pass", pass);
                    homepage.putExtras(mydata);

                    startActivityForResult(homepage, 103); // have not put finish() anywhere in homepage
                }
                else {
                    Toast.makeText(SignIn.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                }
//
            }
        });
    } // end on create

    @Override
    protected void onResume(){
        super.onResume();
        username.setText("");
        password.setText("");
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

    public String isExistingUser(String user, String password)
    {
        cursor = signInDatabase.rawQuery("select count(*) from signInTable where Username = '" + user + "' and Password = '"+ password +"'", null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        if (count == 1) {
            Log.d("count is 1", "op");
            return "true";
        }
        else {
            return "false";
        }
    }


} // end of class
