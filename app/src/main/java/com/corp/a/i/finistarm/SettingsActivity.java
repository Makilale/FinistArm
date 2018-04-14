package com.corp.a.i.finistarm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.String;

import static java.sql.Types.NULL;

/**
 * Created by GordeevMaxim on 22.03.2018.
 */

public class SettingsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button button_settings_back;
    Button button_halls, button_accounts;
    Button add_hall, add_account;
    Button btnNew, btnNewDelete;
    TextView TW;
    //String lg = "", ps = "", nm = "";
    public Button btDeletePrev = null;
    public Button btAccPrev = null;
    TableLayout BtList2;
    RelativeLayout VP;
    public int i = 0, m = 0;
    public Boolean flag = true;

    public Button add_btn(int j, View view, int width) {
        Button btn = new Button(BtList2.getContext());
        btn.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
        btn.setLayoutParams(new TableRow.LayoutParams(width, width));
        btn.setTextSize(18);
        btn.setText("");
        btn.setId(j);
        return btn;
    }

    public TableRow add_Trow(Button i, Button j, int id) {
        TableRow tableRow = new TableRow(BtList2.getContext());
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.setId(id * 100);
        return tableRow;
    }

    public void add_clickListener_halls() {
        if (i > 1) {
            btDeletePrev = (Button) findViewById(i * 10);
            //int m = btDeletePrev.getId();
            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btDeletePrev.setVisibility(Button.GONE);
                    int m = BtList2.getChildCount();
                    BtList2.removeViews(m - 1, 1);
                    //удлить из бд
                    db = dbHelper.getWritableDatabase();
                    db.delete("Halls","isActual = " + i, null);
                    db.close();
                    if (i-- > 2) {
                        btDeletePrev = (Button) findViewById(i * 10);
                        btDeletePrev.setVisibility(Button.VISIBLE);
                    }
                }
            });
        }
    }
    public void add_clickListener_accuants() {
        if (i > 1) {
            btDeletePrev = (Button) findViewById(i * 10);
            //int m = btDeletePrev.getId();
            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //определям id строки, которую будем удалть
                    int m = view.getId();
                    m = m*10;
                    //находим эту строку
                    TableRow row = (TableRow)findViewById(m);
                    //m = (int)btDeletePrev.getTag();
                    //удаляем эту строку
                    BtList2.removeView(row);
                    //удлить из бд
                    db = dbHelper.getWritableDatabase();
                    db.delete("Login","isActual = " + m/100, null);
                    db.close();
                }
            });
            btAccPrev = (Button) findViewById(i);
            btAccPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m = view.getId();
                    db = dbHelper.getWritableDatabase();
                    String [] columns = new String[] { "isActual", "login", "pass", "idGroup", "name" };
                    Cursor c = db.query("Login", columns, null, null, null, null, null );
                    c.moveToFirst();
                    do {
                        i = c.getInt(c.getColumnIndex("isActual"));
                        if(i==m) {
                            ((TextView) findViewById(R.id.etLogin)).setText(c.getString(c.getColumnIndex("login")));
                            ((TextView) findViewById(R.id.etName)).setText(c.getString(c.getColumnIndex("pass")));
                            ((TextView) findViewById(R.id.etPass)).setText(c.getString(c.getColumnIndex("name")));
                            ((TextView) findViewById(R.id.etLevel)).setText(""+c.getInt(c.getColumnIndex("idGroup")));
                        }
                    } while (c.moveToNext());
                    dbHelper.close();
                    VP.setVisibility(View.VISIBLE);
                    flag = false;
                }
            });
        }
    }

    public void add_two_btn(View view, int i, int tag) {
        btnNew = add_btn(i, view, LayoutParams.WRAP_CONTENT);
        btnNew.setTag(tag);
        btnNewDelete = add_btn(i * 10, view, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
        btnNewDelete.setTag(tag);
        BtList2.addView(add_Trow(btnNew, btnNewDelete, i));
    }

    public void onClick_AddAccuant(View view) {
        VP.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.etLogin)).setText("");
        ((TextView) findViewById(R.id.etName)).setText("");
        ((TextView) findViewById(R.id.etPass)).setText("");
        ((TextView) findViewById(R.id.etLevel)).setText("");
        flag = true;
    }

    public void onClick_CancelAccuant(View view) {
        VP.setVisibility(View.GONE);
    }

    public void onClick_SaveAccuant(View view) {
        VP.setVisibility(View.GONE);
        String lg = (((TextView) findViewById(R.id.etLogin)).getText().toString());
        String nm = (((TextView) findViewById(R.id.etName)).getText().toString());
        String ps = (((TextView) findViewById(R.id.etPass)).getText().toString());
        int lv = Integer.parseInt(((TextView) findViewById(R.id.etLevel)).getText().toString());
        insert_account(view, lg, nm, ps, lv, flag);
    }
    public  void insert_account(View view, String lg, String nm, String ps,  int lv, boolean flag)
    {
        if(flag) {
            add_two_btn(view, ++i, i);
            btnNew.setText(lg);
            add_clickListener_accuants();
            //вставка в бд
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("isActual", i);
            cv.put("login", lg);
            cv.put("name", nm);
            cv.put("pass", ps);
            cv.put("idGroup", lv);
            db.insert("Login", null, cv);
            db.close();
        }
        else {
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("isActual", m);
            cv.put("login", lg);
            cv.put("name", nm);
            cv.put("pass", ps);
            cv.put("idGroup", lv);
            btAccPrev = (Button)findViewById(m);
            btAccPrev.setText(lg);
            //m = (int)btAccPrev.getTag();
            db.update("Login", cv,"isActual = " + m, null);
            db.close();
        }
    }
    public void initialization_Halls(View view)
    {
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "isActual", "name" };
        Cursor c = db.query("Halls", columns, null, null, null, null, null );
        c.moveToFirst();
        do {
            String NameCol = c.getString(c.getColumnIndex("name"));
            add_two_btn(view, ++i, i);
            btnNew.setText(NameCol);
            add_clickListener_halls();
            if (i!=c.getCount() || i == 1) btnNewDelete.setVisibility(Button.INVISIBLE);
        } while (c.moveToNext());
        dbHelper.close();
    }
    public void onClick_Halls(View view) {
        btnNew = (Button) findViewById(R.id.button_halls);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        btnNew = (Button) findViewById(R.id.button_accounts);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_stock);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_categories);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        BtList2.setVisibility(TableLayout.VISIBLE);
        VP.setVisibility(View.GONE);
        int m = BtList2.getChildCount();
        if (m > 0) {
            BtList2.removeViews(0, m);
            i = 0;
        }
        add_account.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.VISIBLE);
        add_hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > 1) {
                    btDeletePrev = (Button) findViewById(i * 10);
                    btDeletePrev.setVisibility(Button.INVISIBLE);
                }
                add_two_btn(view, ++i, i);
                btnNew.setText("Зал №" + i);
                add_clickListener_halls();
                //вставка в бд
                db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("isActual", i);
                cv.put("name", "Зал №" + i);
                db.insert("Halls",  null, cv);
                db.close();
            }
        });
        TW.setText(view.getContext().getResources().getText(R.string.settings_halls));
        TW.setVisibility(TextView.VISIBLE);
        initialization_Halls(view);
    }

    public void initialization_Accounts(View view)
    {
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "isActual", "login" };
        Cursor c = db.query("Login", columns, null, null, null, null, null );
          c.moveToFirst();
        do {
            int m = c.getCount();
            i = c.getInt(c.getColumnIndex("isActual"));
            String NameCol = c.getString(c.getColumnIndex("login"));
            add_two_btn(view, i, i);
            btnNew.setText(NameCol);
            add_clickListener_accuants();
            if (i == 1) btnNewDelete.setVisibility(Button.INVISIBLE);
        } while (c.moveToNext());
        dbHelper.close();
    }

    public void onClick_Accounts(View view) {
        btnNew = (Button) findViewById(R.id.button_halls);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_accounts);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        btnNew = (Button) findViewById(R.id.button_stock);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_categories);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        BtList2.setVisibility(TableLayout.VISIBLE);
        VP.setVisibility(View.GONE);
        add_hall.setVisibility(Button.GONE);
        add_account.setVisibility(Button.VISIBLE);
        int m = BtList2.getChildCount();
        //очищаем таблицу
        if (m > 0) {
            BtList2.removeViews(0, m);
            i = 1;
        }
        initialization_Accounts(view);
        TW.setText(view.getContext().getResources().getText(R.string.settings_acounts));
        TW.setVisibility(TextView.VISIBLE);
    }
    public void onClick_Stock(View view) {
        btnNew = (Button) findViewById(R.id.button_halls);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_accounts);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_stock);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        btnNew = (Button) findViewById(R.id.button_categories);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        BtList2.setVisibility(TableLayout.GONE);
        add_account.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.GONE);
        VP.setVisibility(View.GONE);
        TW.setText(view.getContext().getResources().getText(R.string.settings_halls));
        TW.setVisibility(TextView.VISIBLE);
    }
    public void onClick_Categories(View view) {
        btnNew = (Button) findViewById(R.id.button_halls);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_accounts);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_stock);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        btnNew = (Button) findViewById(R.id.button_categories);
        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        BtList2.setVisibility(TableLayout.GONE);
        add_account.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.GONE);
        VP.setVisibility(View.GONE);
        TW.setText(view.getContext().getResources().getText(R.string.settings_halls));
        TW.setVisibility(TextView.VISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        button_accounts = (Button) findViewById(R.id.button_accounts);
        button_halls = (Button) findViewById(R.id.button_halls);
        add_hall = (Button) findViewById(R.id.add_hall);
        add_account = (Button) findViewById(R.id.add_account);
        add_hall.setVisibility(Button.GONE);
        add_account.setVisibility(Button.GONE);
        button_settings_back = (Button) findViewById(R.id.settings_back);
        View view = (View) findViewById(R.id.view);
        TW = (TextView) findViewById(R.id.textView);
        TW.setVisibility(TextView.GONE);
        BtList2 = (TableLayout) findViewById(R.id.BtList2);
        BtList2.setVisibility(TableLayout.GONE);
        VP = (RelativeLayout) findViewById(R.id.VP);
        VP.setVisibility(ViewPager.GONE);

        button_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                add_hall.setVisibility(Button.GONE);
                add_account.setVisibility(Button.GONE);
                BtList2.setVisibility(TableLayout.GONE);
                TW.setVisibility(TextView.GONE);
                VP.setVisibility(ViewPager.GONE);
                startActivity(intent);
            }
        });
    }

}
