package com.corp.a.i.finistarm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.corp.a.i.finistarm.Desks.ChoseMenuPositonFragment;

import java.util.ArrayList;
import java.util.List;
public class ChoseMenuProductsAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<String> namesProducts;
    ArrayList<Integer> idsProducts;
    ChoseMenuPositonFragment choseMenuPositonFragment;

    public void setChoseMenuPositonFragment(ChoseMenuPositonFragment choseMenuPositonFragment) {
        this.choseMenuPositonFragment = choseMenuPositonFragment;
    }

    public void setIdsProducts(ArrayList<Integer> idsProducts) {
        this.idsProducts = idsProducts;
    }

    public ChoseMenuProductsAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        namesProducts = (ArrayList<String>) objects;
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
        button.setText(namesProducts.get(position));
        button.setTag(idsProducts.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseMenuPositonFragment.addRow(v.getTag().toString());
            }
        });


        return view;
    }
}
