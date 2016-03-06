package com.example.aswin.nitcmesschronicles;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView endResult;
    EditText extra_taken;
    Button add,show,undo;
    String TEMP_TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

        Intent intent = getIntent();
        TEMP_TABLE_NAME = intent.getExtras().getString("temp_table");

        update();
    }

    private void Initialize() {
        endResult = (TextView)findViewById(R.id.tv_endResult);
        extra_taken = (EditText)findViewById(R.id.et_extra_taken);
        add = (Button)findViewById(R.id.b_add);
        show = (Button)findViewById(R.id.b_show);
        undo = (Button)findViewById(R.id.b_undo);
        add.setOnClickListener(this);
        show.setOnClickListener(this);
        undo.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void update() {

        Integer one;
        DbManager worker = new DbManager(this);
        SQLiteDatabase db= worker.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(" + DbManager.EXTRA + ") FROM " + TEMP_TABLE_NAME, null);

        if(cur.moveToFirst())
        {
            one = cur.getInt(0);
            endResult.setText(String.valueOf(one));
        }
        cur.close();
    }

    private long AddEntry( String date,String day, Integer currentextra) {

        DbManager worker = new DbManager(this);
        SQLiteDatabase db= worker.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbManager.DATE_AND_TIME, date);
        values.put(DbManager.DAY, day);
        values.put(DbManager.EXTRA, currentextra);

        long count = db.insert(TEMP_TABLE_NAME, null, values);
        db.close();
        return count;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_add:

                String et_temp = extra_taken.getText().toString();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                if(et_temp.trim().equals("")){
                    Toast.makeText(this, "It's BLANK!!!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this, "Extra Added", Toast.LENGTH_SHORT).show();
                    AddEntry(currentDateTimeString,date,Integer.parseInt(et_temp));
                    update();
                    extra_taken.setText("");
                }

                break;

            case R.id.b_show:

                Intent i = new Intent(MainActivity.this,ShowData.class);
                i.putExtra("table_name",TEMP_TABLE_NAME);
                startActivity(i);

                break;

            case R.id.b_undo:

                DbManager worker = new DbManager(this);
                SQLiteDatabase db= worker.getWritableDatabase();
                db.execSQL("DELETE FROM " + TEMP_TABLE_NAME + " WHERE " + DbManager.ROW_ID  +
                        "= (SELECT MAX(" + DbManager.ROW_ID +") FROM "+ TEMP_TABLE_NAME + ");");

                Toast.makeText(this,"Last Entry Deleted!",Toast.LENGTH_SHORT).show();
                update();

                break;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this,Home.class);
        startActivity(intent);
    }
}
