package com.example.chrismagnemi.textchange;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ChrisMagnemi on 5/8/17.
 */

public class CustomSellAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<sellItemModel> imageModelArrayList;
    //View myView;

    //  spinner items
    //String[] spinnerValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] spinnerValues;

    // public CustomAdapter(Context context, ArrayList<String> myValues){
    public CustomSellAdapter(Context context, ArrayList<sellItemModel> imageModelArrayList){
        this.mContext = context;
        this.imageModelArrayList = imageModelArrayList;
    }
    @Override
    public int getCount() {
        //return myValues.size();
        return imageModelArrayList.size();
    }

//    @Override
    public void update(ArrayList<sellItemModel> newArrayList){
        imageModelArrayList.clear();
        imageModelArrayList.addAll(newArrayList);
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int arg0) {
        //return myValues.get(arg0);
        return imageModelArrayList.get(arg0);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View myView = convertView;

        final int rowNumber = position;

        if (convertView == null) {
            myView = inflater.inflate(R.layout.sell_list_item, null,true);
            //myView = inflater.inflate(R.layout.my_cell, null);
            //View myView = convertView;
            //LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            //convertView = inflater.inflate(R.layout.row_layout, null, true);
            //myView = convertView;
        }
        TextView names = (TextView) myView.findViewById(R.id.sellListTextView);
        ImageView imageView = (ImageView) myView.findViewById(R.id.sellListImgView);


        names.setText(imageModelArrayList.get(position).getName());
        imageView.setImageResource(R.drawable.stockimg);


        return myView;
    }


}
