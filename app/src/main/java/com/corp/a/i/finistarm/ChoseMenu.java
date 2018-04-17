package com.corp.a.i.finistarm.Desks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.ItemChoseMenu;
import com.corp.a.i.finistarm.Login;
import com.corp.a.i.finistarm.MainActivity;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.fragment.ChoseMenuCategoryFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChoseMenu extends AppCompatActivity implements View.OnClickListener {

    Cursor cursor;
    ContentValues contentValues;
    private String idTable, idHall, tableName;
    private boolean isNewTable;
    String curLog, idUser;
    TextView currentLogin, exitLogin;
    ArrayList<String> list;
    Button saveAll, cancelAll, back;
    TextView nameTable, totalPrice;
    FragmentManager manager;
    FragmentTransaction transaction;
    ChoseMenuCategoryFragment choseMenuCategoryFragment;
    ChoseMenuPositonFragment choseMenuPositonFragment;
    DBHelper dbHelper;
    SQLiteDatabase db;
    int notCash = 0;
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setNewTable(boolean newTable) {
        isNewTable = newTable;
    }
    public void setIdHall(String idHall) {
        this.idHall = idHall;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_menu);
        nameTable = (TextView) findViewById(R.id.nameDesk);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey("idTable"))
                idTable = bundle.getString("idTable");
            tableName = bundle.getString("nameTable");
            nameTable.setText(tableName + " ");
            idHall = bundle.getString("idHall");
            curLog = bundle.getString("curLog");
            idUser = bundle.getString("idUser");
            isNewTable = bundle.getBoolean("isNewTable");
        }
        currentLogin = (TextView) findViewById(R.id.userNameChoseMenu);
        currentLogin.setText(curLog);
        exitLogin = (TextView) findViewById(R.id.exitLoginChoseMenu);
        exitLogin.setOnClickListener(this);
        back = (Button) findViewById(R.id.backToMainMenuDesk);
        saveAll = (Button) findViewById(R.id.saveChange);
        cancelAll = (Button) findViewById(R.id.cancelChange);
        saveAll.setOnClickListener(this);
        cancelAll.setOnClickListener(this);
        back.setOnClickListener(this);

        totalPrice =(TextView) findViewById(R.id.totalPriceOrder);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        initializeDB();

        choseMenuPositonFragment = new ChoseMenuPositonFragment();
        choseMenuPositonFragment.setNewAddedDesk(isNewTable);
        choseMenuPositonFragment.setName(nameTable.getText().toString());
        choseMenuPositonFragment.setIdDesk(idTable);
        choseMenuCategoryFragment = new ChoseMenuCategoryFragment();
        choseMenuCategoryFragment.setIdCategory("0");
        choseMenuCategoryFragment.setIdChildCategory("");
        choseMenuCategoryFragment.setChoseMenuPositonFragment(choseMenuPositonFragment);
        choseMenuCategoryFragment.setNewAddedDesk(isNewTable);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragContCategory, choseMenuCategoryFragment, ChoseMenuCategoryFragment.TAG);
        if(isNewTable==false)
        {
            transaction.add(R.id.fragContPosition,choseMenuPositonFragment,ChoseMenuPositonFragment.TAG);
        }
        transaction.commit();
    }

    private void initializeDB() {
       if(isNewTable)
       {
           cursor = db.query(
                   "Desks",new String[]{"MAX(rowid) AS lastIdDesk"},
                   null,
                   null,
                   null,
                   null,
                   null);
           if(cursor.moveToFirst())
               idTable = Integer.toString(cursor.getInt(cursor.getColumnIndex("lastIdDesk")) + 1);
           else
               idTable = "1";
       }

       try
       {
           cursor = db.query(
                   "DeskProduct join Products ON DeskProduct.idProduct = Products.rowid",
                   new String[]{"SUM(Products.price) AS totalPrice"},
                   "DeskProduct.idTable = ? AND DeskProduct.isActual=? AND Products.isActual=?",new String[]{idTable,"1","1"},
                   null,
                   null,
                   null);
       }
       catch (Exception e)
       {
           Log.d("ss", e.getMessage());
       }

        if(cursor.moveToFirst())
        {
            String price = cursor.getString(cursor.getColumnIndex("totalPrice"));
            if(price != null)
                totalPrice.setText("("+ price + "руб.)");
            else
                totalPrice.setText("(0руб.)");
        }
        else
            totalPrice.setText("(0руб.)");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.saveChange:
                ArrayList<ItemChoseMenu> itemsFromPosition =  choseMenuPositonFragment.getNamesPosition();
                ArrayList<ItemChoseMenu> delFromPosition = choseMenuPositonFragment.getItemDeleted();
                correctStock(itemsFromPosition,delFromPosition);
                if(isNewTable)
                {
                    if(itemsFromPosition!=null)
                        for(ItemChoseMenu itemChoseMenu: itemsFromPosition)
                        {
                            if(!itemChoseMenu.isReport())
                                notCash += Integer.parseInt(itemChoseMenu.getPricePostion());
                            contentValues = new ContentValues();
                            contentValues.put("idTable", Integer.parseInt(idTable));
                            contentValues.put("idProduct", Integer.parseInt(itemChoseMenu.getIdProduct()));
                            contentValues.put("isActual", 1);
                            db.insert("DeskProduct", null,contentValues);
                        }
                    contentValues = new ContentValues();
                    contentValues.put("idHall", Integer.parseInt(idHall));
                    contentValues.put("notCash", notCash);
                    contentValues.put("name", tableName);
                    contentValues.put("isActual", 1);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c);
                    String format  = df1.format(c);
                    contentValues.put("dB", formattedDate + " " + format);
                    contentValues.put("isCheck", 0);
                    db.insert("Desks", null,contentValues);
                }
                else
                {
                    if(!delFromPosition.isEmpty())
                    {
                        contentValues = new ContentValues();
                        contentValues.put("isActual", 0);
                        for(ItemChoseMenu i: delFromPosition)
                        {
                            if(i.isReport())
                                notCash-=Integer.parseInt(i.getPricePostion());
                            db.update("DeskProduct", contentValues, "rowid=?", new String[]{i.getIdProduct()});
                        }
                    }

                    for(ItemChoseMenu itemFromPos: itemsFromPosition)
                    {
                        if(itemFromPos.isNew())
                        {
                            contentValues = new ContentValues();
                            contentValues.put("idTable", Integer.parseInt(idTable));
                            contentValues.put("idProduct", Integer.parseInt(itemFromPos.getIdProduct()));
                            contentValues.put("isActual", 1);
                            db.insert("DeskProduct",
                                    null,contentValues);
                            if(!itemFromPos.isReport())
                                notCash+=Integer.parseInt(itemFromPos.getPricePostion());
                        }
                    }
                    contentValues = new ContentValues();
                    contentValues.put("notCash", notCash);
                    db.update("Desks", contentValues,
                            "rowid=?", new String[]{idTable});

                }

                Intent intent = new Intent(this,MainActivity.class);
                Bundle b = new Bundle();
                b.putString("curLog", curLog);
                b.putString("idUser", idUser);
                intent.putExtras(b);
                startActivity(intent);
                break;

            case R.id.cancelChange:
                Intent intent1 = new Intent(this,MainActivity.class);
                Bundle b1 = new Bundle();
                b1.putString("curLog", curLog);
                b1.putString("idUser", idUser);
                intent1.putExtras(b1);
                startActivity(intent1);
                break;
            case R.id.backToMainMenuDesk:
                Intent intent2 = new Intent(this,MainActivity.class);
                Bundle b2 = new Bundle();
                b2.putString("curLog", curLog);
                b2.putString("idUser", idUser);
                intent2.putExtras(b2);
                startActivity(intent2);
                break;
            case R.id.exitLoginChoseMenu:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }

    private void correctStock(ArrayList<ItemChoseMenu> itemPosition, ArrayList<ItemChoseMenu> deletedPosition)
    {
        Cursor cursor_sec;
        if(itemPosition!=null && deletedPosition!=null)
        {
            if(isNewTable)
            {
                for(int i = 0; i<itemPosition.size();i++)
                {
                    cursor = db.query("Technology",
                            new String[]{"idProductStock", "weight"},
                            "idProduct=? AND isActual = 1",
                            new String[]{itemPosition.get(i).getIdProduct()},
                            null,
                            null,
                            null);

                    while(cursor.moveToNext()) {
                        String idProductStock = cursor.getString(cursor.getColumnIndex("idProductStock"));
                        int wieghtProd = cursor.getInt(cursor.getColumnIndex("weight"));
                        cursor_sec = db.query("Stock", new String[]{"amount"},
                                "rowid=? AND isActual=1", new String[]{idProductStock},
                                null,
                                null,
                                null);
                        cursor_sec.moveToFirst();
                        int amount = cursor_sec.getInt(cursor_sec.getColumnIndex("amount"));
                        if (amount - wieghtProd >= 0)
                        {
                            contentValues = new ContentValues();
                            contentValues.put("amount", amount-wieghtProd);
                            db.update("Stock", contentValues,"rowid=?",new String[]{idProductStock});
                        }
                    }
                }
            }
            else
            {
                if (!deletedPosition.isEmpty()) {
                    for (int i = 0; i < deletedPosition.size(); i++) {

                        cursor = db.query("DeskProduct",
                                new String[]{"idProduct"},
                                "rowid=? AND isActual=?",
                                new String[]{deletedPosition.get(i).getIdProduct(), "1"},
                                null,
                                null,
                                null);
                        cursor.moveToFirst();
                        String id = cursor.getString(cursor.getColumnIndex("idProduct"));

                        cursor = db.query("Technology",
                                new String[]{"idProductStock", "weight"},
                                "idProduct=? AND isActual = 1",
                                new String[]{id},
                                null,
                                null,
                                null);

                        while (cursor.moveToNext()) {
                            String idProductStock = cursor.getString(cursor.getColumnIndex("idProductStock"));
                            int weightTechnology = cursor.getInt(cursor.getColumnIndex("weight"));
                            cursor_sec = db.query("Stock", new String[]{"amount"},
                                    "rowid=? AND isActual=1", new String[]{idProductStock},
                                    null,
                                    null,
                                    null);
                            cursor_sec.moveToFirst();
                            int amount = cursor_sec.getInt(cursor_sec.getColumnIndex("amount"));
                            contentValues = new ContentValues();
                            contentValues.put("amount", amount + weightTechnology);
                            db.update("Stock", contentValues,
                                    "rowid=?", new String[]{idProductStock});
                        }
                    }
                }
                else
                    if(!itemPosition.isEmpty())
                    for(int i = 0; i<itemPosition.size();i++)
                    {
                        if(itemPosition.get(i).isNew())
                        {
                            cursor = db.query("Technology",
                                    new String[]{"idProductStock", "weight"},
                                    "idProduct=? AND isActual = 1",
                                    new String[]{itemPosition.get(i).getIdProduct()},
                                    null,
                                    null,
                                    null);

                        while(cursor.moveToNext()) {
                            String idProductStock = cursor.getString(cursor.getColumnIndex("idProductStock"));
                            int weightTechnology = cursor.getInt(cursor.getColumnIndex("weight"));
                            cursor_sec = db.query("Stock", new String[]{"amount"},
                                "rowid=? AND isActual=1", new String[]{idProductStock},
                                    null, null, null);
                            cursor_sec.moveToFirst();
                            int amount = cursor_sec.getInt(cursor_sec.getColumnIndex("amount"));
                            if (amount - weightTechnology >= 0)
                            {
                                contentValues = new ContentValues();
                                contentValues.put("amount", amount-weightTechnology);
                                db.update("Stock", contentValues,"rowid=?",new String[]{idProductStock});
                            }
                        }
                    }
                }
            }
        }
     }
}

