package com.corp.a.i.finistarm;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by GordeevMaxim on 22.03.2018.
 */

public class SettingsActivity extends AppCompatActivity {
    Button button_settings_back;
    Button button_halls, button_accounts;
    Button add_hall;
    Button btnNew, btnNewDelete;
    TextView TW;
    public Button btDeletePrev = null;
    public Button btAccPrev = null;
    TableLayout BtList2;
    RelativeLayout VP;
    public int i=1, m=0;

    public  Button add_btn(int j, View view, int width)
    {
        Button btn = new Button(BtList2.getContext());
        btn.setBackgroundDrawable( view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
        btn.setLayoutParams(new TableRow.LayoutParams(width, width));
        btn.setTextSize(18);
        btn.setText("");
        btn.setId(j);
        return  btn;
    }

    public  TableRow add_Trow(Button i, Button j, int id)
    {
        TableRow tableRow = new TableRow(BtList2.getContext());
        tableRow.addView(i);
        tableRow.addView(j);
        tableRow.setId(id*100);
        return  tableRow;
    }

    public void add_clickListener_halls ()
    {
        if (i>1) {
            btDeletePrev = (Button)findViewById(i * 10);
            //int m = btDeletePrev.getId();
            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btDeletePrev.setVisibility(Button.GONE);
                    int m = BtList2.getChildCount();
                    BtList2.removeViews(m-1, 1);
                    if (i-- > 2) {
                        btDeletePrev = (Button)findViewById(i * 10);
                        btDeletePrev.setVisibility(Button.VISIBLE);
                    }
                }
            });
        }
    }

    public void add_two_btn(View view, int i)
    {
        btnNew = add_btn(i, view, LayoutParams.WRAP_CONTENT);
        btnNewDelete = add_btn(i*10, view, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
        BtList2.addView(add_Trow(btnNew, btnNewDelete, i));
    }

    public void onClick_AddAccuant(View view) {
        VP.setVisibility(View.VISIBLE);
    }

    public void onClick_CancelAccuant(View view) {
        VP.setVisibility(View.GONE);
    }

    public void onClick_SaveAccuant(View view) {
        VP.setVisibility(View.GONE);
        add_two_btn(view, ++i);
        btnNew.setText((((TextView)findViewById(R.id.etLogin)).getText()));
        //add_clickListener_halls();
        btDeletePrev = (Button)findViewById(i * 10);
        btDeletePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //определям id строки, которую будем удалть
                int m = view.getId();
                m = m*10;
                //находим эту строку
                TableRow row = (TableRow)findViewById(m);
                //удаляем эту строку
                BtList2.removeView(row);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        button_accounts  = (Button)findViewById(R.id.button_accounts);
        button_halls = (Button)findViewById(R.id.button_halls);
        add_hall = (Button)findViewById(R.id.add_hall);
        add_hall.setVisibility(Button.GONE);
        button_settings_back = (Button)findViewById(R.id.settings_back);
        View view = (View)findViewById(R.id.view);
        TW = (TextView)findViewById(R.id.textView);
        TW.setVisibility(TextView.GONE);
        BtList2 = (TableLayout)findViewById(R.id.BtList2);
        BtList2.setVisibility(TableLayout.GONE);
        VP = (RelativeLayout)findViewById(R.id.VP);
        VP.setVisibility(ViewPager.GONE);

        button_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                add_hall.setVisibility(Button.GONE);
                BtList2.setVisibility(TableLayout.GONE);
                TW.setVisibility(TextView.GONE);
                //VP.setVisibility(ViewPager.GONE);
                startActivity(intent);
            }
        });
        button_halls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtList2.setVisibility(TableLayout.VISIBLE);
                int m = BtList2.getChildCount();
                if(m>0) {
                    BtList2.removeViews(0, m);
                    i = 1;
                }
                add_hall.setText(view.getContext().getResources().getText(R.string.settings_halls_add));
                add_hall.setVisibility(Button.VISIBLE);
                TW.setText(view.getContext().getResources().getText(R.string.settings_halls));
                TW.setVisibility(TextView.VISIBLE);
                {
                    add_two_btn(view, i);
                    btnNew.setText("Зал №" + i);
                    btnNewDelete.setVisibility(Button.INVISIBLE);
                }

                add_hall.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if (i>1) {
                            btDeletePrev = (Button)findViewById(i * 10);
                            btDeletePrev.setVisibility(Button.INVISIBLE);
                        }
                        add_two_btn(view, ++i);
                        btnNew.setText("Зал №" + i);
                        add_clickListener_halls();
                    }
                });
                }

        });
        //button_accounts.
        button_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtList2.setVisibility(TableLayout.VISIBLE);
                add_hall.setVisibility(Button.VISIBLE);
                int m = BtList2.getChildCount();
                //очищаем таблицу
                if(m>0) {
                    BtList2.removeViews(0, m);
                    i = 1;
                }
                add_hall.setText(view.getContext().getResources().getText(R.string.settings_acounts_add));
                add_hall.setVisibility(Button.VISIBLE);
                TW.setText(view.getContext().getResources().getText(R.string.settings_acounts));
                TW.setVisibility(TextView.VISIBLE);
                {
                    add_two_btn(view, i);
                    btnNew.setText("Аккуант №" + i);
                    btnNewDelete.setVisibility(Button.INVISIBLE);
                }

               /* add_hall.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        BtList2.setVisibility(TableLayout.VISIBLE);
                        add_hall.setVisibility(Button.VISIBLE);
                        if (i>=1) {
                            btAccPrev = (Button)findViewById(i);
                            btDeletePrev = (Button)findViewById(i * 10);
                        }

                        add_two_btn(view, ++i);
                        btnNew.setText("Аккуант №" + i);
                        if (i>=1)
                        {
                            btAccPrev = (Button)findViewById(i);
                            btAccPrev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    VP.setVisibility(View.VISIBLE);
                                }
                            });
                            btDeletePrev = (Button)findViewById(i * 10);
                            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //определям id строки, которую будем удалть
                                    int m = view.getId();
                                    m = m*10;
                                    //находим эту строку
                                    TableRow row = (TableRow)findViewById(m);
                                    //удаляем эту строку
                                    BtList2.removeView(row);
                                }
                            });
                        }
                    }
                });*/
            }

        });
    }

}
