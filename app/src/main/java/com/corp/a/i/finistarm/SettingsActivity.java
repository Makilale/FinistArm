package com.corp.a.i.finistarm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import static java.sql.Types.NULL;
import static java.sql.Types.NUMERIC;

/**
 * Created by GordeevMaxim on 22.03.2018.
 */

public class SettingsActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button button_settings_back, categories_back, stock_back;
    Button button_halls, button_accounts, button_stock, button_categories;
    Button add_hall, add_account, add_product;
    Button btnNew;
    ImageButton btnNewDelete;
    EditText Products_Name, Products_Price;
    TextView TW;
    String [] HeaderGoods =  new String[] { "Название товара", "Цена", "Удалить" };
    String [] HeaderStock =  new String[] { "Наименование позиции", "Кол-во", "Ед. изм.","Удалить" };
    Spinner sp;
    public ImageButton btDeletePrev = null;
    public Button btAccPrev = null;
    public ScrollView SC;
    public TableLayout BtList2;
    RelativeLayout VP;
    GridView GV;
    public int i = 0, m = 0, parentid = 0, size;
    public Boolean FlagUpdateAcc = true, FlagUpadeteProd = false, Flag = true;

    public Button add_btn_control(int j) {
        Button btn = new Button(BtList2.getContext());
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
        btn.setLayoutParams(new TableRow.LayoutParams(-2, size));
        btn.setTextSize(18);
        btn.setText("");
        btn.setId(j);
        return btn;
    }

    public ImageButton add_btn_delete(int j) {
        ImageButton btn = new ImageButton(BtList2.getContext());
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_butten_delete_settings));
        btn.setLayoutParams(new TableRow.LayoutParams(size, size));
        btn.setId(j);
        return btn;
    }
    public TableRow add_Trow(Button i, ImageButton j, int id) {
        TableRow tableRow = new TableRow(BtList2.getContext());
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.setId(id * 100);
        return tableRow;
    }

    public void add_clickListener_halls() {
        if (i > 1) {
            btDeletePrev = (ImageButton) findViewById(i * 10);
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
                        btDeletePrev = (ImageButton) findViewById(i * 10);
                        btDeletePrev.setVisibility(Button.VISIBLE);
                    }
                }
            });
        }
    }
    public void add_clickListener_accuants() {
        if (i > 1) {
            btDeletePrev = (ImageButton) findViewById(i * 10);
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
                            ((Spinner) findViewById(R.id.etLevel)).setSelection(c.getInt(c.getColumnIndex("idGroup"))-1);
                        }
                    } while (c.moveToNext());
                    dbHelper.close();
                    VP.setVisibility(View.VISIBLE);
                    FlagUpdateAcc = false;
                }
            });
        }
    }

    public void add_two_btn(View view, int i, int tag) {
        btnNew = add_btn_control(i);
        btnNew.setTag(tag);
        btnNewDelete = add_btn_delete(i * 10);
        btnNewDelete.setTag(tag);
        BtList2.addView(add_Trow(btnNew, btnNewDelete, i));
    }

    public void onClick_AddAccuant(View view) {
        VP.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.etLogin)).setText("");
        ((TextView) findViewById(R.id.etName)).setText("");
        ((TextView) findViewById(R.id.etPass)).setText("");
        FlagUpdateAcc = true;
    }

    public void onClick_CancelAccuant(View view) {
        VP.setVisibility(View.GONE);
    }

    public void onClick_SaveAccuant(View view) {
        VP.setVisibility(View.GONE);
        String lg = (((TextView) findViewById(R.id.etLogin)).getText().toString());
        String nm = (((TextView) findViewById(R.id.etName)).getText().toString());
        String ps = (((TextView) findViewById(R.id.etPass)).getText().toString());
        long lv = (((Spinner) findViewById(R.id.etLevel)).getSelectedItemId())+1;
        insert_account(view, lg, nm, ps, (int)lv, FlagUpdateAcc);
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
    public void Inicialization_Halls(View view)
    {
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "rowid as id","isActual", "name" };
        Cursor c = db.query("Halls", columns, null, null, null, null, null );
        if(c.moveToFirst()) {
            do {
                int n = c.getInt(c.getColumnIndex("id"));
                String NameCol = c.getString(c.getColumnIndex("name"));
                add_two_btn(view, ++i, i);
                btnNew.setText(NameCol);
                add_clickListener_halls();
                if (i != c.getCount() || i == 1) btnNewDelete.setVisibility(Button.INVISIBLE);
            } while (c.moveToNext());
            dbHelper.close();
        }
    }

    public void onClick_AddHall(View view) {
        if (i > 1) {
            btDeletePrev = (ImageButton) findViewById(i * 10);
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

    public void onClick_Halls(View view) {
        button_halls.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        button_accounts.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_stock.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_categories.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        SC.setVisibility(ScrollView.VISIBLE);
        BtList2.setVisibility(TableLayout.VISIBLE);
        GV.setVisibility(GridView.GONE);
        VP.setVisibility(View.GONE);
        add_product.setVisibility(Button.GONE);
        add_account.setVisibility(Button.GONE);
        categories_back.setVisibility(Button.GONE);
        stock_back.setVisibility(Button.GONE);
        if (FlagUpadeteProd) {
            update_Products();
            FlagUpadeteProd = false;
        }
        int m = BtList2.getChildCount();
        if (m > 0) {
            BtList2.removeViews(0, m);
            i = 0;
        }
        add_hall.setVisibility(Button.VISIBLE);
        TW.setText(view.getContext().getResources().getText(R.string.settings_halls));
        TW.setVisibility(TextView.VISIBLE);
        Inicialization_Halls(view);
    }

    public void Inicialization_Accounts(View view)
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
        ((Spinner) findViewById(R.id.etLevel)).setAdapter(Create_Adapter_Spinner("GroupLogin"));
    }

    public void onClick_Accounts(View view) {
        button_halls.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_accounts.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        button_stock.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_categories.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        SC.setVisibility(ScrollView.VISIBLE);
        BtList2.setVisibility(TableLayout.VISIBLE);
        GV.setVisibility(GridView.GONE);
        VP.setVisibility(View.GONE);
        add_hall.setVisibility(Button.GONE);
        add_product.setVisibility(Button.GONE);
        categories_back.setVisibility(Button.GONE);
        stock_back.setVisibility(Button.GONE);
        add_account.setVisibility(Button.VISIBLE);
        if (FlagUpadeteProd) {
            update_Products();
            FlagUpadeteProd = false;
        }
        int m = BtList2.getChildCount();
        //очищаем таблицу
        if (m > 0) {
            BtList2.removeViews(0, m);
            i = 1;
        }
        Inicialization_Accounts(view);
        TW.setText(view.getContext().getResources().getText(R.string.settings_acounts));
        TW.setVisibility(TextView.VISIBLE);

    }

    public TableRow Inicialization_Healder( String [] columns)
    {
        TableRow tableRow = new TableRow(this);
        TextView tx;
        for( int i=0; i< columns.length; i++) {
            tx = new TextView(this);
            tx.setText(columns[i]);
            tx.setTextSize(18);
            tx.setGravity(0);
            tx.setTextColor(Color.BLACK);
            tableRow.addView(tx);
            if (columns[i] == "Удалить") {
                TableRow.LayoutParams params = (TableRow.LayoutParams) tx.getLayoutParams();
                params.span = 2;
                tx.setLayoutParams(params);
            }
        }
        return  tableRow;
    }
    public void Inicialization_Categories(String tablename)
    {
        final List<ButtonNameList> products = new ArrayList<ButtonNameList>();
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "isActual", "idParent", "name" };
        Cursor c = db.query(tablename, columns, null, null, null, null, null );
        if(c.moveToFirst()) {
            do {
                int m = c.getCount();
                products.add(new ButtonNameList(c.getString(c.getColumnIndex("name")), c.getInt(c.getColumnIndex("isActual"))));
            } while (c.moveToNext());
        }
        dbHelper.close();
        ButtonArrayAdapter BTA = new ButtonArrayAdapter(this, 10, products);
        BTA.setSettingsActivity(this);
        GV.setAdapter(BTA);
        int m = BtList2.getChildCount();
        if (m > 0) {
            BtList2.removeViews(0, m);
        }
        if(tablename == "StockCategory") {
            BtList2.addView(Inicialization_Healder(HeaderStock));
        } else {
            BtList2.addView(Inicialization_Healder(HeaderGoods));
        }
    }
    public void Inicialization_Stock(int id) {
        SC.setVisibility(ScrollView.VISIBLE);
        BtList2.setVisibility(TableLayout.VISIBLE);
        GV.setVisibility(GridView.GONE);
        TW.setText(getResources().getText(R.string.settings_stock_p2));
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"isActual", "idCategory", "name", "amount", "idValue"};
        Cursor c = db.query("Stock", columns, "idCategory = " + id, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String NameCol = c.getString(c.getColumnIndex("name"));
                String AmountCol = c.getString(c.getColumnIndex("amount"));
                int value  = c.getInt(c.getColumnIndex("idValue"));
                i = c.getInt(c.getColumnIndex("isActual"));
                if (c.getInt(c.getColumnIndex("idCategory")) == id) {
                    add_two_et(i, i);
                    Products_Name.setText(NameCol);
                    Products_Price.setText(AmountCol);
                    sp.setSelection(value-1);
                    add_clickListener_products();
                }
            } while (c.moveToNext());
        }
        dbHelper.close();
        parentid = id;
        add_product.setVisibility(Button.VISIBLE);
        FlagUpadeteProd = true;
    }
    public void onClick_Stock(View view) {
        button_halls.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_accounts.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_stock.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        button_categories.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
        BtList2.setVisibility(TableLayout.GONE);
        add_account.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.GONE);
        add_product.setVisibility(Button.GONE);
        categories_back.setVisibility(Button.GONE);
        stock_back.setVisibility(Button.GONE);
        VP.setVisibility(View.GONE);
        TW.setText(view.getContext().getResources().getText(R.string.settings_stock_p1));
        TW.setVisibility(TextView.VISIBLE);
        GV.setVisibility(GridView.VISIBLE);
        if (FlagUpadeteProd) {
            update_Products();
            FlagUpadeteProd = false;
        }
        Flag = false;
        Inicialization_Categories("StockCategory");
        FlagUpadeteProd = true;
    }

    public EditText add_et() {
        EditText ET = new EditText(this);
        ET.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_edit_text));
        int hight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        ET.setLayoutParams(new TableRow.LayoutParams( hight ,width));
        ET.setTextSize(24);
        ET.setText("");
        ET.setBackgroundResource(R.drawable.login_edit_text);
        ET.setPaddingRelative(50,1,1,1);
        return ET;
    }

    public TableRow add_Trow(EditText i, EditText j, ImageButton ib, int id) {
        TableRow tableRow = new TableRow(this);
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.addView(ib);
        TextView tx = new TextView(this);
        tx.setText("     ");
        tx.setTextSize(18);
        tableRow.addView(tx);
        tableRow.setId(id * 10);
        return tableRow;
    }

    public TableRow add_Trow(EditText i, EditText j, Spinner sp,ImageButton ib, int id) {
        TableRow tableRow = new TableRow(this);
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.addView(sp);
        tableRow.addView(ib);
        TextView tx = new TextView(this);
        tx.setText("     ");
        tx.setTextSize(18);
        tableRow.addView(tx);
        tableRow.setId(id * 10);
        return tableRow;
    }
    public void add_two_et( int i, int tag) {
        Products_Name = add_et();
        Products_Name.setTag(tag);
        Products_Price = add_et();
        Products_Price.setTag(tag);
        Products_Price.setInputType(NUMERIC);
        btnNewDelete = add_btn_delete(i);
        if(Flag) {
            Products_Name.setText("Товар");
            Products_Price.setText("0");
            BtList2.addView(add_Trow(Products_Name, Products_Price, btnNewDelete, i));
        }
        else{
            Products_Name.setText("Позиция");
            Products_Price.setText("0");
            sp = addSpinner();
            BtList2.addView(add_Trow(Products_Name, Products_Price, sp ,btnNewDelete, i));
        }
    }

    public  void add_clickListener_products() {
        btDeletePrev = (ImageButton) findViewById(i);
        btDeletePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //определям id строки, которую будем удалть
                int m = view.getId();
                m = m * 10;
                //находим эту строку
                TableRow row = (TableRow) findViewById(m);
                //m = (int)btDeletePrev.getTag();
                //удаляем эту строку
                BtList2.removeView(row);
                //удлить из бд
                m = m/10;
                db = dbHelper.getWritableDatabase();
                if(Flag) {
                    db.delete("Products", "isActual = " + m + " AND idCategory = " + parentid, null);
                } else
                {
                    db.delete("Stock", "isActual = " + m + " AND idCategory = " + parentid, null);
                }
                db.close();
            }
        });
    }
    public void onClick_AddProduct(View view) {
        add_two_et(++i, i);
        add_clickListener_products();
        //вставка в бд
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("isActual", i);
        cv.put("name", Products_Name.getText().toString());
        cv.put("idCategory", parentid);
        if (Flag) {
            cv.put("price", Products_Price.getText().toString());
            db.insert("Products", null, cv);
        } else
        {
            cv.put("amount", Products_Price.getText().toString());
            cv.put("idValue", sp.getSelectedItemId()+1);
            db.insert("Stock",  null, cv);
        }
        db.close();
    }

    public void update_Products() {
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int j=1; j < BtList2.getChildCount(); j++) {
            TableRow tb = (TableRow) BtList2.getChildAt(j);
            EditText ET1 = (EditText) tb.getChildAt(0);
            EditText ET2 = (EditText) tb.getChildAt(1);
            int m = tb.getId();
            m = m /10;
            cv.put("name", ET1.getText().toString());
            cv.put("idCategory", parentid);
            if(Flag) {
                cv.put("price", ET2.getText().toString());
                db.update("Products", cv, "isActual = " + m + " AND idCategory = " + parentid, null);
            } else {
                Spinner sp = (Spinner) tb.getChildAt(2);
                cv.put("idValue", sp.getSelectedItemId() + 1);
                cv.put("amount", ET2.getText().toString());
                db.update("Stock", cv, "isActual = " + m + " AND idCategory = " + parentid, null);
            }
        }
        db.close();
        dbHelper.close();
    }

    public String[] Create_List_Spinner(String TableName)
    {
        String[] data = new String[3];
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"rowid as id", "name"};
        Cursor c = db.query(TableName, columns, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                data[c.getInt(c.getColumnIndex("id"))-1] = c.getString(c.getColumnIndex("name"));
            } while (c.moveToNext());
        }
        dbHelper.close();
        return data;
    }

    public  ArrayAdapter<String> Create_Adapter_Spinner(String str)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Create_List_Spinner(str));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        return  adapter;
    }

    public Spinner addSpinner()
    {
        Spinner spinner = new Spinner(this);
        spinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner));
        int hight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
        int width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        spinner.setLayoutParams(new TableRow.LayoutParams( hight ,width));
        spinner.setAdapter(Create_Adapter_Spinner("Value"));
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/
        return  spinner;
    }

    public void Inicialization(int id) {
        if(Flag) {
            Inicialization_Goods(id);
            stock_back.setVisibility(Button.GONE);
            categories_back.setVisibility(Button.VISIBLE);
        }
        else {
            Inicialization_Stock(id);
            categories_back.setVisibility(Button.GONE);
            stock_back.setVisibility(Button.VISIBLE);
        }
    }

    public void Inicialization_Goods(int id) {
        SC.setVisibility(ScrollView.VISIBLE);
        BtList2.setVisibility(TableLayout.VISIBLE);
        GV.setVisibility(GridView.GONE);
        TW.setText(getResources().getText(R.string.settings_categories_goods_p2));
        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"isActual", "idCategory", "name", "price"};
        Cursor c = db.query("Products", columns, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String NameCol = c.getString(c.getColumnIndex("name"));
                String PriceCol = c.getString(c.getColumnIndex("price"));
                i = c.getInt(c.getColumnIndex("isActual"));
                if (c.getInt(c.getColumnIndex("idCategory")) == id) {
                    add_two_et(i, i);
                    Products_Name.setText(NameCol);
                    Products_Price.setText(PriceCol);
                    add_clickListener_products();
                }
            } while (c.moveToNext());
        }
        dbHelper.close();
        parentid = id;
        add_product.setVisibility(Button.VISIBLE);
        FlagUpadeteProd = true;
    }

    public void onClick_Categories(View view) {
        button_halls.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_accounts.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_stock.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_button_settings));
        button_categories.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_menu_button_settings_cliked));
        BtList2.setVisibility(TableLayout.GONE);
        add_account.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.GONE);
        add_product.setVisibility(Button.GONE);
        stock_back.setVisibility(Button.GONE);
        categories_back.setVisibility(Button.GONE);
        VP.setVisibility(View.GONE);
        TW.setText(getResources().getText(R.string.settings_categories_goods_p1));
        TW.setVisibility(TextView.VISIBLE);
        GV.setVisibility(GridView.VISIBLE);


        if (FlagUpadeteProd) {
            update_Products();
            FlagUpadeteProd = false;
        }
        Flag = true;
        Inicialization_Categories("Category");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        button_accounts = (Button) findViewById(R.id.button_accounts);
        button_halls = (Button) findViewById(R.id.button_halls);
        button_stock = (Button) findViewById(R.id.button_stock);
        button_categories = (Button) findViewById(R.id.button_categories);
        add_hall = (Button) findViewById(R.id.add_hall);
        add_account = (Button) findViewById(R.id.add_account);
        add_product = (Button) findViewById(R.id.add_product);
        add_product.setVisibility(Button.GONE);
        add_hall.setVisibility(Button.GONE);
        add_account.setVisibility(Button.GONE);
        button_settings_back = (Button) findViewById(R.id.settings_back);
        categories_back = (Button) findViewById(R.id.categories_back);
        stock_back= (Button) findViewById(R.id.stock_back);
        stock_back.setVisibility(Button.GONE);
        categories_back.setVisibility(Button.GONE);
        View view = (View) findViewById(R.id.view);
        TW = (TextView) findViewById(R.id.textView);
        TW.setVisibility(TextView.GONE);
        BtList2 = (TableLayout) findViewById(R.id.BtList2);
        SC = (ScrollView) findViewById(R.id.scroll);
        SC.setVisibility(ScrollView.GONE);
        VP = (RelativeLayout) findViewById(R.id.VP);
        VP.setVisibility(ViewPager.GONE);
        GV = (GridView) findViewById(R.id.gv);
        GV.setVisibility(GridView.GONE);
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        button_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FlagUpadeteProd) {
                    update_Products();
                    FlagUpadeteProd = false;
                }
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                categories_back.setVisibility(Button.GONE);
                stock_back.setVisibility(Button.GONE);
                add_hall.setVisibility(Button.GONE);
                add_account.setVisibility(Button.GONE);
                add_product.setVisibility(Button.GONE);
                BtList2.setVisibility(TableLayout.GONE);
                TW.setVisibility(TextView.GONE);
                VP.setVisibility(ViewPager.GONE);
                GV.setVisibility(GridView.GONE);
                button_halls.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
                button_accounts.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
                button_stock.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
                button_categories.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_button_settings));
                startActivity(intent);
            }
        });
    }

}
