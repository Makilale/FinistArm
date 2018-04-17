package com.corp.a.i.finistarm.adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.corp.a.i.finistarm.ChoseMenuItemProductsFragment;
import com.corp.a.i.finistarm.DataBase.DBHelper;
import com.corp.a.i.finistarm.R;
import com.corp.a.i.finistarm.fragment.ChoseMenuCategoryFragment;
import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;

import java.util.ArrayList;
import java.util.List;

public class ChoseMenuCategoryAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<ChoseMenuItemProductsFragment> namesCategory;
    ArrayList<Integer> idsCategory;
    FragmentManager manager;
    FragmentTransaction transaction;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String idParentCategory;
    ChoseMenuPositonFragment choseMenuPositonFragment;
    boolean isNewAddedDesk;
    public void setNewAddedDesk(boolean newAddedDesk) {
        isNewAddedDesk = newAddedDesk;
    }
    public void setIdParentCategory(String idParentCategory) {
        this.idParentCategory = idParentCategory;
    }
    public void setIdsCategory(ArrayList<Integer> idsCategory) {
        this.idsCategory = idsCategory;
    }
    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }
    public void setChoseMenuPositonFragment(ChoseMenuPositonFragment choseMenuPositonFragment) {
        this.choseMenuPositonFragment = choseMenuPositonFragment;
    }

    public ChoseMenuCategoryAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        namesCategory = (ArrayList<ChoseMenuItemProductsFragment>) objects;
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if(convertView == null)
        {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        else
            view = convertView;
        Button button = (Button) view;
        button.setText(namesCategory.get(position).getName());
        if(namesCategory.get(position).isSelected())
            button.setBackgroundResource(R.drawable.fragment_chose_menu_category_selected);
        button.setTag(idsCategory.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = manager.beginTransaction();
                cursor = db.query("Category",
                        null,
                        "idParent=? AND isActual=?",
                        new String[]{v.getTag().toString(), "1"},
                        null,null,null);
                ChoseMenuCategoryFragment choseMenuCategoryFragment = new ChoseMenuCategoryFragment();
                choseMenuCategoryFragment.setChoseMenuPositonFragment(choseMenuPositonFragment);
                if(cursor.moveToFirst())
                {
                    choseMenuCategoryFragment.setIdCategory(v.getTag().toString());
                    choseMenuCategoryFragment.setIdChildCategory("");
                    choseMenuCategoryFragment.setNewAddedDesk(isNewAddedDesk);
                    transaction.replace(R.id.fragContCategory, choseMenuCategoryFragment,ChoseMenuCategoryFragment.TAG);
                }
                else
                {
                    choseMenuCategoryFragment.setIdCategory(idParentCategory);
                    choseMenuCategoryFragment.setIdChildCategory(v.getTag().toString());
                    choseMenuCategoryFragment.setNewAddedDesk(isNewAddedDesk);
                    transaction.replace(R.id.fragContCategory, choseMenuCategoryFragment,ChoseMenuCategoryFragment.TAG);
                }
                    transaction.addToBackStack("");
                transaction.commit();
            }
        });

        return view;
    }
}
