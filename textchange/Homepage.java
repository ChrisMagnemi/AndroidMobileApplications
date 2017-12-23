package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.OnMapReadyCallback;


public class Homepage extends AppCompatActivity implements OnMapReadyCallback {
//    private GoogleMap map;
    SQLiteDatabase signInDatabase;
    TextView nameholder, emailholder, majorholder, gradyearholder;
    Cursor cursor;
    String password;
    Button mapButton;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        nameholder = (TextView) findViewById(R.id.usernameholder);
        emailholder = (TextView) findViewById(R.id.emailholder);
        majorholder = (TextView) findViewById(R.id.majorholder);
        gradyearholder = (TextView) findViewById(R.id.gradyearholder);
        mapButton = (Button) findViewById(R.id.mapButton);

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
        String user = myBundle.getString("user");
        nameholder.setText(user);
        String pass = myBundle.getString("pass");
        password = pass;

        String email = getStringFromSQLtable(user, pass, "Email");
        emailholder.setText(email);

        Integer gradyear = getIntFromSQLtable(user, pass, "Gradyear");
        gradyearholder.setText(gradyear.toString());

        String major = getStringFromSQLtable(user, pass, "Major");
        majorholder.setText(major);

        mapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent map = new Intent(Homepage.this, MapActivity.class);

//                Intent myLocalIntent = getIntent();
//                Bundle myBundle = myLocalIntent.getExtras();
//                String user = myBundle.getString("user");
//                String pass = myBundle.getString("pass");
//
//                addTextbook.putExtras(myBundle);

                startActivity(map);
            }
        });



    } // end on create

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        mMap.setMyLocationEnabled(true);

        LatLng Boston = new LatLng(42.0, -71.0);
        mMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(Boston).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Boston, 14));
    }

//    public void getMapAsync(){
//
//    }


    @Override
    public void onStart(){
        super.onStart();

        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        String user = myBundle.getString("user");
        nameholder.setText(user);
        String pass = myBundle.getString("pass");
        password = pass;

        Integer gradyear = getIntFromSQLtable(user, pass, "Gradyear");
        gradyearholder.setText(gradyear.toString());

        String major = getStringFromSQLtable(user, pass, "Major");
        majorholder.setText(major);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle item selection
        switch(item.getItemId()){
            case R.id.EditAccount:
                Intent EditAccount = new Intent (Homepage.this, EditAccount.class);
                Bundle mydata = new Bundle();
                String user = nameholder.getText().toString();
                mydata.putSerializable("user", user);
                mydata.putSerializable("pass", password);
                Log.d("home, password is: ", password);

                EditAccount.putExtras(mydata);
                startActivityForResult(EditAccount, 104);

                return true;
            case R.id.mySellingList:
                Intent sellingList = new Intent (Homepage.this, MySellingList.class);
                String sellUser = nameholder.getText().toString();
                Bundle sellData = new Bundle();
                sellData.putSerializable("user", sellUser);
                sellData.putSerializable("pass", password);

                sellingList.putExtras(sellData);
                Log.d("about to start: ", "sell list");
                startActivity(sellingList);

                return true;
            case R.id.myBuyingList:
              Intent buyingList = new Intent (Homepage.this, BuyingList.class);
//              String buyUser = nameholder.getText().toString();
//              Bundle buyData = new Bundle();
//              sellData.putSerializable("user", buyUser);
//              sellData.putSerializable("pass", password);
//
//              buyingList.putExtras(buyData);
              startActivity(buyingList);
                return true;
            case R.id.SignOut:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
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

    public String getStringFromSQLtable(String user, String pass, String colName)
    {
        cursor = signInDatabase.rawQuery("Select " + colName + " from signInTable where Username = '" + user+"' and " +
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

}
