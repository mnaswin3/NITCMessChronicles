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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener {

    ListView home;
    ImageView addicon;


    Integer[] imageLeft = {
            R.drawable.hostel,
            R.drawable.hostel1,
            R.drawable.hostel2,
            R.drawable.hostel3,
            R.drawable.hostel4,
            R.drawable.hostel5,
            R.drawable.hostel6,
            R.drawable.hostel7
    };

    Integer[] imageRight = {
            R.drawable.arrow64
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialize();

        ExtractingTableList();

    }

    private void Initialize() {

        home = (ListView) findViewById(R.id.lv_home);
        addicon = (ImageView) findViewById(R.id.imageview_addicon);
        addicon.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_addicon:
                Intent i = new Intent(Home.this, CreateNew.class);
                startActivity(i);
                break;
        }
    }

    private void ExtractingTableList() {

        final ArrayList<String> table_names = new ArrayList<>();
        DbManager worker = new DbManager(this);
        SQLiteDatabase db = worker.getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToLast()) {
            do {
                String s = c.getString(0);
                if (s.equals("android_metadata") || s.equals("sqlite_sequence")) {
                    continue;
                } else {
                    table_names.add(s);
                }

            } while (c.moveToPrevious());
        }
        c.close();

        CustomListView adapter = new CustomListView(this,table_names,imageLeft,imageRight);
        home.setAdapter(adapter);

        //SimplePress Of ListView
        home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(Home.this, MainActivity.class);
                i.putExtra("temp_table", table_names.get(position));
                startActivity(i);

            }
        });


        //LongPress Of ListView
        home.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setIcon(R.drawable.action_delete);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        String delete_table = table_names.get(pos);

                        DbManager worker = new DbManager(Home.this);
                        SQLiteDatabase db = worker.getWritableDatabase();
                        db.execSQL("DROP TABLE IF EXISTS " + delete_table);

                        ExtractingTableList();      //Refreshing List

                        Toast.makeText(Home.this, "Mission Complete!", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), "Saved By The Bell!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                return true;
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String title,message;
        int icon;

        switch (item.getItemId()) {
            case R.id.action_delete:

                title = "Confirm Execution";
                message = "Long Press On ListView Item To Delete.";
                icon = R.drawable.action_delete;

                ShowDialogueBox(title, message, icon);

                return true;
            case R.id.action_refresh:

                ExtractingTableList();

                return true;
            case R.id.action_about_us:

                title = "About Us?";
                message = "Some Other Time Maybe.";
                icon = R.drawable.action_about_us;

                ShowDialogueBox(title,message,icon);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowDialogueBox(String title, String message, int icon) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(icon);

        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
