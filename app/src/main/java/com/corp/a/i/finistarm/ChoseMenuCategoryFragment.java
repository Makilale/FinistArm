package com.corp.a.i.finistarm.fragment;

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
import android.widget.GridView;

import com.corp.a.i.finistarm.ChoseMenuItemProductsFragment;
import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.adapters.ChoseMenuCategoryAdapter;

import java.util.ArrayList;


public class ChoseMenuCategoryFragment extends Fragment {
    public static final String TAG = "ChoseMenuCategoryFragment";

    GridView categoryGV;
    /** Адаптер */
    ChoseMenuCategoryAdapter choseMenuCategoryAdapter;
    ArrayList<ChoseMenuItemProductsFragment> namesCategory;
    ArrayList<Integer> idsCategory;

    ChoseMenuPositonFragment choseMenuPositonFragment;


    FragmentManager manager;
    FragmentTransaction transaction;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String idCategory;
    String idChildCategory;
    String copy;
    String name;
    boolean isNewAddedDesk;

    public void setNewAddedDesk(boolean newAddedDesk) {
        isNewAddedDesk = newAddedDesk;
    }

    public void setIdChildCategory(String idChildCategory) {
        this.idChildCategory = idChildCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setChoseMenuPositonFragment(ChoseMenuPositonFragment choseMenuPositonFragment) {
        this.choseMenuPositonFragment = choseMenuPositonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chose_menu_category,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbHelper = new DBHelper(getContext());
        db = dbHelper.getReadableDatabase();

        manager = getFragmentManager();

        copy = idCategory;
        categoryGV = (GridView) getActivity().findViewById(R.id.choseMenuCategoryGV);
        namesCategory = new ArrayList<ChoseMenuItemProductsFragment>();
        idsCategory = new ArrayList<Integer>();
        initializeCategoryDB(idCategory);

        choseMenuCategoryAdapter = new ChoseMenuCategoryAdapter(getContext(), R.layout.chose_menu_category_button, namesCategory);
        choseMenuCategoryAdapter.setIdsCategory(idsCategory);
        choseMenuCategoryAdapter.setIdParentCategory(idCategory);
        choseMenuCategoryAdapter.setChoseMenuPositonFragment(choseMenuPositonFragment);
        choseMenuCategoryAdapter.setManager(manager);
        categoryGV.setAdapter(choseMenuCategoryAdapter);

        if( manager.findFragmentByTag(ChoseMenuPositonFragment.TAG) != null)
            if(isNewAddedDesk)
                if(choseMenuPositonFragment.getNamesPosition().size() == 0)
                {
                    transaction = manager.beginTransaction();
                    transaction.remove(manager.findFragmentByTag(ChoseMenuPositonFragment.TAG));
                    transaction.commit();
                }

        if(!idChildCategory.equals(""))
        {
            idCategory = idChildCategory;
            cursor = db.query("Category",
                    new String[]{ "name"},
                    "rowid=? AND isActual=?",
                    new String[]{idCategory, "1"},
                    null,null,null);
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("name"));

        }

        cursor = db.query("Products",
                new String[]{ "rowid AS ID"},
                "idCategory=? AND isActual=?",
                new String[]{idCategory, "1"},
                null,null,null);
        if(cursor.moveToFirst())
        {
            transaction = manager.beginTransaction();
            ChoseMenuProductsFragment choseMenuProductsFragment = new ChoseMenuProductsFragment();
            choseMenuProductsFragment.setIdCategory(idCategory);
            choseMenuProductsFragment.setName(name);
            choseMenuProductsFragment.setChoseMenuPositonFragment(choseMenuPositonFragment);
            if(manager.findFragmentByTag(ChoseMenuPositonFragment.TAG)==null)
                transaction.add(R.id.fragContPosition, choseMenuPositonFragment, ChoseMenuPositonFragment.TAG);

            if(manager.findFragmentByTag(ChoseMenuProductsFragment.TAG)==null)
                transaction.add(R.id.fragContProducts, choseMenuProductsFragment, ChoseMenuProductsFragment.TAG);
            else
                transaction.replace(R.id.fragContProducts, choseMenuProductsFragment, ChoseMenuProductsFragment.TAG);

            transaction.commit();
        }
        else
        {
            if(manager.findFragmentByTag(ChoseMenuProductsFragment.TAG) !=null)
            {
                transaction = manager.beginTransaction();
                transaction.remove(manager.findFragmentByTag(ChoseMenuProductsFragment.TAG));
                transaction.commit();
            }

        }
        idCategory = copy;

    }

    public void initializeCategoryDB(String idParentCategory) {
        cursor = db.query("Category",
                new String[]{"name", "rowid AS ID"},
                "idParent=? AND isActual=?",
                new String[]{idParentCategory, "1"},
                null,null,null);
        String id;
        while(cursor.moveToNext())
        {
            id = cursor.getString(cursor.getColumnIndex("ID"));
            boolean bool = false;
            if(id.equals(idChildCategory))
                bool = true;

            namesCategory.add(new ChoseMenuItemProductsFragment(cursor.getString(cursor.getColumnIndex("name")),bool));
            idsCategory.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID"))));
        }

        //adapterCategory.notifityDataSet();
    }
}
