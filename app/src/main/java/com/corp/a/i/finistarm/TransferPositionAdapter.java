package com.corp.a.i.finistarm.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.corp.a.i.finistarm.ItemButtonMainDesks;
import com.corp.a.i.finistarm.R;

import java.util.ArrayList;
import java.util.List;

public class TransferPositionAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<ItemButtonMainDesks> names;

    public TransferPositionAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context =context;
        this.resource = resource;
        names = (ArrayList<ItemButtonMainDesks>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if(convertView == null)
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        else
            view = convertView;
        Button button = (Button) view;

        if(names.get(position).isChecked())
            button.setBackgroundResource(R.drawable.fragment_chose_menu_category_selected);
        else
            button.setBackgroundResource(R.drawable.main_menu_btn_desk);

        button.setText(names.get(position).getName());
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                names.get(pos).setChecked(true);
                check(pos);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void check(int pos)
    {
        for(int i = 0; i<names.size();i++)
        {
            if(pos == i)
                names.get(i).setChecked(true);
            else
                names.get(i).setChecked(false);
        }
    }
}
