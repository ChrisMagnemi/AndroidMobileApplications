package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MySellingList extends AppCompatActivity {
    Button addTextbook;
    ListView myListView;
    CustomSellAdapter myAdaptor;
    private ArrayList<String> textTitles = new ArrayList<>();
    private ArrayList<sellItemModel> sellItemModelsArrayList = new ArrayList<>();
    Cursor cursor;
    SQLiteDatabase sellDatabase;
    String selltablename;
    String user1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MySelling List:.....", "On Create");
        setContentView(R.layout.activity_my_selling_list);

        addTextbook = (Button) findViewById(R.id.sellListAddTextButton);
        myListView = (ListView) findViewById(R.id.sellListView);

        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        String user = myBundle.getString("user");
        selltablename = user.concat("selltable");




        try{
            sellDatabase = openOrCreateDatabase("sellDatabase", MODE_PRIVATE, null);
            createTable(selltablename, sellDatabase);
        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }



        addTextbook.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent addTextbook = new Intent(MySellingList.this, addTextbook.class);

                Intent myLocalIntent = getIntent();
                Bundle myBundle = myLocalIntent.getExtras();
                String user = myBundle.getString("user");
                String pass = myBundle.getString("pass");

                addTextbook.putExtras(myBundle);
                sellItemModelsArrayList.clear();
                textTitles.clear();

                startActivityForResult(addTextbook, 106);
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> a, View v, int pos,long id){

               String title = textTitles.get(pos);
//                Log.d("title isL ", title);

                Intent bookdetails = new Intent(MySellingList.this, bookDetails.class);

                Bundle mydata = new Bundle();
                mydata.putSerializable("title", title);
                bookdetails.putExtras(mydata);

                startActivity(bookdetails);


            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String title = textTitles.get(position);
                sellDatabase.execSQL("DELETE FROM "+selltablename+" WHERE title = '"+title+"';");
                sellDatabase.execSQL("DELETE FROM "+"buyTable"+" WHERE title = '"+title+"';");


                textTitles.clear();

                cursor = sellDatabase.rawQuery("SELECT Title from " + selltablename, null);

                if (cursor.moveToFirst()){
                    do {
                        String tit = cursor.getString(0);
                        textTitles.add(tit);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sellItemModelsArrayList = populateList();

                myAdaptor.update(sellItemModelsArrayList);
                Toast.makeText(MySellingList.this, title + " deleted from your selling list", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

//        sellItemModelsArrayList = populateList();
////        Log.d("sellITemModelsArray ", sellItemModelsArrayList.getName() );
//        myAdaptor = new CustomSellAdapter(this, sellItemModelsArrayList);
////        Log.d("item0 is: ", myAdaptor.getItem(0).toString());
//        myListView.setAdapter(myAdaptor);
    }  //===================== END ON CREATE ==========================

    @Override
    public void onStart(){
        super.onStart();
        Log.d("MySellingList:..... ", "OnStart Begin");
//        query sellDatabase to popular textTitles
        try{
            sellDatabase = openOrCreateDatabase("sellDatabase", MODE_PRIVATE, null);
            createTable(selltablename, sellDatabase);

        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
//        sellItemModelsArrayList.clear();
//        textTitles.clear();


        cursor = sellDatabase.rawQuery("SELECT Title from " + selltablename, null);

        if (cursor.moveToFirst()){
            do {
                String tit = cursor.getString(0);
                textTitles.add(tit);
            } while (cursor.moveToNext());
        }
        cursor.close();


        if (sellItemModelsArrayList.isEmpty()) {
            sellItemModelsArrayList = populateList();

//            Log.d("sellItemMdlAry obj", sellItemModelsArrayList.get(0).toString());

            myAdaptor = new CustomSellAdapter(this, sellItemModelsArrayList);


            myListView.setAdapter(myAdaptor);
//                    myAdaptor.update(sellItemModelsArrayList);
        }


    }

    @Override
    public void onResume(){
        super.onResume();
//        sellItemModelsArrayList.clear();
        sellItemModelsArrayList.clear();
        textTitles.clear();

        cursor = sellDatabase.rawQuery("SELECT Title from " + selltablename, null);

        if (cursor.moveToFirst()){
            do {
                String tit = cursor.getString(0);
                textTitles.add(tit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sellItemModelsArrayList = populateList();

        myAdaptor.update(sellItemModelsArrayList);


//        sellItemModelsArrayList = populateList();
//        myAdaptor.update(sellItemModelsArrayList);
    }


//    private class DownloadJson extends AsyncTask<Void, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            jsonObject = JSONfunctions.getJSONfromURL("https://api.myjson.com/bins/6oei9");
//
//            try{
//                jsonArray = jsonObject.getJSONArray("textbooks");
//            }
//            catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            int i = 0;
//            while (i < textTitles.size()) {
//                if (textTitles.get(i).equals(jsonObject.optString("title")) ) {
//
//                    sellItemModel item = new sellItemModel();
//                    item.setName(jsonObject.optString("title"));
//                    Bitmap bmp;
//
////                    URL url = new URL(jsonObject.optString("imageURL"));
//                    try {
//                        URL url = new URL(jsonObject.optString("imageURL"));
//                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    }
//                    catch (IOException e){
//
//                    }
//                    Drawable d = new BitmapDrawable(getResources(), bmp);
//
//
////                    item.setImage_drawable();
//                }
//            }
//        }
//    }






    private ArrayList<sellItemModel> populateList(){

        ArrayList<sellItemModel> list = new ArrayList<>();

        for(int i = 0; i < textTitles.size(); i++){
//            Log.d("textTitle is: ", textTitles.toString());
            sellItemModel itemModel = new sellItemModel();
//            Log.d("texttitle items: ", textTitles.get(i));
            itemModel.setName(textTitles.get(i));
//            sellItemModel.setImage_drawable(myImageList[i]);
            list.add(itemModel);
        }
        return list;
    }

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
