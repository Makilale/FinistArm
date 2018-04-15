package com.corp.a.i.finistarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChoseMenu extends AppCompatActivity {

    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_menu);
        btnBack = (Button) findViewById((R.id.btnBackTable));
        View.OnClickListener oclBtnBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        };

        btnBack.setOnClickListener(oclBtnBack);


    }


}
