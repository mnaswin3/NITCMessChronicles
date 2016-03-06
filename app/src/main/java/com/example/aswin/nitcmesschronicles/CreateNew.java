package com.example.aswin.nitcmesschronicles;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNew extends AppCompatActivity implements View.OnClickListener{

    EditText mess_name,month,year;
    Button create;
    String v1,v2,v3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        Initialize();
    }

    private void Initialize() {
        mess_name = (EditText) findViewById(R.id.et_mess_name);
        month = (EditText) findViewById(R.id.et_month);
        year = (EditText) findViewById(R.id.et_year);
        create = (Button)findViewById(R.id.b_create);
        create.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void ResetEditText() {
        mess_name.setText("");
        month.setText("");
        year.setText("");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_create:

                CreateTableFromEditText();

                ResetEditText();

                break;
        }
    }


    private void CreateTableFromEditText() {

        v1 = mess_name.getText().toString();
        v2 = month.getText().toString();
        v3 = year.getText().toString();

        if (v1.trim().equals("") || v2.trim().equals("") || v3.trim().equals("")) {
            Toast.makeText(this, "Sure You Didn't Leave Blanks?", Toast.LENGTH_SHORT).show();
        } else {

            DbManager.TABLE_NAME = "";
            DbManager.CREATE_TABLE_NAME = "";
            DbManager.TABLE_NAME = v1 + "_" + v2 + "_" + v3;
            DbManager.CREATE_TABLE_NAME = "CREATE TABLE " +
                    DbManager.TABLE_NAME + "( " + DbManager.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DbManager.DATE_AND_TIME + " TEXT NOT NULL, "
                    + DbManager.DAY + " TEXT NOT NULL, "
                    + DbManager.EXTRA + " INTEGER NOT NULL);";

            DbManager worker = new DbManager(this);
            SQLiteDatabase db = worker.getWritableDatabase();
            db.execSQL(DbManager.CREATE_TABLE_NAME);

            Intent i = new Intent(CreateNew.this, Home.class);
            startActivity(i);
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent(CreateNew.this,Home.class);
        startActivity(intent);
    }

}
