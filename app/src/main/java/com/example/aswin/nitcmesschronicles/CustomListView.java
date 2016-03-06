package com.example.aswin.nitcmesschronicles;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> text;
    private final Integer [] image1,image2;

    public CustomListView(Activity context, ArrayList<String> text, Integer [] image1, Integer [] image2) {
        super(context, R.layout.custom_listview,text);
        this.context = context;
        this.text = text;
        this.image1 = image1;
        this.image2 = image2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_listview, null, true);

        TextView textView = (TextView)rowView.findViewById(R.id.tv_textview_listview);
        ImageView imageView1 = (ImageView)rowView.findViewById(R.id.imageview_left);
        ImageView imageView2 = (ImageView)rowView.findViewById(R.id.imageview_right);


        textView.setText(text.get(position));
        Random generator = new Random();
        int randomIndex = generator.nextInt(image1.length);
        imageView1.setImageResource(image1[randomIndex]);
        imageView2.setImageResource(image2[0]);

        return rowView;
    }
}
