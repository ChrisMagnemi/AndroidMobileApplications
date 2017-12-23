package com.example.chrismagnemi.textchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class bookDetails extends AppCompatActivity {
    TextView title, author, price;
    ImageView img;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String sauthor;
    String sprice;
    Bitmap bmp;
    String url1, titleFromSellList;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        title = (TextView) findViewById(R.id.titleinput);
        author = (TextView) findViewById(R.id.authorinput);
        price = (TextView) findViewById(R.id.priceinput);
        img = (ImageView) findViewById(R.id.bookdetailsimg);

//        new DownloadJSON().execute();
        Intent MyLocalIntent = getIntent();
        Bundle myBundle = MyLocalIntent.getExtras();
        titleFromSellList = myBundle.getString("title");

        title.setText(titleFromSellList);
        author.setText("Could not Find Data");
        price.setText("Could not Find Data");
//        img.setImageResource();

        url1 = "https://api.myjson.com/bins/9mhy9";

        new bookDetails.CheckConnectionStatus().execute("https://api.myjson.com/bins/17514x");

    }




    class CheckConnectionStatus extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
//As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//Getting inputstream from connection, that is response which we got from server
                InputStream inputStream = urlConnection.getInputStream();
//Reading the response
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
//Returning the response message to onPostExecute method
                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }

            try {
                InputStream in = new java.net.URL(url1).openStream();
                bmp = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
//                            Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("textbooks");
                for (int i =0; i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (titleFromSellList.equals(object.getString("title"))) {
                        Toast.makeText(bookDetails.this, "This Book is in our Database!", Toast.LENGTH_SHORT).show();
                        title.setText(object.getString("title"));
                        author.setText(object.getString("author"));
                        price.setText(object.getString("price"));

                        try {
                            url = new URL(jsonObject.optString("imageURL"));
//                            img.setTag(url);
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            Glide.with(bookDetails.this).load(bmp).into(img);

//                            img.setImageBitmap(bmp);
                        } catch (IOException e) {
                        }
//                        img.setImageBitmap(bmp);



                    }
                    else{
//                        Toast.makeText(bookDetails.this, "Not found in our Database :(", Toast.LENGTH_SHORT).show();
//                        price.setText("N/A");
//                        int id = getResources().getIdentifier("stockimg.jpeg", null, null);
//                        img.setImageResource(id);
                    }
//                    int id = getResources().getIdentifier("stockimg.jpg", null, null);
//                    img.setImageResource(id);

                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }

    }



}
