package com.corp.a.i.finistarm.Desks;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.ItemChoseMenu;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.fragment.ChoseMenuProductsFragment;
import com.corp.a.i.finistarm.fragment.TransferPositionDialogFragment;
import java.util.ArrayList;

public class ChoseMenuPositonFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ChoseMenuPositionFragment";
    FragmentTransaction transaction;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues contentValues;
    String idTable, name;
    Button deletePosition, transferPosition;
    TextView nameDesk, currentPrice;
    TableLayout positionLV;
    ArrayList<ItemChoseMenu> namePositions,itemDeleted;
    ArrayList<Integer> idPositions;
    FragmentManager manager;
    int lastRow=0, currentOrder, totalPrice;
    boolean isNewTable;

    public void setIdTable(String idDesk)
    {
        this.idTable = idDesk;
    }
    public ArrayList<ItemChoseMenu> getNamesPosition()
    {
        return namePositions;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setNewTable(boolean newTable)
    {
        isNewTable = newTable;
    }
    public ArrayList<ItemChoseMenu> getItemDeleted()
    {
        return itemDeleted;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chose_menu_positon,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deletePosition = (Button) getActivity().findViewById(R.id.deletePosition);
        transferPosition = (Button) getActivity().findViewById(R.id.transferPosition);
        deletePosition.setOnClickListener(this);
        transferPosition.setOnClickListener(this);
        if(isNewTable)
            transferPosition.setEnabled(false);
        nameDesk = (TextView) getActivity().findViewById(R.id.nameDeskFragment);
        currentPrice = (TextView) getActivity().findViewById(R.id.currentPriceOrder);
        nameDesk.setText(name.split(" ")[1]);
        positionLV = (TableLayout) getActivity().findViewById(R.id.choseMenuPositionTL);
        namePositions = new ArrayList<ItemChoseMenu>();
        idPositions = new ArrayList<Integer>();
        itemDeleted = new ArrayList<ItemChoseMenu>();
        manager = getFragmentManager();
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        if(!isNewTable)
        {
            initializePositionDB(idTable);
            initializeTableLayout();
            currentPrice.setText(Integer.toString(totalPrice) + " р.");
        }
        else
            currentPrice.setText("0 р.");
    }

    private void initializeTableLayout()
    {
        for(int i =0; i<namePositions.size(); i++)
            createRow(namePositions.get(i).getNamePosition(),namePositions.get(i).getPricePosition(),i);
    }

    private void createRow(String name,String price, int i)
    {
        idPositions.add(lastRow);
        currentOrder += Integer.parseInt(price);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_chose_menu_lv, null);
        TableRow tableRow = (TableRow) view.findViewById(R.id.itemTR);
        TextView textView = (TextView)view.findViewById(R.id.namePosition);
        textView.setText(name);
        TextView textView1 = (TextView)view.findViewById(R.id.pricePostion);
        textView1.setText(price);
        TextView textView2 = (TextView) view.findViewById(R.id.textView30);
        tableRow.setTag(i);
        tableRow.setOnClickListener(this);
        positionLV.addView(tableRow);
        lastRow++;
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_row_delimetr, null);
        TableRow delimetr = (TableRow) view.findViewById(R.id.delimetrTableRow);
        positionLV.addView(delimetr);
    }

    private void initializePositionDB(String idDesk) {
        cursor = db.query("DeskProduct JOIN Products ON DeskProduct.idProduct = Products.rowid " +
                                "JOIN Category ON Products.idCategory = Category.rowid",
                new String[]{"DeskProduct.rowid AS idD", "Products.name AS nameP", "Products.price AS priceP", "Category.isReport"},
                "DeskProduct.idTable=? AND DeskProduct.isActual=? AND Products.isActual =?",
                new String[]{idDesk, "1", "1"},
                null,
                null,
                null);
        while(cursor.moveToNext())
        {
            totalPrice += cursor.getInt(cursor.getColumnIndex("priceP"));
            namePositions.add( new ItemChoseMenu(cursor.getString(cursor.getColumnIndex("nameP")),
                    cursor.getString(cursor.getColumnIndex("priceP")),
                    cursor.getString(cursor.getColumnIndex("idD")),
                    false, false, true));
        }
    }

    public void addRow(String idProduct)
    {
        cursor = db.query("Products JOIN Category ON Products.idCategory = Category.rowid", new String[]{"Products.name", "Products.price", "Category.isReport"},
                            "Products.rowid=? AND Products.isActual=?", new String[]{idProduct,"1"},
                            null,null,null);
        cursor.moveToFirst();
        boolean bool = cursor.getInt(cursor.getColumnIndex("Category.isReport"))!=0;
        String name = cursor.getString(cursor.getColumnIndex("Products.name"));
        String price = cursor.getString(cursor.getColumnIndex("Products.price"));
        namePositions.add(new ItemChoseMenu(name,price,idProduct,false, true, bool));
        createRow(name,price,lastRow);
        currentPrice.setText(currentOrder+" р.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.transferPosition:
                ArrayList<ItemChoseMenu> list = new ArrayList<ItemChoseMenu>();
                for(int i =0; i<namePositions.size(); i++)
                {
                    if(namePositions.get(i).isCheck())
                        list.add(namePositions.get(i));
                }
                if(list.size()!=0)
                {
                    TransferPositionDialogFragment transferPositionDialogFragment = new TransferPositionDialogFragment();
                    transferPositionDialogFragment.setIdDesk(idTable);
                    transferPositionDialogFragment.setManager(manager);
                    transferPositionDialogFragment.setNamesPosition(list);
                    transferPositionDialogFragment.setNameDesk(name);
                    transferPositionDialogFragment.show(getFragmentManager(), TransferPositionDialogFragment.TAG);
                }
                else
                    Toast.makeText(getContext(),"Выберите позиции для переноса", Toast.LENGTH_SHORT).show();
                break;

            case R.id.deletePosition:
                int i = 0;
                while( i<positionLV.getChildCount())
                {
                    TableRow row = (TableRow) positionLV.getChildAt(i);
                    if(row.getTag()!=null)
                    {
                        int id = idPositions.indexOf(Integer.parseInt(row.getTag().toString()));
                        ItemChoseMenu itemChoseMenu = namePositions.get(id);
                        if(itemChoseMenu.isCheck())
                        {
                            positionLV.removeViewAt(i);
                            positionLV.removeViewAt(i);
                            lastRow--;
                            if(!itemChoseMenu.isNew())
                                itemDeleted.add(itemChoseMenu);
                            currentOrder -= Integer.parseInt(itemChoseMenu.getPricePostion());
                            namePositions.remove(idPositions.indexOf(Integer.parseInt(row.getTag().toString())));
                            idPositions.remove(idPositions.indexOf(Integer.parseInt(row.getTag().toString())));
                        }
                        else
                            i++;
                    }
                    else
                        i++;
                }
                currentPrice.setText(currentOrder+" р.");
                if(manager.findFragmentByTag(ChoseMenuProductsFragment.TAG)==null)
                    if(positionLV.getChildCount()==0)
                    {
                        transaction = manager.beginTransaction();
                        transaction.remove(manager.findFragmentByTag(ChoseMenuPositonFragment.TAG));
                        transaction.commit();
                    }
                break;
            case R.id.itemTR:
                if(namePositions.get(idPositions.indexOf(Integer.parseInt(v.getTag().toString()))).isCheck())
                {
                    namePositions.get(idPositions.indexOf(Integer.parseInt(v.getTag().toString()))).setCheck(false);
                    v.setBackgroundResource(R.drawable.row_null);
                }
                else
                {
                    namePositions.get(idPositions.indexOf(Integer.parseInt(v.getTag().toString()))).setCheck(true);
                    v.setBackgroundResource(R.drawable.orange_res);
                }
                break;
        }
    }
}
