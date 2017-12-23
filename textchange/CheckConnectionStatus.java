//package com.example.chrismagnemi.textchange;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
///**
// * Created by ChrisMagnemi on 5/9/17.
// */
//
//public class CheckConnectionStatus extends AsyncTask<String, Void, String> {
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//            }
//
//
//
//        @Override
//    protected String doInBackground(String... params) {
//        URL url = null;
//        try {
////As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
//           url = new URL(params[0]);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//            try {
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
////Getting inputstream from connection, that is response which we got from server
//                InputStream inputStream = urlConnection.getInputStream();
////Reading the response
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String s = bufferedReader.readLine();
//                bufferedReader.close();
////Returning the response message to onPostExecute method
//                return s;
//            } catch (IOException e) {
//                Log.e("Error: ", e.getMessage(), e);
//            }
//            return null;
//        }
//
//        @Override
//    protected void onPostExecutre(String s){
//            super.onPostExecute(s);
//            JSONObject jsonObject = null;
//            try{
//                jsonObject = new JSONObject(s);
//                JSONArray jsonArray = jsonObject.getJSONArray("textbooks");
//                for (int i =0; i<jsonArray.length();i++){
//                    JSONObject object = jsonArray.getJSONObject(i);
//                }
//            }
//            catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//
//}
