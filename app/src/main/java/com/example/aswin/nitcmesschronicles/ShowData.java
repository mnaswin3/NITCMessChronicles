package com.example.aswin.nitcmesschronicles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowData extends AppCompatActivity {

    TextView detailed_result;
    ListView showdata;
    String TEMP_TABLE_NAME, day;

    Integer[] imageLeft = {
            R.drawable.date,
            R.drawable.date1,
            R.drawable.date2,
            R.drawable.date3,
            R.drawable.date4,
            R.drawable.date5,
            R.drawable.date6,
            R.drawable.date7
    };

    Integer[] imageRight = {
            R.drawable.arrow64
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        Initialize();

        Intent intent = getIntent();
        TEMP_TABLE_NAME = intent.getExtras().getString("table_name");

        ShowDatesInLV();
    }

    private void Initialize() {

        detailed_result = (TextView)findViewById(R.id.tv_detailed_result);
        showdata = (ListView)findViewById(R.id.lv_showdata);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void ShowDatesInLV() {

        DbManager worker = new DbManager(this);
        SQLiteDatabase db = worker.getWritableDatabase();
        final ArrayList<String> daily_entry = new ArrayList<>();
        final Cursor c = db.rawQuery("SELECT DISTINCT " + DbManager.DAY + " FROM " + TEMP_TABLE_NAME, null);

        if (c.moveToLast()) {
            do {
                daily_entry.add(c.getString(0));
            } while (c.moveToPrevious());
        }
        c.close();

        CustomListView adapter = new CustomListView(this, daily_entry, imageLeft, imageRight);

        showdata.setAdapter(adapter);

        showdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                day = daily_entry.get(position);
                detailed_result.setText(ShowExtraTaken(day));

            }
        });
    }

    private String ShowExtraTaken(String day) {

        String query = "SELECT * FROM " + TEMP_TABLE_NAME + " WHERE " + DbManager.DAY  + " = '" + day + "'";
        DbManager worker = new DbManager(this);
        SQLiteDatabase db= worker.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        String temp = "\n" + day +":\n";
        if (c.moveToLast()) {
            do {
                String date = c.getString(1);
                String extra = c.getString(3);

                temp += "\n*" +date + "\n"
                        + "   Extra Taken: " + extra + "\n"
                +"------------------------------------------------" + "\n";
            } while (c.moveToPrevious());
        }
        c.close();
        return temp;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_show_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String title,message;
        int icon;
        switch (item.getItemId()) {
            case R.id.action_question_mark:
                title = "Lost Your Way?";
                message = "Click On The Dates To View Detailed Report Of Extras Taken.";
                icon = R.drawable.action_question_mark_black;
                ShowDialogueBox(title, message, icon);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowDialogueBox(String title, String message, int icon) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowData.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(icon);

        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }
}
