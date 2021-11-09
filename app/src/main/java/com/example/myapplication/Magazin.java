package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Magazin extends AppCompatActivity implements View.OnClickListener {

    Button  btnKypit, btnBack;
    TextView textItog;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;
    double p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazin);


        btnKypit = (Button) findViewById(R.id.Kypit);
        btnKypit.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.Back);
        btnBack.setOnClickListener(this);


        textItog = (TextView) findViewById(R.id.Itog);


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


                Button KYPITTOVAR = new Button(this);
                KYPITTOVAR.setOnClickListener(this);
                params.weight = 1.0f;
                KYPITTOVAR.setLayoutParams(params);
                KYPITTOVAR.setText("Buy");
                KYPITTOVAR.setId(cursor.getInt(idIndex));
                tbOUT.addView(KYPITTOVAR);

                tb2.addView(tbOUT);


            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);

        contentValues = new ContentValues();

        switch (v.getId()) {

            case R.id.Back:
                Intent intent =new Intent(this, MainActivity.class);
                startActivity(intent);
                break;



            case R.id.Kypit:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Итоговая сумма заказа в корзине равна = " + textItog.getText(), Toast.LENGTH_SHORT);
                toast.show();
                textItog.setText("0");
                break;


            default:
                Button knopka = (Button) v;
                switch (knopka.getText().toString()){
                    case "Buy":
                        View outBDRow = (View) v.getParent();
                        ViewGroup outBD = (ViewGroup)  outBDRow.getParent();
                        outBD.removeView(outBDRow);
                        outBD.invalidate();

                        String selection = "_id = ?";
                        Cursor c = database.query(DBHelper.TABLE_AVTO, null, selection, new String[]{String.valueOf(v.getId())}, null, null, null);
                        double n = Double.parseDouble(textItog.getText().toString());
                        if (c.moveToFirst()) {
                            int Cena = c.getColumnIndex(DBHelper.KEY_CENA);
                            do {
                                p = c.getFloat(Cena);
                            } while (c.moveToNext());
                        }
                        c.close();
                        n = p + n;
                        textItog.setText("" + n);
                        UpdateTable();
                        break;



                }
        }

        dbHelper.close();
    }
}