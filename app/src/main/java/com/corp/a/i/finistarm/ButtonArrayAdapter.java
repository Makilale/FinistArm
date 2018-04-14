package com.corp.a.i.finistarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.List;
import com.corp.a.i.finistarm.SettingsActivity;

import java.util.List;
import java.util.zip.Inflater;
/**
 * Created by GordeevMaxim on 14.04.2018.
 */

public class ButtonArrayAdapter extends ArrayAdapter{

    private Context context;
    private List<ButtonNameList> buttons;
    DBHelper dbHelper;
    SQLiteDatabase db;
    EditText Products_Name, Products_Price;
    TableLayout BtList2;
    SettingsActivity ST;
    public int i = 0, m = 0;

    public void setSettingsActivity(SettingsActivity Activity)
    {
        this.ST = Activity;
    }

    public EditText add_btn(int j, int width) {
        EditText ET = new EditText(ST);
        ET.setBackgroundDrawable(ST.getResources().getDrawable(R.drawable.login_edit_text));
        ET.setLayoutParams(new TableRow.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, ST.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, ST.getResources().getDisplayMetrics())));
        ET.setTextSize(24);
        ET.setText("");
        ET.setId(j);
        ET.setBackgroundResource(R.drawable.login_edit_text);
        ET.setPaddingRelative(50,1,1,1);
        return ET;
    }

    public TableRow add_Trow(EditText i, EditText j, int id) {
        TableRow tableRow = new TableRow(ST);
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.setId(id * 100);
        return tableRow;
    }
    public void add_two_btn( int i, int tag) {
        Products_Name = add_btn(i, TableLayout.LayoutParams.WRAP_CONTENT);
        Products_Name.setTag(tag);
        Products_Price = add_btn(i * 10,  0);
        Products_Price.setTag(tag);
    }
    int resource;

    public ButtonArrayAdapter(Context context, int resource, List<ButtonNameList> buttons) {
        super(context, resource, buttons);
        this.resource = resource;
        this.buttons = buttons;
        this.context = context;
    }
    @Override
    public int getCount() {
        return buttons.size();
    }
    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setTableLayout(SettingsActivity st) {
        this.BtList2 = BtList2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button button;
        if (convertView == null) {
            button = new Button(context);
            {
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.main_menu_button_settings));
                button.setLayoutParams(new TableRow.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, context.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics())));
                button.setTextSize(18);
            }
            button.setText(buttons.get(position).getName());
        } else {
            button = (Button) convertView;
        }
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper = new DBHelper(ST);
                BtList2 = new TableLayout(ST);
                // подключаемся к базе
                db = dbHelper.getWritableDatabase();
                String [] columns = new String[] { "isActual", "idCategory", "name", "price"};
                Cursor c = db.query("Products", columns, null, null, null, null, null );
                if(c.moveToFirst()) {
                    do {
                        int m = button.getId();
                        String NameCol = c.getString(c.getColumnIndex("name"));
                        String PriceCol = c.getString(c.getColumnIndex("price"));
                        if(c.getInt(c.getColumnIndex("idCategory")) == (button.getId()+(int)1)) {
                            add_two_btn(++i, i);
                            Products_Name.setText(NameCol);
                            Products_Price.setText(PriceCol);
                            ST.setTableLayout(add_Trow(Products_Name, Products_Price, i));
                        }
                    } while (c.moveToNext());
                }
                dbHelper.close();


                /*ST.SC.setVisibility(View.VISIBLE);
                ST.BtList2.setVisibility(View.VISIBLE);*/
                //BtList2  = (TableLayout)ST.findViewById(R.id.BtList2);
            }
        });

        return button;
    }
}
