package com.corp.a.i.finistarm.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.adapters.ChoseMenuProductsAdapter;

import java.util.ArrayList;

public class ChoseMenuProductsFragment extends Fragment {
    public static final String TAG = "ChoseMenuProductFragment";
    TextView nameCategory;
    GridView productsGV;
    ChoseMenuProductsAdapter choseMenuProductsAdapter;
    ArrayList<String> namesProducts;
    ArrayList<Integer> idsProducts;
    ChoseMenuPositonFragment choseMenuPositonFragment;
    FragmentManager manager;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String idCategory, name;

    public void setName(String name) {
        this.name = name;
    }
    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }
    public void setChoseMenuPositonFragment(ChoseMenuPositonFragment choseMenuPositonFragment) {
        this.choseMenuPositonFragment = choseMenuPositonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chose_menu_products,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        manager = getFragmentManager();
        nameCategory = (TextView) getActivity().findViewById(R.id.choseCategory);
        nameCategory.setText(name);
        productsGV = (GridView) getActivity().findViewById(R.id.choseMenuProductsGV);
        namesProducts = new ArrayList<String>();
        idsProducts = new ArrayList<Integer>();
        initializeProductsDB(idCategory);
        choseMenuProductsAdapter = new ChoseMenuProductsAdapter(getContext(),R.layout.chose_menu_products_button, namesProducts);
        choseMenuProductsAdapter.setIdsProducts(idsProducts);
        choseMenuProductsAdapter.setChoseMenuPositonFragment(choseMenuPositonFragment);
        productsGV.setAdapter(choseMenuProductsAdapter);
    }

    private void initializeProductsDB(String idCategory) {
        cursor = db.query("Products",
                new String[]{"name", "rowid AS ID", "price"},
                "idCategory=? AND isActual=?",
                new String[]{idCategory, "1"},
                null,null,null);
        while(cursor.moveToNext())
        {
            namesProducts.add(cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("price")));
            idsProducts.add(cursor.getInt(cursor.getColumnIndex("ID")));
        }
    }
}
