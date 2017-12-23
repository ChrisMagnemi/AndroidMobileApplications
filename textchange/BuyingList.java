package com.example.chrismagnemi.textchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class BuyingList extends AppCompatActivity {
    ListView listview;
    CustomSellAdapter myAdaptor;
    private ArrayList<String> textTitles = new ArrayList<>();
    private ArrayList<sellItemModel> sellItemModelsArrayList = new ArrayList<>();
    Cursor cursor;
    SQLiteDatabase sellDatabase;
    String buytablename = "buyTable";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_list);

        listview = (ListView) findViewById(R.id.buyListView);


        try{
            sellDatabase = openOrCreateDatabase("sellDatabase", MODE_PRIVATE, null);
            createTable(buytablename, sellDatabase);


        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> a, View v, int pos, long id){
                String owner = "";

                String title = textTitles.get(pos);
                Log.d("title isL ", title);

                String statement = "SELECT OWNER FROM "+ buytablename+" WHERE title = " +
                        "'"+title+"';";

                cursor = sellDatabase.rawQuery(statement, null);

                if (cursor.moveToFirst()){
                    do{
                        owner = cursor.getString(0);
                    }while (cursor.moveToNext());

                }
                cursor.close();


//                String owner = sellDatabase.execSQL(statement);

                Intent bookdetails = new Intent(BuyingList.this, buyBookDetails.class);

                Bundle mydata = new Bundle();
                mydata.putSerializable("title", title);
                mydata.putSerializable("owner", owner);
                bookdetails.putExtras(mydata);
//
                startActivity(bookdetails);


            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("MySellingList:..... ", "OnStart Begin");
//        query sellDatabase to popular textTitles
        try{
            sellDatabase = openOrCreateDatabase("sellDatabase", MODE_PRIVATE, null);
            createTable(buytablename, sellDatabase);
            Log.d("buying list, ", "table created successfully");

        }
        catch (SQLiteException se){
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }


        cursor = sellDatabase.rawQuery("SELECT Title from " + buytablename, null);

        if (cursor.getCount() > 10){   Log.d("NOPE NOPE NOPE", "NOPE"); }
//        cursor.get
        int i = 0;
        while (i < cursor.getCount()) {
            cursor.moveToPosition(i);
            String tit = cursor.getString(0);
            Log.d("tit is-------", tit);
            textTitles.add(tit);
            i++;


        }


//
//        while (cursor.moveToNext()){
//
//            String tit = cursor.getString(0);
//            Log.d("tit is-------", tit);
//            textTitles.add(tit);
//            cursor.moveToNext();
//
//        }

        cursor.close();



        if (sellItemModelsArrayList.isEmpty())
            sellItemModelsArrayList = populateList();

        Log.d("sellItemMdlAry obj", "populated list");

        myAdaptor = new CustomSellAdapter(this, sellItemModelsArrayList);



        listview.setAdapter(myAdaptor);
//                    myAdaptor.update(sellItemModelsArrayList);


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

}
