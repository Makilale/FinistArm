package com.corp.a.i.finistarm.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;
import com.corp.a.i.finistarm.ItemButtonMainDesks;
import com.corp.a.i.finistarm.ItemChoseMenu;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.adapters.TransferPositionAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransferPositionDialogFragment extends DialogFragment implements View.OnClickListener {
    public final static String TAG = "transfer";

    Button addDesk;
    Button acceptTransfer;
    Button cancelTransfer;
    TableLayout posTL;
    GridView deskGV;
    TransferPositionAdapter adapter;
    ArrayList<ItemButtonMainDesks> names;
    EditText numberDesk;
    ArrayList<ItemChoseMenu> namesPosition;
    ArrayList<String> list;

    String nameDesk;
    String idDesk;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues contentValues;

    FragmentManager manager;
    FragmentTransaction transaction;

    String idHall;


    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setNameDesk(String nameDesk) {
        this.nameDesk = nameDesk;
    }

    public void setIdDesk(String idDesk) {
        this.idDesk = idDesk;
    }

    public void setNamesPosition(ArrayList<ItemChoseMenu> namesPosition) {
        this.namesPosition = namesPosition;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.transfer_position_with_new_desk,null);
        addDesk = (Button) view.findViewById(R.id.addDeskDF);
        acceptTransfer = (Button) view.findViewById(R.id.acceptTransfer);
        cancelTransfer = (Button) view.findViewById(R.id.cancelTransfer);

        addDesk.setOnClickListener(this);
        acceptTransfer.setOnClickListener(this);
        cancelTransfer.setOnClickListener(this);
        posTL = (TableLayout) view.findViewById(R.id.tableDF);
        deskGV = (GridView) view.findViewById(R.id.deskGVDF);
        numberDesk = (EditText) view.findViewById(R.id.nameDeskDF);
        names = new ArrayList<ItemButtonMainDesks>();
        list = new ArrayList<String>();
        dbHelper = new DBHelper(getContext());
        db =  dbHelper.getWritableDatabase();
        initializeDB();

        adapter = new TransferPositionAdapter(getContext(),R.layout.layout_btn_add,names);
        deskGV.setAdapter(adapter);
        initializeTableLayout();

        return view;
    }

    private void initializeDB() {

        /*AND isCheck=0*/
        cursor = db.query("Desks",
                new String[]{"rowid AS ID", "name"},
                "isActual=1 AND rowid<>? AND  isCheck=0", new String[]{idDesk},
                null,null,null);
        String numDesk;
        while(cursor.moveToNext())
        {
                names.add(new ItemButtonMainDesks(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("ID")),
                        0, false,false));
        }

        cursor = db.query("Desks",
                new String[]{"name"},
                "isActual=1", null,
                null,null,null);
        while(cursor.moveToNext())
             list.add(cursor.getString(cursor.getColumnIndex("name")).split(" №")[1]);

        cursor = db.query("Desks",
                new String[]{"idHall"},
                "isActual=1", null,
                null,null,null);
        cursor.moveToFirst();
        idHall = cursor.getString(cursor.getColumnIndex("idHall"));


    }

    private void initializeTableLayout()
    {
        for(int i =0; i<namesPosition.size(); i++)
            createRow(namesPosition.get(i).getNamePosition(),namesPosition.get(i).getPricePostion());

    }

    private void createRow(String name,String price)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_chose_menu_lv, null);
        TableRow tableRow = (TableRow) view.findViewById(R.id.itemTR);
        TextView textView = (TextView)view.findViewById(R.id.namePosition);
        textView.setText(name);
        TextView textView1 = (TextView)view.findViewById(R.id.pricePostion);
        textView1.setText(price);
        TextView textView2 = (TextView) view.findViewById(R.id.textView30);
        posTL.addView(tableRow);

        view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_row_delimetr, null);
        TableRow delimetr = (TableRow) view.findViewById(R.id.delimetrTableRow);
        posTL.addView(delimetr);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.acceptTransfer:
                int pos = -1;

                for(int i = 0; i<names.size();i++)
                {
                    if(names.get(i).isChecked())
                        pos = i;
                }
                if(pos == -1)
                {
                    Toast.makeText(getContext(),"Выберите или создайте стол", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    contentValues = new ContentValues();
                    contentValues.put("idTable", names.get(pos).getId());
                    for(int i = 0,j = 0; i<posTL.getChildCount(); i=i+2, j++)
                    {
                        db.update("DeskProduct", contentValues,
                                    "rowid=?", new String[]{namesPosition.get(j).getIdProduct()});
                    }

                    transaction = manager.beginTransaction();
                    ChoseMenuPositonFragment choseMenuPositonFragment = new ChoseMenuPositonFragment();
                    choseMenuPositonFragment.setNewTable(false);
                    choseMenuPositonFragment.setIdTable(idDesk);
                    choseMenuPositonFragment.setName(nameDesk);
                    transaction.replace(R.id.fragContPosition,choseMenuPositonFragment,ChoseMenuPositonFragment.TAG);
                    transaction.commit();
                    dismiss();
                }

                break;
            case R.id.cancelTransfer:
                dismiss();
                break;
            case R.id.addDeskDF:
                if(numberDesk.getText().toString().equals(""))
                    numberDesk.setError("Введите название стола");
                else
                {
                    if(list.contains(numberDesk.getText().toString()))
                        Toast.makeText(getContext(),"Такой стол уже существует", Toast.LENGTH_SHORT).show();
                    else
                    {
                        cursor = db.query("Desks", new String[]{"MAX(rowid) As maximum"},
                                "isActual=1",null,null,null,null);
                        cursor.moveToFirst();
                        int maximum = cursor.getInt(cursor.getColumnIndex("maximum"))+1;
                        names.add(new ItemButtonMainDesks("Стол №" + numberDesk.getText().toString(),
                                Integer.toString(maximum),
                                0,false,false));
                        adapter.notifyDataSetChanged();

                        contentValues = new ContentValues();
                        contentValues.put("idHall", Integer.parseInt(idHall));
                        contentValues.put("name", "Стол №"+numberDesk.getText().toString());
                        contentValues.put("isActual", 1);
                        contentValues.put("isCheck", 0);
                        // TODO работа с датой и временем
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                        String formattedDate = df.format(c);
                        String format  =df1.format(c);
                        contentValues.put("dB", formattedDate + " "+ format);
                        db.insert("Desks", null,contentValues);
                        list.add(numberDesk.getText().toString());
                    }
                }
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.width1);
        int height = getResources().getDimensionPixelSize(R.dimen.height1);
        getDialog().getWindow().setLayout(width, height);
    }

}
