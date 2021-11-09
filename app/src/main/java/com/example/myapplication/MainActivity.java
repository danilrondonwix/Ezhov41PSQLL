package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    Button btnAdd, btnClear, btnPage2;
    EditText etNazvanie, etCena;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnPage2 = (Button) findViewById(R.id.Page2);
        btnPage2.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);



        etNazvanie = (EditText) findViewById(R.id.Nazvanie);
        etCena = (EditText) findViewById(R.id.Cena);



        dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_AVTO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nazvanieIndex = cursor.getColumnIndex(DBHelper.KEY_NAZVANIE);
            int cenaIndex = cursor.getColumnIndex(DBHelper.KEY_CENA);
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


                TextView NAZVANIE = new TextView(this);
                params.weight = 1.0f;

                NAZVANIE.setLayoutParams(params);
                NAZVANIE.setText(cursor.getString(nazvanieIndex));
                tbOUT.addView(NAZVANIE);


                TextView CENA = new TextView(this);
                params.weight = 3.0f;
                CENA.setLayoutParams(params);
                CENA.setText(cursor.getString(cenaIndex));
                tbOUT.addView(CENA);

                Button DELETE = new Button(this);
                DELETE.setOnClickListener(this);
                params.weight = 1.0f;
                DELETE.setLayoutParams(params);
                DELETE.setText("Delete");
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
        String nazvanie = etNazvanie.getText().toString();
        String cena = etCena.getText().toString();
        contentValues = new ContentValues();

        switch (v.getId()) {
            case    R.id.Page2:

                Intent intent =new Intent(MainActivity.this, Magazin.class);
                startActivity(intent);
                break;

            case R.id.Add:


                contentValues.put(DBHelper.KEY_NAZVANIE, nazvanie);
                contentValues.put(DBHelper.KEY_CENA, cena);

                database.insert(DBHelper.TABLE_AVTO, null, contentValues);
                UpdateTable();
                etNazvanie.setText(null);
                etCena.setText(null);

                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_AVTO, null, null);
                TableLayout dbOutput = findViewById(R.id.tableLayout2);
                dbOutput.removeAllViews();
                etNazvanie.setText(null);
                etCena.setText(null);
                UpdateTable();
                break;
            default:
                Button knopka = (Button) v;
                switch (knopka.getText().toString()){

                    case "Delete":
                        View outBDRow = (View) v.getParent();
                        ViewGroup outBD = (ViewGroup) outBDRow.getParent();
                        outBD.removeView(outBDRow);
                        outBD.invalidate();
                        database.delete(DBHelper.TABLE_AVTO,DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                        contentValues = new ContentValues();
                        Cursor cursorUPD = database.query(DBHelper.TABLE_AVTO, null, null, null, null, null, null);

                        if (cursorUPD.moveToFirst()) {
                            int idIndex = cursorUPD.getColumnIndex(DBHelper.KEY_ID);
                            int nazvanieIndex = cursorUPD.getColumnIndex(DBHelper.KEY_NAZVANIE);
                            int cenaIndex = cursorUPD.getColumnIndex(DBHelper.KEY_CENA);
                            int realID =1;
                            do{
                                if(cursorUPD.getInt(idIndex)>realID)
                                {
                                    contentValues.put(DBHelper.KEY_ID,realID);
                                    contentValues.put(DBHelper.KEY_NAZVANIE,cursorUPD.getString(nazvanieIndex));
                                    contentValues.put(DBHelper.KEY_CENA,cursorUPD.getString(cenaIndex));
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
        }

        dbHelper.close();
    }
}


