package com.example.cookinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

public class SettingScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnClose;
    private ImageView imgBGM, imgSoundEffect, imgThumbnail;
    private Spinner spinner;
    private Switch BGMSwitch;
    private Switch SoundEffectSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        btnClose = findViewById(R.id.btnClose1);
        btnClose.setOnClickListener(this);
        imgBGM = findViewById(R.id.imageBGM);
        imgSoundEffect = findViewById(R.id.imageSound);
        imgThumbnail = findViewById(R.id.img_thumbnail);

        spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Elegant Restaurant");
        arrayList.add("Night River");
        arrayList.add("Universal");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String selected = spinner.getSelectedItem().toString();
               if(selected == "Elegant Restaurant")
               {
                   imgThumbnail.setImageResource(R.drawable.background);
                   MainActivity.background= 1;
               }
               else if(selected == "Night River")
               {
                   imgThumbnail.setImageResource(R.drawable.night_woodenbridge);
                   MainActivity.background= 2;
               }
               else if(selected == "Universal")
               {
                   imgThumbnail.setImageResource(R.drawable.stars);
                   MainActivity.background= 3;
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        BGMSwitch = findViewById(R.id.switch_bgm);
        SoundEffectSwitch = findViewById(R.id.switch_sound);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnClose1)
        {
            finish();
        }

    }
}
