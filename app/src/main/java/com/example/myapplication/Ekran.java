package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Ekran extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin, btnRegistraciya;
    EditText etLogin, etPassword;
    DBHelper dbHelper;

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekran);

        btnLogin = (Button) findViewById(R.id.Avtorizaciya);
        btnLogin.setOnClickListener(this);

        btnRegistraciya = (Button) findViewById(R.id.Registraciya);
        btnRegistraciya.setOnClickListener(this);

        etLogin = (EditText) findViewById(R.id.Login);
        etPassword = (EditText) findViewById(R.id.Parol);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_LOGIN, "admin");
        contentValues.put(DBHelper.KEY_PAROL, "admin");
        database.insert(DBHelper.TABLE_POLZOVATELI,null, contentValues);
    }

    @Override
    public void onClick(View view) {
        Boolean prow = false;
        switch(view.getId())
        {
            case R.id.Avtorizaciya:
                Cursor cursor = database.query(DBHelper.TABLE_POLZOVATELI, null,null,null,null,null,null);
                Boolean check = false;
                if(cursor.moveToFirst()){
                    int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passIndex = cursor.getColumnIndex(DBHelper.KEY_PAROL);
                    do{ if (etLogin.getText().toString().equals("admin") && etPassword.getText().toString().equals("admin")) {
                        if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPassword.getText().toString().equals(cursor.getString(passIndex))) {
                            startActivity(new Intent(this, MainActivity.class));
                            check = true;
                            break;
                        }
                    } else if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPassword.getText().toString().equals(cursor.getString(passIndex))) {
                        Intent intent =new Intent(this, Magazin.class);
                        startActivity(intent);
                        check = true;
                        break;
                    }


                    }while (cursor.moveToNext());
                }
                cursor.close();

                if (check == false) Toast.makeText(this,"Такой пользователь не зарегестрирован", Toast.LENGTH_LONG).show();
                break;
            case R.id.Registraciya:
                Cursor cursorRegProvLog = database.query(DBHelper.TABLE_POLZOVATELI, null,null,null,null,null,null);

                Boolean checkRegProvLog   = false;

                if(cursorRegProvLog.moveToFirst()){
                    int loginIndex = cursorRegProvLog.getColumnIndex(DBHelper.KEY_LOGIN);

                    do{
                        if (etLogin.getText().toString().equals(cursorRegProvLog.getString(loginIndex))) {
                            Toast.makeText(this,"Введенный логин уже занят", Toast.LENGTH_LONG).show();
                            checkRegProvLog = true;
                            break;
                        }

                    }while (cursorRegProvLog.moveToNext());
                }
                if (checkRegProvLog == false)
                {ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN, etLogin.getText().toString());
                    contentValues.put(DBHelper.KEY_PAROL, etPassword.getText().toString());
                    database.insert(DBHelper.TABLE_POLZOVATELI,null, contentValues);
                    Toast.makeText(this,"Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                }
                cursorRegProvLog.close();

                break;
        }

    }
}