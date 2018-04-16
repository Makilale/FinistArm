package com.corp.a.i.finistarm;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableRow;
import java.util.List;
import com.corp.a.i.finistarm.SettingsActivity;

/**
 * Created by GordeevMaxim on 14.04.2018.
 */

public class ButtonArrayAdapter extends ArrayAdapter{

    private Context context;
    private List<ButtonNameList> buttons;
    SettingsActivity ST;

    public void setSettingsActivity(SettingsActivity Activity)
    {
        this.ST = Activity;
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
            button.setTag(buttons.get(position).getId());
        } else {
            button = (Button) convertView;
        }
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ST.Inicialization((int)(button.getTag()));
            }
        });
        return button;
    }
}
