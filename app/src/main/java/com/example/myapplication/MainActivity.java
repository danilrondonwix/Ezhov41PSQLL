package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear;
    EditText etMarka, etKyzov, etGod;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);

        etMarka = (EditText) findViewById(R.id.Marka);
        etKyzov = (EditText) findViewById(R.id.Kyzov);
        etGod = (EditText) findViewById(R.id.God);


        dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_AVTO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int markaIndex = cursor.getColumnIndex(DBHelper.KEY_MARKA);
            int kyzovIndex = cursor.getColumnIndex(DBHelper.KEY_KYZOV);
            int godIndex = cursor.getColumnIndex(DBHelper.KEY_GOD);
            TableLayout tb2 = findViewById(R.id.tableLayout2);
            tb2.removeAllViews();
            do {
                TableRow tbOUT = new TableRow(this);
                tbOUT.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView ID = new TextView(this);
                params.weight = 1.0f;
                ID.setLayoutParams(params);
                ID.setText(cursor.getString(idIndex));
                tbOUT.addView(ID);


                TextView MARKA = new TextView(this);
                params.weight = 1.0f;

                MARKA.setLayoutParams(params);
                MARKA.setText(cursor.getString(markaIndex));
                tbOUT.addView(MARKA);


                TextView KYZOV = new TextView(this);
                params.weight = 3.0f;
                KYZOV.setLayoutParams(params);
                KYZOV.setText(cursor.getString(kyzovIndex));
                tbOUT.addView(KYZOV);

                TextView GOD = new TextView(this);
                params.weight = 3.0f;
                GOD.setLayoutParams(params);
                GOD.setText(cursor.getString(godIndex));
                tbOUT.addView(GOD);


                Button DELETE = new Button(this);
                DELETE.setOnClickListener(this);
                params.weight = 1.0f;
                DELETE.setLayoutParams(params);
                DELETE.setText("Удалить запись");
                DELETE.setId(cursor.getInt(idIndex));
                tbOUT.addView(DELETE);

                tb2.addView(tbOUT);


            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        String marka = etMarka.getText().toString();
        String kyzov = etKyzov.getText().toString();
        String god = etGod.getText().toString();
        contentValues = new ContentValues();

        switch (v.getId()) {

            case R.id.Add:


                contentValues.put(DBHelper.KEY_MARKA, marka);
                contentValues.put(DBHelper.KEY_KYZOV, kyzov);
                contentValues.put(DBHelper.KEY_GOD, god);

                database.insert(DBHelper.TABLE_AVTO, null, contentValues);
                UpdateTable();
                etMarka.setText(null);
                etKyzov.setText(null);
                etGod.setText(null);

                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_AVTO, null, null);
                TableLayout dbOutput = findViewById(R.id.tableLayout2);
                dbOutput.removeAllViews();
                etMarka.setText(null);
                etKyzov.setText(null);
                etGod.setText(null);
                UpdateTable();
                break;
            default:
                View outBDRow = (View) v.getParent();
                ViewGroup outBD = (ViewGroup)  outBDRow.getParent();
                outBD.removeView(outBDRow);
                outBD.invalidate();

                database.delete(DBHelper.TABLE_AVTO,DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUPD = database.query(DBHelper.TABLE_AVTO, null, null, null, null, null, null);

                if (cursorUPD.moveToFirst()) {
                    int idIndex = cursorUPD.getColumnIndex(DBHelper.KEY_ID);
                    int markaIndex = cursorUPD.getColumnIndex(DBHelper.KEY_MARKA);
                    int kyzovIndex = cursorUPD.getColumnIndex(DBHelper.KEY_KYZOV);
                    int godIndex = cursorUPD.getColumnIndex(DBHelper.KEY_GOD);
                    int realID =1;
                    do{
                        if(cursorUPD.getInt(idIndex)>realID)
                        {
                            contentValues.put(DBHelper.KEY_ID,realID);
                            contentValues.put(DBHelper.KEY_MARKA,cursorUPD.getString(markaIndex));
                            contentValues.put(DBHelper.KEY_KYZOV,cursorUPD.getString(kyzovIndex));
                            contentValues.put(DBHelper.KEY_GOD,cursorUPD.getString(godIndex));
                            database.replace(DBHelper.TABLE_AVTO,null,contentValues);


                        }
                        realID++;
                    }while (cursorUPD.moveToNext());
                    if(cursorUPD.moveToLast() && v.getId()!=realID){
                        database.delete(DBHelper.TABLE_AVTO,DBHelper.KEY_ID+ " = ?", new String[]{cursorUPD.getString(idIndex)});
                    }
                    UpdateTable();
                }


                break;


        }
        dbHelper.close();
    }
}
