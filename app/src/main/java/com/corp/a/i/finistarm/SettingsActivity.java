package com.corp.a.i.finistarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
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
    TextView TW;
    public Button btDeletePrev = null;
    TableLayout BtList2;
    public int i=1, m=0;
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

        button_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                add_hall.setVisibility(Button.GONE);
                BtList2.setVisibility(TableLayout.GONE);
                TW.setVisibility(TextView.GONE);
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
                    Button btnNew = new Button(BtList2.getContext());
                    btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
                    btnNew.setTextSize(18);
                    btnNew.setText("Зал №1");
                    btnNew.setId((int)1);
                    Button btnNewDelete = new Button(BtList2.getContext());
                    btnNewDelete.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_delete_settings));
                    btnNewDelete.setTextSize(18);
                    btnNewDelete.setText("");
                    btnNewDelete.setId(i*10);
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                    btnNewDelete.setLayoutParams(new TableRow.LayoutParams(width, width));
                    TableRow tableRow1 = new TableRow(BtList2.getContext());
                    tableRow1.addView(btnNew);
                    btnNewDelete.setVisibility(Button.INVISIBLE);
                    tableRow1.addView(btnNewDelete);
                    BtList2.addView(tableRow1);
                }


                add_hall.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view) {
                        BtList2.setVisibility(TableLayout.VISIBLE);
                        add_hall.setVisibility(Button.VISIBLE);
                        if (i>1) {
                            btDeletePrev = (Button)findViewById(i * 10);
                            btDeletePrev.setVisibility(Button.INVISIBLE);
                        }

                        Button btnNew = new Button(BtList2.getContext());
                        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
                        btnNew.setTextSize(18);
                        btnNew.setText("Зал №" + ++i);
                        btnNew.setId(i);

                        Button btnNewDelete = new Button(BtList2.getContext());
                        btnNewDelete.setTextSize(18);
                        btnNewDelete.setText("");
                        btnNewDelete.setGravity(Gravity.CENTER);
                        btnNewDelete.setId(i*10);
                        btnNewDelete.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_delete_settings));
                        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                        btnNewDelete.setLayoutParams(new TableRow.LayoutParams(width, width));
                        TableRow tableRow1 = new TableRow(BtList2.getContext());
                        tableRow1.addView(btnNew);
                        tableRow1.addView(btnNewDelete);
                        BtList2.addView(tableRow1);

                        if (i>1) {
                            btDeletePrev = (Button)findViewById(i * 10);
                            int m = btDeletePrev.getId();
                            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    btDeletePrev.setVisibility(Button.GONE);
                                    btDeletePrev.setVisibility(Button.GONE);
                                    int m = BtList2.getChildCount();
                                    BtList2.removeViews(m-1, 1);
                                    if (i-- > 2) {
                                        btDeletePrev = (Button) findViewById(i * 10);
                                        btDeletePrev.setVisibility(Button.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                });
                }

        });
        //button_accounts.
        button_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtList2.setVisibility(TableLayout.VISIBLE);
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
                    Button btnNew = new Button(BtList2.getContext());
                    btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
                    btnNew.setTextSize(18);
                    btnNew.setText("Аккуант №1");
                    btnNew.setId((int)1);
                    Button btnNewDelete = new Button(BtList2.getContext());
                    btnNewDelete.setTextSize(18);
                    btnNewDelete.setText("");//
                    btnNewDelete.setId(i*10);//задали id кнопки удаления
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    btnNewDelete.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_delete_settings));
                    btnNewDelete.setLayoutParams(new TableRow.LayoutParams(width, width));//сделаи кнопку удаления учётки квадратной
                    TableRow tableRow1 = new TableRow(BtList2.getContext());
                    tableRow1.addView(btnNew);//добавили кнопку учётки
                    tableRow1.addView(btnNewDelete);//добавили кнопку удаления учётки
                    tableRow1.setId(i*10+i);//определили id  строки
                    BtList2.addView(tableRow1);//добавили строку с кнопками в таблицу
                    if (i>=1) {
                        btDeletePrev = (Button)findViewById(i * 10);
                        btDeletePrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //определям id строки, которую будем удалть
                                int m = view.getId();
                                m = m/10+m;
                                //находим эту строку
                                TableRow row = (TableRow)findViewById(m);
                                //удаляем эту строку
                                BtList2.removeView(row);
                            }
                        });
                    }
                }

                add_hall.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view) {
                        BtList2.setVisibility(TableLayout.VISIBLE);
                        add_hall.setVisibility(Button.VISIBLE);
                        if (i>=1) {
                            btDeletePrev = (Button)findViewById(i * 10);
                        }

                        Button btnNew = new Button(BtList2.getContext());
                        btnNew.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_control_settings));
                        btnNew.setTextSize(18);
                        btnNew.setText("Аккуант №" + ++i);
                        btnNew.setId(i);

                        Button btnNewDelete = new Button(BtList2.getContext());
                        btnNewDelete.setTextSize(18);
                        btnNewDelete.setText("");
                        btnNewDelete.setId(i*10);
                        btnNewDelete.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.main_menu_butten_delete_settings));
                        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
                        btnNewDelete.setLayoutParams(new TableRow.LayoutParams(width, width));
                        TableRow tableRow1 = new TableRow(BtList2.getContext());
                        tableRow1.addView(btnNew);
                        tableRow1.addView(btnNewDelete);
                        tableRow1.setId(i*10+i);
                        BtList2.addView(tableRow1);

                        if (i>=1) {
                            btDeletePrev = (Button)findViewById(i * 10);
                            btDeletePrev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //определям id строки, которую будем удалть
                                    int m = view.getId();
                                    m = m/10+m;
                                    //находим эту строку
                                    TableRow row = (TableRow)findViewById(m);
                                    //удаляем эту строку
                                    BtList2.removeView(row);
                                }
                            });
                        }
                    }
                });
            }

        });
    }

}
