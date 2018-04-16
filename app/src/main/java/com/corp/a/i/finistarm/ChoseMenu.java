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
import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChoseMenu extends AppCompatActivity implements View.OnClickListener {

    Button saveChanges;
    Button cancelChanges;
    Button back;

    TextView nameDesk;
    TextView totalPriceOrder;


    FragmentManager manager;
    FragmentTransaction transaction;
    ChoseMenuCategoryFragment choseMenuCategoryFragment;
    ChoseMenuPositonFragment choseMenuPositonFragment;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues contentValues;
    private String idDesk;
    private String idHall;
    private String deskName;
    private boolean isNewAddedDesk;
    String nameUser;
    String idUser;

    TextView nameUserTV;
    TextView exitLogin;
    ArrayList<String> list;

    int notCash = 0;

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public void setNewAddedDesk(boolean newAddedDesk) {
        isNewAddedDesk = newAddedDesk;
    }

    public void setIdHall(String idHall) {
        this.idHall = idHall;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_menu);
        nameDesk = (TextView) findViewById(R.id.nameDesk);
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            deskName = b.getString("nameDesk");
            nameDesk.setText(deskName + " ");
            idHall = b.getString("idHall");
            isNewAddedDesk = b.getBoolean("isNewAddedDesk");
            if(b.containsKey("idDesk"))
                idDesk = b.getString("idDesk");
            nameUser = b.getString("nameUser");
            idUser = b.getString("idUser");
        }
        nameUserTV = (TextView) findViewById(R.id.userNameChoseMenu);
        nameUserTV.setText(nameUser);
        exitLogin = (TextView) findViewById(R.id.exitLoginChoseMenu);
        exitLogin.setOnClickListener(this);
        back = (Button) findViewById(R.id.backToMainMenuDesk);
        saveChanges = (Button) findViewById(R.id.saveChange);
        cancelChanges = (Button) findViewById(R.id.cancelChange);
        saveChanges.setOnClickListener(this);
        cancelChanges.setOnClickListener(this);
        back.setOnClickListener(this);


        totalPriceOrder =(TextView) findViewById(R.id.totalPriceOrder);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        initializeDB();

        choseMenuPositonFragment = new ChoseMenuPositonFragment();
        choseMenuPositonFragment.setNewAddedDesk(isNewAddedDesk);
        choseMenuPositonFragment.setName(nameDesk.getText().toString());
        choseMenuPositonFragment.setIdDesk(idDesk);



        choseMenuCategoryFragment = new ChoseMenuCategoryFragment();
        choseMenuCategoryFragment.setIdCategory("0");
        choseMenuCategoryFragment.setIdChildCategory("");
        choseMenuCategoryFragment.setChoseMenuPositonFragment(choseMenuPositonFragment);
        choseMenuCategoryFragment.setNewAddedDesk(isNewAddedDesk);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragContCategory, choseMenuCategoryFragment, ChoseMenuCategoryFragment.TAG);

        if(!isNewAddedDesk)
        {
            transaction.add(R.id.fragContPosition,choseMenuPositonFragment,ChoseMenuPositonFragment.TAG);
        }
        transaction.commit();




    }

    private void initializeDB() {
       if(isNewAddedDesk)
       {
           cursor = db.query("Desks",new String[]{"MAX(rowid) AS lastIdDesk"},
                   null,null,
                   null,null,null);
           if(cursor.moveToFirst())
               idDesk = Integer.toString(cursor.getInt(cursor.getColumnIndex("lastIdDesk")) + 1);
           else
               idDesk = "1";
       }

       try
       {
           cursor = db.query("DeskProduct join Products ON DeskProduct.idProduct = Products.rowid",
                   new String[]{"SUM(Products.price) AS totalPrice"},
                   "DeskProduct.idDesk = ? AND DeskProduct.isActual=? AND Products.isActual=?",new String[]{idDesk,"1","1"},
                   null,null,null);
       }
       catch (Exception e)
       {
           Log.d("ss", e.getMessage());
       }

        if(cursor.moveToFirst())
        {
            String price = cursor.getString(cursor.getColumnIndex("totalPrice"));
            if(price != null)
                totalPriceOrder.setText("("+ price + "руб.)");
            else
                totalPriceOrder.setText("(0руб.)");
        }
        else
            totalPriceOrder.setText("(0руб.)");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.saveChange:
                ArrayList<ItemChoseMenu> itemChoseMenus =  choseMenuPositonFragment.getNamesPosition();
                ArrayList<ItemChoseMenu> idPosition = choseMenuPositonFragment.getItemDeleted();
                incrementAndDecrementStock(itemChoseMenus,idPosition);
                if(isNewAddedDesk)
                {
                    if(itemChoseMenus!=null)
                        for(ItemChoseMenu itemChoseMenu: itemChoseMenus)
                        {
                            if(!itemChoseMenu.isReport())
                                notCash += Integer.parseInt(itemChoseMenu.getPricePostion());
                            contentValues = new ContentValues();
                            contentValues.put("idDesk", Integer.parseInt(idDesk));
                            contentValues.put("idProduct", Integer.parseInt(itemChoseMenu.getIdProduct()));
                            contentValues.put("isActual", 1);
                            db.insert("DeskProduct", null,contentValues);
                        }
                    contentValues = new ContentValues();
                    contentValues.put("idHall", Integer.parseInt(idHall));
                    contentValues.put("notCash", notCash);
                    contentValues.put("name", deskName);
                    contentValues.put("isActual", 1);
                    // TODO работа с датой и временем
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c);
                    String format  =df1.format(c);
                    contentValues.put("dB", formattedDate + " " + format);
                    contentValues.put("isCheck", 0);
                    db.insert("Desks", null,contentValues);

                }
                else
                {
                    if(!idPosition.isEmpty())
                    {
                        contentValues = new ContentValues();
                        contentValues.put("isActual", 0);
                        for(ItemChoseMenu i: idPosition)
                        {
                            if(i.isReport())
                                notCash-=Integer.parseInt(i.getPricePostion());
                            db.update("DeskProduct", contentValues, "rowid=?", new String[]{i.getIdProduct()});
                        }
                    }

                    for(ItemChoseMenu itemChoseMenu: itemChoseMenus)
                    {
                        if(itemChoseMenu.isNew())
                        {

                            contentValues = new ContentValues();
                            contentValues.put("idDesk", Integer.parseInt(idDesk));
                            contentValues.put("idProduct", Integer.parseInt(itemChoseMenu.getIdProduct()));
                            contentValues.put("isActual", 1);
                            db.insert("DeskProduct", null,contentValues);
                            if(!itemChoseMenu.isReport())
                                notCash+=Integer.parseInt(itemChoseMenu.getPricePostion());
                        }
                    }
                    contentValues = new ContentValues();
                    contentValues.put("notCash", notCash);
                    db.update("Desks", contentValues, "rowid=?", new String[]{idDesk});

                }

                Intent intent2 = new Intent(this,MainActivity.class);
                Bundle b3 = new Bundle();
                b3.putString("nameUser", nameUser);
                b3.putString("idUser", idUser);
                intent2.putExtras(b3);
                startActivity(intent2);
                break;

            case R.id.cancelChange:
                Intent intent = new Intent(this,MainActivity.class);
                Bundle b = new Bundle();
                b.putString("nameUser", nameUser);
                b.putString("idUser", idUser);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.backToMainMenuDesk:
                Intent intent1 = new Intent(this,MainActivity.class);
                Bundle b1 = new Bundle();
                b1.putString("nameUser", nameUser);
                b1.putString("idUser", idUser);
                intent1.putExtras(b1);
                startActivity(intent1);
                break;
            case R.id.exitLoginChoseMenu:
                startActivity(new Intent(this, Login.class));
                break;

        }
    }

    private void incrementAndDecrementStock(ArrayList<ItemChoseMenu> idPos, ArrayList<ItemChoseMenu> itemDel)
    {
        Cursor cursor1;
        if(idPos!=null && itemDel!=null)
        if(!idPos.isEmpty())
        {
            if(isNewAddedDesk)
            {
                for(int i = 0; i<idPos.size();i++)
                {

                    cursor = db.query("Technology",
                            new String[]{"idProductStock", "weight"},
                            "idProduct=? AND isActual = 1",
                            new String[]{idPos.get(i).getIdProduct()},
                            null,
                            null,
                            null);

                    while(cursor.moveToNext()) {
                        String idProductStock = cursor.getString(cursor.getColumnIndex("idProductStock"));
                        int weightTechnology = cursor.getInt(cursor.getColumnIndex("weight"));
                        cursor1 = db.query("Stock", new String[]{"amount"},
                                "rowid=? AND isActual=1", new String[]{idProductStock},
                                null, null, null);
                        cursor1.moveToFirst();
                        int amount = cursor1.getInt(cursor1.getColumnIndex("amount"));
                        if (amount - weightTechnology >= 0)
                        {
                            contentValues = new ContentValues();
                            contentValues.put("amount", amount-weightTechnology);
                            db.update("Stock", contentValues,"rowid=?",new String[]{idProductStock});
                        }
                    }
                }
            }
            else
            {
                if (!itemDel.isEmpty()) {
                    for (int i = 0; i < itemDel.size(); i++) {

                        cursor = db.query("DeskProduct",
                                new String[]{"idProduct"},
                                "rowid=? AND isActual=?",
                                new String[]{itemDel.get(i).getIdProduct(), "1"},
                                null,null,null);
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
                            cursor1 = db.query("Stock", new String[]{"amount"},
                                    "rowid=? AND isActual=1", new String[]{idProductStock},
                                    null, null, null);
                            cursor1.moveToFirst();
                            int amount = cursor1.getInt(cursor1.getColumnIndex("amount"));
                            contentValues = new ContentValues();
                            contentValues.put("amount", amount + weightTechnology);
                            db.update("Stock", contentValues, "rowid=?", new String[]{idProductStock});
                        }
                    }
                }
                else
                    if(!idPos.isEmpty())
                    for(int i = 0; i<idPos.size();i++)
                    {
                        if(idPos.get(i).isNew())
                        {
                            cursor = db.query("Technology",
                                    new String[]{"idProductStock", "weight"},
                                    "idProduct=? AND isActual = 1",
                                    new String[]{idPos.get(i).getIdProduct()},
                                    null,
                                    null,
                                    null);

                        while(cursor.moveToNext()) {
                            String idProductStock = cursor.getString(cursor.getColumnIndex("idProductStock"));
                            int weightTechnology = cursor.getInt(cursor.getColumnIndex("weight"));
                            cursor1 = db.query("Stock", new String[]{"amount"},
                                "rowid=? AND isActual=1", new String[]{idProductStock},
                                    null, null, null);
                            cursor1.moveToFirst();
                            int amount = cursor1.getInt(cursor1.getColumnIndex("amount"));
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

